package rolling.redisspringdatademo.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    private Boolean success;
    private String errorMsg;
    private Object data;

    public static Response ok() {
        return new Response(true, null, null);
    }

    public static Response ok(Object data) {
        return new Response(true, null, data);
    }

    public static Response fail(String errorMsg) {
        return new Response(false, errorMsg, null);
    }

}
