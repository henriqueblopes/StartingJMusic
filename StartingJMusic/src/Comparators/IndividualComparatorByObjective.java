package Comparators;

import java.util.Comparator;

import GeneticTools.Individual;

public class IndividualComparatorByObjective implements Comparator<Individual>{
	private int objective;
	public IndividualComparatorByObjective (int objective) {
		this.objective = objective;
	}
	
	@Override
	public int compare(Individual arg0, Individual arg1) {
		if (Double.isNaN(arg0.getCrowdingdistance()) || Double.isNaN(arg1.getCrowdingdistance()))
			System.out.println("Pode ser que de pau, comparator objective");
		if (arg0.fitnesses[objective] - arg1.fitnesses[objective] < 0)
			return -1;
		else if (arg0.fitnesses[objective] - arg1.fitnesses[objective] == 0)
			return 0;
		else return 1;
	}

}
