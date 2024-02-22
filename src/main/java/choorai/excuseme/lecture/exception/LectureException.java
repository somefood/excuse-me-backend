package choorai.excuseme.lecture.exception;

import choorai.excuseme.global.exception.CommonException;
import choorai.excuseme.global.exception.ErrorCode;

import java.util.Map;

public class LectureException extends CommonException {

    public LectureException(ErrorCode errorCode, Map<String, Object> additionalInfos) {
        super(errorCode, additionalInfos);
    }

    public LectureException(ErrorCode errorCode) {
        super(errorCode);
    }
}
