package com.youkeshu.hr.repository;

import com.youkeshu.hr.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: liuxing
 * @Date: 2020/1/16 15:53
 * @Description:
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer > {

}