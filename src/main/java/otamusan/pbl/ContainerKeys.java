package otamusan.pbl;

import otamusan.pbl.Connections.ContainerKey;
import otamusan.pbl.Connections.RegisterKey;
import otamusan.pbl.Data.Types;

/**
 * {@link Container}を登録するためのクラス
 * @author otamusan
 *
 */
public class ContainerKeys {
	public static otamusan.pbl.Connections.ContainerKey<Character> cha;
	public static ContainerKey<String> message;

	public static void init(RegisterKey key) {
		cha = key.register(Types.TYPE_CHAR);
		message = key.register(Types.getTypeString(64));
	}
}
