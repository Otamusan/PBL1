package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
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
	 * @return 切断した後の1フレームだけtrueを返す。
	 */
	public boolean isDisconnecting() {
		return this.getPlayerKey(this.player).map(super::isDisconnecting).orElse(false);
	}

	/**
	 * 接続できたか確認するメソッド
	 * @return
	 */
	public boolean isConnected() {
		return this.getPlayerKey(this.player).map(super::isConnected).orElse(false);
	}

	/**
	 * 指定された{@link Player}が接続できたときにtrueを返す。
	 * 厳密には接続後に指定された{@link Player}に対してこのメソッドが初めて呼び出されたときtrueを返す。
	 * @return
	 */
	public boolean checkConnecting() {
		return this.getPlayerKey(this.player).map(super::checkConnecting).orElse(false);
	}

	/**
	 *
	 * @param send データを送信するアドレス
	 * @param containerRegister {@link Container}を登録する処理を行うメソッド
	 */
	public Connection(InetSocketAddress send, Consumer<RegisterKey> containerRegister) {
		super(new InetSocketAddress(0), containerRegister);
		this.sendAddress = send;
		this.player = new Player(this.sendAddress);
	}

	/**
	 * 通信相手のアドレスを返すメソッド
	 * @return
	 */
	public InetSocketAddress getAddress() {
		return this.player.getAddress();
	}

	/**
	 * 指定した{@link Container}の値が一つ前のフレームから現在にかけて変化しているかを返すメソッド
	 * @param <T> 格納される値のデータ型
	 * @param key 調べたいコンテナへのアクセサ
	 * @return
	 */
	public <T> Boolean isChange(ContainerKey<T> key) {
		return this.getPlayerKey(this.player).map(playerkey -> super.isChange(key, playerkey)).orElse(false);
	}

	/**
	 * 前回呼び出した後から現在までに指定した{@link Container}が値を受け取ったかを確認するメソッド
	 * @param <T> 格納される値のデータ型
	 * @param key 確認したいコンテナへのアクセサ
	 * @return 値を受け取っていたらtrue
	 */
	public <T> Boolean checkReceived(ContainerKey<T> key) {
		return this.getPlayerKey(this.player).map(playerkey -> super.checkReceived(key, playerkey)).orElse(false);
	}

	/**
	 * 指定した{@link Container}の値を取得するメソッド
	 * @param <T> 格納される値のデータ型
	 * @param key 値を取得したいコンテナへのアクセサ
	 * @return 格納されていた値
	 */
	public <T> Optional<T> getData(ContainerKey<T> key) {
		return this.getPlayerKey(this.player).flatMap(playerkey -> super.getData(key, playerkey));
	}

	/**
	 * 通信を開始するメソッド
	 * @throws IOException
	 */
	@Override
	public void open() throws IOException {
		this.channel = DatagramChannel.open();
		this.channel.connect(this.sendAddress);
		this.thread = new Thread(new Read(this.channel, this));
		this.thread.start();
		this.addPlayer(this.player);
		this.startAccessCheck();
	}
}
