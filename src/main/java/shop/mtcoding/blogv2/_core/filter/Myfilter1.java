package shop.mtcoding.blogv2._core.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 필터를 만드는 클래스
// 그 필터를 언제 만들지 설정
// 무조건 import는 javax.filter임!!!
public class Myfilter1 implements Filter {

    // 내가 받은 request,response를 다음 필터한테 전달함
    @Override
    // 필터의 기능
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("필터1 실행됨 ");

        // ServletRequest와 ServletResponse에는 객체가 많이 들고 있지 않음. 객체는 자식한테 많이 들고 있음
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 필터가 없다면 DS로 전달
        // doFilter가 없다면 아무것도 안 뜸. 그냥 멈춘거임 아무 응답도 안한 것

        // 필터로 만든 목적
        // log로 남길 수도 있음 - 누가누가 접속함 이런 식으로
        // 1) ip 로그 남기기
        // 2) user-agent 어떻게 접속했는지 확인
        // 3) 블랙리스트 추방, 차단
        // 4) 값을 가공하기
        // 5) 인증체크 => 주소마다 인증체크하는 게 다르기 때문에 filter에 안 해놓음

        // 클래스가 컴포넌트스캔이 안되면 Autowired가 안됨 - 그말은 DB로 못 땡긴다는 것임( DB에 기록 X)
        // 왜?
        // 왜 컴포넌트스캔이 되야 Autowired가 될까?
        // : 컴포넌트스캔할 때 기본 생성자를 new 함 어떤 값이 들어갈지 모르니까
        // 매개변수가 들어가있는 거 먼저 안 띄움
        // 리플렉션으로 분석 - 파라미터가 있는지 확인 new를 하려고 봤더니 매개변수가 있는 순간

        // 생성자 주입 Autowired

        // DI의 원리
        // 빈생성자가 있으면 바로 new임
        // 디폴트 생성자가 없으면 컴포넌트 스캔 떠있는 myfilter타입을 찾아서
        // 있으면 주입해줌 그걸 di라고 함 의존성 주입

        // 컴포넌트스캔안하고 myfilter를 찾으려고 할 때 오류나는 내용은
        // 찾지 못한다는 내용이 뜸 - ioc컨테이너에 없다는 것임

        // 필터에서는 직접 응답의 setContentType을 text/plain으로 바꿔줘야 함
        // 디스패처서블릿까지 안들어왔기 때문에 type자체를 일일히 바꿔줘야 함
        // PostMan
        // 1. IP 로그 남기기
        System.out.println("접속자 ip : " + req.getRemoteAddr());
        System.out.println("접속자 user agent : " + req.getHeader("User-Agent"));

        // 2. 블랙리스트 추방
        if (req.getHeader("User-Agent").contains("XBox")) {
            // resp.setContentType("text/html; charset=utf-8");
            resp.setHeader("Content-Type", "text/html; charset=utf-8");
            PrintWriter out = resp.getWriter();

            req.setAttribute("name", "홍길동");
            out.println("<h1>나가세요 크롬이면 : " + req.getAttribute("name") + "</h1>");
            return;
        }

        chain.doFilter(request, response); // 다음 필터로 request, response 전달
    }

}
