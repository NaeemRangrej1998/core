package com.ecommerce.dto;


import com.ecommerce.entity.CategoryEntity;
import com.ecommerce.entity.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class ExcelDTO {
    private CategoryEntity categoryEntity;
    private ModelEntity modelEntity;
    private ColorEntity colorEntity;
    private RamEntity ramEntity;
    private InternalStorageEntity internalStorageEntity;

}

