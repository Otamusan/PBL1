package otamusan.pbl.Data;

import java.nio.ByteBuffer;

public interface IDataType<T> {
	public ByteBuffer encode(T t, ByteBuffer buffer);

	public T decode(ByteBuffer buffer);

	public String name();

	public int getCapacity();
}
