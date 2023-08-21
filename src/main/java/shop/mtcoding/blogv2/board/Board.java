package shop.mtcoding.blogv2.board;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.dynamic.TypeResolutionStrategy.Lazy;
import shop.mtcoding.blogv2.reply.Reply;
import shop.mtcoding.blogv2.user.User;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "board_tb")
@Entity // ddl-auto가 create
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = true, length = 10000)
    private String content;

    // Json으로 user 정보는 안 주겠다라는 뜻
    @JsonIgnore
    // fetch는 프로젝션하는 방식 제공
    @ManyToOne(fetch = FetchType.LAZY) // board만 조회
    // @ManyToOne(fetch = FetchType.EAGER) Eager은 미리 조회를 다한다는 것임, board와 user 다
    private User user; // 1+N
    // LAZY를 붙이면 이 객체를 조회안한다는 뜻, 모순의 불일치가 안 생김
    // EAGER 이 값은 디폴트임, 연관애들 바로 조회해버림

    // DB는 컬렉션이 못 들어가기 때문에
    // ManyToOne => Eager 전략 (디폴트)
    // OneToMany => lazy 전략 (디폴트)
    @JsonIgnoreProperties({ "board" }) // 내부를 얘기하는 것임 = 양방향매핑 때 무한루프를 막기 위해서 달아야함
    // (메시지컨버터 발동하면 모든 애들한테 get을 다 때림)
    // 그 객체 안에 있는 필드를 json으로 직렬화하지말라는 뜻
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY) // (mappedBy = "board") fk가 아니라는 설정을 해줘야 함
    private List<Reply> replies = new ArrayList<>();

    @CreationTimestamp
    // insert될 때 시간을 넣어줌
    private Timestamp createdAt;

    @Builder
    public Board(Integer id, String title, String content, User user, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.createdAt = createdAt;
    }

}