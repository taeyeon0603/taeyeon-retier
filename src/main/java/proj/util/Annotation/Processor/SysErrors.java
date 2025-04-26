package proj.util.Annotation.Processor;

public enum SysErrors {
    userError(1000, "用户方面的错误"), inputError(1001, "无效输入"), otherUserError(1999, "其他用户方面的错误"),
    appError(2000, "应用系统方面的错误"), safeError(3000, "安全检查类方面的错误"), IPLimited(3001, "IP受限"),
    timeLimited(3002, "时间受限"), optTimeLimited(3003, "操作次数受限"), authError(3004, "用户名与密码不匹配"),
    certCancelled(3005, "数字证书被注销"), userForzen(3006, "用户账号被冻结"), noOptPermission(3007, "无操作权限"),
    otherSafeError(3999, "其他安全检查方面的错误");

    private Integer code;
    private String msg;

    SysErrors(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
