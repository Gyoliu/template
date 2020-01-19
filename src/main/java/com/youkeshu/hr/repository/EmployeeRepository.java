package com.youkeshu.hr.repository;

import com.youkeshu.hr.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Author: liuxing
 * @Date: 2020/1/16 18:42
 * @Description:
 */
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer >, JpaSpecificationExecutor<EmployeeEntity> {
}