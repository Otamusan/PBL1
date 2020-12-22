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
		this.connection = new Connections(new InetSocketAddress(445), connection -> {
			ContainerKeys.init(connection);
		});
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

		while (true) {
			Scanner scan = new Scanner(System.in);

			String str = scan.next();
			try {
				this.connection.send(str.charAt(0), ContainerKeys.cha);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void onUpdate() {
		for (Player player : this.connection.getPlayers()) {
			if (this.connection.isChange(ContainerKeys.cha, player)) {
				System.out.println(this.connection.getData(ContainerKeys.cha, player));
			}
		}

		this.connection.onUpdate();
	}
}
