package technomaker;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;

public class MelodyMaker {

	Sequence s;
	Track t;

	public MelodyMaker(Sequence s) throws Exception {
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
		byte[] bt = { 0x10, (byte) 0x00, 0x00 };
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

		generateMelody();
	}

	private void generateMelody() throws Exception {
		for (int i = 0; i < 10; i++)
			addStrophe(40 * i);
	}

	private void addStrophe(int offset) throws Exception {

		int variation = 1 + (int) (Math.random() * 6);

		int ton = 60;
		for (int i = 0; i < 40;) {

			i += 1 + Math.pow(Math.random() * 3, 2) * 0.3;

			ton += (int) (Math.random() * (variation * 2 + 1)) - variation;

			// **** set instrument to Piano ****
			ShortMessage mm = new ShortMessage();
			mm.setMessage(ShortMessage.PROGRAM_CHANGE, (int) (Math.random()*100), 0x00);
			MidiEvent me = new MidiEvent(mm, (long) 0 + offset);
			t.add(me);

			// **** note on - middle C ****
			mm = new ShortMessage();
			mm.setMessage(ShortMessage.NOTE_ON, ton, 127);
			me = new MidiEvent(mm, (long) i + offset);
			t.add(me);
			// **** note off - middle C - 120 ticks later ****
			mm = new ShortMessage();
			mm.setMessage(ShortMessage.NOTE_OFF, ton, 127);
			me = new MidiEvent(mm, (long) 1 + i + offset);
			t.add(me);

		}
	}

}