package GeneticTools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

public abstract class Selection {
	public static Individual selection(ArrayList<Individual> population, String method) {
		//Class<?> parameterTypes;
		try {
			Class[] classes = new Class[] {ArrayList.class};
			Method m = Selection.class.getMethod(method, classes);			
			Object[] objs = new Object[1];
			objs[0] = population;
			try {
				return (Individual) m.invoke(m, objs);
				
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
	}
	
	public static Individual binaryTournament(ArrayList<Individual> population) {
		Random r = new Random();
		Individual i1 = population.get(r.nextInt(population.size()));
		Individual i2 = population.get(r.nextInt(population.size()));
		while (i1 == i2)
			i2 = population.get(r.nextInt(population.size()));
		if (i1.getFitness() >= i2.getFitness())
			return i1;
		else
			return i2;
	}
	
	public static Individual binaryTournamentCrowdedComparison(ArrayList<Individual> population) {
		Random r = new Random();
		Individual i1 = population.get(r.nextInt(population.size()));
		Individual i2 = population.get(r.nextInt(population.size()));
		while (i1 == i2)
			i2 = population.get(r.nextInt(population.size()));
		if (i1.getNonDominationRank() < i2.getNonDominationRank())
			return i1;
		else if (i1.getNonDominationRank() == i2.getNonDominationRank()) {
			if (i1.getCrowdingdistance() > i2.getCrowdingdistance())
				return i1;
			else {
				return i2;
			}
		}
		else
			return i2;
	}
}
