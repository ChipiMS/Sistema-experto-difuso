package sistemaexperto;

public class SustituirVariables {
    public static Clausula sustituye(Clausula regla, String meta){
        int i, j, k;
        Clausula reglaSustituida = new Clausula();
        String base = regla.predicado, predicadoNegado, nombrePredicadoNegado;
        String[] nombresBase = base.split("\\(")[1].split("\\)")[0].split(","), nombresVariables, variablesBase = meta.split("\\(")[1].split("\\)")[0].split(",");
        boolean sustituido;
        for(i = 0; i < regla.predicadosNegados.length; i++){
            predicadoNegado = regla.predicadosNegados[i];
            nombrePredicadoNegado = predicadoNegado.split("\\(")[0];
            if(predicadoNegado.charAt(0) != '*' && predicadoNegado.charAt(0) != ' '){
                nombresVariables = predicadoNegado.split("\\(")[1].split("\\)")[0].split(",");
                reglaSustituida.predicadosNegados[i] = nombrePredicadoNegado+"(";
                for(j = 0; j < nombresVariables.length; j++){
                    sustituido = false;
                    for(k = 0; k < nombresBase.length; k++){
                        if(nombresVariables[j].equals(nombresBase[k])){
                            reglaSustituida.predicadosNegados[i] += variablesBase[k];
                            sustituido = true;
                        }
                    }
                    if(!sustituido){
                        reglaSustituida.predicadosNegados[i] += nombresVariables[j];
                    }
                    if(j < nombresVariables.length-1){
                        reglaSustituida.predicadosNegados[i] += ",";
                    }
                }
                reglaSustituida.predicadosNegados[i] += ")";
            }
            else{
                reglaSustituida.predicadosNegados[i] = "******************************";
            }
        }
        reglaSustituida.predicado = meta;
        return reglaSustituida;
    }
}
