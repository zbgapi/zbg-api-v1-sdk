package com.dd.api.entity.commom.result;

import com.dd.api.constant.ApiConstants;
import com.dd.api.exceptions.ApiException;
import lombok.Data;

/**
 * api 接口返回对象
 *
 * @author zhangzp
 */
@Data
public class HttpResult<T> {

    private T datas;

    private ResMsg resMsg;

    public boolean isSucc() {
        return resMsg != null && ApiConstants.RESPONSE_OK_CODE.equals(resMsg.getCode());
    }

    public T checkAndGetDatas() {
        if (resMsg == null) {
            if (datas != null) {
                return datas;
            }

            throw new ApiException("empty data");
        }

        if (ApiConstants.RESPONSE_OK_CODE.equals(resMsg.getCode())) {
            return datas;
        }

        throw new ApiException(resMsg.code, resMsg.message);
    }

    @Data
    public class ResMsg {
        private String message;

        private String method;

        private String code;
    }
}