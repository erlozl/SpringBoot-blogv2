package shop.mtcoding.blogv2.board;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.blogv2.user.User;

@DataJpaTest // 모든 Repository, EntityManager
// 메인과 따로 새로운 스레드가 동작한다고 생각하면 됨
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void mFindByIdJoinRepliesInUser_test() {
        Board board = boardRepository.mFindByIdJoinRepliesInUser(1).get();
        // System.out.println("board : id : " + board.getId());
        // System.out.println("board : title : " + board.getTitle());
        // System.out.println("board : content : " + board.getContent());
        // System.out.println("board : createAt : " + board.getCreatedAt());
        // System.out.println("===============================");
        // // System.out.println("board in user : id : " + board.getUser().getId());
        // // System.out.println("board in user : username : " +
        // // board.getUser().getUsername());
        // // 쿼리로 user를 들고 온 적 없지만, get으로 들고왔기 때문에 lazy loading 됨
        // // 캐싱이 되니까 조회를 안함
        // board.getReplies().stream().forEach(r -> {
        // System.out.println("board in replies : id : " + r.getId());
        // System.out.println("board in replies : comment : " + r.getComment());
        // System.out.println("board in replies in user : id : " + r.getUser().getId());
        // System.out.println("board in replies in user : username : " +
        // r.getUser().getUsername());
        // });
        // 추가적인 쿼리없이 한방에 나온다면 잘 된 것임 => lazyloading 발동 안 한것
    }

    @Test
    public void findById_test() {
        Optional<Board> boardOP = boardRepository.findById(5);
        // if (boardOP.isPresent()) {
        // System.out.println("테스트 : board가 있습니다");
        // }
    }

    @Test
    public void findAll_paging_test() throws JsonProcessingException {
        Pageable pageable = PageRequest.of(0, 3, Sort.Direction.DESC, "id");
        Page<Board> boardPG = boardRepository.findAll(pageable);
        // JSON으로 보는 게 편함

        // objectMapper는 boardPg객체의 getter를 호출하면서 json으로 만든다
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(boardPG); // 자바 객체를 JSON으로 변환
        System.out.println(json);
        // 이렇게 해서 Fail On Empty라는 오류가 나온다면 Eager로 바꿔주기
        // console에 나온 것을 Online JSON Viewer에 출력해보기

        // 오류해결 첫번째

    }

    @Test
    public void mFindAll_test() {
        boardRepository.mFindAll();
        // 유저 정보가 프로젝션이 안된거
        // 세로로 컬럼을 고르는 것 - 프로젝션
    }

    @Test
    public void findAll_test() {
        System.out.println("조회 직전");
        List<Board> boardList = boardRepository.findAll();
        System.out.println("조회 후 : Lazy");
        // 연관된 user까지 조회됨, 그래서 select를 3번이나 날림
        // 행 : 5개 -> 객체 : 5개
        // 각행 : Board(id=1, title = 제목1, content=내용1, created_at= 날짜, User(id=1)) =
        // lazy(pk만 땡김)
        System.out.println(boardList.get(0).getId()); // 1 ( 조회 x )
        System.out.println(boardList.get(0).getUser().getId()); // 1 ( 조회 x )

        // lazyloding 지연로딩 - 없으니까 알아서 채워줌
        // 영속화된 객체에 null값을 찾으려고 하면, 조회가 일어남
        // 연관된 객체에 null을 참조하려고 하면 없으니까 조회
        System.out.println(boardList.get(0).getUser().getUsername()); // null -> ssar
        // 필요한 상황에만 땡겨옴
    }

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
