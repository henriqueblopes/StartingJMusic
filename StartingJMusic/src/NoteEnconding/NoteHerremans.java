package NoteEnconding;
import java.io.Serializable;
import java.math.*;
import java.util.Comparator;
public class NoteHerremans implements Serializable {
	private int midiPitch; //valor midi da nota (inteiro que a representa no formato midi)
	private double duration; //duração em número de batimentos
	private int measure; //compasso que a nota está
	private int tied; //se a nota não é ligada, se está no começo de uma ligação, ou no final
	
	public NoteHerremans(int midiPitch, double duration) {
		this.midiPitch = midiPitch;
		this.duration = duration;
		/*for (int i = 2; i>-4; i--) {
			if (Math.abs((duration - Math.pow(2, i) )) < 0.005) {
				this.duration = Math.pow(2, i);
				break;
			}
		}*/
	}
	public NoteHerremans(NoteHerremans nh) {
		this.midiPitch = nh.midiPitch;
		this.duration = nh.duration;
		this.measure = nh.getMeasure();
		this.tied = nh.getTied();
	}
	
	public int getMidiPitch() {
		return this.midiPitch;
	}
	public void setMidiPitch(int midiPitch) {
		this.midiPitch = midiPitch;
	}
	
	public double getDuration() {
		return this.duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	
	public int getMeasure() {
		return this.measure;
	}
	public void setMeasure(int measure) {
		this.measure = measure;
	}
	
	public int getTied() {
		return this.tied;
	}
	public void setTied(int tied) {
		this.tied = tied;
	}
}

class NoteHerremansPitchComparator implements Comparator<NoteHerremans>{
	@Override
	public int compare(NoteHerremans arg0, NoteHerremans arg1) {
		return arg0.getMidiPitch() - arg1.getMidiPitch();  
	}
}
