package otamusan.pbl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
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

		DatagramSocket sock = new DatagramSocket(10005);//10005ポートでUDP受信用ソケット構築
		byte[] data = new byte[1024];//受信最大バッファ
		DatagramPacket packet = new DatagramPacket(data, data.length);//受信用パケットを構築
		sock.receive(packet);//受信
		System.out.println("UDP受信:" + new String(Arrays.copyOf(packet.getData(), packet.getLength()), "UTF-8"));//受信データの表示
		sock.close();//ソケットのクローズ
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
