package shop.mtcoding.blogv2.reply;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blogv2.user.User;

@Controller
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private HttpSession session;

    @PostMapping("/reply/{id}/delete")
    public String delete(@PathVariable Integer id, Integer boardId) {
        replyService.삭제(id);
        return "redirect:/board/" + boardId;
    }

    @PostMapping("/reply/insert")
    public String insert(ReplyRequest.ReplyWriteDTO replyWriteDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        replyService.댓글등록(replyWriteDTO, sessionUser.getId());
        return "redirect:/board/" + replyWriteDTO.getBoardId();
    }
}
