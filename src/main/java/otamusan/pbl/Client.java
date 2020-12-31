package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
	private int delay;
	protected Connection connection;

	public Client(int delay) {
		this.delay = delay;
		this.connection = new Connection(new InetSocketAddress("localhost", 445), connection -> {
			ContainerKeys.init(connection);
		});

	}

	public static void main(String[] args) {
		Client client = new Client(17);
		client.run();
	}

	public void run() {
		try {
			this.connection.open();
		} catch (IOException e) {

		}
		Timer main = new Timer();
		main.schedule(new TimerTask() {
			@Override
			public void run() {
				Client.this.onUpdate();
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
		if (this.connection.checkReceived(ContainerKeys.message)) {
			this.connection.getData(ContainerKeys.message).ifPresent(System.out::println);
		}
		if (this.connection.isDisconnecting()) {
			System.out.println("disconnected:" + this.connection.getAddress());
		}
		if (this.connection.checkConnecting()) {
			System.out.println("connected:" + this.connection.getAddress());
		}
		this.connection.onUpdate();
	}
}
