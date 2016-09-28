package Comparators;

import java.util.Comparator;

import GeneticTools.Individual;

public class IndividualComparatorNonDominationRank implements Comparator<Individual>{
	@Override
	public int compare(Individual arg0, Individual arg1) {
		return (arg0.getNonDominationRank() - arg1.getNonDominationRank());  
	}
}
