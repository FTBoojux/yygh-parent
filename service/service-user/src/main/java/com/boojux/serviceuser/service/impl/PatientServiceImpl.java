package com.boojux.serviceuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boojux.serviceuser.mapper.PatientMapper;
import com.boojux.serviceuser.service.PatientService;
import com.boojux.yygh.cmn.client.DictFeignClient;
import com.example.yygh.enums.DictEnum;
import com.example.yygh.model.user.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient>
    implements PatientService {
    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public List<Patient> findAllUserId(Long userId) {
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<Patient> patientList = baseMapper.selectList(wrapper);
        patientList.stream().forEach(item->{
            this.packPatient(item);
        });
        return patientList;
    }

    @Override
    public Patient getPatientById(Long id) {
        return this.packPatient(baseMapper.selectById(id));
    }

    private Patient packPatient(Patient patient) {
        String certificatesTypeString = dictFeignClient
                .getName(DictEnum.CERTIFICATES_TYPE.getDictCode(),patient.getCertificatesType());
        String contactsCertificatesTypeString  = dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getContactsCertificatesType());        String provinceString = dictFeignClient.getName(patient.getProvinceCode());
        String cityString = dictFeignClient.getName(patient.getCityCode());
        String districtString = dictFeignClient.getName(patient.getDistrictCode());

        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());

        return patient;
    }
}
