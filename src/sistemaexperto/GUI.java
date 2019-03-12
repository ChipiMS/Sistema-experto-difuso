package sistemaexperto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class GUI extends JFrame {

    BaseDeConocimientos baseDeConocimientos;
    AccesoDominio ad = new AccesoDominio();
    AccesoPredicadoDAO ap = new AccesoPredicadoDAO();
    JTextArea messages;
    JPanel panelHechos;
    Predicado predicados[] = ap.Predicados();
    /*Predicado predicados[] = new Predicado[]{
        new Predicado("p sufrió ch", "b(p,ch)", new String[][]{
            new String[]{},
            new String[]{"Susto","Susto 2"}
        }),
        new Predicado("p sufre sh", "h(p,sh)", new String[][]{
            new String[]{},
            new String[]{"Miedo"}
        }),
        new Predicado("p padece histeria", "c(p)", new String[][]{
            new String[]{}
        }),
        new Predicado("p sufrió co", "d(p,co)", new String[][]{
            new String[]{},
            new String[]{"Susto"}
        }),
        new Predicado("p sufre so", "i(p,so)", new String[][]{
            new String[]{},
            new String[]{"Miedo"}
        }),
        new Predicado("p es obsesivo compulsivo", "e(p)", new String[][]{
            new String[]{}
        }),
        new Predicado("p sufrió cf", "f(p,cf)", new String[][]{
            new String[]{},
            new String[]{"Susto"}
        }),
        new Predicado("p sufre sf", "j(p,sf)", new String[][]{
            new String[]{},
            new String[]{"Miedo"}
        }),
        new Predicado("p tiene fobias", "g(p)", new String[][]{
            new String[]{}
        }),
        new Predicado("p padece neurosis", "a(p)", new String[][]{
            new String[]{}
        }),
        new Predicado("p necesita ser tratado psicológicamente", "k(p)", new String[][]{
            new String[]{}
        }),
        new Predicado("s es un sintoma físico", "m(s)", new String[][]{
            new String[]{"colitis"}
        }),
        new Predicado("p sufre s", "o(p,s)", new String[][]{
            new String[]{},
            new String[]{"colitis"}
        }),
        new Predicado("p necesita ser tratado psiquiatricamente", "l(p)", new String[][]{
            new String[]{}
        })
    };*/
    JButton botonesHechos[];
    JPanel panelesHechos[];
    Component variablesHechos[][];
    BaseDeHechos baseDeHechos;

    /*
      ____ _   _ ___ 
     / ___| | | |_ _|
    | |  _| | | || | 
    | |_| | |_| || | 
     \____|\___/|___|*/
    GUI(BaseDeConocimientos baseDeConocimientos, BaseDeHechos baseDeHechos) throws IOException {
        this.baseDeConocimientos = baseDeConocimientos;
        this.baseDeHechos = baseDeHechos;
        Container cp = getContentPane();
        setSize(600, 600);
        setTitle("Compilador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cp.add(createMenu(cp), BorderLayout.NORTH);

        panelHechos = new JPanel();
        interfazHechos(panelHechos);
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
        JMenu menuConocimientos;
        JMenu menuHechos;
        JMenu menuDominios, menuInferir;
        JMenuItem menuItemClausula;
        JMenuItem menuItemMuestra;
        JButton buttonCompile;
        JFrame frame = (JFrame) this;
        menuBar = new JMenuBar();
        
        menuConocimientos = new JMenu("Base de conocimientos");
        menuBar.add(menuConocimientos);

        menuHechos = new JMenu("Predicados");
        menuBar.add(menuHechos);

        menuDominios = new JMenu("Dominios");
        menuBar.add(menuDominios);
        
        menuInferir = new JMenu("Inferir");
        menuBar.add(menuInferir);
        
        //Base de conocimientos-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        menuItemClausula = new JMenuItem("Mostrar cláusulas");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Clausula clausulas[] = null;
                int i;
                String reglas = "";
                try {
                    clausulas = baseDeConocimientos.recuperarSecuencial();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (clausulas != null) {
                    for (i = 0; i < clausulas.length; i++) {
                        reglas += clausulas[i].muestraClausula() + "\n";
                    }
                } else {
                    reglas = "Hubo un error al recuperar las cláusulas.";
                }
                JOptionPane.showMessageDialog(cp, reglas);
            }
        });
        menuConocimientos.add(menuItemClausula);

        menuItemClausula = new JMenuItem("Actualizar cláusula");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la cláusula a actualizar:", JOptionPane.INPUT_VALUE_PROPERTY));
                    Clausula clausula = baseDeConocimientos.recuperarAleatorio(llave);
                    if (clausula != null) {
                        new FormularioClausula(clausula, baseDeConocimientos, true);
                    } else {
                        JOptionPane.showMessageDialog(cp, "No existe una cláusula con esa llave.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuConocimientos.add(menuItemClausula);

        menuItemClausula = new JMenuItem("Nueva cláusula");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la nueva cláusula", JOptionPane.INPUT_VALUE_PROPERTY));
                    Clausula clausula = baseDeConocimientos.recuperarAleatorio(llave);
                    if (clausula == null) {
                        clausula = new Clausula();
                        clausula.llave = llave;
                        new FormularioClausula(clausula, baseDeConocimientos, false);
                    } else {
                        JOptionPane.showMessageDialog(cp, "Ya existe una cláusula con esa llave.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuConocimientos.add(menuItemClausula);

        menuItemClausula = new JMenuItem("Borrar cláusula");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    int llave = Integer.parseInt(JOptionPane.showInputDialog(cp, "Llave de la cláusula que se va a borrar", JOptionPane.INPUT_VALUE_PROPERTY));
                    Clausula clausula = baseDeConocimientos.recuperarAleatorio(llave);
                    if (clausula != null) {
                        baseDeConocimientos.borrar(llave);
                    } else {
                        JOptionPane.showMessageDialog(cp, "No existe una cláusula con esa llave.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuConocimientos.add(menuItemClausula);

        //Base de hechos-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        menuItemMuestra = new JMenuItem("Mostrar Predicado");
        menuItemMuestra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                try {
                    //ap.Predicados();
                    JOptionPane.showMessageDialog(cp, ap.mostrar());
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuHechos.add(menuItemMuestra);

        menuConocimientos.add(menuItemClausula);
        menuItemClausula = new JMenuItem("Agregar Predicado");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            
                    ap.guardar();
     
            }
        });
        menuHechos.add(menuItemClausula);

        menuItemClausula = new JMenuItem("Eliminar Predicado");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    ap = new AccesoPredicadoDAO();
                    ap.eliminar();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuHechos.add(menuItemClausula);

        menuItemClausula = new JMenuItem("Modificar predicado");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    ap = new AccesoPredicadoDAO();
                    ap.Editar();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuHechos.add(menuItemClausula);
        
        //Dominios-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        menuItemClausula = new JMenuItem("Ver dominios");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    
                    JOptionPane.showMessageDialog(cp, ad.mostrar());
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuDominios.add(menuItemClausula);

        menuItemClausula = new JMenuItem("Agregar Dominio");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ad.guardar();
            }
        });
        menuDominios.add(menuItemClausula);

        menuItemClausula = new JMenuItem("Modificar dominio");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    ad.Editar();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuDominios.add(menuItemClausula);

        menuItemClausula = new JMenuItem("Eliminar dominio");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    ad.eliminar();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuDominios.add(menuItemClausula);

        //Inferir-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        menuItemClausula = new JMenuItem("Con encadenamiento hacia adelante");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    MotorDeInferencia motor = new MotorDeInferencia(baseDeConocimientos, baseDeHechos);
                    String meta = JOptionPane.showInputDialog(cp, "Escribe una meta o deja vacío para inferir sin meta.", JOptionPane.INPUT_VALUE_PROPERTY), mensaje;
                    if(meta != null){
                        mensaje = motor.inferir(meta);
                        messages.append("\n" + motor.moduloJustificacion);
                        messages.append(mensaje);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuInferir.add(menuItemClausula);
        
        menuItemClausula = new JMenuItem("Con encadenamiento hacia atrás");
        menuItemClausula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    String meta = JOptionPane.showInputDialog(cp, "Escribe una meta por favor.", JOptionPane.INPUT_VALUE_PROPERTY), mensaje;
                    if(meta != null && meta.length() > 0){
                        EncadenamientoAtras.justificacion = "";
                        mensaje = EncadenamientoAtras.inferir(baseDeConocimientos, baseDeHechos, meta);
                        messages.append(EncadenamientoAtras.justificacion);
                        if(mensaje.length() > 0){
                            messages.append("\n"+meta+" es cierto.");
                        }
                        else{
                            messages.append("\n"+meta+" es falso.");
                        }
                        //Módulo de justificación
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuInferir.add(menuItemClausula);

        return menuBar;
    }

    private void interfazHechos(JPanel panelHechos) {
        panelHechos.setLayout(new BoxLayout(panelHechos, BoxLayout.PAGE_AXIS));
        int i, j, k;
        JPanel predicado;
        String[] nombreVariables;
        panelesHechos = new JPanel[predicados.length];
        botonesHechos = new JButton[predicados.length];
        variablesHechos = new Component[predicados.length][10];
        JComboBox combo;
        for (i = 0; i < predicados.length; i++) {
            panelesHechos[i] = new JPanel();
            panelesHechos[i].setLayout(new BoxLayout(panelesHechos[i], BoxLayout.PAGE_AXIS));
            panelesHechos[i].setAlignmentX(LEFT_ALIGNMENT);
            panelesHechos[i].setBorder(new EmptyBorder(10, 10, 10, 10));
            panelesHechos[i].add(new JLabel(predicados[i].predicado + ": " + predicados[i].nombre));
            nombreVariables = predicados[i].predicado.split("\\(")[1].split("\\)")[0].split(",");
            predicado = new JPanel();
            predicado.setLayout(new BoxLayout(predicado, BoxLayout.LINE_AXIS));
            predicado.setAlignmentX(LEFT_ALIGNMENT);
            for (j = 0; j < predicados[i].variables.length; j++) {
                predicado.add(new JLabel(nombreVariables[j] + ":"));
                if (predicados[i].variables[j].length == 0) {
                    variablesHechos[i][j] = new JTextField();
                    variablesHechos[i][j].setPreferredSize(new Dimension(100, 30));
                } else {
                    combo = new JComboBox<>();
                    for (k = 0; k < predicados[i].variables[j].length; k++) {
                        combo.addItem(predicados[i].variables[j][k]);
                    }
                    variablesHechos[i][j] = combo;
                }
                predicado.add(variablesHechos[i][j]);
            }
            panelesHechos[i].add(predicado);
            botonesHechos[i] = new JButton("Agregar hecho");
            botonesHechos[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    int i, indicePredicado = 0;
                    String hecho;
                    for (i = 0; i < predicados.length; i++) {
                        if (ae.getSource() == botonesHechos[i]) {
                            indicePredicado = i;
                        }
                    }
                    hecho = predicados[indicePredicado].predicado.split("\\(")[0];
                    hecho += "(";
                    for (i = 0; i < predicados[indicePredicado].variables.length; i++) {
                        if (i > 0) {
                            hecho += ",";
                        }
                        if (predicados[indicePredicado].variables[i].length == 0) {
                            hecho += ((JTextField) variablesHechos[indicePredicado][i]).getText();
                        } else {
                            hecho += ((JComboBox) variablesHechos[indicePredicado][i]).getSelectedItem();
                        }
                    }
                    hecho += ")";
                    panelesHechos[indicePredicado].add(new JLabel(hecho));
                    baseDeHechos.agregarHecho(hecho);
                    panelHechos.repaint();
                    panelHechos.revalidate();
                }
            });
            panelesHechos[i].add(botonesHechos[i]);
            panelHechos.add(panelesHechos[i]);
        }
    }
}
