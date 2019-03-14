package sistemaexpertodifuso;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.swing.event.ChangeEvent;

public class GUI extends JFrame {

    VariablesLinguisticas variablesLinguisticas;
    JTextArea messages;
    JPanel panelHechos;
    static JLabel valor;

    /*
      ____ _   _ ___ 
     / ___| | | |_ _|
    | |  _| | | || | 
    | |_| | |_| || | 
     \____|\___/|___|*/
    GUI(VariablesLinguisticas variablesLinguisticas) throws IOException {
        this.variablesLinguisticas = variablesLinguisticas;
        Container cp = getContentPane();
        setSize(600, 600);
        setTitle("Sistema experto difuso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cp.add(createMenu(cp), BorderLayout.NORTH);

        panelHechos = new JPanel();
        JScrollPane codeScrollPane = new JScrollPane(panelHechos);
        cp.add(codeScrollPane, BorderLayout.CENTER);

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
        JMenu menuVariablesLinguisticas;
        JMenu menuFuzzy;
        JMenuItem menuItem, menuItemVL;
        menuBar = new JMenuBar();
        menuVariablesLinguisticas = new JMenu("Variables lingüisticas");
        menuFuzzy = new JMenu("Difuzificar");
        menuBar.add(menuVariablesLinguisticas);
        menuBar.add(menuFuzzy);

        //Variables lingüisticas-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        menuItem = new JMenuItem("Mostrar las variables lingüisticas");
        menuItemVL = new JMenuItem("Evaluar variable lingüistica");

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

        menuItemVL.addActionListener(new ActionListener() {
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

                        messages.append("Variable linguistica a evaluar:" + variable.nombre+"\n");
                        messages.append("Valor de entrada:" + valor_de_entrada+"\n");
                        messages.append("-----------------------------------------\n");
                       
                        for (int i = 0; i < 8; i++) {
                            if (variable.conjuntos[i] != null) {
                                
                                messages.append("Nombre: " + variable.conjuntos[i].nombre+"\n");
                                messages.append("Puntos Criticos:\n");
                                for (int j = 0; j < 4; j++) {
                                    if (variable.conjuntos[i].puntosCriticos[j] != null && variable.conjuntos[i].puntosCriticos[j].y != -1) {
                                        messages.append(variable.conjuntos[i].puntosCriticos[j].x + "," + variable.conjuntos[i].puntosCriticos[j].y+"\n");
                                    }
                                }
                                 messages.append("Grado de membresia: " + variable.conjuntos[i].evaluar((double)(valor_de_entrada))+"\n"); 
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

        menuVariablesLinguisticas.add(menuItem);
        menuFuzzy.add(menuItemVL);

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
                        JOptionPane.showMessageDialog(cp, "No existen una las variable lingüistica con esa llave.");
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
                    int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la la nueva variable lingüistica", JOptionPane.INPUT_VALUE_PROPERTY));
                    VariableLinguistica variable = variablesLinguisticas.recuperarAleatorio(llave);
                    if (variable == null) {
                        variable = new VariableLinguistica();
                        variable.llave = llave;
                        new FormularioVariablesLinguisticas(variable, variablesLinguisticas, false);
                    } else {
                        JOptionPane.showMessageDialog(cp, "Ya existen una variable lingüistica con esa llave.");
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
                        JOptionPane.showMessageDialog(cp, "No existen una variable lingüistica con esa llave.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuVariablesLinguisticas.add(menuItem);

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
