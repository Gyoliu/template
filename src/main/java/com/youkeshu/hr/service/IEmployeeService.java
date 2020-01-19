package com.youkeshu.hr.service;

import com.youkeshu.hr.dto.EmployeeDto;
import com.youkeshu.hr.dto.Result;

import java.util.List;

/**
 * @Author: liuxing
 * @Date: 2020/1/16 18:40
 * @Description:
 */
public interface IEmployeeService {

    public Result list( EmployeeDto employeeDto);

    public Result update( EmployeeDto employeeDto);

    public Result insert( EmployeeDto employeeDto);

    Result insertBatch(List<EmployeeDto> employeeDto);

    EmployeeDto findOne(EmployeeDto param);
}