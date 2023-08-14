package shop.mtcoding.blogv2.user;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(UserQueryRepository.class)
// JpaRepository로 연결을 안했다면 import로 연결시켜줘야 함
@DataJpaTest // jpaRepository만 메모리에 올려준다, IOC에 안 올려줌, Autowwired를 못함
public class UserQueryRepositoryTest {
    @Autowired
    private UserQueryRepository userQueryRepository;

    @Autowired
    private EntityManager em;

    // persist ( 영속화 )
    @Test
    public void save_test() {
        User user = User.builder()
                .username("love")
                .password("1234")
                .email("love@nate.com")
                .build();
        userQueryRepository.save(user);
    }

    // 1차 캐시
    @Test
    public void findById_test() {
        // 현재 PC는 비워있다
        System.out.println("1, pc는 비어있다");
        userQueryRepository.findById(1);
        em.clear();
        // 비워졌기 때문에 쿼리가 두번
        System.out.println("2, pc의 user 1은 영속화되어 있다");
        userQueryRepository.findById(1);
    }

    @Test
    public void update_test() {
        // jpa에서 update 알고리즘
        // 1. 업데이트 할 객체를 영속화 시켜야 한다
        // 2. 객체 상태 변경 ex) 기존 패스워드 1234를, 5678로 바꾸는 것
        // 3. em.flush or @Transactioal 정상 종료( 자동 flush )
        // 4. 테스트의 끝은 무조건 롤백이 일어남 - 강제로 em.flush를 해줘야 함
        User user = userQueryRepository.findById(1);
        user.setEmail("ssarmango@nate.com");
        em.flush();
    }
    // rollback
}
