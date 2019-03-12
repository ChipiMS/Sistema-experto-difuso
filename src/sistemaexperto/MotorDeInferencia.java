package sistemaexperto;

import java.io.IOException;
import java.util.ArrayList;

public class MotorDeInferencia {
    String moduloJustificacion;
    BaseDeConocimientos BC;
    BaseDeHechos BH;
    Clausula[] clausulas;
    String[] variables,hechos;
    ElementoCC R;
    ConjuntoConflicto conjuntoConflicto;
    ElementoCC elementoCC;
    ArrayList<String> nombreVariablesClausula;
    ArrayList<String> valoresVariablesClausula;
    boolean[] reglasUsadas;

    public MotorDeInferencia(BaseDeConocimientos BC, BaseDeHechos BH) {
        this.BC = BC;
        this.BH = BH;
    }
    
    public String inferir(String meta) throws IOException{
        int i;
        moduloJustificacion = "";
        clausulas=BC.recuperarSecuencial();
        hechos=BH.regresaHechos();
        /*for(i = 0; i < clausulas.length; i++){
            System.out.println(clausulas[i].muestraClausula());
        }
        for(i = 0; i < hechos.length; i++){
            System.out.println(hechos[i]);
        }*/
        reglasUsadas = new boolean[clausulas.length];
        for(i = 0; i < reglasUsadas.length; i++){
            reglasUsadas[i] = false;
        }
        conjuntoConflicto = new ConjuntoConflicto();
        equiparar();
        while((meta.length() == 0 || !contenida(meta, hechos)) && !conjuntoConflicto.estaVacio()){
            if(!conjuntoConflicto.estaVacio()){
                String[] nuevosHechos;
                R=resolver();
                nuevosHechos=aplicar(R);
                actualizar(nuevosHechos);
            }
            hechos=BH.regresaHechos();
            equiparar();
        }
        if(meta.length() > 0){
            if(contenida(meta,hechos)){
                return meta+" es cierto.";
            }
            else{
                return meta+" es falso.";
            }
        }
        return "Fin de la inferencia";
    }
    
    private void equiparar(){
        /*Regla por regla hacer*/
        /*si regla no esta marcada o no esta en el conjunto conflicto analisar*/
        int i;
        for(i = 0; i < clausulas.length; i++){
            if(!reglasUsadas[i]){
                analisar(clausulas[i]);
                if(elementoCC.nombreVariables != null){ //si hay que agregar se agrega
                    //System.out.println("Sí agrega");
                    reglasUsadas[i] = true;
                    conjuntoConflicto.agregarElemento(elementoCC); //agregar elemento al conjunto conflicto
                }
            }
        }
    }
    
    private String[] analisar(Clausula clausula){
        /*Por cada predicado negado de una regla traer los hechos*/
        /*Todos los hechos del primer predicado ejecutar unificar*/
        //System.out.println(clausula.muestraClausula());
        int i, j;
        String predicadoNegado, nombrePredicadoNegado, nombreHecho;
        ArrayList<ArrayList<String>> hechosInvolucrados = new ArrayList<ArrayList<String>>();
        ArrayList<String> hechosDelPredicado, hechosDelPrimerPredicado;
        elementoCC = new ElementoCC();
        elementoCC.clausula = clausula;
        for(i = 0; i < clausula.predicadosNegados.length; i++){
            hechosDelPredicado = new ArrayList<String>();
            predicadoNegado = clausula.predicadosNegados[i];
            nombrePredicadoNegado = predicadoNegado.split("\\(")[0];
            if(predicadoNegado.charAt(0) != '*' && predicadoNegado.charAt(0) != ' '){
                //System.out.println(predicadoNegado);
                //System.out.println(nombrePredicadoNegado);
                hechosInvolucrados.add(hechosDelPredicado);
                for(j = 0; j < hechos.length; j++){
                    nombreHecho = hechos[j].split("\\(")[0];
                    if(nombreHecho.equals(nombrePredicadoNegado)){
                        //System.out.println("Mismo predicado");
                        hechosDelPredicado.add(hechos[j]);
                    }
                }
            }
        }
        /*for(i = 0; i < hechosInvolucrados.size(); i++){
            System.out.println("Ocurrencias predicado: "+i);
            for(j = 0; j < hechosInvolucrados.get(i).size(); j++){
                System.out.println(hechosInvolucrados.get(i).get(j));
            }
        }*/
        hechosDelPrimerPredicado = hechosInvolucrados.get(0);
        for(i = 0; i < hechosDelPrimerPredicado.size(); i++){
            nombreVariablesClausula = new ArrayList<String>();
            valoresVariablesClausula = new ArrayList<String>();
            unificar(hechosDelPrimerPredicado.get(i), 0, hechosInvolucrados);
        }
        return variables;
    }
    
