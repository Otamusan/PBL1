package otamusan.pbl;

import java.nio.ByteBuffer;

import otamusan.pbl.Data.DataTypes;

public class App {
	public final static int delay = 17;

	public static void main(String[] args) {
		DataTypes dataTypes = new DataTypes();
		ByteBuffer buffer = dataTypes.getBuffer('A', DataTypes.TYPE_CHAR);
		Object value = dataTypes.getValue(buffer);
		System.out.println(value);
	}
}