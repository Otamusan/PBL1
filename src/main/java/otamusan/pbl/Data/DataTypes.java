package otamusan.pbl.Data;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import otamusan.pbl.Connection;
import otamusan.pbl.Wrap;

public class DataTypes {

	private List<IDataType<?>> types = new ArrayList<IDataType<?>>();
	public Map<Integer, String> ids = new HashMap<Integer, String>();
	public int count = 0;
	public static final IDataType<Integer> TYPE_INT = new ByteInt();
	public static final IDataType<Double> TYPE_DOUBLE = new ByteDouble();
	public static final IDataType<String> TYPE_STRING = new ByteString();

	public DataTypes() {
		this.register(TYPE_INT);
		this.register(TYPE_DOUBLE);
		this.register(TYPE_STRING);
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
		System.out.println(type.decode(type.encode(t)));
		return this.addType(type.encode(t), type);
	}

	private <T> ByteBuffer addType(ByteBuffer buffer, IDataType<T> type) {
		ByteBuffer newBuffer = ByteBuffer.allocate(Connection.cap);
		if (!this.getNameById(type.name()).isPresent())
			throw new Error("Unknown ID");
		this.getNameById(type.name()).ifPresent(id -> {
			newBuffer.putInt(id);
		});

		newBuffer.put(buffer);
		return newBuffer;
	}

	public Object getValue(ByteBuffer buffer) {
		int id = buffer.get();
		IDataType<?> type = this.getTypeById(id).get();
		Object value = type.decode(buffer);
		return value;
	}

	public static class ByteInt implements IDataType<Integer> {
		@Override
		public ByteBuffer encode(Integer integer) {
			return ByteBuffer.allocate(4).putInt(integer);
		}

		@Override
		public String name() {
			return "int";
		}

		@Override
		public Integer decode(ByteBuffer buffer) {
			return buffer.getInt();
		}
	}

	public static class ByteDouble implements IDataType<Double> {
		@Override
		public ByteBuffer encode(Double n) {
			return ByteBuffer.allocate(8).putDouble(n);
		}

		@Override
		public String name() {
			return "double";
		}

		@Override
		public Double decode(ByteBuffer buffer) {
			return buffer.getDouble();
		}
	}

	public static class ByteString implements IDataType<String> {
		@Override
		public ByteBuffer encode(String n) {
			return StandardCharsets.UTF_8.encode(n);
		}

		@Override
		public String name() {
			return "string";
		}

		@Override
		public String decode(ByteBuffer buffer) {
			return StandardCharsets.UTF_8.decode(buffer).toString();
		}
	}
}
