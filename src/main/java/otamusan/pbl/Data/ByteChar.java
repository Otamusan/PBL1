package otamusan.pbl.Data;

import java.nio.ByteBuffer;
import java.util.Optional;

public class ByteChar implements IDataType<Character> {
	@Override
	public ByteBuffer encode(Character n, ByteBuffer buffer) {
		return buffer.putChar(n);
	}

	@Override
	public String name() {
		return "char";
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
		if (o instanceof Character)
			return Optional.of((Character) o);
		return Optional.empty();
	}
}