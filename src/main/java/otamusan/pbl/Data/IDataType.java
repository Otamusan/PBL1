package otamusan.pbl.Data;

import java.nio.ByteBuffer;

public interface IDataType<T> {
	public ByteBuffer encode(T t);

	public T decode(ByteBuffer buffer);

	public String name();
}
