package com.unicen.app;

import com.unicen.app.indicators.Indicator;
import com.unicen.app.indicators.Response;
import com.unicen.app.ahp.Decision;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AHP {
	
	/*
	 * elements: <id escuela, vector probabilidades> donde la posición "i" del vector
	 * tendrá el valor de probabilidad de la escuela respecto al indicador "i".
	 * 
	 * names: <Id escuela, nombre>. Para mapear el nombre y no tener que buscar en una
	 * lista de response todo el tiempo
	 */
	
	private HashMap <Integer, ArrayList<Double>> elements = new HashMap<Integer,ArrayList<Double>>();
	private HashMap <Integer, String> names = new HashMap<Integer,String>();
	private List<Indicator> indicators = new ArrayList<Indicator>();
	private double[] indicatorsPriorityVector;
	
	
	public AHP (List<Indicator> indicators, double[][] indicatorsComparisonMatrix) {
		
		this.indicators.addAll(indicators);
		
		this.initializeIndicatorsPriorityVector(indicatorsComparisonMatrix);
		
		this.initElementsAndNames();
		
	}
	
	/*
	 * Inicializa el vector de prioridades de los indicadores. Debe procesar la matriz
	 * que compara la importancia de los indicadores respecto a los demas
	 */
	private void initializeIndicatorsPriorityVector (double[][] indicatorsComparisonMatrix) {
		int n = indicators.size();
		double [] sumOfColumn = new double [n];
		
		for (int j=0; j<n; j++) {
			double sum = 0;
			for (int i=0; i<n; i++)
				sum += indicatorsComparisonMatrix[i][j];
			sumOfColumn[j] = sum;
		}
		
		//normalizing matrix
		for (int i=0; i<n; i++)
			for (int j=0; j<n; j++)
				indicatorsComparisonMatrix[i][j] = indicatorsComparisonMatrix[i][j]/sumOfColumn[j];
		
		//averaging across the rows
		indicatorsPriorityVector = new double[n];
		for (int i=0; i<n; i++) {
			double sum = 0;
			for (int j=0; j<n; j++)
				sum += indicatorsComparisonMatrix[i][j];
			indicatorsPriorityVector[i] = sum;
		}
		for (int i=0; i<n; i++) 
			indicatorsPriorityVector[i] = indicatorsPriorityVector[i]/n;
		
	}
	
	/*
	 * Inicializa la hash tomando como base la primera lista de response.
	 * Se llama desde el constructor
	 */
	private void initElementsAndNames () {
		List <Response> responses = new ArrayList<Response>();
		try {
			responses.addAll(indicators.get(0).evaluateAll());
		} catch (Exception e){}
		
		for (Response r : responses) {
			elements.put (r.getSchoolId(), new ArrayList<Double>());
			names.put(r.getSchoolId(), r.getSchoolName());
		}

	}
	
	/*
	 * Calcula el vector de prioridades de un indicador dado, pero lo deja 
	 * distribuido en la Hash de los elementos. Para eso crea la matriz
	 * correspondiente y calcula todo.
	 */
	private void calculateVectorForIndicator (int indicator) {
		Indicator actualIndicator = indicators.get(indicator);
		//obtaining responses from this indicator
		List <Response> responses = new ArrayList<Response>();


		try {
			responses.addAll(actualIndicator.evaluateAll());
		} catch (Exception e){
			App.throwError(e);
		}



		//creating the comparison matrix for this indicator
		int n = responses.size();
		double[][] matrix = new double[n][n];
		for (int i=0; i<n; i++)
			for (int j=0; j<n; j++) {
				double value1 = responses.get(i).getValue().doubleValue();
				double value2 = responses.get(j).getValue().doubleValue();
				matrix[i][j] = actualIndicator.getMatrixComparisonValue(value1,value2);
			}
		
		/*
		 * Starting to compute the priority vector for this matrix
		 */

		double [] priorityVector;
		
		//calculating the sum of columns
		double [] sumOfColumn = new double [n];
		
		for (int j=0; j<n; j++) {
			double sum = 0;
			for (int i=0; i<n; i++)
				sum += matrix[i][j];
			sumOfColumn[j] = sum;
		}
		
		//normalizing matrix
		for (int i=0; i<n; i++)
			for (int j=0; j<n; j++)
				matrix[i][j] = matrix[i][j]/sumOfColumn[j];
		
		//averaging across the rows
		priorityVector = new double[n];
		for (int i=0; i<n; i++) {
			double sum = 0;
			for (int j=0; j<n; j++)
				sum += matrix[i][j];
			priorityVector[i] = sum;
		}
		for (int i=0; i<n; i++) 
			priorityVector[i] = priorityVector[i]/n;
		
		/*
		 * The priority vector is already calculated. Now distributing the
		 * probability values across the elements map
		 */
		for (int i=0; i<n; i++) {
			Response r = responses.get(i);
			elements.get(r.getSchoolId()).add(new Double(priorityVector[i]));

			/*
			 * Aca estoy agregandole a la escuela el valor de probabilidad asociado al
			 * indicador actual. No hace falta indicar posicion en la lista, porque se agrega
			 * al final y se hace en orden. Asi que la probailidad del indicador "i" va a quedar en
			 * la posicion "i" de su lista
			 */
		}
	}
	
	/*
	 * Devuelve la probabilidad final de ese elemento (escuela). Para eso lo
	 * busca en la hash, obtiene sus probabilidades para cada indicador, y calcula
	 * la probabilidad como la combinación lineal de los vectores
	 */
	private double getProbability (Integer elementId) {
		double result = 0;
		ArrayList<Double> elementProbabilities = elements.get(elementId);
		
		//linear combination
		for (int i=0; i<elementProbabilities.size(); i++)
			result += elementProbabilities.get(i).doubleValue() * indicatorsPriorityVector[i];
		
		return result;
	}
	
	public List<Decision> calculateDecision () {
		List<Decision> decision = new ArrayList<Decision>();

		//se calculan todos los vectores de prioridad
		for (int i=0; i<indicators.size(); i++) 
			calculateVectorForIndicator(i);


		//se calcula la decision final y de paso se crea la lista a devolver
		for (Integer id : elements.keySet()) {
			decision.add(new Decision (id, names.get(id),this.getProbability(id)));
		}


		Collections.sort(decision);

		return decision;
	}
	
					
}
