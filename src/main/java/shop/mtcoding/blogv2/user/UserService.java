package shop.mtcoding.blogv2.user;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blogv2._core.error.ex.MyException;
import shop.mtcoding.blogv2._core.vo.MyPath;
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

        UUID uuid = UUID.randomUUID(); // 랜덤한 해시값을 만들어줌
        // 네트워크상에서 고유성을 보장하는 ID를 만들기 위한 표준 규약
        // 즉, 전 세계 유일한 식별자
        String fileName = uuid + "_" + joinDTO.getPic().getOriginalFilename();
        // 확장자때문에 joinDTO.getPic().getOriginalFilename(); 이게 뒤로 와야함
        System.out.println("fileName : " + fileName);

        // 프로젝트 실행 파일 변경 -> blogv2-1.0.jar 자바 실행파일
        // 해당 실행파일 경로에 images 폴더가 필요함
        Path filePath = Paths.get(MyPath.IMG_PATH + fileName);
        // ./ 내 서버의 현재 위치 (springboot-blog-v5폴더 내부 ) - 상대경로
        try {
            Files.write(filePath, joinDTO.getPic().getBytes());
        } catch (Exception e) {
            // 경로오류,용량부족 등
            throw new MyException(e.getMessage());
        }

        User user = User.builder()
                .username(joinDTO.getUsername())
                .password(joinDTO.getPassword())
                .email(joinDTO.getEmail())
                .picUrl(fileName)
                // .picUrl("./images/" + fileName)
                // 이렇게 넣으면 위험 - 사진폴더를 변경하고 싶은데 폴더 변경이 안되기 때문에
                // DB에는 파일에 이름만 저장
                // 하드디스크에 저장하고 저장되어 있는 경로를 넣기
                .build();
        userRepository.save(user); // em.persist 영속화, 응답될 때 다 날려버림
        // persist 되는 것임
    }

    public User 로그인(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername());

        // else를 하는 것보다 if문으로 나누는 게 가독성이 좋아보임

        // 1. 유저네임 검증
        if (user == null) {
            throw new MyException("유저네임이 없습니다");
        }

        // 2. 패스워드 검증
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new MyException("패스워드가 잘못되었습니다");
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

    public User 유저찾기(String username) {
        return userRepository.findByUsername(username);
    }

}
