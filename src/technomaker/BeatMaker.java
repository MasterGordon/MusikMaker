package technomaker;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;

public class BeatMaker {
	Sequence s;
	Track t;
	private int drum = 13;

	public BeatMaker(Sequence s, int speed) throws InvalidMidiDataException {
		this.s = s;
		t = s.createTrack();

		// **** General MIDI sysex -- turn on General MIDI sound set ****
		byte[] b = { (byte) 0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte) 0xF7 };
		SysexMessage sm = new SysexMessage();
		sm.setMessage(b, 6);
		MidiEvent me = new MidiEvent(sm, (long) 0);
		t.add(me);

		// **** set tempo (meta event) ****
		MetaMessage mt = new MetaMessage();
		byte[] bt = { (byte) speed, (byte) 0x00, 0x00 };
		mt.setMessage(0x51, bt, 3);
		me = new MidiEvent(mt, (long) 0);
		t.add(me);

		// **** set track name (meta event) ****
		mt = new MetaMessage();
		String TrackName = new String("midifile track");
		mt.setMessage(0x03, TrackName.getBytes(), TrackName.length());
		me = new MidiEvent(mt, (long) 0);
		t.add(me);

		// **** set omni on ****
		ShortMessage mm = new ShortMessage();
		mm.setMessage(ShortMessage.CONTROL_CHANGE, 0x7D, 0x00);
		me = new MidiEvent(mm, (long) 0);
		t.add(me);

		// **** set poly on ****
		mm = new ShortMessage();
		mm.setMessage(ShortMessage.CONTROL_CHANGE, 0x7F, 0x00);
		me = new MidiEvent(mm, (long) 0);
		t.add(me);

		generateDrums();
	}

	private void generateDrums() throws InvalidMidiDataException {

		// int b1 = (int) (Math.random() * 3);
		// int b2 = (int) (Math.random() * 3);
		int b1 = 0;
		int b2 = 2;

		for (int i = 0; i < 100; i++) {
			// **** set instrument to Piano ****
			ShortMessage mm = new ShortMessage();
			mm.setMessage(ShortMessage.PROGRAM_CHANGE, 1, drum , 0x00);
			MidiEvent me = new MidiEvent(mm, (long) 0);
			t.add(me);

			// **** note on - middle C ****
			mm = new ShortMessage();
			mm.setMessage(ShortMessage.NOTE_ON, 1, 20, 127);
			me = new MidiEvent(mm, (long) i * 4 + b1);
			t.add(me);
			// **** note off - middle C - 120 ticks later ****
			mm = new ShortMessage();
			mm.setMessage(ShortMessage.NOTE_OFF, 1, 20, 127);
			me = new MidiEvent(mm, (long) i * 4 + b1);
			t.add(me);

			// **** note on - middle C ****
			mm = new ShortMessage();
			mm.setMessage(ShortMessage.NOTE_ON, 1, 20, 127);
			me = new MidiEvent(mm, (long) i * 4 + b2);
			t.add(me);
			// **** note off - middle C - 120 ticks later ****
			mm = new ShortMessage();
			mm.setMessage(ShortMessage.NOTE_OFF, 1, 20, 127);
			me = new MidiEvent(mm, (long) i * 4 + b2);
			t.add(me);

		}
	}
}
