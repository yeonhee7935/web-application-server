package util;

public enum Status{
    HTTP_200_OK(200, "OK"), HTTP_302_FOUND(302, "Found");
    private final int code;
    private final String message;
    Status(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
