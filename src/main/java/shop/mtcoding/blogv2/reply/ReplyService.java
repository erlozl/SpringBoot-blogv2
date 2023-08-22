package shop.mtcoding.blogv2.reply;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blogv2._core.error.ex.MyApiException;
import shop.mtcoding.blogv2.board.Board;
import shop.mtcoding.blogv2.reply.ReplyRequest.SaveDTO;
import shop.mtcoding.blogv2.user.User;

@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    @Transactional
    public void 댓글쓰기(SaveDTO saveDTO, Integer id) {

        // 1. board_id가 존재하는지 유무 - 조회라는 개념이 필요함
        // 클라이언트로부터 받은 데이터들은 신뢰할 수가 없음
        // -> 게시판 1번이 존재하지만 100번이 존재하지 않을 수도 있기 때문에

        // 2. 제약조건이 없다면 조회를 반드시 하고 insert해야함 꼬오오오오옥

        // insert시도할 때 메타데이터를 보고 터트려버림

        // 서비스로직을 짤 때
        // ㅡ> 정상적인 insert를 했을 때 오류가 안 남
        // ㅡ> 비정상적인 접근이 있을 때 오류가 남

        // 서비스의 시간은 transaction의 시간

        Board board = Board.builder().id(saveDTO.getBoardId()).build();
        User user = User.builder().id(id).build();
        Reply reply = Reply.builder()
                .comment(saveDTO.getComment())
                .board(board)
                .user(user)
                .build();
        replyRepository.save(reply); // 영속화
    }

    @Transactional
    public void 댓글삭제(Integer id, Integer sessionUserId) {
        // 반드시 권한체크
        Optional<Reply> replyOP = replyRepository.findById(id);

        // 아닌 것들은 필터링하듯이 쫓아내지, if else를 할 게 아니라 if로 처리

        if (replyOP.isEmpty()) {
            throw new MyApiException("삭제할 댓글이 없습니다");
        }

        Reply reply = replyOP.get();
        if (reply.getUser().getId() != sessionUserId) {
            throw new MyApiException("해당 댓글을 삭제할 권한이 없습니다");
        }

        replyRepository.deleteById(id);
    }

}
