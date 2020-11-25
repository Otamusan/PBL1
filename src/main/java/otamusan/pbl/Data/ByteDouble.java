package otamusan.pbl.Data;

import java.nio.ByteBuffer;

public class ByteDouble implements IDataType<Double> {
	@Override
	public ByteBuffer encode(Double n, ByteBuffer buffer) {
		return buffer.putDouble(n);
	}

	@Override
	public String name() {
		return "double";
	}

	@Override
	public Double decode(ByteBuffer buffer) {
		return buffer.getDouble();
	}

	@Override
	public int getCapacity() {
		return 8;
	}
}