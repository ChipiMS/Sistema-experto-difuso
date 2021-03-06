package sistemaexpertodifuso;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

public class GUI extends JFrame {

    VariablesLinguisticas variablesLinguisticas;
    ArchivoReglas reglasDifusas;
    JTextArea messages;
    JPanel panelVariables, pnlSliders;
    static JLabel valors;
    JTextField[] valores;
    JLabel[] llaves;
    JLabel[] nombres;
    JSlider[] sliders;
    int variableLinguisticaResultado;

    /*
      ____ _   _ ___ 
     / ___| | | |_ _|
    | |  _| | | || | 
    | |_| | |_| || | 
     \____|\___/|___|*/
    GUI(VariablesLinguisticas variablesLinguisticas, ArchivoReglas reglasDifusas) throws IOException {
        Container cp = getContentPane();
        recuperaVariableLinguisticaResultado(cp);
        this.variablesLinguisticas = variablesLinguisticas;
        this.reglasDifusas = reglasDifusas;
        setSize(900, 650);
        setTitle("Sistema experto difuso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cp.add(createMenu(cp), BorderLayout.NORTH);

        messages = new JTextArea("Mensajes");
        messages.setEnabled(false);
        messages.setBackground(Color.black);
        JScrollPane messagesScrollPane = new JScrollPane(messages);
        messagesScrollPane.setMinimumSize(new Dimension(100, 200));
        messagesScrollPane.setPreferredSize(new Dimension(100, 200));
        messagesScrollPane.setAlignmentX(LEFT_ALIGNMENT);
        cp.add(messagesScrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    /*
     __  __              __  
    |  \/  | ___ _ __  _/_/_ 
    | |\/| |/ _ \ '_ \| | | |
    | |  | |  __/ | | | |_| |
    |_|  |_|\___|_| |_|\__,_|*/
    JMenuBar createMenu(Container cp) throws IOException {
        JMenuBar menuBar;
        JMenu menuVariablesLinguisticas, menuFuzzy, menuReglas;
        JMenuItem menuItem;
        menuBar = new JMenuBar();
        menuVariablesLinguisticas = new JMenu("Variables lingüisticas");
        menuFuzzy = new JMenu("Inferencia Difusa");
        menuReglas = new JMenu("Reglas difusas");
        menuBar.add(menuVariablesLinguisticas);
        menuBar.add(menuFuzzy);
        menuBar.add(menuReglas);

        //Variables lingüisticas-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        menuItem = new JMenuItem("Mostrar las variables lingüisticas");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                VariableLinguistica variables[] = null;
                int i;
                String reglas = "";
                try {
                    variables = variablesLinguisticas.recuperarSecuencial();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (variables != null) {
                    for (i = 0; i < variables.length; i++) {
                        reglas += variables[i].muestraVariableLinguistica() + "\n";
                    }
                } else {
                    reglas = "Hubo un error al recuperar las variables lingüisticas.";
                }
                JTextArea textArea = new JTextArea(reglas);
                JScrollPane scrollPane = new JScrollPane(textArea);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                scrollPane.setPreferredSize(new Dimension(500, 500));
                JOptionPane.showMessageDialog(null, scrollPane, "Variables Linguisticas",
                        JOptionPane.YES_NO_OPTION);

                //JOptionPane.showMessageDialog(cp, reglas);
            }
        });
        menuVariablesLinguisticas.add(menuItem);

        menuItem = new JMenuItem("Actualizar variable lingüistica");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la variable lingüistica a actualizar:", JOptionPane.INPUT_VALUE_PROPERTY));
                    VariableLinguistica variable = variablesLinguisticas.recuperarAleatorio(llave);
                    if (variable != null) {
                        new FormularioVariablesLinguisticas(variable, variablesLinguisticas, true);
                    } else {
                        JOptionPane.showMessageDialog(cp, "No existe una las variable lingüistica con esa llave.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuVariablesLinguisticas.add(menuItem);

        menuItem = new JMenuItem("Nueva variable lingüistica");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la nueva variable lingüistica", JOptionPane.INPUT_VALUE_PROPERTY));
                    VariableLinguistica variable = variablesLinguisticas.recuperarAleatorio(llave);
                    if (variable == null) {
                        variable = new VariableLinguistica();
                        variable.llave = llave;
                        new FormularioVariablesLinguisticas(variable, variablesLinguisticas, false);
                    } else {
                        JOptionPane.showMessageDialog(cp, "Ya existe una variable lingüistica con esa llave.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuVariablesLinguisticas.add(menuItem);

        menuItem = new JMenuItem("Borrar variable lingüistica");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la variable lingüistica que se va a borrar", JOptionPane.INPUT_VALUE_PROPERTY));
                    VariableLinguistica variable = variablesLinguisticas.recuperarAleatorio(llave);
                    if (variable != null) {
                        variablesLinguisticas.borrar(llave);
                    } else {
                        JOptionPane.showMessageDialog(cp, "No existe una variable lingüistica con esa llave.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuVariablesLinguisticas.add(menuItem);

        menuItem = new JMenuItem("Especificar VL del resultado");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    RandomAccessFile escritor;
                    escritor = new RandomAccessFile("variableResultado", "rw");
                    variableLinguisticaResultado = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la variable difusa que indica el resultado", JOptionPane.INPUT_VALUE_PROPERTY));
                    escritor.writeInt(variableLinguisticaResultado);
                    escritor.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        menuVariablesLinguisticas.add(menuItem);

        //Evaluación-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        menuItem = new JMenuItem("Evaluar variable lingüistica");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    valors = new JLabel("");
                    int valor_de_entrada = 0;
                    int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la variable linguistica a evaluar:", JOptionPane.INPUT_VALUE_PROPERTY));
                    VariableLinguistica variable = variablesLinguisticas.recuperarAleatorio(llave);
                    if (variable != null) {
                        JOptionPane optionPane = new JOptionPane();

                        JSlider slider = getSlider(optionPane);
                        optionPane.setMessage(new Object[]{"Ingresa el valor a evaluar:", slider});
                        optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
                        optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
                        optionPane.add(valors);
                        JDialog dialog = optionPane.createDialog(cp, "Difuzificar:" + variable.nombre);
                        dialog.setVisible(true);
                        valor_de_entrada = slider.getValue();

                        messages.append("\nVariable linguistica a evaluar:" + variable.nombre + "\n");
                        messages.append("Valor de entrada:" + valor_de_entrada + "\n");
                        messages.append("-----------------------------------------\n");

                        for (int i = 0; i < 8; i++) {
                            if (variable.conjuntos[i] != null) {

                                messages.append("Nombre: " + variable.conjuntos[i].nombre + "\n");
                                messages.append("Puntos Criticos:\n");
                                for (int j = 0; j < 4; j++) {
                                    if (variable.conjuntos[i].puntosCriticos[j] != null && variable.conjuntos[i].puntosCriticos[j].y != -1) {
                                        messages.append(variable.conjuntos[i].puntosCriticos[j].x + "," + variable.conjuntos[i].puntosCriticos[j].y + "\n");
                                    }
                                }
                                messages.append("Grado de membresia: " + variable.conjuntos[i].evaluar((double) (valor_de_entrada)).valor + "\n");
                                messages.append("\n");
                                messages.append("------------------------------------------------------\n");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(cp, "No existe una variable lingüistica con esa llave.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuFuzzy.add(menuItem);

        menuItem = new JMenuItem("Inferir");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //arrayResultadosDifusos = new ArrayList<>();
                //Obtener datos de las variables linguisticas
                VariableLinguistica variables[] = null;
                VariablesLinguisticas var = null;
                int num_var;
                JButton btnDifuzificar = null;
                try {
                    variables = variablesLinguisticas.recuperarSecuencial();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                num_var = variables.length;

                for (int i = 0; i < variables.length; i++) {

                    System.out.println(variables[i].obtenLLaveVariable());
                    System.out.println(variables[i].obtenNombreVar());

                }

                JLabel[] llaves = new JLabel[num_var];
                JLabel[] nombres = new JLabel[num_var];
                JSlider[] sliders = new JSlider[num_var];

                valores = new JTextField[num_var];

                Map<JSlider, JTextField> fieldMap;
                fieldMap = new HashMap<>();

                JPanel pnlVarLin = new JPanel(new GridLayout(num_var, 4));

                for (int i = 0; i <= variables.length - 1; i++) {//ciclo para crear, añadir, establecer propiedades a los botones
                    llaves[i] = new JLabel("" + variables[i].obtenLLaveVariable());
                    nombres[i] = new JLabel(variables[i].obtenNombreVar());

                    sliders[i] = new JSlider(0, 100);
                    sliders[i].setMajorTickSpacing(10);
                    sliders[i].setPaintTicks(true);
                    sliders[i].setPaintLabels(true);

                    valores[i] = new JTextField("50");
                    fieldMap.put(sliders[i], valores[i]);
                    sliders[i].addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            JSlider slider = (JSlider) e.getSource();
                            JTextField field = fieldMap.get(slider);
                            field.setText(Integer.toString(slider.getValue()));
                        }
                    });

                    if (Integer.parseInt(llaves[i].getText()) == variableLinguisticaResultado) {

                    } else {
                        pnlVarLin.add(llaves[i]);
                        pnlVarLin.add(nombres[i]);
                        pnlVarLin.add(sliders[i]);
                        pnlVarLin.add(valores[i]);
                    }

                }
                Container cp = getContentPane();
                btnDifuzificar = new JButton("Inferir");

                panelVariables = new JPanel();
                JScrollPane codeScrollPane = new JScrollPane(pnlVarLin);
                cp.add(codeScrollPane, BorderLayout.CENTER);
                cp.add(btnDifuzificar, BorderLayout.EAST);

                btnDifuzificar.addActionListener(new ActionListener() {

                    VariableLinguistica vls = new VariableLinguistica();

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        try {
                            ArrayList<ResultadoDifuso> arrayResultadosDifusos = new ArrayList<>();
                            VariableLinguistica variable = null;
                            for (int i = 0; i < valores.length; i++) {
                                variable = variablesLinguisticas.recuperarAleatorio(Integer.parseInt(llaves[i].getText()));
                                double valor_entrada = (double) Integer.parseInt(valores[i].getText());
                                messages.append("\nVariable linguistica a evaluar:" + variable.nombre + "\n");
                                messages.append("Llave de la variable linguistica:" + variable.llave + "\n");
                                messages.append("Valor de entrada:" + valor_entrada + "\n");
                                messages.append("-----------------------------------------\n");

                                for (int j = 0; j < 8; j++) {
                                    if (variable.conjuntos[j] != null) {

                                        messages.append("Nombre: " + variable.conjuntos[j].nombre + "\n");
                                        messages.append("Puntos Criticos:\n");
                                        for (int k = 0; k < 4; k++) {
                                            if (variable.conjuntos[j].puntosCriticos[k] != null && variable.conjuntos[j].puntosCriticos[k].y != -1) {
                                                messages.append(variable.conjuntos[j].puntosCriticos[k].x + "," + variable.conjuntos[j].puntosCriticos[k].y + "\n");
                                            }
                                        }

                                        ResultadoDifuso objResultadoDifuso = new ResultadoDifuso();
                                        objResultadoDifuso = variable.conjuntos[j].evaluar((double) (valor_entrada));

                                        objResultadoDifuso.variableConjunto = new VariableConjunto(variable.llave, j);

                                        arrayResultadosDifusos.add(objResultadoDifuso);

                                        messages.append("Grado de membresia: " + variable.conjuntos[j].evaluar((double) (valor_entrada)).valor + "\n");
                                        messages.append("\n");
                                        messages.append("------------------------------------------------------\n");
                                    }

                                }
                            }

                            ArchivoReglas archivo = new ArchivoReglas();
                            List<ResultadoDifuso> resultados = MaxMin.procesar(Arrays.asList(archivo.recuperarTodo()), arrayResultadosDifusos);

                            for (ResultadoDifuso resultadoDifuso : resultados) {
                                messages.append("Difusificación: Llave variable linguistica: " + resultadoDifuso.variableConjunto.llaveVariableLiguistica + "\n");
                                messages.append("Difusificación: Llave del conjunto: " + resultadoDifuso.variableConjunto.llaveConjunto + "\n");
                                messages.append("Difusificación: Resultado difuso: " + resultadoDifuso.valor + "\n\n");
                            }

                            m_muestraResultadosDifusos(arrayResultadosDifusos);
                            Centroide centroide = new Centroide(variablesLinguisticas);
                            messages.append("Resultado:"+centroide.procesar(resultados));
                            //m_muestraResultadosDifusos(arrayResultadosDifusos);

                        } catch (IOException ex) {
                            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                });

            }
        }
        );
        menuFuzzy.add(menuItem);

        //Reglas difusas-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        menuItem = new JMenuItem("Nueva regla difusa");

        menuItem.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae
            ) {
                int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la nueva regla difusa", JOptionPane.INPUT_VALUE_PROPERTY));
                ReglaDifusa regla;
                try {
                    if (reglasDifusas.existe(llave)) {
                        JOptionPane.showMessageDialog(cp, "Ya existe una regla difusa con esa llave.");
                    } else {
                        regla = new ReglaDifusa(llave);
                        new FormularioReglasDifusas(regla, reglasDifusas, false, variablesLinguisticas);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        menuReglas.add(menuItem);

        menuItem = new JMenuItem("Editar reglas difusas");

        menuItem.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae
            ) {
                int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la regla difusa a editar", JOptionPane.INPUT_VALUE_PROPERTY));
                ReglaDifusa regla = null, reglas[];
                try {
                    if (!reglasDifusas.existe(llave)) {
                        JOptionPane.showMessageDialog(cp, "No existe una regla difusa con esa llave.");
                    } else {
                        reglas = reglasDifusas.recuperarTodo();
                        for(int i = 0; i < reglas.length; i++){
                            if(reglas[i].llave == llave){
                                regla = reglas[i];
                            }
                        }
                        new FormularioReglasDifusas(regla, reglasDifusas, true, variablesLinguisticas);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        menuReglas.add(menuItem);

        menuItem = new JMenuItem("Borrar regla difusa");

        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la regla difusa a eliminar", JOptionPane.INPUT_VALUE_PROPERTY));
                ReglaDifusa regla;
                try {
                    if (!reglasDifusas.existe(llave)) {
                        JOptionPane.showMessageDialog(cp, "No existe una regla difusa con esa llave.");
                    } else {
                        reglasDifusas.elimina(llave);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuReglas.add(menuItem);

        menuItem = new JMenuItem("Listar reglas difusas");

        menuItem.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae
            ) {
                try {
                    messages.append("\n--------------------------------\n");
                    messages.append("\n" + reglasDifusas.mostrarTodo());

                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        menuReglas.add(menuItem);
        
        menuItem = new JMenuItem("Importar CSV");

        menuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                int result;
                File file, workingDirectory;
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                workingDirectory = new File(System.getProperty("user.dir"));
                chooser.setCurrentDirectory(workingDirectory);
                result = chooser.showOpenDialog(cp);
                int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Primera llave consecutiva", JOptionPane.INPUT_VALUE_PROPERTY));
                if(result == 0){
                    try {
                        file = chooser.getSelectedFile();
                        reglasDifusas.importar(file, llave);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        menuReglas.add(menuItem);

        return menuBar;
    }

    static JSlider getSlider(final JOptionPane optionPane) {
        JSlider slider = new JSlider();
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                JSlider theSlider = (JSlider) changeEvent.getSource();
                if (!theSlider.getValueIsAdjusting()) {
                    optionPane.setInputValue(new Integer(theSlider.getValue()));
                    valors.setText("" + theSlider.getValue());
                }
            }
        };
        slider.addChangeListener((javax.swing.event.ChangeListener) changeListener);
        return slider;
    }

    public void m_muestraResultadosDifusos(ArrayList<ResultadoDifuso> p_array) {
        for (int i = 0; i < p_array.size(); i++) {

            System.out.println("Llave variable linguistica: " + p_array.get(i).variableConjunto.llaveVariableLiguistica);
            System.out.println("Llave del conjunto: " + p_array.get(i).variableConjunto.llaveConjunto);
            System.out.println("Resultado difuso: " + p_array.get(i).valor + "\n");
            //System.out.println(p_array.get(i).variableConjunto);

        }

        ArchivoReglas archivo = new ArchivoReglas();

        try {
            List<ResultadoDifuso> resultados = MaxMin.procesar(Arrays.asList(archivo.recuperarTodo()), p_array);

            for (ResultadoDifuso resultadoDifuso : resultados) {
                System.out.println("Difusificación: Llave variable linguistica: " + resultadoDifuso.variableConjunto.llaveVariableLiguistica);
                System.out.println("Difusificación: Llave del conjunto: " + resultadoDifuso.variableConjunto.llaveConjunto);
                System.out.println("Difusificación: Resultado difuso: " + resultadoDifuso.valor);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void recuperaVariableLinguisticaResultado(Container cp) throws IOException {
        boolean existeArchivo = true;
        RandomAccessFile lector = null, escritor;
        try {
            lector = new RandomAccessFile("variableResultado", "r");
        } catch (FileNotFoundException e) {
            existeArchivo = false;
        }
        if (existeArchivo) {
            variableLinguisticaResultado = lector.readInt();
            lector.close();
        } else {
            escritor = new RandomAccessFile("variableResultado", "rw");
            variableLinguisticaResultado = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la variable difusa que indica el resultado", JOptionPane.INPUT_VALUE_PROPERTY));
            escritor.writeInt(variableLinguisticaResultado);
            escritor.close();
        }
    }
}
