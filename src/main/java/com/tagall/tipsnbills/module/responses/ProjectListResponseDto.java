package com.tagall.tipsnbills.module.responses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectListResponseDto {

    private long numberOfProjects;

    private List<ProjectResponseDto> projects;

}
