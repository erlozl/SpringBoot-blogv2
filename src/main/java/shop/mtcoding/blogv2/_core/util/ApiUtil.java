package shop.mtcoding.blogv2._core.util;

import lombok.Getter;
import lombok.Setter;

// json응답해주기 위한 ApiUtil를 만듬
// 공통 DTO = 틀은 이거에 맞춤
@Getter
@Setter
public class ApiUtil<T> {
    private boolean success; // true
    private T data; // 댓글쓰기 성공
    // 어떻게 응답해줄지 모를 때 제네릭

    public ApiUtil(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

}
