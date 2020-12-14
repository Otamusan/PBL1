package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Optional;

import otamusan.pbl.DataTypeManager.ContainerKey;
import otamusan.pbl.Data.IDataSerializer;

public class dConnection {
	private DatagramChannel channel;
	private InetSocketAddress addressSend;
	private InetSocketAddress addressReceive;

	private DataTypeManager typeManager;
	private Thread thread;

	public dConnection(InetSocketAddress send, InetSocketAddress receive) {
		this.addressSend = send;
		this.addressReceive = receive;
		this.typeManager = new DataTypeManager();
	}

	public dConnection(InetSocketAddress address) {
		this(address, address);
	}

	public <T> ContainerKey<T> register(IDataSerializer<T> dataType) {
		return this.typeManager.register(dataType);
	}

	public <T> Boolean isChange(ContainerKey<T> key) {
		return this.typeManager.isChange(key);
	}

	public <T> Optional<T> getData(ContainerKey<T> key) {
		return this.typeManager.getData(key);
	}

	public void open() throws IOException {
		this.typeManager.lock();
		this.channel = DatagramChannel.open();
		this.channel.socket().bind(this.addressReceive);
		this.thread = new Thread(new Read(this.channel, this.typeManager));
		this.thread.start();
	}

	public static class Read implements Runnable {
		private DatagramChannel channel;
		private DataTypeManager data;

		public Read(DatagramChannel channel, DataTypeManager data) {
			this.channel = channel;
			this.data = data;
		}

		@Override
		public void run() {
			while (true) {
				ByteBuffer bb = ByteBuffer.allocate(this.data.CAP);
				try {
					this.channel.receive(bb);
				} catch (IOException e) {
					e.printStackTrace();
				}
				bb.flip();
				this.data.receive(bb);
			}
		}
	}

	public void onUpdate() {
		this.typeManager.update();
	}

	public <T> void send(T t, ContainerKey<T> key) throws IOException {
		ByteBuffer buffer = this.typeManager.getBuffer(t, key);
		this.channel.send(buffer, this.addressSend);
	}

	public void close() throws IOException {
		this.channel.close();
	}
}
