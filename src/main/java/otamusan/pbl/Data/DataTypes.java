package otamusan.pbl.Data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import otamusan.pbl.Wrap;

public class DataTypes {

	private List<IDataType<?>> types = new ArrayList<IDataType<?>>();
	public Map<Integer, String> ids = new HashMap<Integer, String>();
	public int count = 0;
	public static final IDataType<Integer> TYPE_INT = new ByteInt();
	public static final IDataType<Double> TYPE_DOUBLE = new ByteDouble();
	public static final IDataType<Character> TYPE_CHAR = new ByteChar();

	public DataTypes() {
		this.register(TYPE_INT);
		this.register(TYPE_DOUBLE);
		this.register(TYPE_CHAR);
	}

	public <T> void register(IDataType<T> dataType) {
		if (this.types.stream().anyMatch((type) -> type.name() == dataType.name())) {
			throw new Error("This DataType name is already used : " + dataType.name());
		}
		this.ids.put(this.count, dataType.name());
		this.count++;
		this.types.add(dataType);

	}

	private Optional<IDataType<?>> getTypeByName(String string) {
		for (IDataType<?> iDataType : this.types) {
			if (iDataType.name() == string)
				return Optional.of(iDataType);
		}
		return Optional.empty();
	}

	private Optional<IDataType<?>> getTypeById(Integer id) {
		if (this.ids.get(id) != null) {
			return this.getTypeByName(this.ids.get(id));
		} else {
			return Optional.empty();
		}
	}

	private Optional<Integer> getNameById(String name) {
		Wrap<Optional<Integer>> id = new Wrap<Optional<Integer>>(Optional.empty());
		this.ids.forEach((fid, fname) -> {
			if (name == fname)
				id.setT(Optional.of(fid));
		});
		return id.getT();
	}

	public <T> ByteBuffer getBuffer(T t, IDataType<T> type) {
		ByteBuffer newBuffer = ByteBuffer.allocate(type.getCapacity() + 4);

		if (!this.getNameById(type.name()).isPresent())
			throw new Error("Unknown ID");

		this.getNameById(type.name()).ifPresent(id -> {
			newBuffer.putInt(id);
		});

		type.encode(t, newBuffer);

		newBuffer.flip();
		return newBuffer;
	}

	public Object getValue(ByteBuffer buffer) {
		int id = buffer.getInt();
		IDataType<?> type = this.getTypeById(id).get();
		Object value = type.decode(buffer);
		return value;
	}

}
