package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

public class Connection {
	private DatagramChannel channel;
	private InetSocketAddress addressSend;
	private InetSocketAddress addressReceive;

	private Thread thread;
	private Data data;
	public static final int cap = 1024;

	public Connection(InetSocketAddress send, InetSocketAddress receive) {
		this.addressSend = send;
		this.addressReceive = receive;
	}

	public Connection(InetSocketAddress address) {
		this(address, address);
	}

	public void open() throws IOException {
		this.channel = DatagramChannel.open();
		this.channel.bind(this.addressReceive);
		this.data = new Data();

		this.thread = new Thread(new Read(this.channel, this.data));
		this.thread.start();

	}

	public static class Read implements Runnable {
		private DatagramChannel channel;
		private Data data;

		public Read(DatagramChannel channel, Data data) {
			this.channel = channel;
			this.data = data;
		}

		@Override
		public void run() {
			while (true) {
				ByteBuffer bb = ByteBuffer.allocate(cap);
				try {
					this.channel.receive(bb);
				} catch (IOException e) {
					e.printStackTrace();
				}
				bb.flip();
				System.out.println(StandardCharsets.UTF_8.decode(bb).toString());
				this.data.string = StandardCharsets.UTF_8.decode(bb).toString();
			}
		}
	}

	public static class Data {
		public String string = "empty";
	}

	public void onUpdate() {

	}

	public void send(ByteBuffer buffer) throws IOException {
		this.channel.send(buffer, this.addressSend);
	}

	public void close() throws IOException {
		this.channel.close();
	}
}
