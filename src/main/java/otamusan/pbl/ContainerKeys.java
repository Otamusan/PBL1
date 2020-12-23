package otamusan.pbl;

import otamusan.pbl.Connections.RegisterKey;

/**
 * {@link Container}を登録するためのクラス
 * @author otamusan
 *
 */
public class ContainerKeys {
	public static otamusan.pbl.Connections.ContainerKey<Character> cha;

	public static void init(RegisterKey key) {
		cha = key.register(TypeManager.TYPE_CHAR);
	}
}
