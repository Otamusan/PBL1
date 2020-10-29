package otamusan.pbl;

import java.io.IOException;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
@ServerEndpoint("/hellowebsocket")
public class Server {
	private int delay;
	public Server(int delay) {
		this.delay = delay;
	}
	public void run() {
		Timer main = new Timer();
		main.schedule(new TimerTask() {
			@Override
			public void run() {
				Server.this.onUpdate();
			}
		}, 1, delay);
	}
	public void onUpdate() {
		
	}
	// 現在のセッションを記録
    Session currentSession = null;

    /*
     * 接続がオープンしたとき
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig ec) {
        this.currentSession = session;
    }

    /*
     * メッセージを受信したとき
     */
    @OnMessage
    public void receiveMessage(String msg) throws IOException {
        // メッセージをクライアントに送信する
        this.currentSession.getBasicRemote().sendText("Hello " + msg + ".");

        Set<Session> sessions = currentSession.getOpenSessions();
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText("Hello " + msg + ".(2)");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 接続がクローズしたとき
     */
    @OnClose
    public void onClose(Session session, CloseReason reason) {
        // ...
    }

    /*
     * 接続エラーが発生したとき
     */
    @OnError
    public void onError(Throwable t) {
        // ...
    }
}
