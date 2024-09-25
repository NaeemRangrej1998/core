package com.ecommerce.controller;

import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.entity.Tutorial;
import com.ecommerce.service.ExcelService;
import com.ecommerce.utils.GetClaimsUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;
    private final GetClaimsUtils getClaimsUtils;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadExcelFileToDatabase(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        System.out.println("multipartFile = " );
        GetTokenClaimsDTO claimsDTO=getClaimsUtils.getClaims(request);
        excelService.readExcelFileAndSaveToDatabase(multipartFile,claimsDTO);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Excel File Upload And Saved successfully"), HttpStatus.OK);

    }
}
