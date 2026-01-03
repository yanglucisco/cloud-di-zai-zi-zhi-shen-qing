package org.ziranziyuanting.authcenter.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户实体
 *
 * @author xuyuxiang
 * @date 2022/4/21 16:13
 **/
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUser extends CommonEntity {
    /** 头像 */
    private String avatar;

    /** 签名 */
    private String signature;

    /** 账号 */
    private String account;

    /** 密码 */
    private String password;

    /** 姓名 */
    private String name;

    /** 昵称 */
    private String nickname;

    /** 性别 */
    private String gender;

    /** 年龄 */
    private String age;

    /** 出生日期 */
    private String birthday;

    /** 民族 */
    private String nation;

    /** 籍贯 */
    private String nativePlace;

    /** 家庭住址 */
    private String homeAddress;

    /** 通信地址 */
    private String mailingAddress;

    /** 证件类型 */
    private String idCardType;

    /** 证件号码 */
    private String idCardNumber;

    /** 文化程度 */
    private String cultureLevel;

    /** 政治面貌 */
    private String politicalOutlook;

    /** 毕业院校 */
    private String college;

    /** 学历 */
    private String education;

    /** 学制 */
    private String eduLength;

    /** 学位 */
    private String degree;

    /** 手机 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 家庭电话 */
    private String homeTel;

    /** 办公电话 */
    private String officeTel;

    /** 紧急联系人 */
    private String emergencyContact;

    /** 紧急联系人电话 */
    private String emergencyPhone;

    /** 紧急联系人地址 */
    private String emergencyAddress;

    /** 员工编号 */
    private String empNo;

    /** 入职日期 */
    private String entryDate;

    /** 组织id */
    private Long orgId;

    /** 职位id */
    private Long positionId;

    /** 职级 */
    private String positionLevel;

    /** 主管id */
    private Long directorId;

    /** 兼任信息 */
    private String positionJson;

    /** 上次登录ip */
    private String lastLoginIp;

    /** 上次登录地点 */
    private String lastLoginAddress;

    /** 上次登录时间 */
    private Date lastLoginTime;

    /** 上次登录设备 */
    private String lastLoginDevice;

    /** 最新登录ip */
    private String latestLoginIp;

    /** 最新登录地点 */
    private String latestLoginAddress;

    /** 最新登录时间 */
    private Date latestLoginTime;

    /** 最新登录设备 */
    private String latestLoginDevice;

    /** 用户状态 */
    private String userStatus;

    /** 排序码 */
    private Integer sortCode;

    /** 扩展信息 */
    private String extJson;
}

