package shop.mtcoding.blogv2._core.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import shop.mtcoding.blogv2._core.filter.Myfilter1;

// 동작을 안할 때는 => 스프링의 버전이 방식이 바꼈을 때

@Configuration
public class FilterConfig {
    // 컴포넌트스캔이 되면 내부를 리플렉션이 됨
    // @Bean 이 어노테이션을 붙이면 클래스가 아니라 메서드의 리턴값을 IOC컨테이너에 띄움
    @Bean
    // 아까 만든 그 필터를 생성자에 등록 - order가 0 이면 제일 먼저 실행 - 우선순위가 높음
    // 저 myfilter1이 DS 전에 등록이 되어있음
    public FilterRegistrationBean<Myfilter1> myFilter1() {
        FilterRegistrationBean<Myfilter1> bean = new FilterRegistrationBean<>(new Myfilter1());
        bean.addUrlPatterns("/*"); // 슬래시로 시작하는 모든 주소에 발동됨 - 항상 발동한다는 것임
        bean.setOrder(0); // 낮은 번호부터 실행됨
        return bean;
        // return에 있는 이 bean 값 자체가 필터에 등록이 됨
    }

}
