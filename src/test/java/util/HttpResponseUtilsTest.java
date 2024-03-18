package util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;



public class HttpResponseUtilsTest {
    @Test
    public void sendResponseTest() throws IOException {
        // given
        HttpStatus status = HttpStatus.HTTP_200_OK;
        Map<String,String> headers = new HashMap<>();
        headers.put("Set-Cookie", "logined="+true);
        byte[] body = "로그인 성공".getBytes();
        HttpResponse response = new HttpResponse(status, headers, body);
        //when
        DataOutputStream dos = new DataOutputStream(Files.newOutputStream(new File("./src/test/resources/test_response.txt").toPath()));
        HttpResponseUtils.sendResponse(dos, response);

        // then
        // 수동 확인
    }
}
