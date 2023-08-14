package shop.mtcoding.blogv2.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blogv2.user.UserRequest.JoinDTO;
import shop.mtcoding.blogv2.user.UserRequest.LoginDTO;
import shop.mtcoding.blogv2.user.UserRequest.UpdateDTO;

// 핵심로직 처리, 트랜잭션 관리, 예외처리
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 핵심로직은 서비스한테 넘기는 것임
    // 면접 질문 : 트랜잭션을 어디에 붙여야 효과적인지?
    // 트랜잭션의 관리를 어디서 했는가? Service
    // 트랜잭셔널은 서비스나 컨트롤러에 어디든지 붙일 수 있음
    // Service에 붙이는 게 왜 좋은지 = DB가 고립성의 시간이 줄어들어서
    // 자원을 효율적으로 쓰기 위해서?

    // 깔끔한 데이터를 만들기 위해 트랙잭션 사용
    // 컨트롤러에는 DB코드 말고 유효성같은 다른 코드가 많은데 굳이
    // 컨트롤러에서부터 Transactional을 탈 필요 없다
    @Transactional
    public void 회원가입(JoinDTO joinDTO) {
        User user = User.builder()
                .username(joinDTO.getUsername())
                .password(joinDTO.getPassword())
                .email(joinDTO.getEmail())
                .build();
        userRepository.save(user); // em.persist 영속화, 응답될 때 다 날려버림
        // persist 되는 것임
    }

    public User 로그인(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername());

        // else를 하는 것보다 if문으로 나누는 게 가독성이 좋아보임

        // 1. 유저네임 검증
        if (user == null) {
            return null;
        }

        // 2. 패스워드 검증
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            return null;
        }

        // 3. 로그인 성공
        return user;

        // if (user == null) { // username이 존재하지 않음
        // return null;
        // } else { // username이 존재함
        // if (user.getPassword().equals(loginDTO.getPassword())) {
        // return user;
        // } else {
        // return null;
        // }
        // }
    }

    public User 회원정보보기(Integer id) {
        return userRepository.findById(id).get();
    }

    @Transactional
    public User 회원수정(UpdateDTO updateDTO, Integer id) {
        // 1. 조회 ( 영속화 )
        User user = userRepository.findById(id).get();
        // 2. 변경
        user.setPassword(updateDTO.getPassword());
        return user;
    } // 3. flush

}
