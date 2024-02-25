package choorai.excuseme.oauth.exception;

import choorai.excuseme.global.exception.CommonException;
import choorai.excuseme.global.exception.ErrorCode;
import java.util.Map;

public class OauthException extends CommonException {


    public OauthException(ErrorCode errorCode, Map<String, Object> additionalInfos) {
        super(errorCode, additionalInfos);
    }

    public OauthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
