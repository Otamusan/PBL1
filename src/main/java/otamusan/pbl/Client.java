package otamusan.pbl;

import java.util.Timer;
import java.util.TimerTask;

public class Client {
	private int delay;

	public Client(int delay) {
		this.delay = delay;
	}

	public void run() {
		Timer main = new Timer();
		main.schedule(new TimerTask() {
			@Override
			public void run() {
				Client.this.onUpdate();
			}
		}, 1, this.delay);
	}

	public void onUpdate() {
	}
}
