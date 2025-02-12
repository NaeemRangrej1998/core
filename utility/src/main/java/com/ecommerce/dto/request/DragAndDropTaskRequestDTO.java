package com.ecommerce.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DragAndDropTaskRequestDTO {

    @NotBlank(message = "TaskName cannot be blank")
    private String taskName;

    @NotBlank(message = "taskStatus cannot be blank")
    private String taskStatus;

    @NotBlank(message = "TaskColumnName cannot be blank")
    private String taskColumnName;
}
