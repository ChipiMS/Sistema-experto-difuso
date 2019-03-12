package sistemaexperto;

import java.io.IOException;

public class EvaluarPredicado {

    BaseDeConocimientos BC;
    BaseDeHechos BH;
    String meta;

    public static String evaluarPredicado(BaseDeConocimientos BC, BaseDeHechos BH, String meta, String regla) throws IOException {
        int j, v;
        String[]   variables, variables_meta, variables_regla;
        String[] hechos = BH.regresaHechos();
        String nombre_meta, hecho;
        boolean funciona;
        //Por cada hecho de la base de hechos
        for (j = 0; j < hechos.length; j++) {
            //Extraer el nombre de la meta y del hecho, y si son los mismos
            hecho = hechos[j].split("\\(")[0]; //nombre hecho                 .split("\\)")[0].split(",")
            nombre_meta = meta.split("\\(")[0];  //Nombre de la meta

            //Por cada hecho de la base de hechos
            if (hecho.equals(nombre_meta)) {
                variables_meta = meta.split("\\(")[1].split("\\)")[0].split(","); // variable de la meta
                variables = hechos[j].split("\\(")[1].split("\\)")[0].split(","); //variable del hecho
                funciona = true;
                for(v = 0; v < variables_meta.length; v++){
                    //Variable por variable de la meta
                    //Si es igual a la variable del hecho
                    //System.out.println(meta);
                    //System.out.println(hechos[j]);
                    //System.out.println(regla);
                    //System.out.println(variables[v]);
                    //System.out.println(variables_meta[v]);
                    //System.out.println(variables[v].equals(variables_meta[v]));
                    if(!variables[v].equals(variables_meta[v])){
                        //Si es igual la variable de la meta y el nombre de la variable en la regla
                        variables_regla = regla.split("\\(")[1].split("\\)")[0].split(","); //variable de la regla ---AQUI NO ENTENDI MUY BIEN
                        if (!variables_regla[v].equals(variables_meta[v])) {
                            funciona = false;
                        }
                    }
                }
                if(funciona){
                    return hechos[j];
                }
            }
        }
        return EncadenamientoAtras.inferir(BC, BH, meta);
    }

}
