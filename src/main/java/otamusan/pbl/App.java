package otamusan.pbl;

import java.io.IOException;

public class App {
	public final static int delay = 17;

	public static void main(String[] args) {

		Client client = new Client(delay);
		Server server = new Server(delay);

		try {
			client.run();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		try {
			server.run();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}