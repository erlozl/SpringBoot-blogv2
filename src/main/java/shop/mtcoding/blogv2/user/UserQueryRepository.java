package shop.mtcoding.blogv2.user;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserQueryRepository {
    @Autowired
    private EntityManager em;

    @Transactional
    public void save(User user) {
        em.persist(user);
        // 이게 insert쿼리가 날라가는 것임 - 영속화 시키는 행위 (영속성 컨텍스트)
        // 객체만 관리하면 됨
    }

    // JPA findById
    public User findById(Integer id) {
        // Query query = em.createQuery("select u from User u where u.id = :id",
        // User.class);
        // query.setParameter("id", id);
        // return (User) query.getSingleResult();
        // 쿼리를 날리면 날리는 대로 요청됨

        return em.find(User.class, id);
        // 이건 영속성컨텍스트에서 찾는 거임, 캐싱을 하는 것, 그렇기 때문에 쿼리가 하나밖에 안 날라감
        // 통신을 줄여줌
        // 쿼리 1개만 날라감
    }
}
