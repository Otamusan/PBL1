package otamusan.pbl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import otamusan.pbl.Data.IDataSerializer;
import otamusan.pbl.Data.TypeChar;
import otamusan.pbl.Data.TypeDouble;
import otamusan.pbl.Data.TypeInt;

/**
 * 送受信する値の型を管理するクラス
 * @author otamusan
 *
 */
public class TypeManager {
	private int count;
	private List<IDataSerializer<?>> serializers;
	public final int CAP = 1024;

	public static final IDataSerializer<Integer> TYPE_INT = new TypeInt();
	public static final IDataSerializer<Double> TYPE_DOUBLE = new TypeDouble();
	public static final IDataSerializer<Character> TYPE_CHAR = new TypeChar();

	public TypeManager() {
		this.serializers = new ArrayList<IDataSerializer<?>>();
	}

	public int getSerializerSize() {
		return this.serializers.size();
	}

	public <T> ByteBuffer getBuffer(T t, IDataSerializer<T> serializer) {
		ByteBuffer newbyte = ByteBuffer.allocate(serializer.getCapacity() + 4);
		newbyte.putInt(this.getSerializerId(serializer).orElseThrow(() -> new Error()));
		serializer.encode(t, newbyte);
		newbyte.flip();
		return newbyte;
	}

	public int parseContainerId(ByteBuffer raw) {
		return raw.getInt();
	}

	public IDataSerializer<?> getSerializer(int serializerid) {
		return this.serializers.get(serializerid);
	}

	public Optional<Integer> getSerializerId(IDataSerializer<?> serializer) {
		for (int i = 0; i < this.serializers.size(); i++) {
			if (this.serializers.get(i) == serializer)
				return Optional.of(i);
		}
		return Optional.empty();
	}

	public int register(IDataSerializer<?> serializer) {
		if (serializer.getCapacity() > this.CAP)
			throw new Error();
		int i = this.count;
		this.serializers.add(serializer);
		this.count++;
		return i;
	}

	@Override
	public String toString() {
		return this.serializers.toString();
	}
}
