package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
	private int delay;

	private Connection connection;

	public Client(int delay) {
		this.delay = delay;
		Scanner scan = new Scanner(System.in);

		String address = scan.next();
		String port = scan.next();
		String address2 = scan.next();
		String port2 = scan.next();
		this.connection = new Connection(new InetSocketAddress(address, Integer.parseInt(port)),
				new InetSocketAddress(address2, Integer.parseInt(port2)));
		ContainerKeys.init(this.connection);
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
				this.connection.send(str.charAt(0), ContainerKeys.cha);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void onUpdate() {
		/*if (this.connection.isChange(ContainerKeys.cha)) {
			System.out.println(this.connection.getData(ContainerKeys.cha));
		}*/
		this.connection.onUpdate();
	}
}
