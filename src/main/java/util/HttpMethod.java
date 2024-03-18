package util;

public enum HttpMethod {
    GET("GET"), POST("POST");
    private final   String value;
    HttpMethod(String value){
        this.value = value;
    }

}
