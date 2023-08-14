package shop.mtcoding.blogv2.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository
/* jpaRepository => 기본 CRUD메서드를 주는 역할
    <entity이름, pk키 타입>
 * save(), findById()-pk키로 조회할 때, findAll(), count(), deleteById()
 * 기본 CRUD를 만들어줌
 */
public interface BoardRepository extends JpaRepository<Board, Integer> {
}
