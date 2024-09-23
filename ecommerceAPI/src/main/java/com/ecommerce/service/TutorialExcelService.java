package com.ecommerce.service;

import com.ecommerce.entity.Tutorial;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface TutorialExcelService {

    List<Tutorial> save(MultipartFile file) throws IOException;
}
