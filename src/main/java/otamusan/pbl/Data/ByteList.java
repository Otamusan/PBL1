package otamusan.pbl.Data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ByteList<T> implements IDataType<List<T>> {

	private int length;
	private IDataType<T> type;

	public ByteList(int length, IDataType<T> type) {
		this.length = length;
		this.type = type;
	}

	@Override
	public void encode(List<T> t, ByteBuffer buffer) {
		for (int i = 0; i < this.length; i++) {
			this.type.encode(t.get(i), buffer);
		}
	}

	@Override
	public List<T> decode(ByteBuffer buffer) {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < this.length; i++) {
			list.add(this.type.decode(buffer));
		}
		return list;
	}

	@Override
	public int getCapacity() {
		return this.length * this.type.getCapacity();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<List<T>> cast(Object o) {
		if (!this.isCastable(o))
			return Optional.empty();
		List<T> list = (List<T>) o;
		return Optional.of(list);
	}

	@Override
	public boolean isCastable(Object o) {
		if (o instanceof List) {
			List<?> list = (List<?>) o;
			if (list.size() != this.length)
				return false;
			for (int i = 0; i < this.length; i++) {
				if (!this.type.isCastable(list.get(i)))
					return false;
			}
		}
		return true;
	}

}
