package otamusan.pbl;

import java.io.IOException;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
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

		// 初期化のため WebSocket コンテナのオブジェクトを取得する
		WebSocketContainer container = ContainerProvider
				.getWebSocketContainer();
		// サーバー・エンドポイントの URI
		URI uri = URI
				.create("ws://localhost:9080/hellowebsocket/server");
		// サーバー・エンドポイントとのセッションを確立する
		Session session;
		try {
			session = container.connectToServer(Client.class,
					uri);
			session.getBasicRemote().sendText("こんにちは");

			while (session.isOpen()) {
				Thread.sleep(100 * 1000);
				System.err.println("open");
			}
		} catch (DeploymentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@OnOpen
	public void onOpen(Session session) {
		/* セッション確立時の処理 */
		System.err.println("[セッション確立]");
	}

	@OnMessage
	public void onMessage(String message) {
		/* メッセージ受信時の処理 */
		System.err.println("[受信]:" + message);
	}

	@OnError
	public void onError(Throwable th) {
		/* エラー発生時の処理 */
	}

	@OnClose
	public void onClose(Session session) {
		/* セッション解放時の処理 */
	}

	public void onUpdate() {
	}
}
