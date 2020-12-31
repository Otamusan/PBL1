package otamusan.pbl;

import otamusan.pbl.Connections.ContainerKey;
import otamusan.pbl.Connections.RegisterKey;
import otamusan.pbl.data.Types;

/**
 * {@link Container}を登録するためのクラス
 * @author otamusan
 *
 */
public class ContainerKeys {
	public static ContainerKey<String> message;

	public static void init(RegisterKey key) {
		message = key.register(Types.getTypeString(64));
	}
}
