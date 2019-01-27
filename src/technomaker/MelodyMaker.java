package technomaker;

import java.util.Random;

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
	int instrument = 80 + (int) (Math.random() * 7);
	int instrumentR = 80 + (int) (Math.random() * 7);
	private int ton = 60;

	public MelodyMaker(Sequence s, int speed) throws Exception {
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

		generateMelody();
	}

	private void generateMelody() throws Exception {
		generateRefrain();
		addStrophe(40 * 0);
		addRefrain(40 * 1);
		addStrophe(40 * 2);
		addStrophe(40 * 3);
		addRefrain(40 * 4);
		addStrophe(40 * 5);
		addStrophe(40 * 6);
		generateRefrain();
		addRefrain(40 * 7);
//		for (int i = 0; i < 8; i++)
//			addRefrain(40 * i);
	}

	private void addStrophe(int offset) throws Exception {

		int variation = 1 + (int) (Math.random() * 3);

		for (int i = 0; i < 40;) {
			int länge = (int) (1 + Math.random() * 2);

			i += länge;

			ton += new Random().nextInt(variation * 2 + 1) - variation;
			if (ton <= 10)
				ton = 10;
			if (ton >= 90)
				ton = 90;

			// **** set instrument to Piano ****
			ShortMessage mm = new ShortMessage();
			mm.setMessage(ShortMessage.PROGRAM_CHANGE, instrument, 0x00);
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
			me = new MidiEvent(mm, (long) länge + i + offset);
			t.add(me);

		}
	}

	ShortMessage[] refrainOn = new ShortMessage[20];
	ShortMessage[] refrainOff = new ShortMessage[20];
	int[] tonLänge = new int[40];

	private void generateRefrain() throws InvalidMidiDataException {
		int variation = 1 + (int) (Math.random() * 3);

		for (int i = 0; i < 19;) {
			int länge = (int) (1 + Math.random() * 2);
			i += länge;

			ton += new Random().nextInt(variation * 2 + 1) - variation;
			if (ton <= 10)
				ton = 10;
			if (ton >= 90)
				ton = 90;
			ShortMessage mm = new ShortMessage();
			mm.setMessage(ShortMessage.NOTE_ON, ton, 127);
			ShortMessage mm2 = new ShortMessage();
			mm2.setMessage(ShortMessage.NOTE_OFF, ton, 127);
			if (i >= 20)
				i = 19;
			refrainOn[i] = mm;
			refrainOff[i] = mm2;
			tonLänge[i] = länge;
		}
	}

	private void addRefrain(int offset) throws Exception {

		for (int i = 0; i < 40; i++) {

			// **** set instrument to Piano ****
			ShortMessage mm = new ShortMessage();
			mm.setMessage(ShortMessage.PROGRAM_CHANGE, instrumentR, 0x00);
			MidiEvent me = new MidiEvent(mm, (long) 0 + offset);
			t.add(me);

			if (refrainOn[i % 19] == null) {
				System.out.println("exit" + i);
				continue;
			}

			// **** note on - middle C ****
			me = new MidiEvent(refrainOn[i % 19], (long) i + offset);
			t.add(me);
			// **** note off - middle C - 120 ticks later ****
			me = new MidiEvent(refrainOff[i % 19], (long) tonLänge[i % 19] + offset + i);
			t.add(me);

		}
	}

}
