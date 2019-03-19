package sistemaexpertodifuso;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class MaxMin {
	public Collection<ResultadoDifuso> procesar(List<ReglaDifusa> reglas, List<ResultadoDifuso> resultados) {
		Hashtable<VariableConjunto, ResultadoDifuso> hash = new Hashtable<VariableConjunto, ResultadoDifuso>();
		
		for (ReglaDifusa regla : reglas) {
			ResultadoDifuso resultadoNuevo = min(regla, resultados);
			ResultadoDifuso resultadoViejo = hash.get(regla.consecuente);
			
			/* Verifico si ya procese ese consecuente antes */
			if (resultadoViejo != null) {
				if (resultadoNuevo.valor > resultadoViejo.valor) {
					hash.put(regla.consecuente, resultadoNuevo);
				}
			} else {
				hash.put(regla.consecuente, resultadoNuevo);
			}
		}
		
		return hash.values();
	}
	
	private ResultadoDifuso min(ReglaDifusa regla, List<ResultadoDifuso> resultados) {
		Double minValor = 1.0;
		
		for (VariableConjunto variable : regla.antecedentes) {
			Double nuevoValor = matchearResultados(variable, resultados);
			
			if (minValor > nuevoValor) {
				minValor = nuevoValor;
			}
		}
		
		return new ResultadoDifuso(minValor, regla.consecuente);
	}
	
	private Double matchearResultados(VariableConjunto variable, List<ResultadoDifuso> resultados) {
		for (ResultadoDifuso resultadoDifuso : resultados) {
			if (resultadoDifuso.variableConjunto == variable) {
				return resultadoDifuso.valor;
			}
		}
		
		return 0.0;
	}
}
