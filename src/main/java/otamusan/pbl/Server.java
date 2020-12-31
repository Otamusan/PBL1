package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
	private int delay;
	private Connections connection;

	public Server(int delay) {
		this.delay = delay;
		this.connection = new Connections(new InetSocketAddress(445), key -> {
			ContainerKeys.init(key);
		});
	}

	public static void main(String[] args) {
		Server server = new Server(17);
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

		while (true) {
			Scanner scan = new Scanner(System.in);

			String str = scan.next();
			try {
				this.connection.send(str, ContainerKeys.message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void onUpdate() {

		this.connection.IteratePlayers((playerkey) -> {
			if (this.connection.checkReceived(ContainerKeys.message, playerkey)) {
				this.connection.getData(ContainerKeys.message, playerkey).ifPresent(System.out::println);
			}
			if (this.connection.isDisconnecting(playerkey)) {
				System.out.println("disconnected:" + this.connection.getAddress(playerkey));
			}
			if (this.connection.checkConnecting(playerkey)) {
				System.out.println("connected:" + this.connection.getAddress(playerkey));
			}
		});

		this.connection.onUpdate();
	}
}
