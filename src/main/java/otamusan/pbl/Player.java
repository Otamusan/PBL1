package otamusan.pbl;

import java.net.InetSocketAddress;

public class Player {

	private InetSocketAddress address;

	public Player(InetSocketAddress address) {
		this.address = address;
	}

	public InetSocketAddress getAddress() {
		return this.address;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Player))
			return false;
		Player player = (Player) obj;
		return this.address.getPort() == player.getAddress().getPort()
				&& this.address.getHostString() == player.getAddress().getHostString();
	}
}
