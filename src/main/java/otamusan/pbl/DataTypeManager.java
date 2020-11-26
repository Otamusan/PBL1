package otamusan.pbl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import otamusan.pbl.Data.ByteChar;
import otamusan.pbl.Data.ByteDouble;
import otamusan.pbl.Data.ByteInt;
import otamusan.pbl.Data.IDataType;

public class DataTypeManager {
	private List<DataHolder> buffers;
	private int count;
	private Map<Integer, HolderKey<?>> id2keyMap;

	public static final IDataType<Integer> TYPE_INT = new ByteInt();
	public static final IDataType<Double> TYPE_DOUBLE = new ByteDouble();
	public static final IDataType<Character> TYPE_CHAR = new ByteChar();

	public DataTypeManager() {
		this.buffers = new ArrayList<DataHolder>();
		this.id2keyMap = new HashMap<Integer, DataTypeManager.HolderKey<?>>();
	}

	public Optional<Integer> getIDfromKey(HolderKey<?> holderKey) {
		for (Entry<Integer, HolderKey<?>> entry : this.id2keyMap.entrySet()) {
			if (holderKey == entry.getValue())
				return Optional.of(entry.getKey());
		}
		return Optional.empty();
	}

	public void update() {
		for (DataHolder dataHolder : this.buffers) {
			dataHolder.update();
		}
	}

	public <T> ByteBuffer getBuffer(T t, HolderKey<T> key) {
		if (!this.getIDfromKey(key).isPresent())
			throw new Error();
		if (key.getDataType() != this.getDataTypeById(this.getIDfromKey(key).get()))
			throw new Error("dataType and anonther from id are mismatched");

		ByteBuffer newBuffer = ByteBuffer.allocate(key.getDataType().getCapacity() + 4);

		newBuffer.putInt(this.getIDfromKey(key).get());

		key.getDataType().encode(t, newBuffer);

		newBuffer.flip();
		return newBuffer;
	}

	/*public Object getValue(ByteBuffer buffer) {
		int id = buffer.getInt();
		IDataType<?> type = this.getDataTypeById(id);
		Object value = type.decode(buffer);
		return value;
	}*/

	public void receive(ByteBuffer buffer) {
		int id = buffer.getInt();
		IDataType<?> type = this.getDataTypeById(id);
		Object value = type.decode(buffer);
		this.buffers.get(id).put(value);
	}

	public <T> Optional<T> getData(HolderKey<T> key) {
		if (!this.getIDfromKey(key).isPresent())
			return Optional.empty();
		DataHolder holder = this.buffers.get(this.getIDfromKey(key).get());
		IDataType<T> type = key.getDataType();
		return holder.get().flatMap(t -> type.cast(t));
	}

	public IDataType<?> getDataTypeById(int i) {
		return this.buffers.get(i).getDataType();
	}

	public <T> HolderKey<T> register(IDataType<T> dataType) {
		int i = this.count;
		this.buffers.add(new DataHolder(dataType));
		HolderKey<T> holderKey = new HolderKey<T>(dataType);
		this.id2keyMap.put(i, holderKey);
		this.count++;
		return holderKey;
	}

	public static class HolderKey<T> {
		private IDataType<T> dataType;

		public HolderKey(IDataType<T> type) {
			this.dataType = type;
		}

		public IDataType<T> getDataType() {
			return this.dataType;
		}

	}

	public static class DataHolder extends Buffer<Object> {
		private IDataType<?> dataType;

		public DataHolder(IDataType<?> datatype) {
			this.dataType = datatype;
		}

		public IDataType<?> getDataType() {
			return this.dataType;
		}
	}

	@Override
	public String toString() {
		return this.buffers.toString();
	}
}
