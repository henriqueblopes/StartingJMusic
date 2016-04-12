package JMusicTools;
import jm.JMC;
import jm.music.data.*;
import jm.music.tools.*;
import jm.util.*;

public abstract class MidiFile implements JMC{
	
	public static void readMidi (){
		makeFileOne();
    	makeFileTwo();
    	makeFileThree();
    	Score theScore = new Score("Temporary score");
     	Score newScore = new Score("Combination Score");
        
		// read the MIDI files made earlier as input                
		Read.midi(theScore, "ScoreOne.mid");
		        
		// get the part from it                
		Part tempPart = theScore.getPart(0);
		        
		// add it to the new score                
		newScore.addPart(tempPart.copy());
	        
	    theScore.empty();
	        
	    //do the same for the next MIDI file riff                
	    Read.midi(theScore, "ScoreTwo.mid");
	    tempPart = theScore.getPart(0);
	    tempPart.setChannel(1); // set a different channel                
	    newScore.addPart(tempPart.copy());
	        
	    theScore.empty();
	
	    // similarly for the third MIDI file                
	    Read.midi(theScore, "ScoreThree.mid");
	    tempPart = theScore.getPart(0);
	    tempPart.setChannel(2); 
	    Mod.transpose(tempPart, -12); 
	    // 	make a modification to the data from the MIDI file
	    newScore.addPart(tempPart.copy());
	         
	    // show it          
	  	View.show(newScore);
	        
		//now we can write the new score to a MIDI file                
		Write.midi(newScore, "ReadMIDI.mid");
	}
	
	public static void makeFileOne() {
  		Score score = new Score();
  		score.addPart(riffMaker(70));
  		Write.midi(score, "ScoreOne.mid");
	}

 	public static void makeFileTwo() {
 	 	Score score = new Score();
      	score.addPart(riffMaker(50));
   		Write.midi(score, "ScoreTwo.mid");
  	}

 	public static void makeFileThree() {
        Score score = new Score();
      	score.addPart(riffMaker(40));
   		Write.midi(score, "ScoreThree.mid");
	}

 	public static Part riffMaker(int pitch) {
   		int tempPitch = pitch;
  		Part p = new Part("title",(int)(Math.random()*80), 0);
  		Phrase phr = new Phrase();
    	for (int i = 0; i < 16; i++) {
           		Note n = new Note(tempPitch += (int)(Math.random()*8 - 4), SQ);
         		phr.addNote(n);
    	}    
    	p.addPhrase(phr);
    	return p;
  	}
}
