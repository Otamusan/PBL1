package otamusan.pbl.sample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import otamusan.pbl.Connections;
import otamusan.pbl.ContainerKeys;

public class SampleServer {
	public static Connections connection;
	public final static int period = 17;

	public static void main(String[] args) {
		connection = new Connections(new InetSocketAddress(445),
				ContainerKeys::init); //第一引数は通信を行う相手のアドレス、今回はポートだけを指定している。第二引数はContainerを登録するメソッド。
		try {
			connection.open(); //接続を開始する
		} catch (IOException e) {
			e.printStackTrace();
		}

		Timer main = new Timer();
		main.schedule(new TimerTask() {
			@Override
			public void run() {
				SampleServer.onUpdate();
			}
		}, 1, period);// 更新処理を行うためのスレッド
	}

	public static void onUpdate() {
		connection.IteratePlayers((playerkey) -> { //接続しているすべてのプレイヤーに対して以下のブロックの処理を実行する
			if (connection.checkReceived(ContainerKeys.message, playerkey)) { // データを新たに受け取ったかを確認する
				connection.getData(ContainerKeys.message, playerkey).ifPresent(System.out::println); // 作成したContainerをContainerKeyでアクセスし、データを取得、異常が無ければ表示している
			}
		});
		connection.onUpdate(); //必ず更新処理を行う
	}
}
