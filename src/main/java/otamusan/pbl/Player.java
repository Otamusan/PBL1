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
		if (obj instanceof Player) {
			return this.address.equals(((Player) obj).getAddress());
		}
		return false;
	}

	@Override
	public String toString() {
		return "[" + this.address.getHostString() + "," + this.address.getPort() + "]";
	}
}
