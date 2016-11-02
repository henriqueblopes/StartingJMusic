package Comparators;

import java.util.Comparator;

import GeneticTools.Individual;

public class IndividualComparatorCrowdingDistance implements Comparator<Individual>{
	@Override
	public int compare(Individual arg0, Individual arg1) {
		if (Double.isNaN(arg0.getCrowdingdistance()) || Double.isNaN(arg1.getCrowdingdistance()))
			System.out.println("Pode ser que de pau, comparator Crowding");
		try {
			return (int) (arg0.getCrowdingdistance() - arg1.getCrowdingdistance());
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
