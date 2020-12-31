package otamusan.pbl.Data;

import java.nio.ByteBuffer;
import java.util.Optional;

public class TypeFloat implements IDataSerializer<Float> {
	@Override
	public void encode(Float n, ByteBuffer buffer) {
		buffer.putFloat(n);
	}

	@Override
	public Float decode(ByteBuffer buffer) {
		return buffer.getFloat();
	}

	@Override
	public int getCapacity() {
		return 4;
	}

	@Override
	public Optional<Float> cast(Object o) {
		if (this.isCastable(o))
			return Optional.of((Float) o);
		return Optional.empty();
	}

	@Override
	public boolean isCastable(Object o) {
		return o instanceof Float;
	}
}