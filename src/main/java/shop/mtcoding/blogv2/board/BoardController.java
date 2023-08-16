package shop.mtcoding.blogv2.board;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blogv2.user.User;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    @GetMapping("/test")
    public @ResponseBody Page<Board> test(@RequestParam(defaultValue = "0") Integer page, HttpServletRequest request) {
        Page<Board> boardPG = boardService.게시글목록보기(page);
        return boardPG; // ViewResolver (X), MessageConverter (O) -> json 직렬화
    }

    // invoke, reflection
    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "0") Integer page,
            HttpServletRequest request) {
        Page<Board> boardPG = boardService.게시글목록보기(page);
        request.setAttribute("boardPG", boardPG);
        request.setAttribute("prevPage", boardPG.getNumber() - 1);
        request.setAttribute("nextPage", boardPG.getNumber() + 1);
        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    // 1. 데이터 받기 - 테스트 해봐야 함(sysout)
    // 2. 인증 체크 ( :TODO)
    // 3. 유효성 검사 ( :TODO)
    // 4. 핵심로직 호출 ( 서비스 - 트랜잭션 관리를 함 )
    // 5. view(일반) or data-(ajax) 응답
    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO saveDTO) {
        System.out.println("title : " + saveDTO.getTitle());
        System.out.println("content : " + saveDTO.getContent());
        // 1. 이런 식으로 값을 제대로 받았는지 먼저 테스트 꼭!!!!!!!! ok

        // boardService.글쓰기(saveDTO, 1);
        // 세션 아이디가 없을 때는 임의로 정해주기
        // 글쓰기를 할 때는 무조건 userid가 필요함
        // 누가 썼는지를 확인해야 하니까

        boardService.글쓰기(saveDTO, 1);
        return "redirect:/";
    }
}