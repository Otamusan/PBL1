package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ClientConnection {

	private DatagramChannel channel;
	private InetSocketAddress address;

	public ClientConnection(InetSocketAddress address) {
		this.address = address;
	}

	public void open() throws IOException {
		this.channel = DatagramChannel.open();
		System.out.println("ClientOpen");
	}

	public void onUpdate() {

	}

	public void send(ByteBuffer buffer) throws IOException {
		this.channel.send(buffer, this.address);
	}

	public void close() throws IOException {
		this.channel.close();
	}
}
