package test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

public class TestServer {
	public static void main(String[] args) throws IOException {
		DatagramChannel channel = DatagramChannel.open();
		channel.bind(new InetSocketAddress("localhost", 10003));
		ByteBuffer bb = ByteBuffer.allocate(1024);
		channel.receive(bb);
		bb.flip();
		System.out.println("受信:" + StandardCharsets.UTF_8.decode(bb).toString());
		channel.close();
	}
}
