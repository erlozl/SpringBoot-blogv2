package shop.mtcoding.blogv2.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import shop.mtcoding.blogv2.user.User;

@DataJpaTest // 모든 Repository, EntityManager
// 메인과 따로 새로운 스레드가 동작한다고 생각하면 됨
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void save_test() {
        // 영속화 되기 전 객체 - 비영속객체
        Board board = Board.builder()
                .title("제목")
                .content("내용")
                .user(User.builder().id(1).build())
                // User user = new User();
                // board.setUser(user);
                // System.out.println(board.getId());
                .build();

        // 영속 객체
        // 영속화 되는 순간 insert가 자동으로 실행됨
        boardRepository.save(board);
        // 그래서 DB데이터가 동기화돼서 id가 나옴
        System.out.println(board.getId());

    }
}
