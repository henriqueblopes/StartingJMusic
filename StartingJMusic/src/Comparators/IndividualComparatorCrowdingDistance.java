package Comparators;

import java.util.Comparator;

import GeneticTools.Individual;

public class IndividualComparatorCrowdingDistance implements Comparator<Individual>{
	@Override
	public int compare(Individual arg0, Individual arg1) {
		if (arg0.getCrowdingdistance() - arg1.getCrowdingdistance() < 0)
			return 1;
		else return -1;
	}
}
