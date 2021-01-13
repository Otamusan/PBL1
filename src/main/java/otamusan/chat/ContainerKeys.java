package otamusan.chat;

import otamusan.pblconnection.Connections.ContainerKey;
import otamusan.pblconnection.Connections.RegisterKey;
import otamusan.pblconnection.Container;
import otamusan.pblconnection.data.Types;

/**
 * {@link Container}を登録するためのクラス
 * @author otamusan
 *
 */
public class ContainerKeys {
	public static ContainerKey<String> message;
	public static ContainerKey<String> userName;

	public static void init(RegisterKey key) {
		message = key.register(Types.getTypeString(64));
		userName = key.register(Types.getTypeString(64));
	}
}
