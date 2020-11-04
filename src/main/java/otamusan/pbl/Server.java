package otamusan.pbl;

import java.util.Timer;
import java.util.TimerTask;

public class Server {
	private int delay;

	public Server(int delay) {
		this.delay = delay;
	}

	public void run() {
		Timer main = new Timer();
		main.schedule(new TimerTask() {
			@Override
			public void run() {
				Server.this.onUpdate();
			}
		}, 1, this.delay);
	}

	public void onUpdate() {

	}
}
