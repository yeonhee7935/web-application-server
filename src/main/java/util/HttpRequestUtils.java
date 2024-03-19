package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public class HttpRequestUtils {
    public static HttpRequest parseRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        // 1. request line
        String line = br.readLine();
        String[] tokens = HttpRequestUtils.parseRequestLine(line);
        String method = tokens[0];
        String uri = tokens[1];
        int index = uri.indexOf("?");
        String url = uri.contains("?") ? uri.substring(0, index) : uri;
        Map<String, String> queryString = parseQueryString(uri.substring(index + 1));

        // 2. headers
        Map<String, String> headers = new HashMap<>();
        Map<String, String> cookies = new HashMap<>();
        while (!line.isEmpty()) {
            line = br.readLine();
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            if (pair != null) {
                headers.put(pair.getKey(), pair.getValue());
                if (pair.getKey().equals("Cookie")) cookies = parseCookies(pair.getValue());

            }
        }
        int contentLength = headers.containsKey("Content-Length") ? Integer.parseInt(headers.get("Content-Length")) : 0;

        // 3. body
        String body = IOUtils.readData(br, contentLength);
        HttpRequest request = new HttpRequest();
        request.setMethod(method);
        request.setUri(uri);
        request.setUrl(url);
        request.setQueryString(queryString);
        request.setHeaders(headers);
        request.setCookies(cookies);
        request.setBody(body);
        return request;
    }

    private static String[] parseRequestLine(String requestLine) {
        if (requestLine == null || requestLine.isEmpty()) throw new RuntimeException("first line should not empty!");
        String[] tokens = requestLine.split(" ");
        if (tokens.length != 3) throw new RuntimeException("first line should be like (method url version)!");
        return tokens;

    }

    /**
     * @param queryString URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param cookies 값은 name1=value1; name2=value2 형식임
     * @return
     */
    private static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    private static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0], tokens[1]);
    }

    private static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }

    private static class Pair {
        String key;
        String value;

        Pair(String key, String value) {
            this.key = key.trim();
            this.value = value.trim();
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pair other = (Pair) obj;
            if (key == null) {
                if (other.key != null)
                    return false;
            } else if (!key.equals(other.key))
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
