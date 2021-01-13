package otamusan.chat;

import otamusan.pblconnection.Connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
	private int delay;
	protected Connection connection;
	private String userName;
	private Scanner scan;

	public Client(int delay) {
		this.delay = delay;
		scan = new Scanner(System.in);

		System.out.println("User Name?");
		userName = scan.next();
		System.out.println("Server Address?");
		String address= scan.next();

		this.connection = new Connection(new InetSocketAddress(address, 44557), connection -> {
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
			try {
				connection.send(userName,ContainerKeys.userName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.connection.onUpdate();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		connection.close();
	}
}
