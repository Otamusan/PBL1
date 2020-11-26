package otamusan.pbl;

import java.nio.ByteBuffer;
import java.util.Optional;

import otamusan.pbl.DataTypeManager.HolderKey;

public class App {
	public final static int delay = 17;

	public static void main(String[] args) {
		DataTypeManager manager = new DataTypeManager();
		HolderKey<Character> key = manager.register(DataTypeManager.TYPE_CHAR);

		ByteBuffer buffer = manager.getBuffer('A', key);

		manager.receive(buffer);

		manager.update();

		Optional<Character> character = manager.getData(key);
		System.out.println(character);
	}
}