package com.boojux.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.boojux.yygh.cmn.client.DictFeignClient;
import com.boojux.yygh.hosp.service.HospitalService;
import com.boojux.yygh.hosp.repository.HospitalRepository;
import com.example.yygh.model.hosp.Hospital;
import com.example.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private DictFeignClient dictFeignClient;
    @Override
    public void save(Map<String, Object> paramMap) {
//        map转换为hospital
        String mapString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);
//        判断有无数据
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);
//        有则修改，无则添加
        if(null != hospitalExist){
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }else{
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }

    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }

    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalSetQueryVo) {
        Pageable of = PageRequest.of(page - 1, limit);
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                                                .withIgnoreCase(true);
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalSetQueryVo,hospital);
        Example<Hospital> example = Example.of(hospital, matcher);
        Page<Hospital> pages = hospitalRepository.findAll(example, of);
        List<Hospital> content = pages.getContent();
//        遍历封装医院等级
        content.stream().forEach(item->this.setHospitalHosType(item));
        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    @Override
    public Map<String, Object> getHospById(String id) {
        Map<String,Object> res = new HashMap<>();
        Hospital hospital = this.setHospitalHosType(hospitalRepository.findById(id).get());
        res.put("hospital",hospital);
        res.put("bookingRule",hospital.getBookingRule());
        hospital.setBookingRule(null);
        return res;
    }

    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if(null != hospital){
            return hospital.getHosname();
        }
        return "";
    }

    @Override
    public List<Hospital> findByHosname(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String,Object> res = new HashMap<>();
        Hospital hospital = this.setHospitalHosType(this.getByHoscode(hoscode));
        res.put("bookingRule",hospital.getBookingRule());
        res.put("hospital",hospital);
        hospital.setBookingRule(null);
        return res;
    }

    public Hospital setHospitalHosType(Hospital hospital) {
        String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
        String provinceName = dictFeignClient.getName(hospital.getProvinceCode());
        String cityCode = dictFeignClient.getName(hospital.getCityCode());
        String districtCode = dictFeignClient.getName(hospital.getDistrictCode());

        hospital.getParam().put("fullAddress",provinceName+cityCode+districtCode);
        hospital.getParam().put("hostypeString",hostypeString);
        return hospital;
    }

}
