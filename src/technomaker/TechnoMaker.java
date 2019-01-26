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
	
	public TechnoMaker() {
		try {
			s = new Sequence(Sequence.PPQ, 4);
			Track t = s.createTrack();
			mm = new MelodyMaker(s);
			bm = new BeatMaker(s);
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
