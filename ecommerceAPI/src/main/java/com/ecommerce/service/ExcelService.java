package com.ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ExcelService {
    void readExcelFileAndSaveToDatabase(MultipartFile multipartFile) throws IOException;
}
