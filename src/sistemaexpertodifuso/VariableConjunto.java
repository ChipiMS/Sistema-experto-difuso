package sistemaexpertodifuso;

public class VariableConjunto {
    int llaveVariableLiguistica, llaveConjunto;
    Conjunto conjunto;

    public VariableConjunto() {
    }

    public VariableConjunto(int llaveVariableLiguistica, int llaveConjunto) {
        this.llaveVariableLiguistica = llaveVariableLiguistica;
        this.llaveConjunto = llaveConjunto;
    }

    public int getLlaveVariableLiguistica() {
        return llaveVariableLiguistica;
    }

    public void setLlaveVariableLiguistica(int llaveVariableLiguistica) {
        this.llaveVariableLiguistica = llaveVariableLiguistica;
    }

    public int getLlaveConjunto() {
        return llaveConjunto;
    }

    public void setLlaveConjunto(int llaveConjunto) {
        this.llaveConjunto = llaveConjunto;
    }
    
    public String getClave() {
    	return llaveVariableLiguistica + "," + llaveConjunto;
    }
    
    public Conjunto getConjunto() {
    	if (conjunto == null) {
    		conjunto = new Conjunto();
    		conjunto.variableConjunto = this;
    		
    	}
    	
    	return conjunto; 
    }
}
