package otamusan.pbl.data;

import java.nio.ByteBuffer;
import java.util.Optional;

public class TypeBoolean implements IDataSerializer<Boolean> {
	private TypeInt typeint = new TypeInt();

	@Override
	public void encode(Boolean t, ByteBuffer buffer) {
		if (t)
			this.typeint.encode(1, buffer);
		else
			this.typeint.encode(0, buffer);
	}

	@Override
	public Boolean decode(ByteBuffer buffer) {
		if (this.typeint.decode(buffer) == 1)
			return true;
		else if (this.typeint.decode(buffer) == 0)
			return false;
		return false;
	}

	@Override
	public int getCapacity() {
		return this.typeint.getCapacity();
	}

	@Override
	public Optional<Boolean> cast(Object o) {
		if (this.isCastable(o))
			return Optional.of((Boolean) o);
		return Optional.empty();
	}

	@Override
	public boolean isCastable(Object o) {
		return o instanceof Boolean;
	}
}
