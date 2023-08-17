package shop.mtcoding.blogv2;

class MyController {
    MyService myService = new MyService();

    void home(boolean check) throws Exception {
        myService.홈가기(check);
    }
}

class MyService {
    MyRepository myRepository = new MyRepository();

    void 홈가기(boolean check) throws Exception {
        myRepository.findHome(check);

    }
}

class MyRepository {
    void findHome(boolean check) throws Exception {
        if (check) {
            System.out.println("조회 완료");
        } else {
            throw new Exception("조회 오류");
            // throw RuntimeException = 강제로 터트릴 수 있음
        }
    }
}

public class HelloTest {
    public static void main(String[] args) {
        MyController myController = new MyController();
        try {
            myController.home(false);
        } catch (Exception e) {
            System.out.println("괜찮아");
        }
    }
    // if가 아니라 왜 예외처리로 해야되는가?
}