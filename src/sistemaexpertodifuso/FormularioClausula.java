package sistemaexpertodifuso;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class FormularioClausula extends JDialog{
    JButton button;
    JTextField predicadosNegados[];
    JTextField predicadoNoNegado;

    public FormularioClausula(Clausula clausula, ConjuntosDifusos baseDeConocimientos, boolean actualiza){
        setLayout(new FlowLayout());
        int i;
        String predicado;
        predicadosNegados = new JTextField[16];
        for(i = 0; i < 16; i++){
            predicado = "";
            if(clausula.predicadosNegados[i] != null && clausula.predicadosNegados[i].length() > 0 && clausula.predicadosNegados[i].charAt(0) != '*'){
                predicado = clausula.predicadosNegados[i].trim();
            }
            if(i != 0){
                add(new JLabel("^"));
            }
            predicadosNegados[i] = new JTextField(predicado);
            predicadosNegados[i].setPreferredSize(new Dimension(100, 30));
            add(predicadosNegados[i]);
        }
        
        add(new JLabel("->"));
        
        predicado = "";
        if(clausula.predicado != null && clausula.predicado.length() > 0 && clausula.predicado.charAt(0) != '*'){
            predicado = clausula.predicado.trim();
        }
        predicadoNoNegado = new JTextField(predicado);
        predicadoNoNegado.setPreferredSize(new Dimension(100, 30));
        add(predicadoNoNegado);
        
        button = new JButton("Terminar");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    StringBuffer buffer;
                    int i;
                    for(i = 0; i < 16; i++){
                        buffer = new StringBuffer(predicadosNegados[i].getText());
                        buffer.setLength(30);
                        clausula.predicadosNegados[i] = buffer.toString();
                    }
                    buffer = new StringBuffer(predicadoNoNegado.getText());
                    buffer.setLength(30);
                    clausula.predicado = buffer.toString();
                    if(actualiza){
                        baseDeConocimientos.actualizar(clausula);
                    }
                    else{
                        baseDeConocimientos.insertar(clausula);
                    }
                    dispose();
                } catch (IOException ex) {
                    Logger.getLogger(FormularioClausula.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        add(button);
        
        setPreferredSize(new Dimension(600, 200));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
        setVisible(true);
    }
}