package org.ziranziyuanting.account.config;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

public class ReactiveUserContext {
    private static final String USER_ID_KEY = "userId";
    private static final String USER_NAME_KEY = "userName";

    // 存入 Context（通常在 Filter 中调用）
    public static Context write(String userId, String userName) {
        return Context.of(USER_ID_KEY, userId, USER_NAME_KEY, userName);
    }

    // 提取 userId
    public static Mono<String> getUserId() {
        return Mono.deferContextual(ctx -> {
            String userIdOptional = ctx.getOrDefault(USER_ID_KEY, null);
            return Mono.justOrEmpty(userIdOptional);
        }

        );
    }

    // 提取 userName
    public static Mono<String> getUserName() {
        return Mono.deferContextual(ctx -> Mono.justOrEmpty(ctx.getOrDefault(USER_NAME_KEY, null)));
    }
}
