package com.ecommerce.controller;

import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.entity.TutorialEntity;
import com.ecommerce.service.Impl.TutorialExcelService;
import com.ecommerce.service.Impl.TutorialExcelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/excel")
public class TutorialExcelController {


    private final TutorialExcelServiceImpl excelService;

    public TutorialExcelController(TutorialExcelServiceImpl excelService) {
        this.excelService = excelService;
    }


    @PostMapping("/upload")
    public ResponseEntity<ApiResponse>uploadFile(@RequestParam("file")MultipartFile multipartFile) {
        System.out.println("multipartFile = " );
        try {
            excelService.save(multipartFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Upload Successfully"));

    }
}
