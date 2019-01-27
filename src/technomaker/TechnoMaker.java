package technomaker;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

public class TechnoMaker {
	
	Sequence s;
	BeatMaker bm;
	MelodyMaker mm;
	int speed;
	
	public TechnoMaker(int speed) {
		this.speed = speed;
		try {
			s = new Sequence(Sequence.PPQ, 4);
			Track t = s.createTrack();
			mm = new MelodyMaker(s,this.speed);
			bm = new BeatMaker(s,this.speed);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save(String filename) {
		File f = new File(filename);
		try {
			MidiSystem.write(s, 1, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
