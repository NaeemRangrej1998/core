package com.ecommerce.service.Impl;

import com.ecommerce.entity.TutorialEntity;
import com.ecommerce.repository.TutorialRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TutorialExcelServiceImpl implements TutorialExcelService {

    static String SHEET = "Tutorials";
    private final TutorialRepository tutorialRepository;

    public TutorialExcelServiceImpl(TutorialRepository tutorialRepository) {
        this.tutorialRepository = tutorialRepository;
    }

    @Override
    public void save(MultipartFile file) throws IOException {
    List<TutorialEntity> tutorialEntities=excelFileToTutorialEntity(file.getInputStream());
        tutorialRepository.saveAll(tutorialEntities);
    }

    public List<TutorialEntity> excelFileToTutorialEntity(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);
        System.out.println("inputStream = " + inputStream);
        Sheet sheet = workbook.getSheet(SHEET);
        System.out.println("sheet = " + sheet);
        Iterator<Row> rows = sheet.iterator();

        List<TutorialEntity> tutorials = new ArrayList<>();
        int rowNumber = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();

            if (rowNumber == 0) {
                rowNumber++;
                continue;
            }
            Iterator<Cell> cellsInRow = currentRow.iterator();

            TutorialEntity tutorial = new TutorialEntity();

            int cellIndex = 0;

            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();
                switch (cellIndex) {
                    case 0:
                        tutorial.setId((long) currentCell.getNumericCellValue());
                        break;
                    case 1:
                        tutorial.setTitle(currentCell.getStringCellValue());
                        break;
                    case 2:
                        tutorial.setDescription(currentCell.getStringCellValue());
                        break;
                    case 3:
                        tutorial.setPublished(currentCell.getBooleanCellValue());
                        break;
                    default:
                        break;
                }
                cellIndex++;
            }
            tutorials.add(tutorial);
        }
        workbook.close();
        return tutorials;
    }
}
