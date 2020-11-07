package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
	private int delay;

	private ClientConnection connection;

	public Client(int delay) {
		this.delay = delay;
		this.connection = new ClientConnection(new InetSocketAddress("localhost", 10003));
	}

	public static void main(String[] args) {
		Client client = new Client(App.delay);
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
			System.out.println(str);
			try {
				this.connection.send(StandardCharsets.UTF_8.encode(str));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void onUpdate() {
		this.connection.onUpdate();
	}
}
