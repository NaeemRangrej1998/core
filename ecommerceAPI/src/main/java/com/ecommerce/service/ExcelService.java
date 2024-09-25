package com.ecommerce.service;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ExcelService {
    void readExcelFileAndSaveToDatabase(MultipartFile multipartFile, GetTokenClaimsDTO claimsDTO) throws IOException;
}
