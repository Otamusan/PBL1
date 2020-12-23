package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import otamusan.pbl.Data.IDataSerializer;

/**
 * 通信を管理するクラス。
 * @author otamusan
 *
 */
public class Connections {
	private DatagramChannel channel;
	private InetSocketAddress addressReceive;
	private Map<Integer, Player> players;
	protected DataManagers dataManager;
	protected TypeManager typeManager;
	private Thread thread;
	private Map<Integer, ContainerKey<?>> keyMap;

	/**
	 *
	 * @param receive データを受信するアドレス
	 * @param containerRegister {@link Container}を登録する処理を行うメソッド
	 */
	public Connections(InetSocketAddress receive, Consumer<RegisterKey> containerRegister) {
		this.addressReceive = receive;
		this.typeManager = new TypeManager();
		this.players = new HashMap<Integer, Player>();
		this.keyMap = new HashMap<>();
		containerRegister.accept(new RegisterKey(this));
		this.dataManager = new DataManagers(this.typeManager.getSerializerSize());
	}

	private Optional<Integer> getPlayerID(Player player) {
		for (Integer id : this.players.keySet()) {
			if (this.players.get(id).equals(player))
				return Optional.of(id);
		}
		return Optional.empty();
	}

	public List<Player> getPlayers() {
		return new ArrayList<Player>(this.players.values());
	}

	/**
	 *
	 * @param player
	 * @return 与えられたプレイヤーが存在するか
	 */
	public boolean isExist(Player player) {
		for (Player p : this.getPlayers()) {
			if (player.equals(p))
				return true;
		}
		return false;
	}

	/**
	 * {@link Container}を登録するためのアクセサ
	 * @author otamusan
	 *
	 */
	public static class RegisterKey {
		private Connections connections;

		public RegisterKey(Connections connections) {
			this.connections = connections;
		}

		/**
		 * {@link Container}を登録するメソッド。同じ{@link IDataSerializer}を渡しても別のコンテナとして管理される
		 * @param <T> {@link Container}に格納される値のデータ型
		 * @param serializer 格納される値を変換するシリアライザ
		 * @return 格納された値を取得する時などに必要なアクセサ
		 */
		public <T> ContainerKey<T> register(IDataSerializer<T> serializer) {
			int containerid = this.connections.typeManager.register(serializer);
			ContainerKey<T> key = new ContainerKey<>(serializer);
			this.connections.keyMap.put(containerid, key);
			return key;
		}
	}

	private Optional<Integer> getContainerID(ContainerKey<?> key) {
		for (Integer id : this.keyMap.keySet()) {
			if (this.keyMap.get(id) == key)
				return Optional.of(id);
		}
		return Optional.empty();
	}

	/**
	 * 指定した{@link Container}の値が一つ前のフレームから現在にかけて変化しているかを返すメソッド
	 * @param <T> 格納される値のデータ型
	 * @param key 調べたいコンテナへのアクセサ
	 * @param player 通信を行っているプレイヤー
	 * @return
	 */
	public <T> Boolean isChange(ContainerKey<T> key, Player player) {
		return this.getContainerID(key).flatMap(containerid -> this.getPlayerID(player)
				.map(playerid -> this.dataManager.isChange(containerid, playerid))).orElse(false);
	}

	/**
	 * 指定した{@link Container}の値を取得するメソッド
	 * @param <T> 格納される値のデータ型
	 * @param key 値を取得したいコンテナへのアクセサ
	 * @param player 通信を行っているプレイヤー
	 * @return 格納されていた値
	 */
	public <T> Optional<T> getData(ContainerKey<T> key, Player player) {
		return this.getContainerID(key).flatMap(containerid -> this.getPlayerID(player)
				.flatMap(playerid -> this.dataManager.getData(containerid, playerid)
						.flatMap(t -> key.serializer.cast(t))));
	}

