package com.ecommerce.controller;

import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.entity.Tutorial;
import com.ecommerce.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadExcelFileToDatabase(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        System.out.println("multipartFile = " );
        excelService.readExcelFileAndSaveToDatabase(multipartFile);
        return new  ResponseEntity < > (new ApiResponse(HttpStatus.OK, "Upload Successfully"),HttpStatus.OK);

    }
}
