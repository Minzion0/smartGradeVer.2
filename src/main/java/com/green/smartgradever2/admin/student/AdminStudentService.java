package com.green.smartgradever2.admin.student;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminStudentService {

    private final PasswordEncoder PW_ENCODER;
}
