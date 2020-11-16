package otamusan.pbl.Data;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataTypes {

	private List<IDataType<?>> types = new ArrayList<IDataType<?>>();
	public static final IDataType<Integer> TYPE_INT = new ByteInt();
	public static final IDataType<Double> TYPE_DOUBLE = new ByteDouble();
	public static final IDataType<String> TYPE_STRING = new ByteString();

	public DataTypes() {
		this.register(TYPE_INT);
		this.register(TYPE_DOUBLE);
		this.register(TYPE_STRING);
	}

	public <T> void register(IDataType<T> dataType) {
		if (this.types.stream().allMatch((type) -> type.name() != dataType.name())) {
			throw new Error("This DataType name is already used : " + dataType.name());
		}
		this.types.add(dataType);
	}

	public Optional<IDataType<?>> getTypeByName(String string) {
		for (IDataType<?> iDataType : this.types) {
			if (iDataType.name() == string)
				return Optional.of(iDataType);
		}
		return Optional.empty();
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
