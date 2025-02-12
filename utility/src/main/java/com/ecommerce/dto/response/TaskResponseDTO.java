package com.ecommerce.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {

    private  Long id;

    @NotBlank(message = "TaskName cannot be blank")
    private String taskName;

    @NotBlank(message = "taskStatus cannot be blank")
    private String taskStatus;

    @NotBlank(message = "TaskColumnName cannot be blank")
    private String taskColumnName;
}
