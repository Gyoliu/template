package com.youkeshu.hr.service.impl;

import com.youkeshu.hr.config.SessionInterceptor;
import com.youkeshu.hr.dto.EmployeeDto;
import com.youkeshu.hr.dto.ResCode;
import com.youkeshu.hr.dto.Result;
import com.youkeshu.hr.entity.EmployeeEntity;
import com.youkeshu.hr.entity.UserEntity;
import com.youkeshu.hr.repository.EmployeeRepository;
import com.youkeshu.hr.service.IEmployeeService;
import com.youkeshu.hr.util.WebUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.datetime.joda.DateTimeParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: liuxing
 * @Date: 2020/1/16 18:41
 * @Description:
 */
@Service
public class EmployeeServiceImpl implements IEmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Result list(EmployeeDto employeeDto) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        if(!StringUtils.isEmpty(employeeDto.getSortField()) && !StringUtils.isEmpty(employeeDto.getSortOrder())){
            if("asc".equalsIgnoreCase(employeeDto.getSortOrder())){
                sort = Sort.by(Sort.Order.asc(employeeDto.getSortField()));
            } else {
                sort = Sort.by(Sort.Order.desc(employeeDto.getSortField()));
            }
        }
        Specification specification = new Specification<EmployeeEntity>() {
            @Override
            public Predicate toPredicate(Root<EmployeeEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicate = new ArrayList<>();
                predicate.add(criteriaBuilder.equal(root.get("status"), 1));
                // 姓名
                if(!StringUtils.isEmpty(employeeDto.getName())){
                    predicate.add(criteriaBuilder.like(root.get("name"), "%" + employeeDto.getName() + "%"));
                }
                // 入职时间
                if(!StringUtils.isEmpty(employeeDto.getJoinDateStart()) && !StringUtils.isEmpty(employeeDto.getJoinDateEnd())){
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        predicate.add(criteriaBuilder.between(root.get("joiningTime")
                                , format.parse(employeeDto.getJoinDateStart()), format.parse(employeeDto.getJoinDateEnd())));
                    } catch (ParseException e) {

                    }
                }
                // 岗位
                if(!StringUtils.isEmpty(employeeDto.getPost())){
                    predicate.add(criteriaBuilder.like(root.get("post"), "%" + employeeDto.getPost() + "%"));
                }

                if(!StringUtils.isEmpty(employeeDto.getType())){
                    predicate.add(criteriaBuilder.like(root.get("type"), "%" + employeeDto.getType() + "%"));
                }
                if(!StringUtils.isEmpty(employeeDto.getIdcard())){
                    predicate.add(criteriaBuilder.equal(root.get("idcard"), employeeDto.getIdcard()));
                }

                Predicate[] pre = new Predicate[predicate.size()];
                return criteriaQuery.where(predicate.toArray(pre)).getRestriction();
            }
        };
        /*EmployeeEntity employeeEntity = new EmployeeEntity();
        BeanUtils.copyProperties(employeeDto, employeeEntity);
        employeeEntity.setStatus(1);*/
        if(employeeDto.getPage() < 0){
            List all = employeeRepository.findAll(specification);
            return new Result(all);
        }
        PageRequest of = PageRequest.of((employeeDto.getPage() - 1), employeeDto.getLimit(), sort);
        Page<EmployeeEntity> all = employeeRepository.findAll(specification, of);
        return new Result(all);
    }

    @Override
    public EmployeeDto findOne(EmployeeDto param) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        BeanUtils.copyProperties(param, employeeEntity);
        employeeEntity.setStatus(1);
        List<EmployeeEntity> all = employeeRepository.findAll(Example.of(employeeEntity));
        if(!CollectionUtils.isEmpty(all)){
            BeanUtils.copyProperties(all.get(0), param);
            return param;
        } else {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(EmployeeDto employeeDto) {
        UserEntity sessionUser = WebUtils.getSessionUser();
        EmployeeEntity employeeEntity = new EmployeeEntity();
        BeanUtils.copyProperties(employeeDto, employeeEntity);
        employeeEntity.setModifyDate(new Date());
        employeeEntity.setEditor(sessionUser.getUsername());
        EmployeeEntity save = employeeRepository.save(employeeEntity);
        return new Result(save);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insert(EmployeeDto employeeDto) {
        UserEntity sessionUser = WebUtils.getSessionUser();

        EmployeeEntity employeeEntity = new EmployeeEntity();
        BeanUtils.copyProperties(employeeDto, employeeEntity);
        employeeEntity.setCreateDate(new Date());
        employeeEntity.setCreator(sessionUser.getUsername());
        employeeEntity.setModifyDate(new Date());
        employeeEntity.setEditor(sessionUser.getUsername());
        employeeEntity.setStatus(1);
        EmployeeEntity save = employeeRepository.save(employeeEntity);
        return new Result(save);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertBatch(List<EmployeeDto> employeeDto) {
        if(CollectionUtils.isEmpty(employeeDto)){
            return new Result(ResCode.SUCCESS);
        }
        UserEntity sessionUser = WebUtils.getSessionUser();
        List<EmployeeEntity> collect = employeeDto.stream().map(x -> {
            EmployeeEntity employeeEntity = new EmployeeEntity();
            BeanUtils.copyProperties(x, employeeEntity);
            employeeEntity.setCreateDate(new Date());
            employeeEntity.setCreator(sessionUser.getUsername());
            employeeEntity.setModifyDate(new Date());
            employeeEntity.setEditor(sessionUser.getUsername());
            employeeEntity.setStatus(1);
            return employeeEntity;
        }).collect(Collectors.toList());
        List<EmployeeEntity> employeeEntities = employeeRepository.saveAll(collect);
        return new Result(ResCode.SUCCESS);
    }
}