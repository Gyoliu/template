package com.youkeshu.hr.dto;

import com.youkeshu.hr.config.SpringContextHolder;
import com.youkeshu.hr.service.IEmployeeService;
import com.youkeshu.hr.util.IExcelValidate;
import com.youkeshu.hr.util.UploadField;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: liuxing
 * @Date: 2020/1/16 17:13
 * @Description:
 */
@Data
public class EmployeeDto implements IExcelValidate {

    @Override
    public String validate(Object o) {
        EmployeeDto employeeDto = (EmployeeDto) o;
        EmployeeDto param = new EmployeeDto();
        param.setIdcard(employeeDto.getIdcard());
        param.setPage(1);
        param.setLimit(10);
        IEmployeeService bean = SpringContextHolder.getBean(IEmployeeService.class);
        EmployeeDto one = bean.findOne(param);
        if(one != null){
            return "身份证已存在，请确认！\n";
        } else {
            return null;
        }
    }

    private Integer id;

    private Integer status;

    private Integer salary;

    private Date createDate;

    private String creator;

    private Date modifyDate;

    private String editor;

    @UploadField(order = 1)
    private String employeeNumber;

    /**
     * 员工类型
     * 1 正式员工
     * 2 离职
     * 3 试用
     * 4 实习
     */
    @UploadField(order = 2)
    private String type;

    /**
     * 姓名
     */
    @UploadField(order = 3)
    private String name;

    /**
     * 入职时间
     */
    @UploadField(order = 4)
    private Date joiningTime;

    /**
     * 性别
     */
    @UploadField(order = 5)
    private String sex;

    /**
     * 岗位
     */
    @UploadField(order = 6)
    private String post;

    /**
     * 职务类型
     */
    @UploadField(order = 7)
    private String jobType;

    /**
     * 归属
     */
    @UploadField(order = 8)
    private String belongTo;

    /**
     * 一级机构
     */
    @UploadField(order = 9)
    private String levelOrg1;

    /**
     * 2级机构
     */
    @UploadField(order = 10)
    private String levelOrg2;

    /**
     * 3级机构
     */
    @UploadField(order = 11)
    private String levelOrg3;

    @UploadField(order = 12)
    private String levelOrg4;


    /**
     * 电话
     */
    @UploadField(order = 13)
    private String phoneNumber;

    /**
     * 紧急联系电话
     */
    @UploadField(order = 14)
    private String urgentPhoneNumber;

    /**
     * 学历
     */
    @UploadField(order = 15)
    private String education;

    /**
     * 毕业学校
     */
    @UploadField(order = 16)
    private String school;

    /**
     * 所读专业
     */
    @UploadField(order = 17)
    private String specializedSubject;

    /**
     * 毕业时间
     */
    @UploadField(order = 18)
    private String graduation;

    /**
     * 学校类型
     */
    @UploadField(order = 19)
    private String schoolType;

    /**
     * 政治面貌
     */
    @UploadField(order = 20)
    private String politicalStatus;

    /**
     * 婚姻状态
     */
    @UploadField(order = 21)
    private String maritalStatus;

    /**
     * 生育情况
     */
    @UploadField(order = 22)
    private String fertilityStatus;

    /**
     * 身份证
     */
    @UploadField(order = 23, required = true)
    private String idcard;

    /**
     * 出生日期
     */
    @UploadField(order = 24)
    private Date birth;

    /**
     * 年龄
     */
    @UploadField(order = 25)
    private Integer age;

    /**
     * 民族
     */
    @UploadField(order = 26)
    private String nation;

    /**
     * 籍贯
     */
    @UploadField(order = 27)
    private String nativePlace;

    /**
     * 户籍类型
     */
    @UploadField(order = 28)
    private String domicileType;

    /**
     * 家庭住址
     */
    @UploadField(order = 29)
    private String address;

    /**
     * 证件有效期
     */
    @UploadField(order = 30)
    private Date expiryStartDate;

    /**
     * 证件有效期
     */
    @UploadField(order = 31)
    private Date expiryEndDate;

    /**
     * 发证机构
     */
    @UploadField(order = 32)
    private String certificationAuthority;

    /**
     * 试用期
     */
    @UploadField(order = 33)
    private String probation;

    /**
     * 转正日期
     */
    @UploadField(order = 34)
    private Date becomeRegularWorkerDate;

    /**
     * 离职日期
     */
    private Date leaveOfficeDate;

    /**
     * 签订合同日期
     */
    @UploadField(order = 35)
    private Date signContractStart1;

    @UploadField(order = 36)
    private Date signContractEnd1;

    @UploadField(order = 38)
    private Date signContractStart2;

    @UploadField(order = 39)
    private Date signContractEnd2;

    private Date signContractStart3;

    private Date signContractEnd3;

    /**
     * 合同公司
     */
    @UploadField(order = 41)
    private String contractSigningCompany;

    /**
     * 招聘渠道
     */
    @UploadField(order = 42)
    private String recruitChannel;

    /**
     * 一面
     */
    @UploadField(order = 43)
    private String interview1;

    /**
     * 一面
     */
    @UploadField(order = 44)
    private String interview2;

    /**
     * 一面
     */
    private String interview3;

    /**
     * 导师
     */
    @UploadField(order = 45)
    private String teacher;

    /**
     * 地点
     */
    @UploadField(order = 46)
    private String location;

    /**
     * 曾经职务
     */
    @UploadField(order = 47)
    private String oldJob;

    /**
     * 人事关系归属
     */
    @UploadField(order = 48)
    private String ascription;

    /**
     * 排序字段
     */
    private String sortField;
    private String sortOrder;

    private String joinDateStart;
    private String joinDateEnd;


    private int page;
    private int limit;


}