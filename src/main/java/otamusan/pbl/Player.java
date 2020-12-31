package otamusan.pbl;

import java.net.InetSocketAddress;

/**
 * 通信を行う相手を識別するためのクラス
 * @author otamusan
 *
 */
public class Player {
	private InetSocketAddress address;
	private long responseTime;
	private boolean isDead;
	private boolean isConnected;
	private boolean isChecked;

	public Player(InetSocketAddress address) {
		this.address = address;
		this.responseTime = System.currentTimeMillis();
		this.isDead = false;
		this.isChecked = false;
		this.isConnected = false;
	}

	public boolean isConnected() {
		return this.isConnected;
	}

	public boolean checkConnected() {
		if (this.isChecked)
			return false;
		if (this.isConnected)
			this.isChecked = true;
		return this.isConnected;
	}

	public boolean isDead() {
		return this.isDead;
	}

	public void kill() {
		this.isDead = true;
	}

	public InetSocketAddress getAddress() {
		return this.address;
	}

	public void onResponse() {
		this.isConnected = true;
		this.responseTime = System.currentTimeMillis();
	}

	public long getResponseTime() {
		return System.currentTimeMillis() - this.responseTime;
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
