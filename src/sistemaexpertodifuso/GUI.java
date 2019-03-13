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
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GUI extends JFrame {
    VariablesLinguisticas variablesLinguisticas;
    JTextArea messages;
    JPanel panelHechos;
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
        JMenuItem menuItem;
        menuBar = new JMenuBar();
        
        menuVariablesLinguisticas = new JMenu("Variables lingüisticas");
        menuBar.add(menuVariablesLinguisticas);
        
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
}
