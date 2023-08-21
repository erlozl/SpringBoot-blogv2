package shop.mtcoding.blogv2.board;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blogv2.board.BoardRequest.UpdateDTO;
import shop.mtcoding.blogv2.user.User;

// 1. 비지니스 로직 처리 (핵심로직)
// 2. 트랙잭션 관리
// 3. 예외 처리
// 4. DTO 변환 

// 테이블, 릴레이션, 엔티티(테이블이랑 똑같이 생긴 자바객체)

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    public void 글쓰기(BoardRequest.SaveDTO saveDTO, int sessionUserId) {

        Board board = Board.builder()
                .title(saveDTO.getTitle())
                .content(saveDTO.getContent())
                .user(User.builder().id(sessionUserId).build())
                .build();
        boardRepository.save(board);
    }

    public Page<Board> 게시글목록보기(Integer page) {
        Pageable pageable = PageRequest.of(page, 3, Sort.Direction.DESC, "id");
        return boardRepository.findAll(pageable);
    }

    public Board 상세보기(Integer id) {
        // // board만 가져오면 됨
        // Optional<Board> boardOP = boardRepository.findById(id);
        // if (boardOP.isPresent()) {
        // return boardOP.get();
        // } else {
        // throw new RuntimeException(id + "는 찾을 수 없습니다");
        // }
        Optional<Board> boardOP = boardRepository.mFindByIdJoinRepliesInUser(id);
        if (boardOP.isPresent()) {
            return boardOP.get();
        } else {
            throw new RuntimeException(id + "는 찾을 수 없습니다");
        }
    }

    public Board 게시글수정목록(Integer id) {
        Board board = boardRepository.findById(id).get();
        return board;
    }

    @Transactional
    public void 게시글수정(BoardRequest.UpdateDTO updateDTO, Integer id) {
        Optional<Board> boardOP = boardRepository.findById(id);
        if (boardOP.isPresent()) {
            Board board = boardOP.get();
            board.setTitle(updateDTO.getTitle());
            board.setContent(updateDTO.getContent());
        }
    } // flush(더티체킹)

    @Transactional
    public void 삭제(Integer id) {
        try {
            boardRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("6번은 없음");
            // throwable ㅡ> exception ㅡ> RuntimeException
        }
    }
}