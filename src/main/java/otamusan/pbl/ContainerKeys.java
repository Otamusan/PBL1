package otamusan.pbl;

import java.util.List;

import otamusan.pbl.DataTypeManager.ContainerKey;
import otamusan.pbl.Data.TypeList;

public class ContainerKeys {
	public static ContainerKey<Character> cha;
	public static ContainerKey<List<Character>> string;

	public static void init(Connection connection) {
		cha = connection.register(DataTypeManager.TYPE_CHAR);
		string = connection.register(new TypeList<Character>(100, DataTypeManager.TYPE_CHAR));
	}
}
