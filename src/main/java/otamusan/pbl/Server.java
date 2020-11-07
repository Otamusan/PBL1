package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
	private int delay;

	public Server(int delay) {
		this.delay = delay;
	}

	public static void main(String[] args) {
		Server server = new Server(App.delay);
		try {
			server.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() throws IOException {

		System.out.println("サーバー起動");
		DatagramChannel channel = DatagramChannel.open();
		channel.bind(new InetSocketAddress(10003));
		channel.configureBlocking(false);
		Selector selectorRead = Selector.open();
		channel.register(selectorRead, SelectionKey.OP_READ);		
		Iterator<SelectionKey> iterator = selectorRead.selectedKeys().iterator();
		iterator.forEachRemaining((key)->{
			key.
		});
		ByteBuffer bb = ByteBuffer.allocate(1024);
		channel.receive(bb);
		bb.flip();
		System.out.println("UDPチャネル受信:" + StandardCharsets.UTF_8.decode(bb).toString());
		channel.close();

		Timer main = new Timer();
		main.schedule(new TimerTask() {
			@Override
			public void run() {
				Server.this.onUpdate();
			}
		}, 1, this.delay);
	}

	public void onUpdate() {

	}
}
