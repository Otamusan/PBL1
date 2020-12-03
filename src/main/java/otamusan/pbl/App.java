package otamusan.pbl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import otamusan.pbl.DataTypeManager.ContainerKey;
import otamusan.pbl.Data.TypeList;
import otamusan.pbl.Data.IDataSerializer;

public class App {
	public final static int delay = 17;

	public static void main(String[] args) {
		DataTypeManager manager = new DataTypeManager();

		IDataSerializer<List<List<Integer>>> typelist = new TypeList<List<Integer>>(4,
				new TypeList<>(4, DataTypeManager.TYPE_INT));

		ContainerKey<List<List<Integer>>> key = manager.register(typelist);

		List<Integer> list1 = new ArrayList<Integer>() {
			{
				this.add(4);
				this.add(6);
				this.add(3);
				this.add(44445);
			}
		};
		List<Integer> list2 = new ArrayList<Integer>() {
			{
				this.add(4);
				this.add(6);
				this.add(3);
				this.add(44445);
			}
		};
		List<Integer> list3 = new ArrayList<Integer>() {
			{
				this.add(4);
				this.add(6);
				this.add(3);
				this.add(44445);
			}
		};
		List<Integer> list4 = new ArrayList<Integer>() {
			{
				this.add(4);
				this.add(6);
				this.add(3);
				this.add(44445);
			}
		};
		List<List<Integer>> list5 = new ArrayList<List<Integer>>() {
			{
				this.add(list1);
				this.add(list2);
				this.add(list3);
				this.add(list4);
			}
		};

		ByteBuffer buffer = manager.getBuffer(list5, key);

		manager.receive(buffer);

		manager.update();

		Optional<List<List<Integer>>> list6 = manager.getData(key);
		System.out.println(list6);

	}

}