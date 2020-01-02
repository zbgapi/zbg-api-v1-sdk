package com.dd.api.entity.account.result;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * @author zhangzp
 */
@Data
public class Account {
    /**
     * 用户ID
     */
    @SerializedName("user-id")
    private String userId;

    /**
     * 登录名
     */
    @SerializedName("login-name")
    private String loginName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 账户类型，main，sub，virtual
     */
    private String type;

    /**
     * 是否进行了实名认证
     */
    private boolean certification;

    /**
     * 是否进行了手机验证
     */
    @SerializedName("mobile-auth")
    private boolean mobileAuth;

    /**
     * 是否进行了邮箱验证
     */
    @SerializedName("email-auth")
    private boolean emailAuth;

    /**
     * 是否进行了谷歌验证
     */
    @SerializedName("google-auth")
    private boolean googleAuth;

    /**
     * 用户ID
     */
    @SerializedName("safe-pwd-auth")
    private boolean safePwdAuth;

    /**
     * 子账号ID列表
     */
    private List<String> subs;
}
