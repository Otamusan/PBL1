package otamusan.pbl;

import java.util.List;

import otamusan.pbl.Data.TypeList;

public class ContainerKeys {
	public static otamusan.pbl.DataTypeManagers.ContainerKey<Character> cha;
	public static otamusan.pbl.DataTypeManagers.ContainerKey<List<Character>> string;

	public static void init(Connections connection) {
		cha = connection.register(DataTypeManagers.TYPE_CHAR);
		string = connection.register(new TypeList<Character>(100, DataTypeManagers.TYPE_CHAR));
	}
}
