package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 接続先が一つの場合の{@link Connections}
 * @author otamusan
 *
 */
public class Connection extends Connections {
	private InetSocketAddress sendAddress;

	private Player player;

	/**
	 *
	 * @param send データを送信するアドレス
	 * @param receive データを受信するアドレス
	 * @param containerRegister {@link Container}を登録する処理を行うメソッド
	 */
	public Connection(InetSocketAddress send, InetSocketAddress receive, Consumer<RegisterKey> containerRegister) {
		super(receive, containerRegister);
		this.sendAddress = send;
		this.player = new Player(this.sendAddress);
	}

	/**
	 * 指定した{@link Container}の値が一つ前のフレームから現在にかけて変化しているかを返すメソッド
	 * @param <T> 格納される値のデータ型
	 * @param key 調べたいコンテナへのアクセサ
	 * @return
	 */
	public <T> Boolean isChange(ContainerKey<T> key) {
		return super.isChange(key, this.player);
	}

	/**
	 * 指定した{@link Container}の値を取得するメソッド
	 * @param <T> 格納される値のデータ型
	 * @param key 値を取得したいコンテナへのアクセサ
	 * @return 格納されていた値
	 */
	public <T> Optional<T> getData(ContainerKey<T> key) {
		return super.getData(key, this.player);
	}

	public void receive(ByteBuffer raw) {
		super.receive(raw, this.player);
	}

	/**
	 * 通信を開始するメソッド
	 * @throws IOException
	 */
	@Override
	public void open() throws IOException {
		super.open();
		this.addPlayer(this.player);
	}
}
