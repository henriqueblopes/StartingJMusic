package GeneticTools;

import java.io.ObjectInputStream.GetField;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import jm.constants.Durations;

import org.apache.commons.math3.analysis.function.Constant;


import Comparators.NoteForCountComparatorDesc;
import Constants.Constants;
import Constants.MutationConstants;
import Metrics.NoteForCount;
import Metrics.NoteForCountComparator;
import NoteEnconding.NoteHerremans;
import NoteEnconding.Track;

public class Mutation {
	
	public static Individual iAux = new Individual("");
	
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
	
	public static Individual splitNoteDuration( Individual i) {
		Random r = new Random();
		int a = r.nextInt(i.getTrack().getNoteSequence().size());
		NoteHerremans nh = i.getTrack().getNoteSequence().get(a);
		if (nh.getDuration() > Durations.SIXTEENTH_NOTE + Constants.EPSILON_DURATION) {
			nh.setDuration(nh.getDuration()/2);
			NoteHerremans nh2 = new NoteHerremans(nh);
			ArrayList<NoteHerremans> beforePart = new ArrayList<NoteHerremans>(i.getTrack().getNoteSequence().subList(0, a+1));
			ArrayList<NoteHerremans> afterPart = new ArrayList<NoteHerremans>(i.getTrack().getNoteSequence().subList(a+1, i.getTrack().getNoteSequence().size()));
			beforePart.add(nh2);
			ArrayList<NoteHerremans> newTrack = new ArrayList<NoteHerremans>();
			newTrack.addAll(beforePart);
			newTrack.addAll(afterPart);
			i.getTrack().setNoteSequence(newTrack);
		}
		return i;
	}
	
	public static Individual joinNoteDuration( Individual i) {
		Random r = new Random();
		int a = r.nextInt(i.getTrack().getNoteSequence().size()-1);
		NoteHerremans nh = i.getTrack().getNoteSequence().get(a);
		if (nh.getDuration() < Durations.WHOLE_NOTE - Constants.EPSILON_DURATION) {
			if (i.getTrack().getNoteSequence().get(a+1).getDuration() == nh.getDuration()
				&& i.getTrack().getNoteSequence().get(a+1).getMeasure() == nh.getMeasure()) {
				i.getTrack().getNoteSequence().get(a+1).setDuration(nh.getDuration()*2);
				if (r.nextInt(2) == 0)
					i.getTrack().getNoteSequence().get(a+1).setMidiPitch(nh.getMidiPitch());
				ArrayList<NoteHerremans> beforePart = new ArrayList<NoteHerremans>(i.getTrack().getNoteSequence().subList(0, a));
				ArrayList<NoteHerremans> afterPart = new ArrayList<NoteHerremans>(i.getTrack().getNoteSequence().subList(a+1, i.getTrack().getNoteSequence().size()));
				ArrayList<NoteHerremans> newTrack = new ArrayList<NoteHerremans>();
				newTrack.addAll(beforePart);
				newTrack.addAll(afterPart);
				i.getTrack().setNoteSequence(newTrack);
			}
			
		}
		return i;
	}
	
	public static Individual splitOrJoinNoteDuration (Individual i) {
		Random r = new Random();
		if (r.nextInt(2) == 0) 
			splitNoteDuration(i);
		else
			joinNoteDuration(i);
		return i;
	}
	
	public static Individual mixMutationTest (Individual i) {
		Random r = new Random();
		int nMut = 1;
		switch (r.nextInt(nMut)) {
			case 0: 
				changeOneNoteBar(i);
				splitOrJoinNoteDuration(i);
				break;
			case 1:
				mutateRhythmTrigramBar(i);
				mutateMelodicTrigram(i);
				break;
		}
		return i;
	}
	
