package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Optional;

import otamusan.pbl.DataTypeManagers.ContainerKey;

public class Connection extends Connections {

	private InetSocketAddress sendAddress;

	private

	public Connection(InetSocketAddress send, InetSocketAddress receive) {
		super(receive);
		this.sendAddress = send;
		this.addPlayer(new Player(this.sendAddress));
	}

	@Override
	public <T> Boolean isChange(ContainerKey<T> key, Player player) {
		return super.isChange(key, player);
	}

	@Override
	public <T> Optional<T> getData(ContainerKey<T> key, Player player) {
		return this.typeManager.getData(key, player);
	}

	@Override
	public void addPlayer(Player player) {
		this.players.add(player);
		this.typeManager.addConnection(player);
	}

	@Override
	public void open() throws IOException {
		this.typeManager.lock();
		this.channel = DatagramChannel.open();
		this.channel.socket().bind(this.addressReceive);
		this.thread = new Thread(new Read(this.channel, this));
		this.thread.start();
	}

	public static class Read implements Runnable {
		private DatagramChannel channel;
		private Connection data;

		public Read(DatagramChannel channel, Connection data) {
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

	@Override
	public void receive(ByteBuffer raw, Player player) {
		if (!this.isExist(player)) {
			System.out.println("connected by" + player.toString());
			this.addPlayer(player);
		}
		this.typeManager.receive(raw, player);
	}

	@Override
	public void onUpdate() {
		this.typeManager.update();
	}

	@Override
	public <T> void send(T t, ContainerKey<T> key) throws IOException {
		ByteBuffer buffer = this.typeManager.getBuffer(t, key);
		this.channel.send(buffer, this.addressSend);

		for (Player player : this.players) {
			this.channel.send(buffer, player.getAddress());
		}
	}

	@Override
	public void close() throws IOException {
		this.channel.close();
	}
}
