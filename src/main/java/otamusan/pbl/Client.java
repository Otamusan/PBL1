package otamusan.pbl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
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
		String sendData = "UDPてすとですよ";//送信データ
		byte[] data = sendData.getBytes("UTF-8");//UTF-8バイト配列の作成
		DatagramSocket sock = new DatagramSocket();//UDP送信用ソケットの構築
		DatagramPacket packet = new DatagramPacket(data, data.length, new InetSocketAddress("localhost", 10005));//指定アドレス、ポートへ送信するパケットを構築
		sock.send(packet);//パケットの送信
		System.out.println("UDP送信:" + sendData);//送信データにの表示
		sock.close();//ソケットのクローズ

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