	public static Individual changeOneNoteBar (Individual i) {
		int n = i.getTrack().getBarNumber();

		Random r = new Random();
		int a = r.nextInt(i.getMusicLenthBars())+1;
		NoteHerremans changed = new NoteHerremans(0, 0.0);
		changed.setMeasure(a);
		
		ArrayList<NoteHerremans> bar = pickCurrentBar(i.getTrack().getNoteSequence(), changed);
		i.getTrack().rebuildMeasure();
		
		ArrayList<NoteHerremans> barCopied = Track.copyNoteSequence(bar);
		changed = barCopied.get(r.nextInt(bar.size()));
		
		changed.setDuration(i.randomDuration());
		changed.setMidiPitch(i.randomPitch());

		ArrayList<NoteHerremans> fixedNotes = new ArrayList<NoteHerremans>();
		fixedNotes.add(changed);
		fixBarDurationforMutation(barCopied, fixedNotes, Constants.BAR_TEMPO);
		
		//System.out.print(i.getTrack().getNoteSequence().get(i.getTrack().getNoteSequence().size()-1).getMeasure());
		ArrayList<NoteHerremans> aux = switchBar(changed.getMeasure(), i.getTrack().getNoteSequence(), barCopied);
		i.getTrack().setNoteSequence(aux);
		i.getTrack().rebuildMeasure();
	//	System.out.println(" " +i.getTrack().getNoteSequence().get(i.getTrack().getNoteSequence().size()-1).getMeasure());
		
		return i;
		
	}
	
