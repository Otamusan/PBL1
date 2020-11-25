package otamusan.pbl.Data;

import java.nio.ByteBuffer;

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
}