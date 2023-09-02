package it.craftopoly.co_tickets.utils;

public class Response
{
    private Object param;
    private Integer code;
    private String date;
    private boolean success;

    public Response(Object param, Integer code, String date, boolean success)
    {
        this.param = param;
        this.code = code;
        this.date = date;
        this.success = success;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}

