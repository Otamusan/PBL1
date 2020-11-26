package otamusan.pbl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import otamusan.pbl.DataTypeManager.HolderKey;
import otamusan.pbl.Data.ByteList;
import otamusan.pbl.Data.IDataType;

public class App {
	public final static int delay = 17;

	public static void main(String[] args) {
		DataTypeManager manager = new DataTypeManager();

		IDataType<List<Character>> typelist = new ByteList<Character>(4, DataTypeManager.TYPE_CHAR);

		HolderKey<List<Character>> key = manager.register(typelist);

		List<Character> list = new ArrayList<Character>() {
			{
				this.add('t');
				this.add('e');
				this.add('s');
				this.add('t');
			}
		};

		ByteBuffer buffer = manager.getBuffer(list, key);

		manager.receive(buffer);

		manager.update();

		Optional<List<Character>> list1 = manager.getData(key);
		System.out.println(list1);

	}
}