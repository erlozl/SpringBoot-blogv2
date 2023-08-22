package shop.mtcoding.blogv2._core.error.ex;

// AJAX통신 MYapi JSON응답 
public class MyApiException extends RuntimeException {
    public MyApiException(String message) {
        super(message);
    }
}
