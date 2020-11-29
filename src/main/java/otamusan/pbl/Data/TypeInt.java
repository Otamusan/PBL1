package otamusan.pbl.Data;

import java.nio.ByteBuffer;
import java.util.Optional;

public class ByteInt implements IDataType<Integer> {
	@Override
	public void encode(Integer integer, ByteBuffer buffer) {
		buffer.putInt(integer);
	}

	@Override
	public Integer decode(ByteBuffer buffer) {
		return buffer.getInt();
	}

	@Override
	public int getCapacity() {
		return 4;
	}

	@Override
	public Optional<Integer> cast(Object o) {
		if (this.isCastable(o))
			return Optional.of((Integer) o);
		return Optional.empty();
	}

	@Override
	public boolean isCastable(Object o) {
		return o instanceof Integer;
	}
}