package NoteEnconding;

import java.util.ArrayList;

import Constants.Constants;
import jm.JMC;
import jm.constants.Instruments;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Read;
import jm.util.Write;

public class Track implements JMC{
	private double bpm = 185;
	private String name;
	ArrayList<NoteHerremans> noteSequence;
	
	public Track(String name){
		noteSequence = new ArrayList<NoteHerremans>();
		this.name = name;
	}
	public Track(Phrase p) {
		noteSequence = new ArrayList<NoteHerremans>();
		for (Note n: p.getNoteArray()) 
			noteSequence.add( new NoteHerremans(n.getPitch(), n.getRhythmValue()));
	}
	
	public Track(Phrase p, String name){
		this.name = name;
		int countBar = 1;
		double filledBar = 0.0;
		noteSequence = new ArrayList<NoteHerremans>();
		for (Note n: p.getNoteArray()) {
			NoteHerremans nh = new NoteHerremans(n.getPitch(), n.getRhythmValue());
			filledBar += n.getRhythmValue();
			if (filledBar > 4.005) {
				nh.setDuration(n.getRhythmValue() - (filledBar - 4.0));
				nh.setTied(1);
				nh.setMeasure(countBar);
				noteSequence.add(nh);
				
				countBar++;
				
				nh = new NoteHerremans(n.getPitch(), (filledBar - 4.0));
				nh.setTied(2);
				nh.setMeasure(countBar);
				filledBar -= 4.0;
				
			}
			else {
				nh.setTied(0);
				nh.setMeasure(countBar);
			}
			noteSequence.add(nh);
			if (filledBar < 4.005 && filledBar > 3.995) {
				filledBar = 0.0;
				countBar++;
			}
		}
		fixPause();
		
	}
	public void sortNoteSequence() {
		this.noteSequence.sort(new NoteHerremansPitchComparator());
	}
	public ArrayList<NoteHerremans> getNoteSequence(){
		return this.noteSequence;
	}
	public void setNoteSequence(ArrayList<NoteHerremans> noteSequence) {
		this.noteSequence = noteSequence;
	}
	
	public String getName(){
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void trackToMidi (String folder) {
		ArrayList<Note> noteArray = new ArrayList<Note>();
		//Note[] noteArray = new Note[noteSequence.size()];
		Note n = new Note();
		for (NoteHerremans nh: this.noteSequence) {
			if(nh.getTied() == 1) {
				n.setPitch(nh.getMidiPitch());
				n.setRhythmValue(nh.getDuration());
				n.setDuration(nh.getDuration());
			}
			else if (nh.getTied() == 2) {
				n.setRhythmValue(n.getRhythmValue() + nh.getDuration());
				n.setDuration(n.getDuration() + nh.getDuration());
				noteArray.add(n);
				n = new Note();
			}
			else if (nh.getTied() == 0) {
				n.setPitch(nh.getMidiPitch());
				n.setRhythmValue(nh.getDuration());
				n.setDuration(nh.getDuration());
				noteArray.add(n);
				n = new Note();
			}
			
		}
		Phrase frase1 = new Phrase(Instruments.DISTORTED_GUITAR);
		frase1.setStartTime(0);
		frase1.addNoteList(noteArray.toArray(new Note[noteArray.size()]), false);
		Part p = new Part();
		p.add(frase1);
		Score tmp = new Score(this.getName());
		tmp.setTempo(bpm);
		tmp.add(p);
		Write.midi(tmp, "CreatedMelodies/"+folder+"/"+this.getName());
			
	}
	
	public void trackToScaleMidi(int type, String folder) {
		//type = 1 Major, =2 Minor
		ArrayList<Note> noteArray = new ArrayList<Note>();
		for (NoteHerremans nh: getNoteSequence()) {
			Note n = new Note();
			int octave = nh.getMidiPitch()/7;
			if(nh.getMidiPitch() == -1)
				n.setPitch(nh.getMidiPitch());
			else if (nh.getMidiPitch()%7 <3)
				n.setPitch(2*(nh.getMidiPitch()%7) +octave*12);
			else if (nh.getMidiPitch()%7 < 7)
				n.setPitch(2*(nh.getMidiPitch()%7)-1 +octave*12);
			n.setRhythmValue(nh.getDuration());
			n.setDuration(nh.getDuration());
			noteArray.add(n);
		}
		Phrase frase1 = new Phrase(Instruments.DISTORTED_GUITAR);
		frase1.setStartTime(0);
		frase1.addNoteList(noteArray.toArray(new Note[noteArray.size()]), false);
		Part p = new Part();
		p.add(frase1);
		Score tmp = new Score(this.getName());
		tmp.setTempo(bpm);
		tmp.add(p);
		Write.midi(tmp, "CreatedMelodiesInC+/"+folder+"/"+this.getName());
	}
	
	private void fixPause () {
		for (NoteHerremans nh: getNoteSequence()) {
			if (nh.getMidiPitch() < 0)
				nh.setMidiPitch(-1);
		}
	}
	
	public int getBarNumber() {
		return getNoteSequence().get(getNoteSequence().size()-1).getMeasure();
	}
}
