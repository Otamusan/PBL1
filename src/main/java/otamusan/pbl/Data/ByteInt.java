package otamusan.pbl.Data;

import java.nio.ByteBuffer;
import java.util.Optional;

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

	@Override
	public Optional<Integer> cast(Object o) {
		if (o instanceof Character)
			return Optional.of((Integer) o);
		return Optional.empty();
	}
}