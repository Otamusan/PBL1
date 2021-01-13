package otamusan.pblconnection;

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
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import otamusan.pblconnection.data.IDataSerializer;
import otamusan.pblconnection.data.Types;

/**
 * 通信を管理するクラス。
 * @author otamusan
 *
 */
public class Connections {
	protected DatagramChannel channel;
	protected InetSocketAddress addressReceive;
	private Map<Integer, Player> players;
	protected DataManagers dataManager;
	protected TypeManager typeManager;
	protected Thread thread;
	private Map<Integer, ContainerKey<?>> keyMap;
	private Map<Integer, PlayerKey> playerKeys;
	private ContainerKey<Integer> connectionCheck;
	private long maxResponseTime = 1500;
	private long sendResponseCheckInterval = 1000;

	/**
	 *
	 * @param receive データを受信するアドレス
	 * @param containerRegister {@link Container}を登録する処理を行うメソッド
	 */
	public Connections(InetSocketAddress receive, Consumer<RegisterKey> containerRegister) {
		this.addressReceive = receive;
		this.typeManager = new TypeManager();
		this.players = new HashMap<Integer, Player>();
		this.playerKeys = new HashMap<Integer, Connections.PlayerKey>();
		this.keyMap = new HashMap<>();
		RegisterKey key = new RegisterKey(this);
		containerRegister.accept(key);
		this.connectionCheck = key.register(Types.TYPE_INT);
		this.dataManager = new DataManagers(this.typeManager.getSerializerSize());
	}

	/**
	 * 送受信するデータのByte数の最大値。デフォルトは1024
	 * @param cap
	 */
	public void setByteCapacity(int cap) {
		this.typeManager.setByteCapacity(cap);
	}

	private Optional<Integer> getPlayerID(Player player) {
		for (Integer id : this.players.keySet()) {
			if (this.players.get(id).equals(player))
				return Optional.of(id);
		}
		return Optional.empty();
	}

	private Optional<Integer> getPlayerID(PlayerKey key) {
		for (Integer id : this.playerKeys.keySet()) {
			if (this.playerKeys.get(id) == key)
				return Optional.of(id);
		}
		return Optional.empty();
	}

	protected Optional<PlayerKey> getPlayerKey(Player player) {
		return this.getPlayerID(player).map(this.playerKeys::get);
	}

	/**
	 *
	 * @param key 接続状況を調べる{@link PlayerKey}
	 * @return 指定した{@link Player}が存在しないときは{@link Optional#empty()}、ちょうどフレーム内で切断したときはtrueを返す。
	 */
	public boolean isDisconnecting(PlayerKey key) {
		return this.getPlayer(key).map(Player::isDead).orElse(false);
	}

	/**
	 * 接続できたか確認するメソッド
	 * @param key
	 * @return
	 */
	public boolean isConnected(PlayerKey key) {
		return this.getPlayer(key).map(Player::isConnected).orElse(false);
	}

	/**
	 * 指定された{@link Player}が接続できたときにtrueを返す。
	 * 厳密には接続後に指定された{@link Player}に対してこのメソッドが初めて呼び出されたときtrueを返す。
	 * @param key
	 * @return
	 */
	public boolean checkConnecting(PlayerKey key) {
		return this.getPlayer(key).map(Player::checkConnected).orElse(false);
	}

	private Optional<Player> getPlayer(PlayerKey key) {
		return this.getPlayerID(key).flatMap(this::getPlayer);
	}

	private Optional<Player> getPlayer(int id) {
		if (!this.players.containsKey(id))
			return Optional.empty();
		return Optional.of(this.players.get(id));
	}

	public Optional<InetSocketAddress> getAddress(PlayerKey key) {
		return this.getPlayer(key).map(Player::getAddress);
	}

	/**
	 * use {@link Connections#IteratePlayers(Consumer)}
	 * @return 各プレイヤーへのアクセサの全体
	 */
	@Deprecated
	public List<PlayerKey> getPlayerKeys() {
		return new ArrayList<Connections.PlayerKey>(this.playerKeys.values());
	}

	/**
	 * 与えられた{@link Consumer}を各{@link Player}の{@link PlayerKey}で反復処理を行うメソッド
	 * @param consumer 与えられたPlayerKeyに対して行いたい処理
	 */
	public void IteratePlayers(Consumer<PlayerKey> consumer) {
		for (PlayerKey player : this.playerKeys.values()) {
			consumer.accept(player);
		}
	}

