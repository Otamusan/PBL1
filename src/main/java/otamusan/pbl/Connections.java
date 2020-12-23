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

public class Connections {
	private DatagramChannel channel;
	private InetSocketAddress addressReceive;
	private Map<Integer, Player> players;
	protected DataManagers dataManager;
	protected TypeManager typeManager;
	private Thread thread;
	public Map<Integer, ContainerKey<?>> keyMap;

	public Connections(InetSocketAddress receive, Consumer<RegisterKey> containerRegister) {
		this.addressReceive = receive;
		this.typeManager = new TypeManager();
		this.players = new HashMap<Integer, Player>();
		this.keyMap = new HashMap<>();

		containerRegister.accept(new RegisterKey(this));
		this.dataManager = new DataManagers(this.typeManager.getSerializerSize());

	}

	public Optional<Integer> getPlayerID(Player player) {
		for (Integer id : this.players.keySet()) {
			if (this.players.get(id).equals(player))
				return Optional.of(id);
		}
		return Optional.empty();
	}

	public List<Player> getPlayers() {
		return new ArrayList<Player>(this.players.values());
	}

	public boolean isExist(Player player) {
		for (Player p : this.getPlayers()) {
			if (player.equals(p))
				return true;
		}
		return false;
	}

	public static class RegisterKey {
		private Connections connections;

		public RegisterKey(Connections connections) {
			this.connections = connections;
		}

		public <T> ContainerKey<T> register(IDataSerializer<T> serializer) {
			int containerid = this.connections.typeManager.register(serializer);
			ContainerKey<T> key = new ContainerKey<>(serializer);
			this.connections.keyMap.put(containerid, key);
			return key;
		}
	}

	public Optional<Integer> getContainerID(ContainerKey<?> key) {
		for (Integer id : this.keyMap.keySet()) {
			if (this.keyMap.get(id) == key)
				return Optional.of(id);
		}
		return Optional.empty();
	}

	public <T> Boolean isChange(ContainerKey<T> key, Player player) {
		return this.getContainerID(key).flatMap(containerid -> this.getPlayerID(player)
				.map(playerid -> this.dataManager.isChange(containerid, playerid))).orElse(false);
	}

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

	public void open() throws IOException {
		this.channel = DatagramChannel.open();
		this.channel.socket().bind(this.addressReceive);
		this.thread = new Thread(new Read(this.channel, this));
		this.thread.start();
	}

	public static class Read implements Runnable {
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

	public void receive(ByteBuffer raw, Player player) {
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

	public void onUpdate() {
		this.dataManager.update();
	}

	public <T> void send(T t, ContainerKey<T> key) throws IOException {
		ByteBuffer bytes = this.typeManager.getBuffer(t, key.serializer);
		this.dataManager.setSendData(t, this.getContainerID(key).orElseThrow(() -> new Error()));
		for (Player player : this.getPlayers()) {
			this.channel.send(bytes.asReadOnlyBuffer(), player.getAddress());
		}
	}

	public <T> void share(T t, ContainerKey<T> key) throws IOException {
		ByteBuffer bytes = this.typeManager.getBuffer(t, key.serializer);
		for (Player player : this.getPlayers()) {
			this.channel.send(bytes.asReadOnlyBuffer(), player.getAddress());
		}

		this.dataManager.setSendData(t, this.getContainerID(key).orElseThrow(() -> new Error()));
	}

	public void close() throws IOException {
		this.channel.close();
	}

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
