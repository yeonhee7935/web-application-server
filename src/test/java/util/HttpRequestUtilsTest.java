package util;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.Files;

import org.junit.Test;


public class HttpRequestUtilsTest {
    @Test
    public void parseRequest() throws IOException {
        // given
        InputStream in = Files.newInputStream(new File("./src/test/resources/user_login.txt").toPath());
        // when
        HttpRequest request = HttpRequestUtils.parseRequest(in);
        // then
        assertEquals(request.getUrl(), "/user/login");
        assertEquals(request.getMethod(), HttpMethod.POST);
        assertEquals(request.getBody(), "userId=test&password=test");
    }
}
