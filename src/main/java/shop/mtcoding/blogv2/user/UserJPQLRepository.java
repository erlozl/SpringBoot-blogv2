package shop.mtcoding.blogv2.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserJPQLRepository extends JpaRepository<User, Integer> {
    // executeQuery
    @Query(value = "select u from User u where u.id = :id")
    Optional<User> mFindById(@Param("id") Integer id);
    // 이건 id가 있으니까 사용안해도 됨

    // executeQuery
    // 쿼리메서드 안 붙여도 됨 - 협업을 같이 할 때 어떤 쿼리가 동작하는지 확인할 수 있으니 붙이기
    @Query(value = "select u from User u where u.username = :username")
    User findByUsername(@Param("username") String username);

    // insert, update, delete는 JPQL 사용하지 못함
    @Modifying // executeUpdate
    @Query(value = "insert into user_tb(created_at, email, password, username) values(now(), :email, :password, :username)", nativeQuery = true)
    void mSave(@Param("username") String username, @Param("password") String password, @Param("email") String email);
}