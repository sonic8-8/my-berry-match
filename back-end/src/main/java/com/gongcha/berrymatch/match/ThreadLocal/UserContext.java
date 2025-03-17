package com.gongcha.berrymatch.match.ThreadLocal;

//매칭요청한 id를 로컬로 빼서 매칭완료쪽으로 보냄
// 매칭대기열이 자동으로 돌아가고있어서 id를 추가안했음
//매칭요청 컨트롤러에서 MatchCompletionService로 전송
public class UserContext {
    private static final ThreadLocal<Long> userId = new ThreadLocal<>();

    public static void setUserId(Long id) {
        userId.set(id);
    }

    public static Long getUserId() {
        return userId.get();
    }

    public static void clear() {
        userId.remove();
    }
}
