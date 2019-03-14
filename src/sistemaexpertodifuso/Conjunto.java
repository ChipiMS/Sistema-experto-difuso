package sistemaexpertodifuso;
class Conjunto{
    String nombre;
    Punto[] puntosCriticos;

    public Conjunto(){
        puntosCriticos = new Punto[4];
    }
    
    public Conjunto(boolean vacio){
        int i;
        nombre = "                    ";
        puntosCriticos = new Punto[4];
        for(i = 0; i < 4; i++){
            puntosCriticos[i] = new Punto(true);
        }
    }
}
