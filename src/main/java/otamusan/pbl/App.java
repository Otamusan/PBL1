package otamusan.pbl;

import java.nio.ByteBuffer;
import java.util.Optional;

import otamusan.pbl.DataTypeManager.HolderKey;
import otamusan.pbl.Data.ByteTuple;
import otamusan.pbl.Data.IDataType;
import otamusan.pbl.Data.Tuple;

public class App {
	public final static int delay = 17;

	public static void main(String[] args) {
		DataTypeManager manager = new DataTypeManager();

		IDataType<Tuple<Integer, Character>> typeTuple = new ByteTuple<Integer, Character>(DataTypeManager.TYPE_INT,
				DataTypeManager.TYPE_CHAR);

		HolderKey<Tuple<Integer, Character>> key = manager.register(typeTuple);

		ByteBuffer buffer = manager.getBuffer(new Tuple<Integer, Character>(4, 'A'), key);

		manager.receive(buffer);

		manager.update();

		Optional<Tuple<Integer, Character>> tuple = manager.getData(key);
		System.out.println(tuple);
		tuple.ifPresent((t) -> {
			System.out.println(t.getRight().toString() + t.getLeft());
		});
	}
}