package GeneticTools;

import java.io.ObjectInputStream.GetField;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.apache.commons.math3.analysis.function.Constant;

import Comparators.NoteForCountComparatorDesc;
import Constants.Constants;
import Constants.MutationConstants;
import Metrics.NoteForCount;
import Metrics.NoteForCountComparator;
import NoteEnconding.NoteHerremans;

public class Mutation {
	public static Individual mutation(Individual i, String method) {
		try {
			Class[] classes = new Class[] {Individual.class};
			Method m = Mutation.class.getMethod(method, classes);			
			Object[] objs = new Object[] {i};
			
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
	
	public static Individual changeOneNote(Individual i) {
		Random r = new Random();
		int a = r.nextInt(i.getTrack().getNoteSequence().size());
		int pitchAux = r.nextInt(Constants.RANGE_MAX_PITCH-Constants.RANGE_MIN_PITCH+1)
				+Constants.RANGE_MIN_PITCH;
		if (pitchAux != Constants.RANGE_MAX_PITCH+1) 
			i.getTrack().getNoteSequence().get(a).setMidiPitch(pitchAux);
		else
			i.getTrack().getNoteSequence().get(a).setMidiPitch(-1);
		
		if (i.getTrack().getNoteSequence().get(a).getTied() == 1) {
			i.getTrack().getNoteSequence().get(a).setTied(0);
			i.getTrack().getNoteSequence().get(a+1).setTied(0);
		}
		else if (i.getTrack().getNoteSequence().get(a).getTied() == 2) {
			i.getTrack().getNoteSequence().get(a).setTied(0);
			i.getTrack().getNoteSequence().get(a-1).setTied(0);
		}
		
		a = r.nextInt(i.getTrack().getNoteSequence().size()-2)+1;
		if(i.getTrack().getNoteSequence().get(a-1).getMeasure() == i.getTrack().getNoteSequence().get(a).getMeasure()) {
			i.getTrack().getNoteSequence().get(a-1).setDuration(i.getTrack().getNoteSequence().get(a-1).getDuration() 
					+ i.getTrack().getNoteSequence().get(a).getDuration()/2);
			i.getTrack().getNoteSequence().get(a).setDuration(i.getTrack().getNoteSequence().get(a).getDuration()/2);
		}
		else if (i.getTrack().getNoteSequence().get(a+1).getMeasure() == i.getTrack().getNoteSequence().get(a).getMeasure()) {
			i.getTrack().getNoteSequence().get(a+1).setDuration(i.getTrack().getNoteSequence().get(a+1).getDuration() 
					+ i.getTrack().getNoteSequence().get(a).getDuration()/2);
			i.getTrack().getNoteSequence().get(a).setDuration(i.getTrack().getNoteSequence().get(a).getDuration()/2);
		}
		return i;
	}
	
	public static Individual mutateMelodicTrigram (Individual i) {
		double targetSlope = -1.0;
		int nGramOrder = 3;
		Random r = new Random();
		int a = 0;
		if (i.getZipfMetrics().melodicTrigramMetricCalculator(i.getTrack()) > targetSlope) {
			if (r.nextInt(10) < 9)
				a = r.nextInt(i.getZipfMetrics().getCountedMelodicTrigrams().size()/2)+i.getZipfMetrics().getCountedMelodicTrigrams().size()/2;
			else
				a = r.nextInt(i.getZipfMetrics().getCountedMelodicTrigrams().size()/2);
		}
		else {
			if (r.nextInt(10) < 9)
				a = r.nextInt(i.getZipfMetrics().getCountedMelodicTrigrams().size()/2);
			else
				a = r.nextInt(i.getZipfMetrics().getCountedMelodicTrigrams().size()/2)+i.getZipfMetrics().getCountedMelodicTrigrams().size()/2;
		}
			
		int b = r.nextInt(i.getZipfMetrics().getCountedMelodicTrigrams().get(a).getCount())+1;
		int c = 1;
		
		NoteForCount trigram = i.getZipfMetrics().getCountedMelodicTrigrams().get(a);
		
		int positionTrigram = discoverTargetMelodicNgram(i.getZipfMetrics().getMelodicIntervals(), trigram, nGramOrder, b);
		
		int pitchAux = r.nextInt(Constants.RANGE_MAX_PITCH-Constants.RANGE_MIN_PITCH+1)
				+Constants.RANGE_MIN_PITCH;
		if (pitchAux != Constants.RANGE_MAX_PITCH+1) 
			i.getTrack().getNoteSequence().get(positionTrigram+1).setMidiPitch(pitchAux);
		else
			i.getTrack().getNoteSequence().get(positionTrigram+1).setMidiPitch(-1);
		positionTrigram++;
		pitchAux = r.nextInt(Constants.RANGE_MAX_PITCH-Constants.RANGE_MIN_PITCH+1)
				+Constants.RANGE_MIN_PITCH;
		if (pitchAux != Constants.RANGE_MAX_PITCH+1) 
			i.getTrack().getNoteSequence().get(positionTrigram+1).setMidiPitch(pitchAux);
		else
			i.getTrack().getNoteSequence().get(positionTrigram+1).setMidiPitch(-1);
		return i;
	}
	
	public static Individual mutateRhythmTrigram (Individual i) {
		
		double targetSlope = -1.0;
		int nGramOrder = 3;
		Random r = new Random();
		int a = 0;
		if (i.getZipfMetrics().rhythmTrigramMetricCalculator(i.getTrack()) > targetSlope) {
			if (r.nextInt(10) < 9)
				a = r.nextInt(i.getZipfMetrics().getCountedRhythmTrigrams().size()/2)+i.getZipfMetrics().getCountedRhythmTrigrams().size()/2;
			else
				a = r.nextInt(i.getZipfMetrics().getCountedRhythmTrigrams().size()/2);
		}
		else {
			if (r.nextInt(10) > 8)
				a = r.nextInt(i.getZipfMetrics().getCountedRhythmTrigrams().size()/2)+i.getZipfMetrics().getCountedRhythmTrigrams().size()/2;
			else
				a = r.nextInt(i.getZipfMetrics().getCountedRhythmTrigrams().size()/2);
		}
		int b = r.nextInt(i.getZipfMetrics().getCountedRhythmTrigrams().get(a).getCount())+1;
		
		int c = 1;
		
		NoteForCount trigram = i.getZipfMetrics().getCountedRhythmTrigrams().get(a);
		
		ArrayList<NoteHerremans> nhs = i.getZipfMetrics().getRhythmIntervals();
		int flag = 1;
		int positionTrigram = discoverTargetMelodicNgram(i.getZipfMetrics().getRhythmIntervals(), trigram, nGramOrder, b);
		i.getTrack().getNoteSequence().get(positionTrigram+1).setDuration(i.randomDuration());
		i.getTrack().getNoteSequence().get(positionTrigram+2).setDuration(i.randomDuration());
		return i;
	}
	
	public static Individual mutateMelodicAndRhythmTrigram (Individual i) {
		mutateMelodicTrigram(i);
		mutateRhythmTrigram(i);
		return i;
	}
	
	public static Individual mutateRankRankedMelodicTrigram(Individual i) {
		Random r = new Random();
		i.getZipfMetrics().melodicTrigramMetricCalculator(i.getTrack());
		ArrayList<NoteForCount> maxRankedMeloTrigrams = countedRankedEventToRemove(i.getZipfMetrics().getCountedMelodicTrigrams());
		int indexRandomNGram = r.nextInt(maxRankedMeloTrigrams.size());
		NoteForCount trigram = maxRankedMeloTrigrams.get(indexRandomNGram);
		int randomOcurrencyOfTrigram = r.nextInt(i.getZipfMetrics().getCountedMelodicTrigrams().get(indexRandomNGram).getCount())+1;
		int positionTrigram = discoverTargetMelodicNgram(i.getZipfMetrics().getMelodicIntervals(), trigram, 3, randomOcurrencyOfTrigram);
		int pitchAux = r.nextInt(Constants.RANGE_MAX_PITCH-Constants.RANGE_MIN_PITCH+1)
				+Constants.RANGE_MIN_PITCH;
		if (pitchAux != Constants.RANGE_MAX_PITCH+1) 
			i.getTrack().getNoteSequence().get(positionTrigram+1).setMidiPitch(pitchAux);
		else
			i.getTrack().getNoteSequence().get(positionTrigram+1).setMidiPitch(-1);
		positionTrigram++;
		pitchAux = r.nextInt(Constants.RANGE_MAX_PITCH-Constants.RANGE_MIN_PITCH+1)
				+Constants.RANGE_MIN_PITCH;
		if (pitchAux != Constants.RANGE_MAX_PITCH+1) 
			i.getTrack().getNoteSequence().get(positionTrigram+1).setMidiPitch(pitchAux);
		else
			i.getTrack().getNoteSequence().get(positionTrigram+1).setMidiPitch(-1);
		return i;
		
	}
	
	public static Individual mutateRankRankedRhythmTrigram(Individual i) {
		Random r = new Random();
		i.getZipfMetrics().rhythmTrigramMetricCalculator(i.getTrack());
		ArrayList<NoteForCount> maxRankedRhyTrigrams = countedRankedEventToRemove(i.getZipfMetrics().getCountedRhythmTrigrams());
		int indexRandomNGram = r.nextInt(maxRankedRhyTrigrams.size());
		NoteForCount trigram = maxRankedRhyTrigrams.get(indexRandomNGram);
		int randomOcurrencyOfTrigram = r.nextInt(i.getZipfMetrics().getCountedRhythmTrigrams().get(indexRandomNGram).getCount())+1;
		int positionTrigram = discoverTargetRhythmNgram(i.getZipfMetrics().getRhythmIntervals(), trigram, 3, randomOcurrencyOfTrigram);
		i.getTrack().getNoteSequence().get(positionTrigram+1).setDuration(i.randomDuration());
		i.getTrack().getNoteSequence().get(positionTrigram+2).setDuration(i.randomDuration());
		return i;
		
	}
	
	public static Individual mutateRankRankedMelodicAndRhythmTrigram(Individual i) {
		mutateRankRankedMelodicTrigram(i);
		mutateRankRankedRhythmTrigram(i);
		return i;
	}
	
	//testar pra ver se ta funcionando direitinho
	public static Individual mutateNormalOrRankRankedMelodicAndRhythmTrigram(Individual i) {
		Random r = new Random();
		float a = r.nextFloat();
		if (a < 0.5)
			mutateMelodicAndRhythmTrigram(i);
		else if (a < 0.1)
			mutateRankRankedMelodicAndRhythmTrigram(i);
		return i;
	}
	
	public static Individual mutateRankRankedPitchDuration(Individual i) {
		Random r = new Random();
		i.getZipfMetrics().pitchDurationMetricCalculator(i.getTrack());
		ArrayList<NoteForCount> maxRankedPitchDurations = countedRankedEventToRemove(i.getZipfMetrics().getCountedPitchDurations());
		int indexRandomPitchDur = r.nextInt(maxRankedPitchDurations.size());
		NoteForCount pitchDur = maxRankedPitchDurations.get(indexRandomPitchDur);
		int randomOcurrencyOfPitchDur = r.nextInt(pitchDur.getCount())+1;
		int positionTrigram = discoverTargetPitchDuration(i.getTrack().getNoteSequence(), pitchDur, randomOcurrencyOfPitchDur);
		i.getTrack().getNoteSequence().get(positionTrigram).setDuration(i.randomDuration());
		i.getTrack().getNoteSequence().get(positionTrigram).setMidiPitch(i.randomPitch());
		return i;
	}
	
	public static Individual mutateCopyingPartMusic (Individual i) {
		Random r = new Random();
		double sizeMutation = r.nextDouble()*(MutationConstants.M_COPYING_SIZE_MAX-MutationConstants.M_COPYING_SIZE_MIN)+MutationConstants.M_COPYING_SIZE_MIN;
		int sizeMutationTrunc = (int) Math.ceil(sizeMutation*i.getTrack().getNoteSequence().size());
		int startIndex = r.nextInt(i.getTrack().getNoteSequence().size()-sizeMutationTrunc);
		ArrayList<NoteHerremans> partToCopy = new ArrayList<NoteHerremans>(i.getTrack().getNoteSequence().subList(startIndex, startIndex + sizeMutationTrunc));
		
		int flag = 0;
		int startingNote = 0;
		while (flag == 0) {
			startingNote = r.nextInt(i.getTrack().getNoteSequence().size()-sizeMutationTrunc);
			if (startingNote + sizeMutationTrunc  <= startIndex)
				flag = 1;
			if (startingNote >= startIndex + sizeMutationTrunc)
				flag = 1;
		}
		
		int it = 0;
		for (NoteHerremans nh: partToCopy) {
			i.getTrack().getNoteSequence().get(startingNote+it).setMidiPitch(nh.getMidiPitch());
			i.getTrack().getNoteSequence().get(startingNote+it).setDuration(nh.getDuration());
			it++;
		}
		return i;
	}
	
	//Em reforma, Não use. Desculpe-nos o transtorno.
	public static Individual mutateCopyingPartMusicBar (Individual i) {
		double barsizeTempos = 4.0;
		Random r = new Random();
		double sizeMutation = r.nextDouble()*(MutationConstants.M_COPYING_SIZE_MAX-MutationConstants.M_COPYING_SIZE_MIN)+MutationConstants.M_COPYING_SIZE_MIN;
		int sizeMutationTrunc = (int) Math.ceil(sizeMutation*i.getTrack().getNoteSequence().size());
		int startIndex = r.nextInt(i.getTrack().getNoteSequence().size()-sizeMutationTrunc);
		ArrayList<NoteHerremans> partToCopy = new ArrayList<NoteHerremans>(i.getTrack().getNoteSequence().subList(startIndex, startIndex + sizeMutationTrunc));
		
		int flag = 0;
		int startingBar = 0;
		int nWriteBars = i.getTrack().getNoteSequence().get(startIndex+sizeMutationTrunc).getMeasure() 
				- i.getTrack().getNoteSequence().get(startIndex).getMeasure();
				
		while (flag == 0) {
			startingBar = r.nextInt(i.getTrack().getNoteSequence().size()-sizeMutationTrunc);
			if (startingBar + nWriteBars  
					<= i.getTrack().getNoteSequence().get(startIndex).getMeasure())
				flag = 1;
			if (startingBar >= i.getTrack().getNoteSequence().get(startIndex+sizeMutationTrunc).getMeasure())
				flag = 1;
		}
		
		double finalBeat = 0.0;
		for (NoteHerremans nh: partToCopy) {
			if (nh.getMeasure() != startingBar)
				break;
			finalBeat += nh.getDuration();
		}
		
		for (NoteHerremans nh: i.getTrack().getNoteSequence()) {
			double sizeBarCopying = 0.0;
			if (nh.getMeasure() == startingBar) {
				//sizeBarCopying += nh.getDuration();
				if (sizeBarCopying > barsizeTempos - finalBeat - Constants.EPSILON_DURATION) {
					NoteHerremans nh2 = i.createRandomNote();
					while (barsizeTempos - finalBeat - Constants.EPSILON_DURATION< nh.getDuration() - Constants.EPSILON_DURATION)
						nh2 = i.createRandomNote();
				}
			}
		}
		int it = 0;
		for (NoteHerremans nh: partToCopy) {
			i.getTrack().getNoteSequence().get(it).setMidiPitch(nh.getMidiPitch());
			i.getTrack().getNoteSequence().get(it).setDuration(nh.getDuration());
			it++;
		}
		
		return i;
	}
	
	public static ArrayList<NoteForCount> countedRankedEventToRemove (ArrayList<NoteForCount> nfcsEvent) {
		ArrayList<NoteForCount> nfcsRanked = new ArrayList<NoteForCount>();
		
		Collections.sort(nfcsEvent, new NoteForCountComparatorDesc());
		for (NoteForCount nfc: nfcsEvent) {
			int flag = 0;
			for (NoteForCount rankedNfc: nfcsRanked) {
				if (rankedNfc.getMidiPitch() == nfc.getCount()) {
					rankedNfc.setCount(rankedNfc.getCount()+1);
					flag = 1;
					break;
				}
			}
			if (flag == 0) {
				NoteForCount aux = new NoteForCount(nfc);
				aux.setMidiPitch(nfc.getCount());
				nfcsRanked.add(aux);
				flag = 1;
			}
			
		}
		
		Collections.sort(nfcsRanked, new NoteForCountComparatorDesc());
		int count = nfcsRanked.get(0).getCount();
		for (NoteForCount nfc: nfcsEvent) {
			if(nfc.getCount() == nfcsRanked.get(0).getMidiPitch())
				return new ArrayList<NoteForCount>(nfcsEvent.subList(nfcsEvent.indexOf(nfc), nfcsEvent.indexOf(nfc)+count));
		}
		return null;
	}
	private static int discoverTargetRhythmNgram (ArrayList<NoteHerremans> nhs, NoteForCount trigram, int nGramOrder, int b) {
		int c  = 1;
		int flag = 1;
		int positionTrigram = 0;
		for (NoteHerremans nh: nhs.subList(0, nhs.size()-nGramOrder+1)) {
			for (NoteHerremans nh2: nhs.subList(nhs.indexOf(nh), nhs.indexOf(nh)+nGramOrder)) {
				if(nh2.getDuration() != trigram.getnGram().get(
						nhs.subList(nhs.indexOf(nh), nhs.indexOf(nh)+nGramOrder).indexOf(nh2)).getDuration()) {
					flag = 0;
					break;
				}
				flag = 1;
			}
			if (flag == 1) {
				if (c < b) {
					c++;
					flag = 0;
				}
				else {
					positionTrigram = nhs.indexOf(nh);
					break;
				}
			
			}
			
		}
		return positionTrigram;
		
	}
	private static int discoverTargetMelodicNgram (ArrayList<NoteHerremans> nhs, NoteForCount trigram, int nGramOrder, int b) {
		int c  = 1;
		int flag = 1;
		int positionTrigram = 0;
		for (NoteHerremans nh: nhs.subList(0, nhs.size()-nGramOrder+1)) {
			for (NoteHerremans nh2: nhs.subList(nhs.indexOf(nh), nhs.indexOf(nh)+nGramOrder)) {
				if(nh2.getMidiPitch() != trigram.getnGram().get(
						nhs.subList(nhs.indexOf(nh), nhs.indexOf(nh)+nGramOrder).indexOf(nh2)).getMidiPitch()) {
					flag = 0;
					break;
				}
				flag = 1;
			}
			if (flag == 1) {
				if (c < b) {
					c++;
					flag = 0;
				}
				else {
					positionTrigram = nhs.indexOf(nh);
					break;
				}
			
			}
			
		}
		return positionTrigram;
		
	}
	
	private static int discoverTargetPitchDuration (ArrayList<NoteHerremans> nhs, NoteForCount pitchDur, int b) {
		int c  = 1;
		int flag = 0;
		int positionTrigram = 0;
		for (NoteHerremans nh: nhs) {
			if (nh.getMidiPitch() == pitchDur.getMidiPitch())
				if(nh.getDuration()== pitchDur.getDuration())
					flag = 1;
			if (flag == 1) {
				if (c < b) {
					c++;
					flag = 0;
				}
				else {
					positionTrigram = nhs.indexOf(nh);
					break;
				}
			}
		}
		return positionTrigram;	
	}
	
	public static Individual mutateAllMethods(Individual i) {
		Random r = new Random();
		float a = r.nextFloat();
		if (a < 0.25)
			mutateMelodicAndRhythmTrigram(i);
		else if (a < 0.5)
			mutateRankRankedMelodicAndRhythmTrigram(i);
		else if (a < 0.75)
			changeOneNote(i);
		else 
			mutateCopyingPartMusic(i);
		return i;
	}
	
	public static Individual mutateAllMethodsCopyingLater(Individual i) {
		Random r = new Random();
		int a = r.nextInt(3);
		if (a == 0)
			mutateMelodicAndRhythmTrigram(i);
		else if (a == 1)
			mutateRankRankedMelodicAndRhythmTrigram(i);
		else 
			changeOneNote(i);
		return i;
	}
	
	
	
}
