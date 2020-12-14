package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Optional;

import otamusan.pbl.DataTypeManagers.ContainerKey;

public class Connection extends Connections {

	private InetSocketAddress sendAddress;

	private Player player;

	public Connection(InetSocketAddress send, InetSocketAddress receive) {
		super(receive);
		this.sendAddress = send;
		this.player = new Player(this.sendAddress);
	}

	public <T> Boolean isChange(ContainerKey<T> key) {
		return super.isChange(key, this.player);
	}

	@Override
	public <T> Optional<T> getData(ContainerKey<T> key, Player player) {
		return super.getData(key, this.player);
	}

	public void receive(ByteBuffer raw) {
		super.receive(raw, this.player);
	}

	@Override
	public void open() throws IOException {
		super.open();
		this.addPlayer(this.player);
	}
}
