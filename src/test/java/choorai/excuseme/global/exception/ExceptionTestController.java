package choorai.excuseme.global.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ExceptionTestController {

    @GetMapping("/errorTest")
    public void test() {
        throw new CustomException(TestErrorCode.TEST_ERROR, Map.of("에러", "테스트"));
    }
}
