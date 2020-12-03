package otamusan.pbl;

import otamusan.pbl.DataTypeManager.ContainerKey;

public class ContainerKeys {
	public static ContainerKey<Character> cha;

	public static void init(Connection connection) {
		cha = connection.register(DataTypeManager.TYPE_CHAR);
	}
}
