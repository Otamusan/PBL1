package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
	private int delay;

	public Server(int delay) {
		this.delay = delay;
	}

	public static void main(String[] args) {
		Server server = new Server(App.delay);
		try {
			server.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() throws IOException {

		System.out.println("サーバー起動");
		DatagramChannel channel = DatagramChannel.open();//データグラムチャネルの作成
		channel.bind(new InetSocketAddress(10003));//受信ポートの指定(10003で受け付け)
		ByteBuffer bb = ByteBuffer.allocate(1024);//最大1024バイト受信バッファの作成
		channel.receive(bb);//データの受信
		bb.flip();//現在のデータを受信データとして確定させる
		System.out.println("UDPチャネル受信:" + StandardCharsets.UTF_8.decode(bb).toString());//UTF8でバイト配列からStringへ変換
		channel.close();//チャネルのクローズ
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
