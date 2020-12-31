package otamusan.pbl.data;

import java.nio.ByteBuffer;
import java.util.Optional;

public class TypeLong implements IDataSerializer<Long> {
	@Override
	public void encode(Long l, ByteBuffer buffer) {
		buffer.putLong(l);
	}

	@Override
	public Long decode(ByteBuffer buffer) {
		return buffer.getLong();
	}

	@Override
	public int getCapacity() {
		return 8;
	}

	@Override
	public Optional<Long> cast(Object o) {
		if (this.isCastable(o))
			return Optional.of((Long) o);
		return Optional.empty();
	}

	@Override
	public boolean isCastable(Object o) {
		return o instanceof Long;
	}
}