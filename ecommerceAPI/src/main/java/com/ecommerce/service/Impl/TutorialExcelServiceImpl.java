package com.ecommerce.service.Impl;

import com.ecommerce.entity.Tutorial;
import com.ecommerce.repository.TutorialRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TutorialExcelServiceImpl implements TutorialExcelService {

    static String SHEET = "Tutorials";
    private final TutorialRepository tutorialRepository;

    public TutorialExcelServiceImpl(TutorialRepository tutorialRepository) {
        this.tutorialRepository = tutorialRepository;
    }

    public List<Tutorial> save(MultipartFile file) {
        try {
            List<Tutorial> tutorials = excelFileToTutorialEntity(file);
            System.out.println("tutorials = save " + tutorials);
            tutorialRepository.saveAll(tutorials);
            return tutorials;
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }

    }

    //    public void save(MultipartFile file) {
//        System.out.println("Received file: " + file.getOriginalFilename());
//        if (isValidExcelFile(file)) {
//            try {
//                List<Tutorial> tutorialEntities = ExcelHelper.excelToTutorials(file.getInputStream());
//                System.out.println("Saving tutorials: " + tutorialEntities.size());
//                tutorialRepository.saveAll(tutorialEntities);
//            } catch (IOException e) {
//                throw new IllegalArgumentException("The file is not a valid excel file");
//            }
//        }
//        else {
//            throw new IllegalArgumentException("Invalid Excel file.");
//
//        }
//    }
// System.out.println("Received file: excelFileToTutorialEntity "+file.getContentType() );
//        System.out.println("Received file: excelFileToTutorialEntity "+file.getOriginalFilename() );
//        System.out.println("Received file: " + file.getOriginalFilename());
//        System.out.println("File size: " + file.getSize() + " bytes");
//        System.out.println("File content type: " + file.getContentType());
    public List<Tutorial> excelFileToTutorialEntity(MultipartFile multipartFile) throws IOException {
        System.out.println("Received file: excelFileToTutorialEntity " + multipartFile.getOriginalFilename());

//        InputStream inputStream1 = new FileInputStream("/home/dev1013/Downloads/Tutorials.xlsx");
        Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        System.out.println("sheet = " + workbook.getSheetName(0));
//        Iterator<Row> rows = sheet.iterator();
//        System.out.println("sheet = " + rows);

        List<Tutorial> tutorials = new ArrayList<>();

//        for (Row row : sheet) {
//            System.out.println("row.getSheet() = " + row.getSheet());
//            Tutorial tutorial = new Tutorial();
//            if (row.getRowNum() != 0) {
//                System.out.println("tutorial = " + tutorial);
////                Cell idCell = row.getCell(0);
////                if (idCell != null && idCell.getCellType() == CellType.NUMERIC) {
////                    tutorial.setId((long) idCell.getNumericCellValue());
////                } else {
////                    System.out.println("Invalid or missing ID at row " + row.getRowNum());
////                    continue;  // Skip to the next row if ID is not valid
////                }
//                tutorial.setTitle(row.getCell(0).getStringCellValue());
//                tutorial.setDescription(row.getCell(1).getStringCellValue());
////                System.out.println(false);
//                tutorial.setPublished(row.getCell(2).getBooleanCellValue());
//                System.out.println("tutorial = " + tutorial);
//                tutorials.add(tutorial);
//            }
//
//        }
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                // Skip the header row
                continue;
            }

            Tutorial tutorial = new Tutorial();
            Cell idCell = row.getCell(0);
            if (idCell != null && idCell.getCellType() == CellType.NUMERIC) {
                tutorial.setId((long) idCell.getNumericCellValue());
            } else {
                System.out.println("Missing or invalid title at row " + row.getRowNum());
                continue;
            }
            // Read and validate Title
            Cell titleCell = row.getCell(1);
            if (titleCell != null && titleCell.getCellType() == CellType.STRING) {
                tutorial.setTitle(titleCell.getStringCellValue());
            } else {
                System.out.println("Missing or invalid title at row " + row.getRowNum());
                continue;
            }

            // Read and validate Description
            Cell descriptionCell = row.getCell(2);
            if (descriptionCell != null && descriptionCell.getCellType() == CellType.STRING) {
                tutorial.setDescription(descriptionCell.getStringCellValue());
            } else {
                System.out.println("Missing or invalid description at row " + row.getRowNum());
                continue;
            }

            // Read and validate Published (boolean)
//            Cell publishedCell = row.getCell(2);
//            if (publishedCell != null && publishedCell.getCellType() == CellType.BOOLEAN) {
//                tutorial.setPublished(publishedCell.getBooleanCellValue());
//            } else {
//                System.out.println("Missing or invalid published status at row " + row.getRowNum());
//                continue;
//            }

            System.out.println("tutorial = " + tutorial);
            tutorials.add(tutorial);
        }
//            int rowNumber = 0;
//            while (rows.hasNext()) {
//                System.out.println("rowNumber = " + rowNumber);
//                Row currentRow = rows.next();
//
//                if (rowNumber == 0) {
//                    rowNumber++;
//                    continue;
//                }
//                Iterator<Cell> cellsInRow = currentRow.iterator();
//
//                Tutorial tutorial = new Tutorial();
//
//                int cellIndex = 0;

//                while (cellsInRow.hasNext()) {
//                    System.out.println("cellIndex = " + cellIndex);
//                    Cell currentCell = cellsInRow.next();
////                    System.out.println("currentCell.getNumericCellValue() = " + currentCell.getNumericCellValue());
//                    switch (cellIndex) {
//                        case 0:
//                            System.out.println("currentCell.getNumericCellValue() = " + currentCell.getNumericCellValue());
//                            tutorial.setId((long) currentCell.getNumericCellValue());
//                            break;
//                        case 1:
//                            tutorial.setTitle(currentCell.getStringCellValue());
//                            break;
//                        case 2:
//                            tutorial.setDescription(currentCell.getStringCellValue());
//                            break;
//                        case 3:
//                            tutorial.setPublished(currentCell.getBooleanCellValue());
//                            break;
//                        default:
//                            break;
//                    }
//                    cellIndex++;
//                }
//                System.out.println("tutorials = " + tutorials);
//            }
//            workbook.close();
        System.out.println("tutorials = " + tutorials);
        return tutorials;
    }

    public static boolean isValidExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
}
