package otamusan.pbl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
	private int delay;

	public Client(int delay) {
		this.delay = delay;
	}

	public static void main(String[] args) {
		Client client = new Client(App.delay);
		try {
			client.run();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public void run() throws IOException {
		System.out.println("クライアント起動");
		String sendData = "UDPチャネルのてすとですよ";//送信データ
		DatagramChannel channel = DatagramChannel.open();//DatagramChannelの作成
		ByteBuffer bb = StandardCharsets.UTF_8.encode(sendData);//送信データをUTF8バイト配列に変換
		channel.send(bb, new InetSocketAddress("localhost", 10003));//10003へ送信
		System.out.println("UDPチャネル送信:" + sendData);//送信データの表示
		channel.close();//チャネルクローズ

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
