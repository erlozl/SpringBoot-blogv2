package shop.mtcoding.blogv2.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blogv2._core.error.ex.MyApiException;
import shop.mtcoding.blogv2._core.error.ex.MyException;
import shop.mtcoding.blogv2._core.util.ApiUtil;
import shop.mtcoding.blogv2._core.util.Script;

@Controller
public class UserController {

    // 유효성 검사는 Controller에서 - 단일 책임의 원칙

    @Autowired
    private UserRepository userRepository;

    @Autowired // DI 컴포넌트 스캔 중
    private UserService userService;

    @Autowired
    private HttpSession session;

    // c - v
    // DB 관련 모든 로직 - model ( mvc에서는 모델이라고 함)
    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    // M - V - C
    @PostMapping("/join")
    public String join(UserRequest.JoinDTO joinDTO) {
        System.out.println(joinDTO.getPic().getOriginalFilename());
        System.out.println(joinDTO.getPic().getSize());
        System.out.println(joinDTO.getPic().getContentType());
        // cat02.jpg
        // 94644
        // image/jpeg
        userService.회원가입(joinDTO);
        // service에 위임 - 여기서 내가 할 일이 아님

        return "user/loginForm"; // persist 초기화, 독립적으로 존재
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @PostMapping("/login")
    public @ResponseBody String login(UserRequest.LoginDTO loginDTO) {
        User sessionUser = userService.로그인(loginDTO);
        // if (sessionUser == null) {
        // throw new MyException("로그인 실패");
        // } 서비스에서 비즈니스 로직이 날라왔기 때문에 필요가 없음
        session.setAttribute("sessionUser", sessionUser);

        return Script.href("/");
    }

    // 브라우저 GET /logout 요청을 함 ( request 1)
    // 서버는 / 주소를 응답의 헤더에 담음 ( Location 이라는 키 값을 담음), 상태코드 302 (재요청 프로토콜)
    // 브라우저는 GET / 로 재요청 ( request 2)
    // index 페이지 응답받고 렌더링함
    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/user/updateForm")
    public String updateForm(HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        User user = userService.회원정보보기(sessionUser.getId());
        request.setAttribute("user", user);
        return "user/updateForm";
    }

    @PostMapping("/user/update")
    public String update(UserRequest.UpdateDTO updateDTO) {
        // 1. 회원수정(서비스)
        // 2. 세션 동기화
        User sessionUser = (User) session.getAttribute("sessionUser");
        User user = userService.회원수정(updateDTO, sessionUser.getId());
        // 회원수정이 된 객체를 받음
        session.setAttribute("sessionUser", user);

        // 그 다음 세션 동기화
        return "redirect:/";
    }

    @GetMapping("/api/check")
    public @ResponseBody ApiUtil<String> check(String username) {
        // User user = userRepository.findByUsername(username);
        User user = userService.유저찾기(username);

        if (user != null) {
            throw new MyApiException("아이디를 사용하실 수 없습니다");
            // return new ApiUtil<String>(false, "아이디를 사용하실 수 없습니다");
        }
        return new ApiUtil<String>(true, "아이디를 사용하실 수 있습니다");
    }

}
