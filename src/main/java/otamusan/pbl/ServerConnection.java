package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

public class ServerConnection {
	private InetSocketAddress address;
	private DatagramChannel channel;
	private Thread thread;
	private Data data;

	public ServerConnection(InetSocketAddress address) {
		this.address = address;
	}

	public void onUpdate() {
	}

	public void open() throws IOException {
		System.out.println("ServerOpen");
		this.channel = DatagramChannel.open();
		this.channel.bind(this.address);
		this.data = new Data();

		this.thread = new Thread(new Read(this.channel, this.data));
		this.thread.run();
	}

	public void close() throws IOException {
		this.channel.close();
	}

	public static class Data {
		public String string = "empty";

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
				ByteBuffer bb = ByteBuffer.allocate(1024);
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
}
