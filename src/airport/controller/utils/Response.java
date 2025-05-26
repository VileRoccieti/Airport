/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airport.controller.utils;

/**
 *
 * @author plobb
 */
public class Response {
    private String message;
    private int status;
    private Object object;

    public Response(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public Response(String message, int status, Object object) {
        this.message = message;
        this.status = status;
        this.object = object;
    }

    public static Response ok(String message) {
        return new Response(message, Status.OK);
    }

    public static Response ok(String message, Object object) {
        return new Response(message, Status.OK, object);
    }

    public static Response error(String message) {
        return new Response(message, Status.BAD_REQUEST);
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public Object getObject() {
        return object;
    }
    public boolean isSuccess() {
        return status == Status.OK;
    }
}
