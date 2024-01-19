package choorai.excuseme.member.exception;

import choorai.excuseme.global.exception.CommonException;
import choorai.excuseme.global.exception.ErrorCode;

import java.util.Map;

public class MemberException extends CommonException {


    public MemberException(ErrorCode errorCode, Map<String, Object> additionalInfos) {
        super(errorCode, additionalInfos);
    }

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
