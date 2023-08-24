package shop.mtcoding.blogv2._core.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import shop.mtcoding.blogv2._core.error.ex.MyException;
import shop.mtcoding.blogv2._core.vo.MyPath;
import shop.mtcoding.blogv2.user.UserRequest;

public class FileWrite {

    public static String save(MultipartFile pic, String originalFilename) {

        UUID uuid = UUID.randomUUID(); // 랜덤한 해시값을 만들어줌
        // 네트워크상에서 고유성을 보장하는 ID를 만들기 위한 표준 규약
        // 즉, 전 세계 유일한 식별자
        String fileName = uuid + "_" + originalFilename;
        // 확장자때문에 joinDTO.getPic().getOriginalFilename(); 이게 뒤로 와야함
        System.out.println("fileName : " + fileName);
        // fileName : 140c6ed0-76e6-4b96-9729-e430d4025112_cat02.jpg
        // cat02.jpg
        // 94644
        // image/jpeg
        // ori : cat02.jpg
        // 프로젝트 실행 파일 변경 -> blogv2-1.0.jar 자바 실행파일
        // 해당 실행파일 경로에 images 폴더가 필요함
        Path filePath = Paths.get(MyPath.IMG_PATH + fileName);
        // ./ 내 서버의 현재 위치 (springboot-blog-v5폴더 내부 ) - 상대경로

        try {
            Files.write(filePath, pic.getBytes());
            // 바이트 정보를 가져온다. 파일 자체를 만든다 - 경로에 파일 저장
            // 해당 경로에 파일을 생성
            // 이미지는 바이트 배열로 바꿔서 해야하는데, 바꿔주는 게 getByte임
            // 데이터 직렬화해주는 것임
        } catch (Exception e) {
            // 경로오류,용량부족 등
            throw new MyException(e.getMessage());
        }
        return fileName;
    }

}