    private void unificar(String hecho, int predicado, ArrayList<ArrayList<String>> hechosInvolucrados){
        /*variable por variable del hecho*/
        /*si la variable no esta agregada*/
            /*se agrega*/
        /*si no*//////////////////////////////////////////////////////////aqui voy
            /*se compara si es igual
              si no son iguales
              regresar error no se pudo*/
        /*fin variable por variable*/
        /*si el hecho es el ultimo predicado negado de la regla*/
            /*se aplica*/
        /*si no*/
            /*vuelve a unificar*/
        int i, j, k, agregados = 0;
        String[] variablesHecho = hecho.split("\\(")[1].split("\\)")[0].split(",");
        String[] nombresVariables = elementoCC.clausula.predicadosNegados[predicado].split("\\(")[1].split("\\)")[0].split(",");
                ArrayList<String> hechosDelSiguientePredicado;
        boolean variableEncontrada = false;
        for(i = 0; i < nombresVariables.length; i++) {
            for(j = 0; j < nombreVariablesClausula.size(); j++){
                if(nombresVariables[i].equals(nombreVariablesClausula.get(j))){
                    if(!variablesHecho[i].equals(valoresVariablesClausula.get(j))){ //no coinciden
                        for(k = 0; k < agregados; k++){
                            nombreVariablesClausula.remove(nombreVariablesClausula.size()-1);
                            valoresVariablesClausula.remove(valoresVariablesClausula.size()-1);
                        }
                        return;
                    }
                    variableEncontrada = true;
                }
            }
            if(!variableEncontrada){ //la variable no está agregada
                nombreVariablesClausula.add(nombresVariables[i]);
                valoresVariablesClausula.add(variablesHecho[i]);
                agregados++;
            }
        }
        if(predicado == hechosInvolucrados.size()-1){ //si es el último predicado negado
            String[] arregloDeStrings;
            arregloDeStrings = new String[nombreVariablesClausula.size()];
            for(i = 0; i < nombreVariablesClausula.size(); i++){
                arregloDeStrings[i] = nombreVariablesClausula.get(i);
            }
            elementoCC.nombreVariables = arregloDeStrings;
            arregloDeStrings = new String[valoresVariablesClausula.size()];
            for(i = 0; i < valoresVariablesClausula.size(); i++){
                arregloDeStrings[i] = valoresVariablesClausula.get(i);
            }
            elementoCC.variables.add(arregloDeStrings);
        }
        else{
            hechosDelSiguientePredicado = hechosInvolucrados.get(predicado+1);
            for(i = 0; i < hechosDelSiguientePredicado.size(); i++){
                unificar(hechosDelSiguientePredicado.get(i), predicado+1, hechosInvolucrados);
            }
        }
    }
    
    private boolean contenida(String meta,String[] hechos){
        for(int i=0;i<hechos.length;i++){
            if(hechos[i].equals(meta))
                return true;
        }
        return false;
    }
    
    private ElementoCC resolver(){
        ElementoCC R;
        R = conjuntoConflicto.elementos.get(0);
        return R;
    }
    
    private String[] aplicar(ElementoCC R){
        /*sustituir variable del predicado no negado
          con las variables[] eliminar regla usada del conjunto conflicto*/
        String[] nHechos = new String[R.variables.size()];
        String nombrePredicado = R.clausula.predicado.split("\\(")[0], nuevoHecho;
        String[] variablesPredicado = R.clausula.predicado.split("\\(")[1].split("\\)")[0].split(",");
        String justificacion = "";
        int i, j;
        for(i = 0; i < R.variables.size(); i++){
            for(j = 0; j < 16 && R.clausula.predicadosNegados[j].charAt(0) != '*' && R.clausula.predicadosNegados[j].charAt(0) != ' '; j++){
                if(j != 0){
                    justificacion += " ^ ";
                }
                justificacion += sustituye(R.clausula.predicadosNegados[j].split("\\(")[1].split("\\)")[0].split(","), R.clausula.predicadosNegados[j].split("\\(")[0], R.variables.get(i), R.nombreVariables);
            }
            justificacion += "->";
            nuevoHecho = sustituye(variablesPredicado, nombrePredicado, R.variables.get(i), R.nombreVariables);
            justificacion += nuevoHecho;
            moduloJustificacion += justificacion+"\n";
            nHechos[i] = nuevoHecho;
        }
        conjuntoConflicto.eliminarElemento(R); //eliminar regla usada del conjunto conflicto
        return nHechos;
    }
    
    private String sustituye(String[] variablesPredicado, String nombrePredicado, String[] valoresVariables, String[] nombreVariables){
        int j, k;
        String nuevoHecho = nombrePredicado+"(";
        for(j = 0; j < variablesPredicado.length; j++){
            if(j != 0){
                nuevoHecho += ",";
            }
            for(k = 0; k < nombreVariables.length; k++){
                if(nombreVariables[k].equals(variablesPredicado[j])){
                    nuevoHecho += valoresVariables[k];
                }
            }
        }
        nuevoHecho += ")";
        return nuevoHecho;
    }
    
    private void actualizar(String[] nuevosHechos){
        for(int i = 0; i < nuevosHechos.length; i++){
            BH.agregarHecho(nuevosHechos[i]);
        }
    }

}
