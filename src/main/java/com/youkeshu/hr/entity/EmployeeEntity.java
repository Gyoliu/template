package com.youkeshu.hr.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: liuxing
 * @Date: 2020/1/16 17:13
 * @Description:
 */
@Data
@Entity
@Table(name = "hr_employee")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "status")
    private Integer status;

    @Column(name = "salary")
    private Integer salary;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "creator")
    private String creator;

    @Column(name = "modify_date")
    private Date modifyDate;

    @Column(name = "editor")
    private String editor;

    @Column(name = "employee_number")
    private String employeeNumber;

    /**
     * 员工类型
     * 1 正式员工
     * 2 离职
     * 3 试用
     * 4 实习
     */
    @Column(name = "type")
    private String type;

    /**
     * 姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 入职时间
     */
    @Column(name = "joining_time")
    private Date joiningTime;

    /**
     * 性别
     */
    @Column(name = "sex")
    private String sex;

    /**
     * 岗位
     */
    @Column(name = "post")
    private String post;

    /**
     * 职务类型
     */
    @Column(name = "job_type")
    private String jobType;

    /**
     * 归属
     */
    @Column(name = "belong_to")
    private String belongTo;

    /**
     * 一级机构
     */
    @Column(name = "level_org1")
    private String levelOrg1;

    /**
     * 2级机构
     */
    @Column(name = "level_org2")
    private String levelOrg2;

    /**
     * 3级机构
     */
    @Column(name = "level_org3")
    private String levelOrg3;

    @Column(name = "level_org4")
    private String levelOrg4;


    /**
     * 电话
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * 紧急联系电话
     */
    @Column(name = "urgent_phone_number")
    private String urgentPhoneNumber;

    /**
     * 学历
     */
    @Column(name = "education")
    private String education;

    /**
     * 毕业学校
     */
    @Column(name = "school")
    private String school;

    /**
     * 所读专业
     */
    @Column(name = "specialized_subject")
    private String specializedSubject;

    /**
     * 毕业时间
     */
    @Column(name = "graduation")
    private String graduation;

    /**
     * 学校类型
     */
    @Column(name = "school_type")
    private String schoolType;

    /**
     * 政治面貌
     */
    @Column(name = "political_status")
    private String politicalStatus;

    /**
     * 婚姻状态
     */
    @Column(name = "marital_status")
    private String maritalStatus;

    /**
     * 生育情况
     */
    @Column(name = "fertility_status")
    private String fertilityStatus;

    /**
     * 身份证
     */
    @Column(name = "idcard")
    private String idcard;

    /**
     * 出生日期
     */
    @Column(name = "birth")
    private Date birth;

    /**
     * 年龄
     */
    @Column(name = "age")
    private Integer age;

    /**
     * 民族
     */
    @Column(name = "nation")
    private String nation;

    /**
     * 籍贯
     */
    @Column(name = "native_place")
    private String nativePlace;

    /**
     * 户籍类型
     */
    @Column(name = "domicile_type")
    private String domicileType;

    /**
     * 家庭住址
     */
    @Column(name = "address")
    private String address;

    /**
     * 证件有效期
     */
    @Column(name = "expiry_start_date")
    private Date expiryStartDate;

    /**
     * 证件有效期
     */
    @Column(name = "expiry_end_date")
    private Date expiryEndDate;

    /**
     * 发证机构
     */
    @Column(name = "certification_authority")
    private String certificationAuthority;

    /**
     * 试用期
     */
    @Column(name = "probation")
    private String probation;

    /**
     * 转正日期
     */
    @Column(name = "become_regular_worker_date")
    private Date becomeRegularWorkerDate;

    /**
     * 离职日期
     */
    @Column(name = "leave_office_date")
    private Date leaveOfficeDate;

    /**
     * 签订合同日期
     */
    @Column(name = "sign_contract_start1")
    private Date signContractStart1;

    @Column(name = "sign_contract_end1")
    private Date signContractEnd1;

    @Column(name = "sign_contract_start2")
    private Date signContractStart2;

    @Column(name = "sign_contract_end2")
    private Date signContractEnd2;

    @Column(name = "sign_contract_start3")
    private Date signContractStart3;

    @Column(name = "sign_contract_end3")
    private Date signContractEnd3;

    /**
     * 合同公司
     */
    @Column(name = "contract_signing_company")
    private String contractSigningCompany;

    /**
     * 招聘渠道
     */
    @Column(name = "recruit_channel")
    private String recruitChannel;

    /**
     * 一面
     */
    @Column(name = "interview1")
    private String interview1;

    /**
     * 一面
     */
    @Column(name = "interview2")
    private String interview2;

    /**
     * 一面
     */
    @Column(name = "interview3")
    private String interview3;

    /**
     * 导师
     */
    @Column(name = "teacher")
    private String teacher;

    /**
     * 地点
     */
    @Column(name = "location")
    private String location;

    /**
     * 曾经职务
     */
    @Column(name = "old_job")
    private String oldJob;

    /**
     * 人事关系归属
     */
    @Column(name = "ascription")
    private String ascription;




}