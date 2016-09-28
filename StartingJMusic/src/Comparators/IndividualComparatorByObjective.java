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
		if (arg0.fitnesses[objective] - arg1.fitnesses[objective] < 0)
			return 1;
		else return -1;
	}

}
