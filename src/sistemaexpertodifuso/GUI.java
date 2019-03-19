package sistemaexpertodifuso;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;
import javax.swing.JDialog;
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
    static JLabel valor;
    JTextField[] valores;

    /*
      ____ _   _ ___ 
     / ___| | | |_ _|
    | |  _| | | || | 
    | |_| | |_| || | 
     \____|\___/|___|*/
    GUI(VariablesLinguisticas variablesLinguisticas, ArchivoReglas reglasDifusas) throws IOException {
        this.variablesLinguisticas = variablesLinguisticas;
        this.reglasDifusas = reglasDifusas;
        Container cp = getContentPane();
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
        menuFuzzy = new JMenu("Difuzificar");
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
                JOptionPane.showMessageDialog(cp, reglas);
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

        //Evaluación-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        menuItem = new JMenuItem("Evaluar variable lingüistica");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    valor = new JLabel("");
                    int valor_de_entrada = 0;
                    int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la variable linguistica a evaluar:", JOptionPane.INPUT_VALUE_PROPERTY));
                    VariableLinguistica variable = variablesLinguisticas.recuperarAleatorio(llave);
                    if (variable != null) {
                        JOptionPane optionPane = new JOptionPane();

                        JSlider slider = getSlider(optionPane);
                        optionPane.setMessage(new Object[]{"Ingresa el valor a evaluar:", slider});
                        optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
                        optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
                        optionPane.add(valor);
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

        menuItem = new JMenuItem("Difizificar todas");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //Obtener datos de las variables linguisticas
                VariableLinguistica variables[] = null;
                int num_var;
                JButton btnDifuzificar;
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

                    pnlVarLin.add(llaves[i]);
                    pnlVarLin.add(nombres[i]);
                    pnlVarLin.add(sliders[i]);
                    pnlVarLin.add(valores[i]);

                }
                Container cp = getContentPane();

                panelVariables = new JPanel();
                JScrollPane codeScrollPane = new JScrollPane(pnlVarLin);
                cp.add(codeScrollPane, BorderLayout.CENTER);
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
                        new FormularioReglasDifusas(regla, reglasDifusas, false);
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
                ReglaDifusa regla;
                try {
                    if (!reglasDifusas.existe(llave)) {
                        JOptionPane.showMessageDialog(cp, "No existe una regla difusa con esa llave.");
                    } else {
                        System.out.println("Aqui se edita la regla ");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        menuReglas.add(menuItem);

        menuItem = new JMenuItem("Borrar regla difusa");

        menuItem.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae
            ) {
                int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la regla difusa a eliminar", JOptionPane.INPUT_VALUE_PROPERTY));
                ReglaDifusa regla;
                try {
                    if (!reglasDifusas.existe(llave)) {
                        JOptionPane.showMessageDialog(cp, "No existe una regla difusa con esa llave.");
                    } else {
                        System.out.println("Aqui se borra la regla ");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
        menuReglas.add(menuItem);

        menuItem = new JMenuItem("Listar reglas difusas");

        menuItem.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae
            ) {
                try {
                    messages.append("\n--------------------------------\n");
                    messages.append(reglasDifusas.muestra_reglasDifusas());
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
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
                    valor.setText("" + theSlider.getValue());
                }
            }
        };
        slider.addChangeListener((javax.swing.event.ChangeListener) changeListener);
        return slider;
    }
}
