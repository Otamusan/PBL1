package otamusan.pbl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import otamusan.pbl.Data.IDataSerializer;
import otamusan.pbl.Data.Tuple;
import otamusan.pbl.Data.TypeChar;
import otamusan.pbl.Data.TypeDouble;
import otamusan.pbl.Data.TypeInt;

public class DataTypeManagers {
	private List<Tuple<List<Buffer<Object>>, Player>> buffers;
	private int count;
	private Map<Integer, ContainerKey<?>> id2keyMap;
	private boolean isLocked;

	private List<IDataSerializer<?>> serializers;
	public final int CAP = 1024;

	public static final IDataSerializer<Integer> TYPE_INT = new TypeInt();
	public static final IDataSerializer<Double> TYPE_DOUBLE = new TypeDouble();
	public static final IDataSerializer<Character> TYPE_CHAR = new TypeChar();

	public DataTypeManagers() {
		this.buffers = new ArrayList<Tuple<List<Buffer<Object>>, Player>>();
		this.id2keyMap = new HashMap<Integer, DataTypeManagers.ContainerKey<?>>();
		this.isLocked = false;
		this.serializers = new ArrayList<IDataSerializer<?>>();
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

	private Optional<List<Buffer<Object>>> getContainers(Player player) {
		for (Tuple<List<Buffer<Object>>, Player> tuple : this.buffers) {
			if (tuple.getRight().equals(player))
				return Optional.of(tuple.getLeft());
		}
		return Optional.empty();
	}

	private Optional<Buffer<Object>> getContainer(ContainerKey<?> key, Player player) {
		return this.getContainers(player).flatMap(list -> this.getIDfromKey(key).map((id -> list.get(id))));
	}

	public void update() {
		for (Tuple<List<Buffer<Object>>, Player> containers : this.buffers) {
			for (Buffer<Object> dataHolder : containers.getLeft()) {
				dataHolder.update();
			}
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

	public void addConnection(Player player) {
		ArrayList<Buffer<Object>> containers = new ArrayList<Buffer<Object>>(this.serializers.size());
		for (int i = 0; i < this.serializers.size(); i++) {
			containers.add(new Buffer<Object>());
		}
		this.buffers.add(new Tuple<List<Buffer<Object>>, Player>(containers, player));
	}

	/*public Object getValue(ByteBuffer buffer) {
		int id = buffer.getInt();
		IDataType<?> type = this.getDataTypeById(id);
		Object value = type.decode(buffer);
		return value;
	}*/

	public void receive(ByteBuffer buffer, Player player) {
		int id = buffer.getInt();
		IDataSerializer<?> type = this.getDataTypeById(id);
		Object value = type.decode(buffer);

		this.getContainer(this.id2keyMap.get(id), player).ifPresent(container -> {
			container.put(value);
		});
	}

	public <T> Optional<T> getData(ContainerKey<T> key, Player player) {
		if (!this.getIDfromKey(key).isPresent())
			return Optional.empty();
		Buffer<Object> holder = this.getContainers(player).get().get(this.getIDfromKey(key).get());
		IDataSerializer<T> type = key.getDataType();
		return holder.get().flatMap(t -> type.cast(t));
	}

	public <T> Boolean isChange(ContainerKey<T> key, Player player) {
		if (!this.getContainer(key, player).isPresent())
			return false;
		return this.getContainer(key, player).get().isChange();
	}

	public IDataSerializer<?> getDataTypeById(int i) {
		return this.serializers.get(i);
	}

	public <T> ContainerKey<T> register(IDataSerializer<T> dataType) {
		if (this.isLocked)
			throw new Error();
		if (dataType.getCapacity() > this.CAP)
			throw new Error();
		int i = this.count;
		this.serializers.add(dataType);
		ContainerKey<T> containerKey = new ContainerKey<T>(dataType);
		this.id2keyMap.put(i, containerKey);
		this.count++;
		return containerKey;
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

	/*public static class DataContainer extends Buffer<Object> {
		private IDataSerializer<?> dataType;
	
		public DataContainer(IDataSerializer<?> datatype) {
			this.dataType = datatype;
		}
	
		public IDataSerializer<?> getDataType() {
			return this.dataType;
		}
	}*/

	@Override
	public String toString() {
		return this.buffers.toString();
	}
}
