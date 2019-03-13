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
    ConjuntosDifusos conjuntosDifusos;
    JTextArea messages;
    JPanel panelHechos;
    /*
      ____ _   _ ___ 
     / ___| | | |_ _|
    | |  _| | | || | 
    | |_| | |_| || | 
     \____|\___/|___|*/
    GUI(ConjuntosDifusos conjuntosDifusos) throws IOException {
        this.conjuntosDifusos = conjuntosDifusos;
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
        JMenu menuConjuntosDifusos;
        JMenuItem menuItemClausula;
        menuBar = new JMenuBar();
        
        menuConjuntosDifusos = new JMenu("Conjuntos difusos");
        menuBar.add(menuConjuntosDifusos);
        
        //Conjuntos difusos-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        menuItemClausula = new JMenuItem("Mostrar los conjuntos");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Clausula clausulas[] = null;
                int i;
                String reglas = "";
                try {
                    clausulas = conjuntosDifusos.recuperarSecuencial();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (clausulas != null) {
                    for (i = 0; i < clausulas.length; i++) {
                        reglas += clausulas[i].muestraClausula() + "\n";
                    }
                } else {
                    reglas = "Hubo un error al recuperar los conjuntos.";
                }
                JOptionPane.showMessageDialog(cp, reglas);
            }
        });
        menuConjuntosDifusos.add(menuItemClausula);

        menuItemClausula = new JMenuItem("Actualizar conjuntos");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de los conjuntos a actualizar:", JOptionPane.INPUT_VALUE_PROPERTY));
                    Clausula clausula = conjuntosDifusos.recuperarAleatorio(llave);
                    if (clausula != null) {
                        new FormularioClausula(clausula, conjuntosDifusos, true);
                    } else {
                        JOptionPane.showMessageDialog(cp, "No existen unos conjuntos con esa llave.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuConjuntosDifusos.add(menuItemClausula);

        menuItemClausula = new JMenuItem("Nuevos conjuntos");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de los nuevos conjuntos", JOptionPane.INPUT_VALUE_PROPERTY));
                    Clausula clausula = conjuntosDifusos.recuperarAleatorio(llave);
                    if (clausula == null) {
                        clausula = new Clausula();
                        clausula.llave = llave;
                        new FormularioClausula(clausula, conjuntosDifusos, false);
                    } else {
                        JOptionPane.showMessageDialog(cp, "Ya existen unos conjuntos con esa llave.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuConjuntosDifusos.add(menuItemClausula);

        menuItemClausula = new JMenuItem("Borrar conjuntos");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de los conjuntos que se van a borrar", JOptionPane.INPUT_VALUE_PROPERTY));
                    Clausula clausula = conjuntosDifusos.recuperarAleatorio(llave);
                    if (clausula != null) {
                        conjuntosDifusos.borrar(llave);
                    } else {
                        JOptionPane.showMessageDialog(cp, "No existen unos conjuntos con esa llave.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuConjuntosDifusos.add(menuItemClausula);

        return menuBar;
    }
}