	public static Individual mutateRhythmTrigramBar (Individual i) {
		int positionTrigram = rhythmTrigramPositionBySideLine(i);
		ArrayList<NoteHerremans> fixedNotes = new ArrayList<NoteHerremans>();
		NoteHerremans changed = new NoteHerremans(0, 0.0);
		changed.setMeasure(i.getTrack().getNoteSequence().get(positionTrigram).getMeasure());
		
		if (changed.getMeasure() == i.getTrack().getNoteSequence().get(positionTrigram+1).getMeasure()) {
			ArrayList<NoteHerremans> bar = pickCurrentBar(i.getTrack().getNoteSequence(), changed);
			//definir antes de copiar compassoquais notas serão alteradas
			ArrayList<NoteHerremans> barCopied = Track.copyNoteSequence(bar);
			
			int indexBar = i.getTrack().getNoteSequence().indexOf(bar.get(0));
			int indexChanged = positionTrigram - indexBar;
			if(indexChanged >= barCopied.size())
				System.out.println("ops");
			fixedNotes.add(barCopied.get(indexChanged));
			fixedNotes.add(barCopied.get(indexChanged+1));
			
			double dur1 = iAux.randomDuration();
			double dur2 = iAux.randomDuration();
			while (dur1 + dur2 >= Constants.BAR_TEMPO + Constants.EPSILON_DURATION) {
				dur1 = iAux.randomDuration();
				dur2 = iAux.randomDuration();
			}
			barCopied.get(indexChanged).setDuration(dur1);
			barCopied.get(indexChanged+1).setDuration(dur2);
			fixBarDurationforMutation(barCopied, fixedNotes, Constants.BAR_TEMPO);
			i.getTrack().setNoteSequence(switchBar(changed.getMeasure(), i.getTrack().getNoteSequence(), barCopied));
			i.getTrack().rebuildMeasure();
		}
		else {
			ArrayList<NoteHerremans> bar = pickCurrentBar(i.getTrack().getNoteSequence(), changed);
			ArrayList<NoteHerremans> barCopied = Track.copyNoteSequence(bar);
			double dur1 = iAux.randomDuration();
			int indexBar = i.getTrack().getNoteSequence().indexOf(bar.get(0));
			int indexChanged = positionTrigram - indexBar;
			fixedNotes.add(barCopied.get(indexChanged));
			barCopied.get(indexChanged).setDuration(dur1);
			fixBarDurationforMutation(barCopied, fixedNotes, Constants.BAR_TEMPO);
			i.getTrack().setNoteSequence(switchBar(changed.getMeasure(), i.getTrack().getNoteSequence(), barCopied));
			i.getTrack().rebuildMeasure();
			
			if(changed.getMeasure() == 30)
				return i;
			changed.setMeasure(changed.getMeasure()+1);
			
			bar = pickCurrentBar(i.getTrack().getNoteSequence(), changed);
			if(bar.size() == 0)
				System.out.println("Ops");
			barCopied = Track.copyNoteSequence(bar);
			dur1 = iAux.randomDuration();
			indexBar = i.getTrack().getNoteSequence().indexOf(bar.get(0));
			indexChanged = 0;
			fixedNotes = new ArrayList<NoteHerremans>();
			fixedNotes.add(barCopied.get(indexChanged));
			barCopied.get(indexChanged).setDuration(dur1);
			fixBarDurationforMutation(barCopied, fixedNotes, Constants.BAR_TEMPO);
			i.getTrack().setNoteSequence(switchBar(changed.getMeasure(), i.getTrack().getNoteSequence(), barCopied));
			i.getTrack().rebuildMeasure();
		}
		return i;
		
		
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
	
	public static Individual mutateMelodicAndRhythmTrigramBar (Individual i) {
		mutateMelodicTrigram(i);
		mutateRhythmTrigramBar(i);
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
		int positionTrigram = discoverTargetRhythmNgram(i.getZipfMetrics().getRhythmIntervals(), trigram, 3, randomOcurrencyOfTrigram, i);
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
		//double b = printTotalDuration(i.getTrack().getNoteSequence());
		double barsizeTempos = 4.0;
		Random r = new Random();
		double sizeMutation = r.nextDouble()*(MutationConstants.M_COPYING_SIZE_MAX-MutationConstants.M_COPYING_SIZE_MIN)+MutationConstants.M_COPYING_SIZE_MIN;
		int sizeMutationTrunc = (int) Math.ceil(sizeMutation*i.getTrack().getNoteSequence().size());
		int startIndex = r.nextInt(i.getTrack().getNoteSequence().size()-sizeMutationTrunc);
		ArrayList<NoteHerremans> partToCopy = new ArrayList<NoteHerremans>(i.getTrack().getNoteSequence().subList(startIndex, startIndex + sizeMutationTrunc));
		
		int flag = 0;
		int startingBar = 0;
		int nWriteBars = i.getTrack().getNoteSequence().get(startIndex+sizeMutationTrunc-1).getMeasure() 
				- i.getTrack().getNoteSequence().get(startIndex).getMeasure()+1;
				
		while (flag == 0) {
			startingBar = r.nextInt(i.getTrack().getBarNumber()-nWriteBars)+1;
			if (startingBar + nWriteBars  
					<= i.getTrack().getNoteSequence().get(startIndex).getMeasure())
				flag = 1;
			if (startingBar >= i.getTrack().getNoteSequence().get(startIndex+sizeMutationTrunc-1).getMeasure()+1)
				flag = 1;
		}
		
		ArrayList<NoteHerremans> beforePartBar = new ArrayList<NoteHerremans>();
		for (NoteHerremans nh: i.getTrack().getNoteSequence())
			if(nh.getMeasure() == startingBar) {
				beforePartBar = new ArrayList<NoteHerremans>(i.getTrack().getNoteSequence().subList(0,
						i.getTrack().getNoteSequence().indexOf(nh)));
				break;
			}
		
		ArrayList<NoteHerremans> afterPartBar = new ArrayList<NoteHerremans>();
		for (NoteHerremans nh: i.getTrack().getNoteSequence())
			if(nh.getMeasure() == startingBar+nWriteBars) {
				afterPartBar = new ArrayList<NoteHerremans>(i.getTrack().getNoteSequence().subList(i.getTrack().getNoteSequence().indexOf(nh),
						i.getTrack().getNoteSequence().size()));
				break;
			}
		
		double finalBeat = 0.0;
		for (NoteHerremans nh: partToCopy) {
			if (nh.getMeasure() != partToCopy.get(0).getMeasure())
				break;
			finalBeat += nh.getDuration();
		}
		
		ArrayList<NoteHerremans> beforeCopy = new ArrayList<NoteHerremans>();
		while (finalBeat < 4.0 - Constants.EPSILON_DURATION) {
			NoteHerremans nh = i.createRandomNote();
			while (finalBeat + nh.getDuration() > 4.0 + Constants.EPSILON_DURATION) 
				nh = i.createRandomNote();
			finalBeat += nh.getDuration();
			beforeCopy.add(nh);
			if (finalBeat > 4.0 - Constants.EPSILON_DURATION)
				break;			
		}
		
		finalBeat = 0.0;
		for (NoteHerremans nh: partToCopy) {
			if (nh.getMeasure() == partToCopy.get(partToCopy.size()-1).getMeasure())
				finalBeat += nh.getDuration();
		}
		
		ArrayList<NoteHerremans> afterCopy = new ArrayList<NoteHerremans>();
		while (finalBeat < 4.0 - Constants.EPSILON_DURATION) {
			NoteHerremans nh = i.createRandomNote();
			while (finalBeat + nh.getDuration() > 4.0 + Constants.EPSILON_DURATION) 
				nh = i.createRandomNote();
			finalBeat += nh.getDuration();
			afterCopy.add(nh);
			if (finalBeat > 4.0 - Constants.EPSILON_DURATION)
				break;			
		}
		
		ArrayList<NoteHerremans> mutated = new ArrayList<NoteHerremans>();
		mutated.addAll(beforePartBar);
		mutated.addAll(beforeCopy);
		mutated.addAll(Track.copyNoteSequence(partToCopy));
		if (nWriteBars > 1)
			//System.out.println("opsss");
			mutated.addAll(afterCopy);
		mutated.addAll(afterPartBar);
		/*printTotalDuration(beforePartBar);
		printTotalDuration(beforeCopy);
		printTotalDuration(partToCopy);
		printTotalDuration(afterCopy);
		printTotalDuration(afterPartBar);*/
		
		//System.out.println();
		//printTotalDuration(mutated);
		//System.out.println();
		//printWrongBar(mutated);
		
		
		//double c = printTotalDuration(mutated);
		//if (c != b )
			//System.out.println();
		
		i.getTrack().setNoteSequence(mutated);
		i.getTrack().rebuildMeasure();
		//System.out.println();
		return i;
	}
	private static void printWrongBar (ArrayList<NoteHerremans> i) {
		double count = 0.0;
		int bar = 1;
		for (NoteHerremans nh: i)
			if (nh.getMeasure() == bar) {
				count += nh.getDuration();
				if(count > 4.0 + Constants.EPSILON_DURATION)
					System.out.println("WrongBar");
			}
	}
	
	
	private static double printTotalDuration(ArrayList<NoteHerremans> i) {
		double a = 0.0;
		for (NoteHerremans nh: i)
			a += nh.getDuration();
		if (a/4 != 30.0) {
			//System.out.print(a + " ");
		}
		System.out.print(a + " ");
		return a;
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
	private static int discoverTargetRhythmNgram (ArrayList<NoteHerremans> nhs, NoteForCount trigram, int nGramOrder, int b, Individual i) {
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
	
	private static int rhythmTrigramPositionBySideLine (Individual i) {
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
		int positionTrigram = discoverTargetRhythmNgram(i.getZipfMetrics().getRhythmIntervals(), trigram, nGramOrder, b, i);
		
		return positionTrigram +1;
		
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
	
	private static ArrayList<NoteHerremans> pickCurrentBar (ArrayList<NoteHerremans> nhs, NoteHerremans nh) {
		ArrayList<NoteHerremans> currentBar = new ArrayList<NoteHerremans>();
		int initIndex = 0;
		int finalIndex = nhs.size();
		for (NoteHerremans nhAux: nhs) {
			if (nhAux.getMeasure() == nh.getMeasure()) {
				initIndex = nhs.indexOf(nhAux);
				break;
			} 
		}
		currentBar = new ArrayList<NoteHerremans>(nhs.subList(initIndex, finalIndex));
		for (NoteHerremans nhAux: currentBar) {
			if (nhAux.getMeasure() != nh.getMeasure()) {
				currentBar = new ArrayList<NoteHerremans>(currentBar.subList(0, currentBar.indexOf(nhAux)));
				break;
			} 
		}
		return currentBar;
	}
	
	private static ArrayList<NoteHerremans> fixBarDurationforMutation (ArrayList<NoteHerremans> bar, ArrayList<NoteHerremans> fixedNotes, double barTempo) {
		double barDuration = 0.0;
		for (NoteHerremans nh: bar) {
			barDuration += nh.getDuration();
		}
		if(bar.size() == 0)
			System.out.println("What???");
		if (barDuration <= barTempo - Constants.EPSILON_DURATION)
			completeBarForMutation(bar, barDuration, bar.get(0).getMeasure());
		else if (barDuration >= barTempo + Constants.EPSILON_DURATION)
			reduceBarForMutation(bar, barDuration, fixedNotes);
		return bar;
	}

	private static void reduceBarForMutation(ArrayList<NoteHerremans> bar, double barDuration, ArrayList<NoteHerremans> fixedNotes) {
		Random r = new Random();
		while (barDuration >= Constants.BAR_TEMPO + Constants.EPSILON_DURATION) {
			int a = r.nextInt(bar.size());
			if (!fixedNotes.contains(bar.get(a))) {
				barDuration -= bar.get(a).getDuration();
				bar.remove(a);
			}
		}
		completeBarForMutation(bar, barDuration, fixedNotes.get(0).getMeasure());
		
	}

	private static void completeBarForMutation(ArrayList<NoteHerremans> bar, double barDuration, int barNumber) {
		while (Math.abs(Constants.BAR_TEMPO - barDuration) >=  Constants.EPSILON_DURATION) {
			NoteHerremans nh = iAux.createRandomNote();
			while (Constants.BAR_TEMPO - barDuration < nh.getDuration() - Constants.EPSILON_DURATION)
				nh = iAux.createRandomNote();
			nh.setMeasure(barNumber);
			bar.add(nh);
			barDuration += nh.getDuration();
		}
		
	}
	
	private static ArrayList<NoteHerremans>  switchBar (int barNumber, ArrayList<NoteHerremans> nhs, ArrayList<NoteHerremans> bar) {
		int naux = nhs.get(nhs.size()-1).getMeasure();
		ArrayList<NoteHerremans> beforePart = new ArrayList<NoteHerremans>();
		ArrayList<NoteHerremans> afterPart = new ArrayList<NoteHerremans>();
		int initIndex = 0;
		int finalIndex = nhs.size()-1;
		for (NoteHerremans nhAux: nhs) {
			if (nhAux.getMeasure() == bar.get(0).getMeasure()) {
				initIndex = nhs.indexOf(nhAux);
				break;
			} 
		}
		beforePart = new ArrayList<NoteHerremans>(nhs.subList(initIndex, nhs.size()));
		for (NoteHerremans nhAux: beforePart) {
			if (nhAux.getMeasure() != barNumber) {
				afterPart = new ArrayList<NoteHerremans>(beforePart.subList(beforePart.indexOf(nhAux), beforePart.size()));
				break;
			} 
		}
		
		beforePart = new ArrayList<NoteHerremans>(nhs.subList(0, initIndex));
		
		ArrayList<NoteHerremans> newTrack = new ArrayList<NoteHerremans>();
		newTrack.addAll(beforePart);
		newTrack.addAll(bar);
		newTrack.addAll(afterPart);
		
		Track t = new Track("aux");
		t.setNoteSequence(newTrack);
		t.rebuildMeasure();
		
		if (naux != t.getBarNumber())
			System.out.println("erro");
		return newTrack;
	}
	
	
	
}
