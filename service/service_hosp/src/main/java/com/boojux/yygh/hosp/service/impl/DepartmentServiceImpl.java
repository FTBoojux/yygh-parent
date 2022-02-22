package com.boojux.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.boojux.yygh.hosp.repository.DepartmentRepository;
import com.boojux.yygh.hosp.service.DepartmentService;
import com.example.yygh.model.hosp.Department;
import com.example.yygh.vo.hosp.DepartmentQueryVo;
import com.example.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
//        map转换为department对象
        String paramMapString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(paramMapString, Department.class);

        Department departmentExist = departmentRepository
                .getDepartmentByHoscodeAndDepcode(department.getHoscode(),department.getDepcode());
//        有则修改，无则添加
        if(null != departmentExist){
            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);
            departmentRepository.save(departmentExist);
        }else{
            department.setUpdateTime(new Date());
            department.setCreateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        Pageable pageable = PageRequest.of(page - 1, limit);
//        创建Example对象
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);
        ExampleMatcher matcher =  ExampleMatcher
                .matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase(true);
        Example<Department> example = Example.of(department, matcher);
        Page<Department> all = departmentRepository.findAll(example,pageable);
        return all;
    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(null != department){
            departmentRepository.deleteById(department.getId());
        }

    }

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {

        List<DepartmentVo> result = new ArrayList<>();
//        根据医院编号查询可科室信息
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        Example example = Example.of(departmentQuery);
        List<Department> departments = departmentRepository.findAll(example);
        Map<String, List<Department>> deptCollect = departments.stream().collect(Collectors.groupingBy(Department::getBigcode));
        for(Map.Entry<String,List<Department>> entry : deptCollect.entrySet()){
            String bigcode = entry.getKey();
            List<Department> departmentList = entry.getValue();
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(bigcode);
            departmentVo.setDepname(departmentList.get(0).getBigname());

//            封装下属子科室
            List<DepartmentVo> children = new ArrayList<>();
            for (Department department : departmentList) {
                DepartmentVo newDepartmentVo = new DepartmentVo();
                newDepartmentVo.setDepname(department.getDepname());
                newDepartmentVo.setDepcode(department.getDepcode());
                children.add(newDepartmentVo);
            }
            departmentVo.setChildren(children);
            result.add(departmentVo);

        }
        return result;
//        //创建list集合，用于最终数据的封装
//        List<DepartmentVo> result = new ArrayList<>();
//
//        //根据医院编号hoscode查询医院所有科室信息
//        Department departmentQuery = new Department();
//        departmentQuery.setHoscode(hoscode);
//        Example example = Example.of(departmentQuery);
//        //所有科室列表
//        List<Department> departmentList = departmentRepository.findAll(example);
//
//        //根据大科室编号 bigcode分组，获取每个大科室里面下级子科室
//        Map<String, List<Department>> departmentMap =
//                departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
//
//        //遍历map集合 departmentMap
//        for(Map.Entry<String, List<Department>> entry : departmentMap.entrySet()){
//            //大科室编号
//            String bigCode = entry.getKey();
//            //大科室对应的全局数据
//            List<Department> departments = entry.getValue();
//            //封装大科室
//            DepartmentVo departmentVo1 = new DepartmentVo();
//            departmentVo1.setDepcode(bigCode);
//            departmentVo1.setDepname(departments.get(0).getBigname());
//
//            //封装小科室
//            List<DepartmentVo> children = new ArrayList<>();
//            for (Department department : departments) {
//                DepartmentVo departmentVo2 = new DepartmentVo();
//                departmentVo2.setDepcode(department.getDepcode());
//                departmentVo2.setDepname(department.getDepname());
//                //封装到list集合
//                children.add(departmentVo2);
//            }
//            //把小科室list集合放到大科室的children里面
//            departmentVo1.setChildren(children);
//            //最后放到result
//            result.add(departmentVo1);
//        }
//
//        return result;
    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(null != department) return department.getDepname();
        return null;
    }

    @Override
    public Department getDepartment(String hoscode, String depcode) {
        return departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
    }
}
