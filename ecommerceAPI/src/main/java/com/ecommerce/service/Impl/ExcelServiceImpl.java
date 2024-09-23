package com.ecommerce.service.Impl;

//import com.ecommerce.repository.*;

import com.ecommerce.dto.ExcelDTO;
import com.ecommerce.entity.*;
import com.ecommerce.repository.*;
import com.ecommerce.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExcelServiceImpl implements ExcelService {

    private final CategoryRepository categoryRepository;
    private final ModelRepository modelRepository;
    private final ColorRepository colorRepository;
    private final RamRepository ramRepository;
    private final InternalStorageRepository internalStorageRepository;
    private final ProductRepository productRepository;

//    Also We Achived the Using |While Loop But Using For Loop It's simpler or

    @Override
    public void readExcelFileAndSaveToDatabase(MultipartFile multipartFile) throws IOException {

        //Create WorkBook For Writing And Reading Excel File I will used XSSFWorkbook Becuase i have file Type is xlxs
        Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());

        //Create Sheet For Which To Use
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
//            System.out.println("row = " + row);
            if (row.getRowNum() == 0) {
                continue;
            }

            //GETTING VALUE FORM EXCEL FILE FOR FINDING THAT VALUE IN DATABASE
            String categoryName = row.getCell(0).getStringCellValue();
            String modelName = row.getCell(1).getStringCellValue();
            String colorName = row.getCell(2).getStringCellValue();
            String ram = row.getCell(3).getStringCellValue();
            String internalStorage = row.getCell(4).getStringCellValue();
            System.out.println("categoryName = " + categoryName + " " + modelName + " ");

            //fOR FIND IF VALUE ALREADY PRESENT THEN IT'S RETURN THAT VALUE OTHERWISE SAVE IT THEN RETURN IT
//            CategoryEntity categoryEntity = categoryRepository.findByCategoryName(categoryName)
//                    .orElseGet(() -> categoryRepository.save(new CategoryEntity(categoryName)));
//
//            ModelEntity modelEntity = modelRepository.findByModalName(modelName)
//                    .orElseGet(() -> modelRepository.save(new ModelEntity(modelName,categoryEntity)));
//
//            RamEntity ramEntity = ramRepository.findByRam(ram)
//                    .orElseGet(() -> ramRepository.save(new RamEntity(ram,categoryEntity)));
//
//            ColorEntity colorEntity = colorRepository.findByColorName(colorName)
//                    .orElseGet(() -> colorRepository.save(new ColorEntity(colorName,categoryEntity)));
//
//            InternalStorageEntity internalStorageEntity = internalStorageRepository.findByInternalStorage(internalStorage)
//                    .orElseGet(() -> internalStorageRepository.save(new InternalStorageEntity(internalStorage,categoryEntity)));

            CategoryEntity categoryEntity = categoryRepository.findByCategoryName(categoryName).orElseGet(() -> getOldOrCreateNewCategoryEntity(categoryName));//Also  i have used Category categoryEntity = categoryRepository.findByName(category).orElseGet(() -> categoryRepository.save(new Category(category))); but i have create seprate method
            ModelEntity modelEntity = modelRepository.findByModalName(modelName).orElseGet(() -> getOldOrCreateNewModelEntity(modelName, categoryEntity));
            ColorEntity colorEntity = colorRepository.findByColorName(colorName).orElseGet(() -> getOldOrCreateNewColorEntity(colorName, categoryEntity));
            RamEntity ramEntity = ramRepository.findByRam(ram).orElseGet(() -> getOldOrCreateNewRamEntity(ram, categoryEntity));
            InternalStorageEntity internalStorageEntity = internalStorageRepository.findByInternalStorage(internalStorage).orElseGet(() -> getOldOrCreateNewInternalStorageEntity(internalStorage, categoryEntity));
//            ProductEntity productEntity = new ProductEntity(categoryEntity, modelEntity, colorEntity, ramEntity, internalStorageEntity);
            ExcelDTO excelDTO =new ExcelDTO(categoryEntity, modelEntity, colorEntity, ramEntity, internalStorageEntity);
            Optional<ProductEntity> productEntity1 = productRepository.findByAllEntities(excelDTO);

            System.out.println("productEntity1 = " + productEntity1.isPresent());
//            System.out.println("productEntity1.get().getCategoryEntity() = " + productEntity1.get().getCategoryEntity());
            if (productEntity1.isEmpty()) {
                ProductEntity productEntity = new ProductEntity();
                productEntity.setCategoryEntity(categoryEntity);
                productEntity.setModelEntity(modelEntity);
                productEntity.setColorEntity(colorEntity);
                productEntity.setRamEntity(ramEntity);
                productEntity.setInternalStorageEntity(internalStorageEntity);
                productRepository.save(productEntity);
            } else {
                System.out.println("Product with the given combination already exists, skipping save.");
            }
        }
    }

    //BEFORE CREATE THIS METHOD I HAD MANAGED SAVING CALL IN CALLING TIME OF ENTITY WITH CLASS INITIALIZATION

    private InternalStorageEntity getOldOrCreateNewInternalStorageEntity(String internalStorage, CategoryEntity categoryEntity) {
        InternalStorageEntity internalStorageEntity = new InternalStorageEntity();
        internalStorageEntity.setInternalStorage(internalStorage);
        internalStorageEntity.setCategoryEntity(categoryEntity);
        return internalStorageRepository.save(internalStorageEntity);
    }

    private RamEntity getOldOrCreateNewRamEntity(String ram, CategoryEntity categoryEntity) {
        RamEntity ramEntity = new RamEntity();
        ramEntity.setRam(ram);
        ramEntity.setCategoryEntity(categoryEntity);
        return ramRepository.save(ramEntity);
    }

    private ColorEntity getOldOrCreateNewColorEntity(String colorName, CategoryEntity categoryEntity) {
        ColorEntity colorEntity = new ColorEntity();
        colorEntity.setColorName(colorName);
        colorEntity.setCategoryEntity(categoryEntity);
        return colorRepository.save(colorEntity);
    }

    private ModelEntity getOldOrCreateNewModelEntity(String modelName, CategoryEntity categoryEntity) {
        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setModalName(modelName);
        modelEntity.setCategoryEntity(categoryEntity);
        return modelRepository.save(modelEntity);
    }

    private CategoryEntity getOldOrCreateNewCategoryEntity(String categoryName) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryName(categoryName);
        return categoryRepository.save(categoryEntity);
    }
}
