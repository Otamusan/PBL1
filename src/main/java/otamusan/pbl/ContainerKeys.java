package otamusan.pbl;

import java.util.List;

public class ContainerKeys {
	public static otamusan.pbl.Connections.ContainerKey<Character> cha;
	public static otamusan.pbl.Connections.ContainerKey<List<Character>> string;

	public static void init(Connections connection) {
		cha = connection.register(TypeManager.TYPE_CHAR);
		//string = connection.register(new TypeList<Character>(100, TypeManager.TYPE_CHAR));
	}
}
