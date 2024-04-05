package ru.hehmdalolkek.qafordevs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hehmdalolkek.qafordevs.enitity.DeveloperEntity;
import ru.hehmdalolkek.qafordevs.enitity.Status;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String speciality;
    private Status status;

    public DeveloperEntity toEntity() {
        return DeveloperEntity.builder()
                .id(this.id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .speciality(this.speciality)
                .status(this.status)
                .build();
    }

    public static DeveloperDto fromEntity(DeveloperEntity developer) {
        return DeveloperDto.builder()
                .id(developer.getId())
                .firstName(developer.getFirstName())
                .lastName(developer.getLastName())
                .email(developer.getEmail())
                .speciality(developer.getSpeciality())
                .status(developer.getStatus())
                .build();
    }

}
