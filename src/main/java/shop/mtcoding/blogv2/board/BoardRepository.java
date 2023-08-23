package shop.mtcoding.blogv2.board;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("select b from Board b join fetch b.user where b.id = :id")
    Board mFindById(@Param("id") Integer id);

    @Query("select b from Board b left join fetch b.replies r left join fetch r.user ru where b.id = :id")
    // 게시물 작성자는 화면에 안보이니까 안 넣기
    // 댓글 작성자는 보이니까 넣기
    Optional<Board> mFindByIdJoinRepliesInUser(@Param("id") Integer id);
    // jpql쿼리사용, dto가 필요없음

    @Query("select b from Board b where title like %:keyword% order by id desc")
    Page<Board> findByKeywordandPage(@Param("keyword") String keyword, Pageable pageable);
}
