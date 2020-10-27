package otamusan.pbl;

public class App {
	public final static int delay = 17;

	public static void main(String[] args) {

		Client client = new Client(delay);
		Server server = new Server(delay);
		
		client.run();
		server.run();
	}
}
