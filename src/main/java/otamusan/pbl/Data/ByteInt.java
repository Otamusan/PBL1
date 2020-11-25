package otamusan.pbl.Data;

import java.nio.ByteBuffer;

public class ByteInt implements IDataType<Integer> {
	@Override
	public ByteBuffer encode(Integer integer, ByteBuffer buffer) {
		return buffer.putInt(integer);
	}

	@Override
	public String name() {
		return "int";
	}

	@Override
	public Integer decode(ByteBuffer buffer) {
		return buffer.getInt();
	}

	@Override
	public int getCapacity() {
		return 4;
	}
}