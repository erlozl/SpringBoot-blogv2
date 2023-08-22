package shop.mtcoding.blogv2.board;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blogv2.user.User;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private HttpSession session;

    @GetMapping("/test")
    public @ResponseBody Page<Board> test(@RequestParam(defaultValue = "0") Integer page, HttpServletRequest request) {
        Page<Board> boardPG = boardService.게시글목록보기(page);
        return boardPG; // ViewResolver (X), MessageConverter (O) -> json 직렬화
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        // DTO에 옮겨줘야 함
        Board board = boardService.상세보기(id);
        model.addAttribute("board", board);

        return "board/detail";
    }

    @GetMapping("test/board/{id}")
    public @ResponseBody Board testDetail(@PathVariable Integer id) {
        // DTO에 옮겨줘야 함
        Board board = boardRepository.mFindByIdJoinRepliesInUser(id).get();
        return board;
    }

    // invoke, reflection
    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "") String keyword, HttpServletRequest request) {

        Page<Board> boardPG = boardService.게시글목록보기(page);
        request.setAttribute("boardPG", boardPG);
        request.setAttribute("prevPage", boardPG.getNumber() - 1);
        request.setAttribute("nextPage", boardPG.getNumber() + 1);
        request.setAttribute("keyword", keyword);
        boardService.키워드(page, keyword);
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

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable Integer id, HttpServletRequest request) {
        Board board = boardService.게시글수정목록(id);
        request.setAttribute("board", board);
        return "board/updateForm";
    }

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable Integer id, BoardRequest.UpdateDTO updateDTO) {
        // where 데이터, body, session값
        boardService.게시글수정(updateDTO, id);
        return "redirect:/board/" + id;
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable Integer id) {
        boardService.삭제(id);
        return "redirect:/";
    }

}