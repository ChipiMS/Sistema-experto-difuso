package sistemaexperto;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class EncadenamientoAtras {
    public static String justificacion;
    public static String inferir(BaseDeConocimientos BC,BaseDeHechos BH,String meta) throws IOException {
        ArrayList<Clausula> reglas=new ArrayList<>();
        int reply;
        int i;
        reglas=busquedaConsecuente(meta,BC);
        for(i=0;i<reglas.size();i++){
            if(EvaluarRegla.evaluarRegla(BC,BH,meta,reglas.get(i)))
                return meta;
        }
        reply=JOptionPane.showConfirmDialog(null,"¿Tiene usted "+meta+"?","Pregunta",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(reply== JOptionPane.YES_OPTION){
            EncadenamientoAtras.justificacion += "\n"+meta+" fue proporcionado por el usuario.";
            BH.agregarHecho(meta);
            return meta;
        }
        return "";
    }
    private static ArrayList<Clausula> busquedaConsecuente(String meta,BaseDeConocimientos BC) throws IOException {
        int posicion;
        ArrayList<Clausula> cc=new ArrayList<>();
        Clausula[]Conocimiento=BC.recuperarSecuencial();

        for (posicion=0;posicion<Conocimiento.length;posicion++){
            if(Conocimiento[posicion].predicado.split("\\(")[0].equals(meta.split("\\(")[0]))
                cc.add(Conocimiento[posicion]);
        }
        return cc;
    }
}
