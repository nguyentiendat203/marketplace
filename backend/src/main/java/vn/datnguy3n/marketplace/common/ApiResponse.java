package vn.datnguy3n.marketplace.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {

    private int statusCode;
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok(String message,int statusCode, T data) {
        ApiResponse<T> res = new ApiResponse<>();
        res.statusCode = statusCode;
        res.success = true;
        res.message = message;
        res.data = data;
        return res;
    }

    public static <T> ApiResponse<T> fail(String message, int statusCode) {
        ApiResponse<T> res = new ApiResponse<>();
        res.statusCode = statusCode;
        res.success = false;
        res.message = message;
        res.data = null;
        return res;
    }
}
