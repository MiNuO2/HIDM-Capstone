package livechat.websocket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

// 이 클래스가 WebSocket 엔드포인트임을 선언
@ServerEndpoint("/chat")
public class ChatServer {

    // 현재 접속 중인 클라이언트들 저장 (동시성 고려해서 CopyOnWrite 사용)
    private static Set<Session> clients = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
        System.out.println("새 연결: " + session.getId());
    }
    
    @OnMessage
    public void onMessage(String message, Session sender) throws IOException {
        for (Session client : clients) {
            if (client.isOpen()) {
                client.getBasicRemote().sendText("User " + sender.getId() + ": " + message);
            }
        }
    }


    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
        System.out.println("연결 종료: " + session.getId());
    }
}
