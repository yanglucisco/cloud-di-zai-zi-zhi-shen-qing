package org.ziranziyuanting.common.api;

import lombok.Getter;

/**
 * 自定义业务异常，用于已知的业务规则冲突（如账号重复、数据不存在等）
 * 由 ValidationExceptionHandler 统一处理，返回给前端友好的提示信息
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 建议的 HTTP 状态码，默认 400
     */
    private final int httpStatus;

    public BusinessException(String message) {
        super(message);
        this.httpStatus = 400;
    }

    public BusinessException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = 400;
    }
}
