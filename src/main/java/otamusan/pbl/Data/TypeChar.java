package otamusan.pbl.Data;

import java.nio.ByteBuffer;
import java.util.Optional;

public class TypeChar implements IDataSerializer<Character> {
	@Override
	public void encode(Character n, ByteBuffer buffer) {
		buffer.putChar(n);
	}

	@Override
	public Character decode(ByteBuffer buffer) {
		return buffer.getChar();
	}

	@Override
	public int getCapacity() {
		return 2;
	}

	@Override
	public Optional<Character> cast(Object o) {
		//System.out.println(o);
		if (this.isCastable(o))
			return Optional.of((Character) o);
		return Optional.empty();
	}

	@Override
	public boolean isCastable(Object o) {
		return o instanceof Character;
	}
}