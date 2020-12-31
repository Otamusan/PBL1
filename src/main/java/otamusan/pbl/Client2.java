package otamusan.pbl;

import java.net.InetSocketAddress;

public class Client2 extends Client {
	public Client2(int delay) {
		super(delay);
		this.connection = new Connection(new InetSocketAddress("localhost", 445), key -> {
			ContainerKeys.init(key);
		});
	}

	public static void main(String[] args) {
		Client2 client = new Client2(17);
		client.run();
	}
}