	private List<Player> getPlayers() {
		return new ArrayList<Player>(this.players.values());
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
	 * @param playerkey 通信を行っているプレイヤー
	 * @return
	 */
	public <T> Boolean isChange(ContainerKey<T> key, PlayerKey playerkey) {
		return this.getContainerID(key).flatMap(containerid -> this.getPlayerID(playerkey)
				.map(playerid -> this.dataManager.isChange(containerid, playerid))).orElse(false);
	}

	/**
	 * 指定した{@link Container}の値を取得するメソッド
	 * @param <T> 格納される値のデータ型
	 * @param key 値を取得したいコンテナへのアクセサ
	 * @param playerkey 通信を行っているプレイヤーへのアクセサ
	 * @return 格納されていた値
	 */
	public <T> Optional<T> getData(ContainerKey<T> key, PlayerKey playerkey) {
		return this.getContainerID(key).flatMap(containerid -> this.getPlayerID(playerkey)
				.flatMap(playerid -> this.dataManager.getData(containerid, playerid)
						.flatMap(t -> key.serializer.cast(t))));
	}

	/**
	 * 前回呼び出した後から現在までに指定した{@link Container}が値を受け取ったかを確認するメソッド
	 * @param <T> 格納される値のデータ型
	 * @param key 確認したいコンテナへのアクセサ
	 * @param playerkey 通信を行っているプレイヤーへのアクセサ
	 * @return 値を受け取っていたらtrue
	 */
	public <T> Boolean checkReceived(ContainerKey<T> key, PlayerKey playerkey) {
		return this.getContainerID(key).flatMap(containerid -> this.getPlayerID(playerkey)
				.map(playerid -> this.dataManager.checkRecieved(containerid, playerid))).orElse(false);
	}

	public void addPlayer(Player player) {
		int c = 0;
		for (int i = 0; i < this.players.size()+1; i++) {
			if (this.players.containsKey(i)) continue;
			c = i;
		}
		this.players.put(c, player);
		this.playerKeys.put(c, new PlayerKey());
		this.dataManager.addContainers(this.getPlayerID(player).orElseThrow(() -> new Error("error")));
	}

	public void removePlayer(Player player) {
		int id = this.getPlayerID(player).orElseThrow(() -> new Error());
		this.players.remove(id);
		this.playerKeys.remove(id);
		this.dataManager.removeContainers(id);
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
		this.startAccessCheck();
	}

	protected void startAccessCheck() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						try {
							Connections.this.send(3, Connections.this.connectionCheck);
						} catch (IOException e) {
						}
					}
				}, 1, Connections.this.sendResponseCheckInterval);
			}
		}).start();
	}

	/**
	 * データを受信するためのスレッド
	 * @author otamusan
	 *
	 */
	static class Read implements Runnable {
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
					//e.printStackTrace();
				}
				bb.flip();
				if (address instanceof InetSocketAddress) {
					this.data.receive(bb, (InetSocketAddress) address);
				}
			}
		}
	}

	private Optional<Player> getPlayer(InetSocketAddress address) {
		for (Player player : this.getPlayers()) {
			if (player.getAddress().equals(address))
				return Optional.of(player);
		}
		return Optional.empty();
	}

	protected void receive(ByteBuffer raw, InetSocketAddress address) {
		Player player = this.getPlayer(address).orElseGet(() -> {
			Player p = new Player(address);
			this.addPlayer(p);
			return p;
		});
		int containerid = this.typeManager.parseContainerId(raw);

		if (containerid == this.getContainerID(this.connectionCheck).get()) {
			player.onResponse();
		}

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

		for (Player player : this.getPlayers()) {
			if (player.isDead()) {
				this.removePlayer(player);
			}
		}
		this.dataManager.update();
		for (Player player : this.getPlayers()) {
			if (player.getResponseTime() >= this.maxResponseTime)
				player.kill();
		}
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
	 * また、送信されなかった場合{@link Connections#checkReceived(ContainerKey, PlayerKey)}に反映されない
	 * @param <T> 送信する値のデータ型
	 * @param t 送信する値
	 * @param key 送信するコンテナへのアクセサ
	 * @throws IOException
	 */
	public <T> void share(T t, ContainerKey<T> key) throws IOException {
		ByteBuffer bytes = this.typeManager.getBuffer(t, key.serializer);
		for (Player player : this.getPlayers()) {
			if (!this.getContainerID(key).map(id -> this.dataManager.shouldSend(t, id)).orElse(false))
				continue;
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
		channel.socket().close();
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

	public static class PlayerKey {
	}
}
