package sistemaexperto;

import java.util.ArrayList;

public class BaseDeHechos {
    ArrayList<String> hechos;

    public BaseDeHechos() {
        hechos = new ArrayList<>();
    }
    
    void agregarHecho(String hecho){
        hechos.add(hecho);
    }
    String[] regresaHechos(){
        String[] base = new String[hechos.size()];
        for(int i = 0; i < base.length; i++){
            base[i] = hechos.get(i);
        }
        return base;
    }
}
