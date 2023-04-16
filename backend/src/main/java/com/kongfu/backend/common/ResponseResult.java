package com.kongfu.backend.common;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author 付聪
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ResponseResult<T> implements Serializable {

    @JSONField(serializeUsing = BaseEnumCodec.class)
    private ResponseResultCode code = ResponseResultCode.Empty;

    private String message;

    private T data;

    public ResponseResult(ResponseResultCode code) {
        this.code = code;
        this.message = "";
        this.data = null;
    }

    public ResponseResult(ResponseResultCode code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    @Override
    public String toString() {
        return "ResponseResult{"
                + "code="
                + code
                + ", message='"
                + message
                + '\''
                + ", data="
                + data
                + '}';
    }
}
