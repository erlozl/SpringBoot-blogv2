package shop.mtcoding.blogv2._core.error;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import shop.mtcoding.blogv2._core.util.Script;

@RestControllerAdvice
// Exception이 터지면 이쪽으로 다 모인다
// 스프링의 디스패처 서블릿이 제어하는 예외 처리를 담당
public class MyExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public String error(RuntimeException e) {
        return Script.back(e.getMessage());
    }
}
