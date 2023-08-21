package shop.mtcoding.blogv2.reply;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blogv2.board.Board;
import shop.mtcoding.blogv2.reply.ReplyRequest.ReplyWriteDTO;
import shop.mtcoding.blogv2.user.User;

@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    @Transactional
    public void 삭제(Integer id) {
        replyRepository.deleteById(id);
    }

    @Transactional
    public void 댓글등록(ReplyWriteDTO replyWriteDTO, Integer userId) {
        Reply reply = Reply.builder()
                .comment(replyWriteDTO.getComment())
                .board(Board.builder().id(replyWriteDTO.getBoardId()).build())
                .user(User.builder().id(userId).build())
                .build();
        replyRepository.save(reply);
    }

}
