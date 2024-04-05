package ru.hehmdalolkek.qafordevs.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hehmdalolkek.qafordevs.dto.DeveloperDto;
import ru.hehmdalolkek.qafordevs.enitity.DeveloperEntity;
import ru.hehmdalolkek.qafordevs.exception.DeveloperNotFoundException;
import ru.hehmdalolkek.qafordevs.exception.DeveloperWithDuplicateEmailException;
import ru.hehmdalolkek.qafordevs.service.DeveloperService;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/developers")
public class DeveloperRestController {

    private final DeveloperService developerService;

    @GetMapping("/{developerId:\\d+}")
    public ResponseEntity<?> findDeveloperById(@PathVariable("developerId") Integer developerId) {
        try {
            DeveloperEntity developer = this.developerService.findDeveloperById(developerId);
            return ResponseEntity.ok(DeveloperDto.fromEntity(developer));
        } catch (DeveloperNotFoundException exception) {
            ProblemDetail problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(problemDetail);
        }
    }

    @PostMapping
    public ResponseEntity<?> createDeveloper(@RequestBody DeveloperDto developerDto) {
        try {
            DeveloperEntity createdDeveloper = this.developerService.saveDeveloper(developerDto.toEntity());
            return ResponseEntity.created(URI.create("/api/v1/developers/" + createdDeveloper.getId()))
                    .body(DeveloperDto.fromEntity(createdDeveloper));
        } catch (DeveloperWithDuplicateEmailException exception) {
            ProblemDetail problemDetail =
                    ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(problemDetail);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateDeveloper(@RequestBody DeveloperDto developerDto) {
        try {
            DeveloperEntity updatedDeveloper = this.developerService.updateDeveloper(developerDto.toEntity());
            return ResponseEntity.ok()
                    .body(DeveloperDto.fromEntity(updatedDeveloper));
        } catch (DeveloperNotFoundException exception) {
            ProblemDetail problemDetail =
                    ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
            return ResponseEntity.badRequest()
                    .body(problemDetail);
        }
    }

    @GetMapping
    public ResponseEntity<?> findAllDevelopers() {
        List<DeveloperEntity> developersEntities = this.developerService.findAllDevelopers();
        List<DeveloperDto> developersDtos = developersEntities.stream()
                .map(DeveloperDto::fromEntity)
                .toList();
        return ResponseEntity.ok()
                .body(developersDtos);
    }

    @GetMapping("/speciality/{developerSpeciality}")
    public ResponseEntity<?> findAllDevelopersBySpeciality(@PathVariable("developerSpeciality") String developerSpeciality) {
        List<DeveloperEntity> developersEntities =
                this.developerService.findActiveDevelopersBySpeciality(developerSpeciality);
        List<DeveloperDto> developersDtos = developersEntities.stream()
                .map(DeveloperDto::fromEntity)
                .toList();
        return ResponseEntity.ok()
                .body(developersDtos);
    }

    @DeleteMapping("/{developerId}")
    public ResponseEntity<?> deleteDeveloper(@PathVariable("developerId") Integer developerId,
                                             @RequestParam(value = "isHard", defaultValue = "false") boolean isHard) {
        try {
            if (isHard) {
                this.developerService.hardDeleteById(developerId);
            } else {
                this.developerService.softDeleteById(developerId);
            }
            return ResponseEntity.ok()
                    .build();
        } catch (DeveloperNotFoundException exception) {
            ProblemDetail problemDetail =
                    ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
            return ResponseEntity.badRequest()
                    .body(problemDetail);
        }
    }

}
