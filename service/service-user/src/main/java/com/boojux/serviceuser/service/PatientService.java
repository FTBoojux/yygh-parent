package com.boojux.serviceuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yygh.model.user.Patient;

import java.util.List;

public interface PatientService extends IService<Patient> {
    List<Patient> findAllUserId(Long userId);

    Patient getPatientById(Long id);
}
