package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Client2 {
	private int delay;

	private Connection connection;

	public Client2(int delay) {
		this.delay = delay;
		this.connection = new Connection(new InetSocketAddress("localhost", 445),
				new InetSocketAddress("localhost", 443));

		ContainerKeys.init(this.connection);
	}

	public static void main(String[] args) {
		Client2 client = new Client2(App.delay);
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
				Client2.this.onUpdate();
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
			System.out.println(this.connection.getPlayers());

		}
	}

	public void onUpdate() {
		if (this.connection.isChange(ContainerKeys.cha)) {
			System.out.println(this.connection.getData(ContainerKeys.cha));
		}
		this.connection.onUpdate();
	}
}
