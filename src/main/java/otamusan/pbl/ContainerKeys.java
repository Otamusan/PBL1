package otamusan.pbl;

import java.util.List;

import otamusan.pbl.Connections.RegisterKey;

public class ContainerKeys {
	public static otamusan.pbl.Connections.ContainerKey<Character> cha;
	public static otamusan.pbl.Connections.ContainerKey<List<Character>> string;

	public static void init(RegisterKey key) {
		cha = key.register(TypeManager.TYPE_CHAR);
		//string = connection.register(new TypeList<Character>(100, TypeManager.TYPE_CHAR));
	}
}
