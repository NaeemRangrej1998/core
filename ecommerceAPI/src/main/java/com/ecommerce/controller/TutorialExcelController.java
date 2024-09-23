package com.ecommerce.controller;

import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.entity.Tutorial;
import com.ecommerce.service.TutorialExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
public class TutorialExcelController {

    private final TutorialExcelService tutorialExcelService;



    @PostMapping("/upload")
    public ResponseEntity<ApiResponse>uploadFile(@RequestParam("file")MultipartFile multipartFile) throws IOException {
        System.out.println("multipartFile = " );
        List<Tutorial> tutorialList=tutorialExcelService.save(multipartFile);
        return new  ResponseEntity < > (new ApiResponse(HttpStatus.OK, "Upload Successfully",tutorialList),HttpStatus.OK);

    }

//    @PostMapping("/upload")
//    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
//        String message = "";
//
//        if (ExcelHelper.hasExcelFormat(file)) {
//            try {
//                excelService.save(file);
//
//                message = "Uploaded the file successfully: " + file.getOriginalFilename();
//                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
//            } catch (Exception e) {
//                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
//                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
//            }
//        }

//        message = "Please upload an excel file!";
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

