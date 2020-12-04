package test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

public class TestClient {
	public static void main(String[] args) throws IOException {
		String sendData = "UDP";
		DatagramChannel channel = DatagramChannel.open();
		ByteBuffer bb = StandardCharsets.UTF_8.encode(sendData);
		channel.send(bb, new InetSocketAddress("localhost", 10003));
		System.out.println("送信:" + sendData);
		channel.close();
	}
}
