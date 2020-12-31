package otamusan.pbl.data;

import java.nio.ByteBuffer;
import java.util.Optional;

public class TypeDouble implements IDataSerializer<Double> {
	@Override
	public void encode(Double n, ByteBuffer buffer) {
		buffer.putDouble(n);
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
		if (this.isCastable(o))
			return Optional.of((Double) o);
		return Optional.empty();
	}

	@Override
	public boolean isCastable(Object o) {
		return o instanceof Double;
	}
}