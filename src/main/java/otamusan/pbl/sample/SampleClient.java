package otamusan.pbl.sample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import otamusan.pbl.Connection;
import otamusan.pbl.ContainerKeys;

public class SampleClient {
	public static Connection connection;
	public final static int period = 17;
	//

	public static void main(String[] args) {
		connection = new Connection(new InetSocketAddress("localhost", 445),
				ContainerKeys::init);////第一引数は通信を行う相手を示すアドレス。第二引数はContainerを登録するメソッド。
		try {
			connection.open(); //接続を開始する
		} catch (IOException e) {
			e.printStackTrace();
		}

		Timer main = new Timer();
		main.schedule(new TimerTask() {
			@Override
			public void run() {
				SampleClient.onUpdate();
			}
		}, 1, period); // 更新処理を行うためのスレッド
	}

	public static void onUpdate() {
		try {
			connection.send("Hello", ContainerKeys.message);//作成したContainerにContainerKeyでアクセスし、文字を送信している。
		} catch (IOException e) {
			e.printStackTrace();
		}
		connection.onUpdate(); //必ず更新処理を行う
	}
}
