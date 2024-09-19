package com.ecommerce.service.Impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface TutorialExcelService {

    void save(MultipartFile file) throws IOException;
}
