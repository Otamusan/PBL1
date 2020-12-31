package otamusan.pbl.Data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TypeString implements IDataSerializer<String> {

	private int length;
	private TypeList<Character> typelist;
	private TypeChar typechar;
	private TypeInt typeInt;
	private Character empty = '_';

	public TypeString(int length) {
		this.length = length;
		this.typechar = new TypeChar();
		this.typelist = new TypeList<Character>(length, new TypeChar());
		this.typeInt = new TypeInt();
	}

	@Override
	public void encode(String t, ByteBuffer buffer) {
		ArrayList<Character> characters = new ArrayList<Character>();
		for (int i = 0; i < t.length(); i++) {
			characters.add(t.charAt(i));
		}
		for (int i = 0; i < this.length - t.length(); i++) {
			characters.add(this.empty);
		}
		this.typeInt.encode(t.length(), buffer);
		this.typelist.encode(characters, buffer);
	}

	@Override
	public String decode(ByteBuffer buffer) {
		int length = this.typeInt.decode(buffer);
		List<Character> characters = this.typelist.decode(buffer);
		String string = new String();
		for (int i = 0; i < length; i++) {
			string = string + characters.get(i);
		}
		return string;
	}

	@Override
	public int getCapacity() {
		return this.typelist.getCapacity() + this.typeInt.getCapacity();
	}

	@Override
	public Optional<String> cast(Object o) {
		if (this.isCastable(o))
			return Optional.of((String) o);
		return Optional.empty();
	}

	@Override
	public boolean isCastable(Object o) {
		return o instanceof String && ((String) o).length() <= this.length;
	}

}
