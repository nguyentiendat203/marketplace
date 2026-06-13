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

    public ApiResponse() {
       
    }

   
}
