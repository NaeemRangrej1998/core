package com.ecommerce.service.Impl;

//import com.ecommerce.repository.*;

import com.ecommerce.dto.ExcelDTO;
import com.ecommerce.dto.request.GetTokenClaimsDTO;
import com.ecommerce.entity.*;
import com.ecommerce.exception.CustomException;
import com.ecommerce.repository.*;
import com.ecommerce.service.ExcelService;
import com.ecommerce.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private final PriceRepository priceRepository;
    private final BrandRepository brandRepository;
    private final NetworkRepository networkRepository;
    private final BatteryCapacityRepository batteryCapacityRepository;
    private final ScreenSizeRepository screenSizeRepository;
    private final ProcessorRepository processorRepository;
    private final SimSlotRepository simSlotRepository;

//    Also We Achived the Using |While Loop But Using For Loop It's simpler or

    @Override
    public void readExcelFileAndSaveToDatabase(MultipartFile multipartFile, GetTokenClaimsDTO claimsDTO) throws IOException {
        System.out.println("claimsDTO.getUserId() = " + claimsDTO.getUserId());
        //Create WorkBook For Writing And Reading Excel File I will used XSSFWorkbook Becuase i have file Type is xlxs
        Workbook workbook = new XSSFWorkbook(multipartFile.getInputStream());

        //Create Sheet For Which To Use
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
//            try {
            //GETTING VALUE FORM EXCEL FILE FOR FINDING THAT VALUE IN DATABASE
            String categoryName = row.getCell(0).getStringCellValue();
            String brandName = row.getCell(1).getStringCellValue();
            String modelName = row.getCell(2).getStringCellValue();
            String colorName = row.getCell(3).getStringCellValue();
            String ram = row.getCell(4).getStringCellValue();
            String internalStorage = row.getCell(5).getStringCellValue();
            String network = row.getCell(6).getStringCellValue();
            String simSlot = row.getCell(7).getStringCellValue();
            String screenSize = row.getCell(8).getStringCellValue();
            String batteryCapacity = row.getCell(9).getStringCellValue();
            String processor = row.getCell(10).getStringCellValue();
//            Double price = row.getCell(5).getNumericCellValue();
            System.out.println("categoryName = " + categoryName + " " + " ");


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

            CategoryEntity categoryEntity = categoryRepository.findByCategoryName(categoryName).orElseGet(() -> getOldOrCreateNewCategoryEntity(categoryName, claimsDTO));//Also  i have used Category categoryEntity = categoryRepository.findByName(category).orElseGet(() -> categoryRepository.save(new Category(category))); but i have create seprate method
            ModelEntity modelEntity = modelRepository.findByModalName(modelName).orElseGet(() -> getOldOrCreateNewModelEntity(modelName, categoryEntity, claimsDTO));
            List<ColorEntity> colorEntityList = getOldOrCreateNewColorEntity(colorName, categoryEntity, claimsDTO);
            List<RamEntity> ramEntityList = getOldOrCreateNewRamEntity(ram, categoryEntity, claimsDTO);
            List<InternalStorageEntity> internalStorageEntityList = getOldOrCreateNewInternalStorageEntity(internalStorage, categoryEntity, claimsDTO);
            BrandEntity brandEntity = brandRepository.findByBrandName(brandName).orElseGet(() -> getOldOrCreateNewBrandEntity(brandName, categoryEntity, claimsDTO));
            NetworkEntity networkEntity = networkRepository.findByNetwork(network).orElseGet(() -> getOldOrCreateNewNetworkEntity(network, categoryEntity, claimsDTO));
            BatteryCapacityEntity batteryCapacityEntity = batteryCapacityRepository.findByBatteryCapacity(batteryCapacity).orElseGet(() -> getOldOrCreateNewBatteryCapacityEntity(batteryCapacity, categoryEntity, claimsDTO));
            ScreenSizeEntity screenSizeEntity = screenSizeRepository.findByScreenSize(screenSize).orElseGet(() -> getOldOrCreateNewScreenSizeEntity(screenSize, categoryEntity, claimsDTO));
            ProcessorEntity processorEntity = processorRepository.findByProcessorName(processor).orElseGet(() -> getOldOrCreateNewProcessorEntity(processor, categoryEntity, claimsDTO));
            SimSlotEntity simSlotEntity = simSlotRepository.findBySimSlot(simSlot).orElseGet(() -> getOldOrCreateNewSimSlotEntity(simSlot, categoryEntity, claimsDTO));
//            PriceEntity priceEntity = priceRepository.findByPrice(price).orElseGet(() -> getOldOrCreateNewPriceEntity(price, categoryEntity, claimsDTO));

//            ProductEntity productEntity = new ProductEntity();
//            productEntity.setCategoryEntity(categoryEntity);
//            productEntity.setModelEntity(modelEntity);
//            productEntity.setColorEntity(colorEntity);
//            productEntity.setRamEntity(ramEntity);
//            productEntity.setInternalStorageEntity(internalStorageEntity);
            List<ProductEntity> productEntities = new ArrayList<ProductEntity>();

            for (ColorEntity colorEntity : colorEntityList) {
                for (RamEntity ramEntity : ramEntityList) {
                    for (InternalStorageEntity internalStorageEntity : internalStorageEntityList) {
                        Optional<ProductEntity> productEntity1 = productRepository.findByEntities(categoryEntity,brandEntity, modelEntity, colorEntity, ramEntity, internalStorageEntity,networkEntity,simSlotEntity,screenSizeEntity,batteryCapacityEntity,processorEntity);
                        if (productEntity1.isEmpty()) {
                            // If it doesn't exist, create a new ProductEntity
                            ProductEntity productEntity = new ProductEntity();
                            productEntity.setCategoryEntity(categoryEntity);
                            productEntity.setBrandEntity(brandEntity);
                            productEntity.setModelEntity(modelEntity);
                            productEntity.setColorEntity(colorEntity);
                            productEntity.setRamEntity(ramEntity);
                            productEntity.setInternalStorageEntity(internalStorageEntity);
                            productEntity.setNetworkEntity(networkEntity);
                            productEntity.setSimSlotEntity(simSlotEntity);
                            productEntity.setScreenSizeEntity(screenSizeEntity);
                            productEntity.setBatteryCapacityEntity(batteryCapacityEntity);
                            productEntity.setProcessorEntity(processorEntity);
                            productEntity.setCreatedDate(CommonUtils.getDateTime());
                            productEntity.setUpdatedDate(CommonUtils.getDateTime());
                            productEntity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
                            productEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
                            productEntity.setStatus(true);
                            productEntity.setDeactivate(false);
                            productEntities.add(productEntity);
                            productRepository.saveAll(productEntities);
                        }
                    }
                }
            }


//            colorEntity.forEach(colorEntity1 -> {

//            });

            // Save the new ProductEntity
//            System.out.println("productEntity1.get().getCategoryEntity() = " + productEntity1.get().getCategoryEntity());
////            System.out.println("productEntity1 = " + productEntity1.isPresent());
//            if (productEntity1.isEmpty()) {
//                productRepository.save(productEntity);
//            }
//            }catch (Exception e) {
//                // Handle row-specific exceptions
//                System.err.println("Error processing row " + row.getRowNum() + ": " + e.getMessage());
//                e.printStackTrace();
//            }
        }
    }

    private SimSlotEntity getOldOrCreateNewSimSlotEntity(String simSlot, CategoryEntity categoryEntity, GetTokenClaimsDTO claimsDTO) {
        SimSlotEntity simSlotEntity = new SimSlotEntity();
        simSlotEntity.setSimSlot(simSlot);
        simSlotEntity.setCategoryEntity(categoryEntity);
        simSlotEntity.setCreatedDate(CommonUtils.getDateTime());
        simSlotEntity.setUpdatedDate(CommonUtils.getDateTime());
        simSlotEntity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
        simSlotEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        simSlotEntity.setStatus(true);
        simSlotEntity.setDeactivate(false);
        return simSlotRepository.save(simSlotEntity);
    }

    private ProcessorEntity getOldOrCreateNewProcessorEntity(String processor, CategoryEntity categoryEntity, GetTokenClaimsDTO claimsDTO) {
        ProcessorEntity processorEntity = new ProcessorEntity();
        processorEntity.setProcessorName(processor);
        processorEntity.setCategoryEntity(categoryEntity);
        processorEntity.setCreatedDate(CommonUtils.getDateTime());
        processorEntity.setUpdatedDate(CommonUtils.getDateTime());
        processorEntity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
        processorEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        processorEntity.setStatus(true);
        processorEntity.setDeactivate(false);
        return processorRepository.save(processorEntity);
    }

    private ScreenSizeEntity getOldOrCreateNewScreenSizeEntity(String screenSize, CategoryEntity categoryEntity, GetTokenClaimsDTO claimsDTO) {
        ScreenSizeEntity screenSizeEntity = new ScreenSizeEntity();
        screenSizeEntity.setScreenSize(screenSize);
        screenSizeEntity.setCategoryEntity(categoryEntity);
        screenSizeEntity.setCreatedDate(CommonUtils.getDateTime());
        screenSizeEntity.setUpdatedDate(CommonUtils.getDateTime());
        screenSizeEntity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
        screenSizeEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        screenSizeEntity.setStatus(true);
        screenSizeEntity.setDeactivate(false);
        return screenSizeRepository.save(screenSizeEntity);
    }

    private BatteryCapacityEntity getOldOrCreateNewBatteryCapacityEntity(String batteryCapacity, CategoryEntity categoryEntity, GetTokenClaimsDTO claimsDTO) {
        BatteryCapacityEntity batteryCapacityEntity = new BatteryCapacityEntity();
        batteryCapacityEntity.setBatteryCapacity(batteryCapacity);
        batteryCapacityEntity.setCategoryEntity(categoryEntity);
        batteryCapacityEntity.setCreatedDate(CommonUtils.getDateTime());
        batteryCapacityEntity.setUpdatedDate(CommonUtils.getDateTime());
        batteryCapacityEntity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
        batteryCapacityEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        batteryCapacityEntity.setStatus(true);
        batteryCapacityEntity.setDeactivate(false);
        return batteryCapacityRepository.save(batteryCapacityEntity);
    }

    private NetworkEntity getOldOrCreateNewNetworkEntity(String network, CategoryEntity categoryEntity, GetTokenClaimsDTO claimsDTO) {
        NetworkEntity networkEntity = new NetworkEntity();
        networkEntity.setNetwork(network);
        networkEntity.setCategoryEntity(categoryEntity);
        networkEntity.setCreatedDate(CommonUtils.getDateTime());
        networkEntity.setUpdatedDate(CommonUtils.getDateTime());
        networkEntity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
        networkEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        networkEntity.setStatus(true);
        networkEntity.setDeactivate(false);
        return networkRepository.save(networkEntity);
    }

    private BrandEntity getOldOrCreateNewBrandEntity(String brandName, CategoryEntity categoryEntity, GetTokenClaimsDTO claimsDTO) {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandName(brandName);
        brandEntity.setCategoryEntity(categoryEntity);
        brandEntity.setCreatedDate(CommonUtils.getDateTime());
        brandEntity.setUpdatedDate(CommonUtils.getDateTime());
        brandEntity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
        brandEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        brandEntity.setStatus(true);
        brandEntity.setDeactivate(false);
        return brandRepository.save(brandEntity);
    }

    private PriceEntity getOldOrCreateNewPriceEntity(Double price, CategoryEntity categoryEntity, GetTokenClaimsDTO claimsDTO) {
        PriceEntity priceEntity = new PriceEntity();
        priceEntity.setPrice(price);
        priceEntity.setCategoryEntity(categoryEntity);
        priceEntity.setCreatedDate(CommonUtils.getDateTime());
        priceEntity.setUpdatedDate(CommonUtils.getDateTime());
        priceEntity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
        priceEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        priceEntity.setStatus(true);
        priceEntity.setDeactivate(false);
        return priceRepository.save(priceEntity);
    }

    //BEFORE CREATE THIS METHOD I HAD MANAGED SAVING CALL IN CALLING TIME OF ENTITY WITH CLASS INITIALIZATION

    private List<InternalStorageEntity> getOldOrCreateNewInternalStorageEntity(String internalStorage, CategoryEntity categoryEntity, GetTokenClaimsDTO claimsDTO) {
        List<String> internalStorageString = Arrays.asList(internalStorage.split(","));
        List<InternalStorageEntity> internalStorageEntities = new ArrayList<>();
        internalStorageString.forEach(s -> {
            Optional<InternalStorageEntity> optionalInternalStorage = internalStorageRepository.findByInternalStorage(s);
            if (optionalInternalStorage.isEmpty()) {
                InternalStorageEntity internalStorageEntity = new InternalStorageEntity();
                internalStorageEntity.setInternalStorage(s);
                internalStorageEntity.setCategoryEntity(categoryEntity);
                internalStorageEntity.setCreatedDate(CommonUtils.getDateTime());
                internalStorageEntity.setUpdatedDate(CommonUtils.getDateTime());
                internalStorageEntity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
                internalStorageEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
                internalStorageEntity.setStatus(true);
                internalStorageEntity.setDeactivate(false);
                internalStorageEntities.add(internalStorageEntity);
            } else {
                internalStorageEntities.add(optionalInternalStorage.get());
            }

        });
        return internalStorageRepository.saveAll(internalStorageEntities);
    }

    private List<RamEntity> getOldOrCreateNewRamEntity(String ram, CategoryEntity categoryEntity, GetTokenClaimsDTO claimsDTO) {
        List<String> ramList = Arrays.asList(ram.split(","));
        List<RamEntity> ramEntities = new ArrayList<>();
        ramList.forEach(ramItem -> {
            Optional<RamEntity> optionalRamEntity = ramRepository.findByRam(ramItem);
            if (optionalRamEntity.isEmpty()) {
                RamEntity ramEntity = new RamEntity();
                ramEntity.setRam(ramItem);
                ramEntity.setCategoryEntity(categoryEntity);
                ramEntity.setCreatedDate(CommonUtils.getDateTime());
                ramEntity.setUpdatedDate(CommonUtils.getDateTime());
                ramEntity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
                ramEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
                ramEntity.setStatus(true);
                ramEntity.setDeactivate(false);
                ramEntities.add(ramEntity);
            } else {
                ramEntities.add(optionalRamEntity.get());
            }
        });
        return ramRepository.saveAll(ramEntities);
    }

    private List<ColorEntity> getOldOrCreateNewColorEntity(String colorName, CategoryEntity categoryEntity, GetTokenClaimsDTO claimsDTO) {
        List<String> colorSplitString = Arrays.asList(colorName.split(","));
        List<ColorEntity> colorEntities = new ArrayList<ColorEntity>();
        colorSplitString.forEach(item -> {
            Optional<ColorEntity> colorEntity = colorRepository.findByColorName(item);
            if (colorEntity.isEmpty()) {
                ColorEntity entity = new ColorEntity();
                entity.setColorName(item);
                entity.setCategoryEntity(categoryEntity);
                entity.setCreatedDate(CommonUtils.getDateTime());
                entity.setUpdatedDate(CommonUtils.getDateTime());
                entity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
                entity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
                entity.setStatus(true);
                entity.setDeactivate(false);
                colorEntities.add(entity);
            } else {
                colorEntities.add(colorEntity.get());
            }
        });
        System.out.println("colorEntities = " + colorEntities);
        return colorRepository.saveAll(colorEntities);
    }

    private ModelEntity getOldOrCreateNewModelEntity(String modelName, CategoryEntity categoryEntity, GetTokenClaimsDTO claimsDTO) {
        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setModalName(modelName);
        modelEntity.setCategoryEntity(categoryEntity);
        modelEntity.setCreatedDate(CommonUtils.getDateTime());
        modelEntity.setUpdatedDate(CommonUtils.getDateTime());
        modelEntity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
        modelEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        modelEntity.setStatus(true);
        modelEntity.setDeactivate(false);
        return modelRepository.save(modelEntity);
    }

    private CategoryEntity getOldOrCreateNewCategoryEntity(String categoryName, GetTokenClaimsDTO claimsDTO) {
        System.out.println("claimsDTO.getUserId() = " + claimsDTO.getUserId());
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryName(categoryName);
        categoryEntity.setCreatedDate(CommonUtils.getDateTime());
        categoryEntity.setUpdatedDate(CommonUtils.getDateTime());
        categoryEntity.setCreatedBy(new UserEntity(claimsDTO.getUserId()));
        categoryEntity.setUpdatedBy(new UserEntity(claimsDTO.getUserId()));
        categoryEntity.setStatus(true);
        categoryEntity.setDeactivate(false);
        return categoryRepository.save(categoryEntity);
    }
}
