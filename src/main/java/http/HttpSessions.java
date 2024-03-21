package http;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpSessions {
    private final static Map<String, HttpSession > sessions = new HashMap<>();

    public static String generateSessionId(){
        return UUID.randomUUID().toString();
    }
    public static HttpSession getSession(String sessionId){
        return sessions.get(sessionId);
    }

    public static void process(HttpRequest req, HttpResponse resp){
        // step1. 요청에서 세션 유무 확인
        HttpSession session = req.getSession();
        
        // step2. 세션이 존재하는 경우
        if(session != null) return;

        // step2.2 세션이 존재하지 않는 경우
        // 세션 신규 생성
        // 응답 헤더에 쿠키로 세션아이디 전달
        String sessionId = generateSessionId();
        session = new HttpSession();
        sessions.put(sessionId, session);
        resp.addCookie("JSESSIONID",sessionId);
    }

}