	public void addPlayer(Player player) {
		int c = 0;
		for (int i = 0; i < this.players.size(); i++) {
			if (!this.players.containsKey(i))
				c = i;
		}
		this.players.put(c, player);
		this.dataManager.addContainers(this.getPlayerID(player).orElseThrow(() -> new Error("error")));
	}

	/**
	 * 通信を開始するメソッド
	 * @throws IOException
	 */
	public void open() throws IOException {
		this.channel = DatagramChannel.open();
		this.channel.socket().bind(this.addressReceive);
		this.thread = new Thread(new Read(this.channel, this));
		this.thread.start();
	}

	/**
	 * データを受信するためのスレッド
	 * @author otamusan
	 *
	 */
	private static class Read implements Runnable {
		private DatagramChannel channel;
		private Connections data;

		public Read(DatagramChannel channel, Connections data) {
			this.channel = channel;
			this.data = data;
		}

		@Override
		public void run() {
			while (true) {
				ByteBuffer bb = ByteBuffer.allocate(this.data.typeManager.CAP);
				SocketAddress address = null;
				try {
					address = this.channel.receive(bb);
				} catch (IOException e) {
					e.printStackTrace();
				}
				bb.flip();
				if (address instanceof InetSocketAddress) {
					this.data.receive(bb, new Player((InetSocketAddress) address));
				}
			}
		}
	}

	protected void receive(ByteBuffer raw, Player player) {
		if (!this.isExist(player)) {
			System.out.println("connected by" + player.toString());
			this.addPlayer(player);
		}
		int containerid = this.typeManager.parseContainerId(raw);
		IDataSerializer<?> serializer = this.typeManager.getSerializer(containerid);
		Object value = serializer.decode(raw);
		this.getPlayerID(player).ifPresent(id -> {
			this.dataManager.receive(value, containerid, id);
		});
	}

	/**
	 * 更新処理、メインスレッドで必ず毎フレームごとに呼び出す
	 */
	public void onUpdate() {
		this.dataManager.update();
	}

	/**
	 * 接続しているプレイヤー全員に値を送信するメソッド
	 * @param <T> 送信する値のデータ型
	 * @param t 送信する値
	 * @param key 送信するコンテナへのアクセサ
	 * @throws IOException
	 */
	public <T> void send(T t, ContainerKey<T> key) throws IOException {
		ByteBuffer bytes = this.typeManager.getBuffer(t, key.serializer);
		this.dataManager.setSendData(t, this.getContainerID(key).orElseThrow(() -> new Error()));
		for (Player player : this.getPlayers()) {
			this.channel.send(bytes.asReadOnlyBuffer(), player.getAddress());
		}
	}

	/**
	 * {@link Connections#send(Object, ContainerKey) send}とは違い、以前に渡された値と今渡された値が一致しなかった時に送信する。
	 * @param <T> 送信する値のデータ型
	 * @param t 送信する値
	 * @param key 送信するコンテナへのアクセサ
	 * @throws IOException
	 */
	public <T> void share(T t, ContainerKey<T> key) throws IOException {
		ByteBuffer bytes = this.typeManager.getBuffer(t, key.serializer);
		for (Player player : this.getPlayers()) {
			this.channel.send(bytes.asReadOnlyBuffer(), player.getAddress());
		}

		this.dataManager.setSendData(t, this.getContainerID(key).orElseThrow(() -> new Error()));
	}

	/**
	 * 接続を閉じるスレッド
	 * @throws IOException
	 */
	public void close() throws IOException {
		this.channel.close();
	}

	/**
	 * 受信した値を格納する{@link Container}へのアクセサ
	 * @author otamusan
	 *
	 * @param <T>
	 */
	public static class ContainerKey<T> {
		private IDataSerializer<T> serializer;

		public ContainerKey(IDataSerializer<T> type) {
			this.serializer = type;
		}

		public IDataSerializer<T> getDataType() {
			return this.serializer;
		}

	}
}
