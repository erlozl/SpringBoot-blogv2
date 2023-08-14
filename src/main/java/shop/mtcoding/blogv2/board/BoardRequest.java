package shop.mtcoding.blogv2.board;

import lombok.Getter;
import lombok.Setter;

// 요청 DTO - 내부 클래스
// 내부 클래스 안을 호출하고 싶다면 상위를 먼저 new를 해야 함
public class BoardRequest {

    @Getter
    @Setter
    public static class SaveDTO {
        private String title;
        private String content;
    }
}
