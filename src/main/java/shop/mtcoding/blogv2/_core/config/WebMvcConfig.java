package shop.mtcoding.blogv2._core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

// 톰캣의 web.xml 설정파일 ㅡ> 자바 파일로 바껴서 설정됨
// 너는 xml에 작성해, 내가 자바 파일로 바꿔줄게라는 뜻
// pojo (Plain Old Java Object) - 설정할 때 xml좀 그만 써

// .확장자가 적혀있으면 static 폴더로 찾아가라고 했기 때문에 
// 문지기한테 /images라고 나오면 static 폴더가 아닌 images 폴더로 가라는 걸 적용시켜줘야 함
// 설정하는 방법을 제시해줌. 하면 web.xml을 재정의 할 수 있음
// 즉, 문지기가 보는 문서 체인지( 기존 것은 그대로 놔두고 추가하는 것임 )
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        // 기존에 하던 일임 ( 절대 지우면 안됨!!!)
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + "./images/")
                .setCachePeriod(10) // 10초
                // 캐시 기간 한시간
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
        // ** -> 파일명은 뭐가 들어오든 상관없다라는 뜻
    }

}
