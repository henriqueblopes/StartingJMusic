package Metrics;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import NoteEnconding.NoteHerremans;
import NoteEnconding.Track;

public abstract class FuxMetrics {
	
	private static int highestPitch;
	private static ArrayList<NoteHerremans> climaxes;
	private static int climaxInNeighbouringTones;
	private static final int[] horizontalConsonantIntervals = {0,1,2,3,4,5,7,8,9,12};
	private static ArrayList<NoteHerremans> intervals;
	private static int nLargeLeaps;
	private static double L;
	
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
		if (count8th > 0)
			return ((double) inStep8th)/(2*count8th);
		else
			return 0.0;
	}
	
	public static double fux2OneClimax (Track track) {
		climaxInNeighbouringTones = 0;
		highestPitch = discoverHighestPitch(track.getNoteSequence());
		climaxes = discoverClimaxes(track.getNoteSequence(), highestPitch);
		ArrayList<NoteHerremans> toRemove = new ArrayList<NoteHerremans>();
		try {
		for (NoteHerremans nh: climaxes) {
			int indexOfnh = track.getNoteSequence().indexOf(nh);
			if (indexOfnh+2 < track.getNoteSequence().size()-1) {
				if (track.getNoteSequence().get(indexOfnh+2).getMidiPitch() == nh.getMidiPitch())
					if (notLeftByStepwise(nh, track.getNoteSequence()) == 0 ) {
						climaxInNeighbouringTones++;
						toRemove.add(track.getNoteSequence().get(indexOfnh+2));
						//climaxes.remove(climaxes.indexOf(nh)+1);
					}
			}
				
		}
		climaxes.removeAll(toRemove);
		}
		catch (Exception e) {
			e.printStackTrace();
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
	
	//Testar daqui pra baixo
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
		L = ((double)track.getBarNumber())/Constants.Constants.FUX_LENGTH_BARS;
		if (nLeaps < 2*L)
			return nLeaps/(track.getBarNumber()-1-(4*L));
		else if (nLeaps <= 4*L)
			return 0;
		else 
			return (nLeaps - (4*L))/(track.getNoteSequence().size()-1-(4*L));
	}
	
	public static double fux6LargeLeapFollStepwise (Track track) {
		int nLLnotFollStep = 0;
		nLargeLeaps = 0;
		for (NoteHerremans nh: intervals.subList(0, intervals.size()-1)) {
			if (isLargLeap(nh.getMidiPitch()) == 1) {
				nLargeLeaps++;
				int indexOfStep = intervals.indexOf(nh)+1;
				nLLnotFollStep += notLeftByStepwise(track.getNoteSequence().get(indexOfStep), track.getNoteSequence());
			}
		}
		if (nLargeLeaps == 0)
			return 0.0;
		return ((double) nLLnotFollStep)/nLargeLeaps;
	}
	
	public static double fux7LargeLeapFollStepwise (Track track) {
		int nLLnotChangedDir = 0;
		//int nLargeLeaps = 0;
		for (NoteHerremans nh: intervals.subList(0, intervals.size()-1)) {
			if (isLargLeap(nh.getMidiPitch()) == 1) {
				//nLargeLeaps++;
				if (nh.getMidiPitch()*intervals.get(intervals.indexOf(nh)+1).getMidiPitch() >= 0)
					nLLnotChangedDir++;
			}
		}
		if(nLargeLeaps == 0)
			return 0.0;
		return ((double) nLLnotChangedDir)/nLargeLeaps;
	}
	
	public static double fux8ClimaxConsonantTonic (Track track) {
		return (allowedInterval((highestPitch-Constants.Constants.RANGE_MIN_PITCH)%12));
	}
	
	public static double fux9MaxTwoConsecutiveLeaps (Track track) {
		int nConsecutive3Leaps = 0;
		for (NoteHerremans nh: intervals.subList(0, intervals.size()-2)) {
			int nLeaps = 0;
			nLeaps += isLeap(nh.getMidiPitch());
			nLeaps += isLeap(intervals.get(intervals.indexOf(nh)+1).getMidiPitch());
			nLeaps += isLeap(intervals.get(intervals.indexOf(nh)+2).getMidiPitch());
			if (nLeaps == 3) 
				nConsecutive3Leaps++;
		}
		return ((double) nConsecutive3Leaps)/(track.getNoteSequence().size()-3);
	}
	
	public static double fux10MaxTwoLargeLeaps(Track track) {
		double L = track.getBarNumber()/Constants.Constants.FUX_LENGTH_BARS;
		if (nLargeLeaps <= 2*L)
			return 0;
		else 
			return (nLargeLeaps - (2*L))/(track.getNoteSequence().size()-1-(2*L));
	}
	
	public static double fux11LongStepwise(Track track) {
		int maxStepwiseSameDir = 0;
		for (NoteHerremans nh: intervals) {
			if (isStepwiseMotion(nh.getMidiPitch()) == 1) {
				int nStepwise = 1;
				NoteHerremans auxPastNote = nh;
				for (NoteHerremans nh2: intervals.subList(intervals.indexOf(nh)+1, intervals.size())) {
					if (isStepwiseMotion(nh2.getMidiPitch()) == 1) {
						if (isStepwiseSameDirection(auxPastNote.getMidiPitch(), nh2.getMidiPitch()) == 1) {
							nStepwise++;
							auxPastNote = nh2;
						}
						else 
							break;
					}
					else 
						break;
				}
				if (nStepwise > maxStepwiseSameDir) {
					maxStepwiseSameDir = nStepwise;
				}
			}
		}
		if (maxStepwiseSameDir > 5)
			return ((double) maxStepwiseSameDir)/track.getNoteSequence().size();
		return 0;
	}
	
	public static double fux12ChangedDirections (Track track) {
		int nChangedDirections = 0;
		for (NoteHerremans nh: intervals.subList(0, intervals.size()-1)) {
			if (nh.getMidiPitch() != 0) {
				for (NoteHerremans nh2: intervals.subList(intervals.indexOf(nh)+1, intervals.size())) {
					if (nh2.getMidiPitch() != 0) {
						if(isStepwiseSameDirection(nh.getMidiPitch(), nh2.getMidiPitch()) == 0)
							nChangedDirections++;
						break;
					}
				}
			}
			
			
		}
		if (nChangedDirections < 3*L)
			return 1-(((double) nChangedDirections)/(3*L));
		return 0.0;
	}
	
	public static double fux13TonicEndNote (Track track) {
		int key = Constants.Constants.RANGE_MIN_PITCH;
		if ((track.getNoteSequence().get(track.getNoteSequence().size()-1).getMidiPitch()- key)%12 != 0)
			return 1.0;
		return 0.0;
	}
	
	public static double fux14PenultimateLeadingTone (Track track) {
		//funciona apenas para escalas maiores
		int key = Constants.Constants.RANGE_MIN_PITCH;
		int penultimateTone = track.getNoteSequence().get(track.getNoteSequence().size()-2).getMidiPitch();
		if ((penultimateTone - key)%12 == 11) {
			int lastTone = track.getNoteSequence().get(track.getNoteSequence().size()-1).getMidiPitch();
			if (penultimateTone/12 == lastTone/12)
				return 0.0;
			else {
				if (lastTone%12 == 0 && penultimateTone/12 +1 == lastTone/12)
					return 0.0;
			}
			return 0.5;
		}
		return 1.0;
	}
	
	public static double fux15ConsonantMotionInterval (Track track) {
		int nDissonanteMotionInterval = 0;
		int nMotionIntervals = 0;
		NoteHerremans nh2 = intervals.get(0);
		int flagStart = 1;
		int toneStartMotion =0;
		for (NoteHerremans nh: intervals.subList(1, intervals.size())) {
			
			if (flagStart == 1) {
				toneStartMotion = track.getNoteSequence().get(intervals.indexOf(nh2)).getMidiPitch();
				flagStart = 0;
			}
			
			if (isStepwiseSameDirection(nh.getMidiPitch(), nh2.getMidiPitch()) == 0) {
				int toneEndMotion = track.getNoteSequence().get(intervals.indexOf(nh)).getMidiPitch();
				nMotionIntervals++;
				if(allowedInterval(toneEndMotion-toneStartMotion)==0)
					nDissonanteMotionInterval++;
				flagStart = 1;
			}
			nh2 = nh;
		}
		if(nMotionIntervals == 0)
			return 0.0;
		return ((double) nDissonanteMotionInterval)/nMotionIntervals;
	}
	
	public static double fux16LargeMotionInterval (Track track) {
		int nLargeMotionInterval = 0;
		int nMotionIntervals = 0;
		NoteHerremans nh2 = intervals.get(0);
		int flagStart = 1;
		int toneStartMotion =0;
		for (NoteHerremans nh: intervals.subList(1, intervals.size())) {
			
			if (flagStart == 1) {
				toneStartMotion = track.getNoteSequence().get(intervals.indexOf(nh2)).getMidiPitch();
				flagStart = 0;
			}
			if (isStepwiseSameDirection(nh.getMidiPitch(), nh2.getMidiPitch()) == 0) {
				int toneEndMotion = track.getNoteSequence().get(intervals.indexOf(nh)).getMidiPitch();
				nMotionIntervals++;
				if(Math.abs(toneEndMotion-toneStartMotion) >9)
					nLargeMotionInterval++;
				flagStart = 1;
			}
			nh2 = nh;
		}
		if(nMotionIntervals == 0)
			return 0.0;
		return ((double) nLargeMotionInterval)/nMotionIntervals;
	}
	
	//Não será necessário devido a exclusão de notas ligadas
	//public static double fux17Max4TiedNotes(Track track) {}
	
	//depois eu implemento esse, pois está gerando confusão na minha cabeça
	/*public static double fux18MaxAllowedSequences (Track track) {
		return 0.0;
	}*/
	
	public static double fux19LargestInterval (Track track) {
		int nLargeIntervals = 0;
		for (NoteHerremans nh: intervals) {
			if (Math.abs(nh.getMidiPitch()) > 12)
				nLargeIntervals++;
		}
		return ((double) nLargeIntervals)/intervals.size();
	}
	
	private static int isStepwiseSameDirection (int interval1, int interval2) {
		if (interval1*interval2 > 0)
			return 1;
		else
			return 0;
	}
	
	private static int isStepwiseMotion (int interval) {
		if (interval == 0)
			return 0;
		else if (Math.abs(interval) <= 2)
			return 1;
		else return 0;
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
			if (Math.abs(interval) == horizontalConsonantIntervals[i])
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
		L = track.getBarNumber()/Constants.Constants.FUX_LENGTH_BARS;
		intervals = MusicMetricsUtil.convertMelodicInterval(track.getNoteSequence());
	}
	
}
