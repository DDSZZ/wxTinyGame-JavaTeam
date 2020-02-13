package top.phosky.mask.dto;

public class StateDTO {
    //枚举开始
    public final static StateDTO LOGIN_SUCCESS_OLD = new StateDTO(1, "登录成功且为老用户");
    public final static StateDTO LOGIN_SUCCESS_NEW = new StateDTO(2, "登录成功且为新用户");
    public final static StateDTO LOGIN_ERROR_PARAM_FORMAT = new StateDTO(2, "传入参数异常");

    //枚举结束
    private int code;
    private String content;

    public StateDTO(int code, String content) {
        this.code = code;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
