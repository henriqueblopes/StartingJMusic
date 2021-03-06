package GeneticTools;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import jm.constants.Durations;
import Constants.Constants;
import NoteEnconding.NoteHerremans;
import NoteEnconding.Track;

public class Individual {
	private Track track;
	private double fitness;
	private int musicLengthBars;
	private Random r;
	 
	public Individual(int musicLengthBars) {
		r = new Random();
		//r.setSeed(0);
		r.setSeed(System.currentTimeMillis());
		this.musicLengthBars = musicLengthBars;
		Date d = new Date(System.currentTimeMillis());
		track = new Track("M_"+(d.getYear()+1900)+"_"+(d.getMonth()+1)+"_"+d.getDate()+"_"+d.getHours()+"_"+d.getMinutes()+"_"+d.getSeconds()+".mid");
		fitness = 0;
	}
	
	public Individual(Track track) {
		r = new Random();
		r.setSeed(0);
		//r.setSeed(System.currentTimeMillis());
		this.musicLengthBars = track.getBarNumber();
		this.setTrack(track);;
		fitness = 0;
	}
	public int getMusicLenthBars () {
		return this.musicLengthBars;
	}
	public Track getTrack() {
		return this.track;
	}
	public void setTrack(Track track) {
		this.track = track;
	}
	public double getFitness() {
		return this.fitness;
	}
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public void createTrack() {
		// TODO Auto-generated method stub
		int bar = 1;
		double barTempo = 0.0;
		
		
		NoteHerremans nh = createRandomNote();
		NoteHerremans previousNh = nh;
		nh.setMeasure(bar);
		nh.setTied(0);
		barTempo +=nh.getDuration();
		getTrack().getNoteSequence().add(nh);
		if(barTempo >3.995 && barTempo <4.005) {
			barTempo =0.0;
			bar +=1;
		}
		while (bar != this.musicLengthBars +1) {
			nh = createRandomNote();
			barTempo += nh.getDuration();
			if(bar < this.musicLengthBars) {
				if (barTempo > 4.005) {
					previousNh = nh;
					nh = new NoteHerremans(previousNh.getMidiPitch(), barTempo -4.0);
					previousNh.setDuration(previousNh.getDuration()-nh.getDuration());
					previousNh.setMeasure(bar);
					previousNh.setTied(1);
					
					bar++;
					barTempo -=4.0;
					nh.setMeasure(bar);
					nh.setTied(2);
					
					getTrack().getNoteSequence().add(previousNh);
					getTrack().getNoteSequence().add(nh);
					previousNh = nh;
					
				}
				else if (barTempo < 3.995){
					nh.setMeasure(bar);
					nh.setTied(0);
					getTrack().getNoteSequence().add(nh);
					previousNh = nh;
				}
				else {
					nh.setMeasure(bar);
					nh.setTied(0);
					getTrack().getNoteSequence().add(nh);
					previousNh = nh;
					barTempo -=4.0;
					bar++;
				}
			}
			else {
				nh.setMeasure(bar);
				nh.setDuration(4.0 - (barTempo - nh.getDuration()));
				nh.setTied(0);
				getTrack().getNoteSequence().add(nh);
				previousNh = nh;
				bar++;
			}
		}
	}
	private double randomDuration() {
		double rDuration = 0.0;
		int a = r.nextInt(Constants.WITHOUT_NOTHING);
		switch (a) {
			case 0:
				rDuration =  Durations.SEMIBREVE;
				break;
			case 1:
				rDuration = Durations.MINIM;
				break;
			case 2:
				rDuration = Durations.QUARTER_NOTE;
				break;
			case 3:
				rDuration = Durations.EIGHTH_NOTE;
				break;
			case 4:
				rDuration = Durations.SIXTEENTH_NOTE;
				break;
			case 5:
				rDuration = Durations.THIRTYSECOND_NOTE;
				break;
			case 6:
				rDuration =  Durations.DOTTED_MINIM;
				break;
			case 7:
				rDuration = Durations.DOTTED_QUARTER_NOTE;
				break;
			case 8:
				rDuration = Durations.DOTTED_EIGHTH_NOTE;
				break;
			case 9:
				rDuration = Durations.DOTTED_SIXTEENTH_NOTE;
				break;
			case 10:
				rDuration = Durations.DOUBLE_DOTTED_MINIM;
				break;
			case 11:
				rDuration = Durations.DOUBLE_DOTTED_QUARTER_NOTE;
				break;
			case 12:
				rDuration = Durations.DOUBLE_DOTTED_EIGHTH_NOTE;
				break;
			case 13:
				rDuration = Durations.MINIM_TRIPLET;
				break;
			case 14:
				rDuration = Durations.QUARTER_NOTE_TRIPLET;				
				break;
			case 15:
				rDuration = Durations.EIGHTH_NOTE_TRIPLET;
				break;
			case 16:
				rDuration = Durations.SIXTEENTH_NOTE_TRIPLET;
				break;
			case 17:
				rDuration = Durations.THIRTYSECOND_NOTE_TRIPLET;
				break;
		}
		return rDuration;
	}
	private NoteHerremans createRandomNote() {
		int pitch = r.nextInt(Constants.RANGE_MAX_PITCH-Constants.RANGE_MIN_PITCH+1)+Constants.RANGE_MIN_PITCH;
		if (pitch == Constants.RANGE_MAX_PITCH+1)
			pitch = -1;
		NoteHerremans nh = new NoteHerremans(pitch, randomDuration());
		//NoteHerremans nh = new NoteHerremans(pitch, 1.0);
		return nh;
	}
	
}


