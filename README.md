# 스프링부트 블로그 V2

## 기획

## 화면 설계 ( 이런 화면에는 이런 데이터가 필요하지 않을까? )

## 화면 코드 ( 프론트 코트 )

## 테이블 설계

## 1단계 기능

- ( 특징 : 자바스크립트, 예외처리)
- 회원가입 (A)
- 로그인 (A)
- 회원 정보 보기 (A)
- 회원 정보 수정 (A)
- 게시글 작성하기 (B)
- 게시글 목록 보기 (B)
- 게시글 상세보기 (C)
- 게시글 삭제하기 (C)
- 게시글 수정하기 (C)
- 댓글 작성하기 (D)
- 댓글 삭제하기 (D)

## 2단계 기능

- 유저네임 중복체크 (AJAX)
- 페이징하기
- 검색하기
- 게시글 작성하기할 때 썸네일 등록
- 게시글 목록 썸네일 보기

## 3단계 기능

- 필터 ( Filter )
- 유효성 검사 ( AOP )
- 인증 검사 ( Interceptor )
- 이메일 보내기 ( 댓글 작성이 되면 게시글 주인에게 )

## 테이블 쿼리

```sql
create database blogdb;
use blogdb;

create table user_tb (
    id integer auto_increment,
    created_at timestamp,
    email varchar(20) not null,
    password varchar(60) not null,
    username varchar(20) not null unique,
    primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

create table board_tb (
    id integer auto_increment,
    content varchar(10000),
    created_at timestamp,
    title varchar(100) not null,
    user_id integer,
    primary key (id),
    constraint fk_board_user_id foreign key (user_id) references user_tb (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

create table reply_tb (
    id integer auto_increment,
    comment varchar(100) not null,
    created_at timestamp,
    board_id integer,
    user_id integer,
    primary key (id),
    constraint fk_reply_board_id foreign key (board_id) references board_tb (id),
    constraint fk_reply_user_id foreign key (user_id) references user_tb (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
```
