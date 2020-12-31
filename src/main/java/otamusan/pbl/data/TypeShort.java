package otamusan.pbl.data;

import java.nio.ByteBuffer;
import java.util.Optional;

public class TypeShort implements IDataSerializer<Short> {
	@Override
	public void encode(Short s, ByteBuffer buffer) {
		buffer.putShort(s);
	}

	@Override
	public Short decode(ByteBuffer buffer) {
		return buffer.getShort();
	}

	@Override
	public int getCapacity() {
		return 2;
	}

	@Override
	public Optional<Short> cast(Object o) {
		if (this.isCastable(o))
			return Optional.of((Short) o);
		return Optional.empty();
	}

	@Override
	public boolean isCastable(Object o) {
		return o instanceof Short;
	}
}