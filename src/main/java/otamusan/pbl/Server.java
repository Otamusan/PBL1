package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
	private int delay;
	private Connection connection;

	public Server(int delay) {
		this.delay = delay;
		this.connection = new Connection(new InetSocketAddress("localhost", 10003),
				new InetSocketAddress("localhost", 10004));
		ContainerKeys.init(this.connection);
	}

	public static void main(String[] args) {
		Server server = new Server(App.delay);
		server.run();
	}

	public void run() {
		try {
			this.connection.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Timer main = new Timer();
		main.schedule(new TimerTask() {
			@Override
			public void run() {
				Server.this.onUpdate();
			}
		}, 1, this.delay);
	}

	public void onUpdate() {
		this.connection.onUpdate();
		if (this.connection.isChange(ContainerKeys.cha)) {
			System.out.println(this.connection.getData(ContainerKeys.cha));
		}
	}
}
