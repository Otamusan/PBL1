package otamusan.pbl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import otamusan.pbl.Data.IDataSerializer;
import otamusan.pbl.Data.TypeChar;
import otamusan.pbl.Data.TypeDouble;
import otamusan.pbl.Data.TypeInt;

public class DataTypeManager {
	private List<DataContainer> buffers;
	private int count;
	private Map<Integer, ContainerKey<?>> id2keyMap;
	private boolean isLocked;

	public final int CAP = 1024;

	public static final IDataSerializer<Integer> TYPE_INT = new TypeInt();
	public static final IDataSerializer<Double> TYPE_DOUBLE = new TypeDouble();
	public static final IDataSerializer<Character> TYPE_CHAR = new TypeChar();

	public DataTypeManager() {
		this.buffers = new ArrayList<DataContainer>();
		this.id2keyMap = new HashMap<Integer, DataTypeManager.ContainerKey<?>>();
		this.isLocked = false;
	}

	public void lock() {
		this.isLocked = true;
	}

	public Optional<Integer> getIDfromKey(ContainerKey<?> holderKey) {
		for (Entry<Integer, ContainerKey<?>> entry : this.id2keyMap.entrySet()) {
			if (holderKey == entry.getValue())
				return Optional.of(entry.getKey());
		}
		return Optional.empty();
	}

	public void update() {
		for (DataContainer dataHolder : this.buffers) {
			dataHolder.update();
		}
	}

	public <T> ByteBuffer getBuffer(T t, ContainerKey<T> key) {
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
		IDataSerializer<?> type = this.getDataTypeById(id);
		Object value = type.decode(buffer);
		this.buffers.get(id).put(value);
	}

	public <T> Optional<T> getData(ContainerKey<T> key) {
		if (!this.getIDfromKey(key).isPresent())
			return Optional.empty();
		DataContainer holder = this.buffers.get(this.getIDfromKey(key).get());
		IDataSerializer<T> type = key.getDataType();
		return holder.get().flatMap(t -> type.cast(t));
	}

	public <T> Boolean isChange(ContainerKey<T> key) {
		DataContainer holder = this.buffers.get(this.getIDfromKey(key).get());
		return holder.isChange();
	}

	public IDataSerializer<?> getDataTypeById(int i) {
		return this.buffers.get(i).getDataType();
	}

	public <T> ContainerKey<T> register(IDataSerializer<T> dataType) {
		if (this.isLocked)
			throw new Error();
		if (dataType.getCapacity() > this.CAP)
			throw new Error();
		int i = this.count;
		this.buffers.add(new DataContainer(dataType));
		ContainerKey<T> holderKey = new ContainerKey<T>(dataType);
		this.id2keyMap.put(i, holderKey);
		this.count++;
		return holderKey;
	}

	public static class ContainerKey<T> {
		private IDataSerializer<T> dataType;

		public ContainerKey(IDataSerializer<T> type) {
			this.dataType = type;
		}

		public IDataSerializer<T> getDataType() {
			return this.dataType;
		}

	}

	public static class DataContainer extends Buffer<Object> {
		private IDataSerializer<?> dataType;

		public DataContainer(IDataSerializer<?> datatype) {
			this.dataType = datatype;
		}

		public IDataSerializer<?> getDataType() {
			return this.dataType;
		}
	}

	@Override
	public String toString() {
		return this.buffers.toString();
	}
}
