package Metrics;

import java.util.ArrayList;

import NoteEnconding.NoteHerremans;
import NoteEnconding.Track;

public abstract class FuxMetrics {
	
	private static int highestPitch;
	private static ArrayList<NoteHerremans> climaxes;
	private static int climaxInNeighbouringTones;
	private static final int[] horizontalConsonantIntervals = {0,1,2,3,4,5,7,8,9,12};
	private static ArrayList<NoteHerremans> intervals;
	private static int nLargeLeaps;
	
	public static double fux1EightNotes (Track track) {
		int key = Constants.Constants.RANGE_MIN_PITCH;
		int extension = 12;
		int count8th = 0;
		int inStep8th = 0;
		for (NoteHerremans nh: track.getNoteSequence()) {
			if(nh.getMidiPitch()!=key) {
				if ((nh.getMidiPitch()-key)%extension == 0) {
					count8th++;
					inStep8th = inStep8th + notPrecededByStepwise(nh, track.getNoteSequence());
					inStep8th = inStep8th + notLeftByStepwise(nh, track.getNoteSequence());
				}
			}
		}
		return ((double) inStep8th)/(2*count8th);
	}
	
	public static double fux2OneClimax (Track track) {
		climaxInNeighbouringTones = 0;
		highestPitch = discoverHighestPitch(track.getNoteSequence());
		climaxes = discoverClimaxes(track.getNoteSequence(), highestPitch);
		for (NoteHerremans nh: climaxes) {
			int indexOfnh = track.getNoteSequence().indexOf(nh);
			if (indexOfnh+2 < track.getNoteSequence().size()-1) {
				if (track.getNoteSequence().get(indexOfnh+2).getMidiPitch() == nh.getMidiPitch())
					if (notLeftByStepwise(nh, track.getNoteSequence()) == 0 ) {
						climaxInNeighbouringTones++;
						climaxes.remove(climaxes.indexOf(nh)+1);
					}
			}
				
		}
		return ((double) (climaxes.size()-1))/((double) track.getBarNumber());
	}
	
	public static double fux3ClimaxOnStrongBeat (Track track) {
		int nClimaxOnWeakBeat = 0;
		for (NoteHerremans nh: climaxes) {
			int indexOfnh = track.getNoteSequence().indexOf(nh);
			if (indexOfnh > 0 )
				if (nh.getMeasure() == track.getNoteSequence().get(indexOfnh-1).getMeasure())
					nClimaxOnWeakBeat++;
		}
		return ((double) nClimaxOnWeakBeat)/climaxes.size();
	}
	
	public static double fux4HorizontalConsonantIntervals (Track track) {
		int nAllowedIntervals = 0;
		intervals = MusicMetricsUtil.convertMelodicInterval(track.getNoteSequence());
		for (NoteHerremans nh: intervals)
			nAllowedIntervals += allowedInterval(nh.getMidiPitch());
		return 1-((double) nAllowedIntervals)/intervals.size();
	}
	
	public static double fux5ConjunctStepwise (Track track) {
		int nLeaps = 0;
		for (NoteHerremans nh: intervals)
			nLeaps += isLeap(nh.getMidiPitch());
		double L = track.getBarNumber()/Constants.Constants.FUX_LENGTH_BARS;
		if (nLeaps < 2*L)
			return nLeaps/(track.getBarNumber()-1-(4*L));
		else if (nLeaps <= 4*L)
			return 0;
		else 
			return (nLeaps - (4*L))/(track.getBarNumber()-1-(4*L));
	}
	
	public static double fux6LargeLeapFollStepwise (Track track) {
		int nLLnotFollStep = 0;
		nLargeLeaps = 0;
		for (NoteHerremans nh: intervals.subList(0, intervals.size()-2)) {
			if (isLargLeap(nh.getMidiPitch()) == 1) {
				nLargeLeaps++;
				nLLnotFollStep += notLeftByStepwise(nh, track.getNoteSequence());
			}
		}
		return ((double) nLLnotFollStep)/nLargeLeaps;
	}
	
	public static double fux7LargeLeapFollStepwise (Track track) {
		int nLLnotChangedDir = 0;
		//int nLargeLeaps = 0;
		for (NoteHerremans nh: intervals.subList(0, intervals.size()-2)) {
			if (isLargLeap(nh.getMidiPitch()) == 1) {
				//nLargeLeaps++;
				if (nh.getMidiPitch()*intervals.get(intervals.indexOf(nh)+1).getMidiPitch() >0)
					nLLnotChangedDir++;
			}
		}
		return ((double) nLLnotChangedDir)/nLargeLeaps;
	}
	
	public static double fux8ClimaxConsonantTonic (Track track) {
		return (allowedInterval((highestPitch-Constants.Constants.RANGE_MIN_PITCH)%12));
	}
	
	public static double fux10MaxTwoLargeLeaps(Track track) {
		double L = track.getBarNumber()/Constants.Constants.FUX_LENGTH_BARS;
		if (nLargeLeaps <= 2*L)
			return 0;
		else 
			return (nLargeLeaps - (2*L))/(track.getBarNumber()-1-(2*L));
	}
	
	private static int isLargLeap (int interval) {
		if (Math.abs(interval) > 4)
			return 1;
		return 0;
	}
	
	private static int isLeap (int interval) {
		if (Math.abs(interval) > 2)
			return 1;
		return 0;
	}
	
	private static int allowedInterval (int interval) {
		for (int i = 0; i< horizontalConsonantIntervals.length; i++)
			if (interval == horizontalConsonantIntervals[i])
				return 1;
		return 0;
	}
	
	private static int notPrecededByStepwise(NoteHerremans nh, ArrayList<NoteHerremans> nhs) {
		int indexNh = nhs.indexOf(nh);
		if (indexNh > 0) {
			if (nhs.get(indexNh-1).getMidiPitch() == nh.getMidiPitch())
				return 1;
			if (Math.abs(nhs.get(indexNh-1).getMidiPitch()-nh.getMidiPitch()) > 2)
				return 1;
		}
		return 0;
	}
	
	private static int notLeftByStepwise(NoteHerremans nh, ArrayList<NoteHerremans> nhs) {
		int indexNh = nhs.indexOf(nh);
		if (indexNh < nhs.size()-1) {
			if (nhs.get(indexNh+1).getMidiPitch() == nh.getMidiPitch())
				return 1;
			if (Math.abs(nhs.get(indexNh+1).getMidiPitch()-nh.getMidiPitch()) > 2)
				return 1;
		}
		return 0;
	}
	
	private static int discoverHighestPitch (ArrayList<NoteHerremans> nhs) {
		highestPitch = Integer.MIN_VALUE;
		for (NoteHerremans nh: nhs) {
			if (nh.getMidiPitch() > highestPitch)
				highestPitch = nh.getMidiPitch();
		}
		return highestPitch;
	}
	
	private static ArrayList<NoteHerremans> discoverClimaxes(ArrayList<NoteHerremans> nhs, int climaxPitch) {
		climaxes = new ArrayList<NoteHerremans>();
		for (NoteHerremans nh: nhs) {
			if(nh.getMidiPitch() == climaxPitch)
				climaxes.add(nh);
		}
		return climaxes;
	}
	
	public static void clearFuxMetrics() {
		highestPitch = Integer.MIN_VALUE;
		climaxes = new ArrayList<NoteHerremans>();
	}
	
	public static void setupFuxMetrics(Track track) {
		highestPitch = discoverHighestPitch(track.getNoteSequence());
		climaxes = discoverClimaxes(track.getNoteSequence(), highestPitch);
	}
	
}
