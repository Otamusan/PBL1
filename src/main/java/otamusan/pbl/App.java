package otamusan.pbl;

import java.nio.ByteBuffer;

import otamusan.pbl.Data.DataTypes;

public class App {
	public final static int delay = 17;

	public static void main(String[] args) {
		DataTypes dataTypes = new DataTypes();
		ByteBuffer buffer = dataTypes.getBuffer(423432, DataTypes.TYPE_INT);
		Object value = dataTypes.getValue(buffer);
		System.out.println(value);
	}
}