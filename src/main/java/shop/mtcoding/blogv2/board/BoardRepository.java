package shop.mtcoding.blogv2.board;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// @Repository
/* jpaRepository => 기본 CRUD메서드를 주는 역할
    <entity이름, pk키 타입>
 * save(), findById()-pk키로 조회할 때, findAll(), count(), deleteById()
 * 기본 CRUD를 만들어줌
 */
public interface BoardRepository extends JpaRepository<Board, Integer> {

    // select id, title, content, user_id, created_at from board_tb b inner join
    // user_tb u on b.user_id = u.id;
    @Query("select b from Board b join b.user")
    // jpql에서는 inner join이 그냥 join임
    // fetch를 안 붙였을 때는 user를 프로젝션 안했는데
    // fetch를 붙여야 *를 한다 전체를 프로젝션한다는 것임
    // fetch = 연관된 객체 안에 들어가서 그 내용들을 다 하나하나 뽑아냄
    List<Board> mFindAll();

}
