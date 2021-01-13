package otamusan.chat;

import otamusan.pblconnection.Connections;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
	private int delay;
	private Connections connection;
	private String userName;
	private Scanner scan;
	public Server(int delay) {
		this.delay = delay;

		scan = new Scanner(System.in);


		this.connection = new Connections(new InetSocketAddress(44557), key -> {
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
			String str = scan.next();
			System.out.println(str);
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
				this.connection.getData(ContainerKeys.message, playerkey).ifPresent((string->{
					try {
						connection.send(this.connection.getData(ContainerKeys.userName, playerkey).orElse("Unknown")+":"+string, ContainerKeys.message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}));

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

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		connection.close();
	}
}
