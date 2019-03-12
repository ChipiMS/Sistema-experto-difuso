/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemaexperto;

import java.io.IOException;

/**
 *
 * @author Adrian Rodriguez
 */
public class EvaluarRegla {
    
    BaseDeConocimientos BC;
    BaseDeHechos BH;
    boolean flag =true;
    static String meta, predicado_negado;
    
    public static boolean evaluarRegla(BaseDeConocimientos BC, BaseDeHechos BH, String meta, Clausula clausula) throws IOException
    {
        int i, ultimo = 0;
       String resultadoEvaluacion;
       Clausula cl_sustituida = SustituirVariables.sustituye(clausula,meta);//metodo para sustuir variables de la meta en la regla
       for(i=0;i<cl_sustituida.predicadosNegados.length;i++)
       {
           predicado_negado = cl_sustituida.predicadosNegados[i];
           if(predicado_negado.charAt(0) != '*' && predicado_negado.charAt(0) != ' '){
               //System.out.println( predicado_negado);
               //System.out.println(clausula.predicadosNegados[i]);
               ultimo = i;
               resultadoEvaluacion = EvaluarPredicado.evaluarPredicado( BC,  BH,  predicado_negado,clausula.predicadosNegados[i]);
               cl_sustituida.predicadosNegados[i] = resultadoEvaluacion;
               if(resultadoEvaluacion.length() == 0)
                   return false; //return false on evaluarRegla
           }    
       }
       EncadenamientoAtras.justificacion += "\n";
       for(i=0;i<cl_sustituida.predicadosNegados.length;i++)
       {
           predicado_negado = cl_sustituida.predicadosNegados[i];
           if(predicado_negado.charAt(0) != '*' && predicado_negado.charAt(0) != ' '){
               EncadenamientoAtras.justificacion += predicado_negado;
               if(i != ultimo){
                   EncadenamientoAtras.justificacion += "^";
               }
           }
       }
       EncadenamientoAtras.justificacion += "->"+meta;
       BH.agregarHecho(meta);
       return true; //todas pasaron
    }
    public static Clausula sustituir(Clausula regla, String meta)
    {
        return null;
    }
    public static boolean EvaluarPredicado(BaseDeConocimientos BC, BaseDeHechos BH, String meta,String cl)
    {
        return false;
    }
}
