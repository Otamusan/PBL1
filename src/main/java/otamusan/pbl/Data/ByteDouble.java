package otamusan.pbl.Data;

import java.nio.ByteBuffer;
import java.util.Optional;

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

	@Override
	public Optional<Double> cast(Object o) {
		if (o instanceof Character)
			return Optional.of((Double) o);
		return Optional.empty();
	}
}