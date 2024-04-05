package ru.hehmdalolkek.qafordevs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hehmdalolkek.qafordevs.enitity.DeveloperEntity;
import ru.hehmdalolkek.qafordevs.enitity.Status;
import ru.hehmdalolkek.qafordevs.exception.DeveloperNotFoundException;
import ru.hehmdalolkek.qafordevs.exception.DeveloperWithDuplicateEmailException;
import ru.hehmdalolkek.qafordevs.repository.DeveloperRepository;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;

    @Override
    public DeveloperEntity findDeveloperById(Integer id) {
        return this.developerRepository.findById(id)
                .orElseThrow(() -> new DeveloperNotFoundException("Developer not found"));
    }

    @Override
    public DeveloperEntity findDeveloperByEmail(String email) {
        DeveloperEntity developer = this.developerRepository.findByEmail(email);
        if (Objects.isNull(developer)) {
            throw new DeveloperNotFoundException("Developer not found");
        }
        return developer;
    }

    @Override
    public DeveloperEntity saveDeveloper(DeveloperEntity developer) {
        DeveloperEntity foundedDeveloper = this.developerRepository.findByEmail(developer.getEmail());
        if (!Objects.isNull(foundedDeveloper)) {
            throw new DeveloperWithDuplicateEmailException("Developer with this email is already exists");
        }
        return this.developerRepository.save(developer);
    }

    @Override
    public DeveloperEntity updateDeveloper(DeveloperEntity developer) {
        this.developerRepository.findById(developer.getId())
                .orElseThrow(() -> new DeveloperNotFoundException("Developer not found"));
        return this.developerRepository.save(developer);
    }

    @Override
    public List<DeveloperEntity> findAllDevelopers() {
        return this.developerRepository.findAll()
                .stream()
                .filter((developer -> developer.getStatus().equals(Status.ACTIVE)))
                .toList();
    }

    @Override
    public List<DeveloperEntity> findActiveDevelopersBySpeciality(String spectiality) {
        return this.developerRepository.findAllByActiveSpeciality(spectiality);
    }

    @Override
    public void softDeleteById(Integer id) {
        DeveloperEntity foundedDeveloper = this.developerRepository.findById(id)
                .orElseThrow(() -> new DeveloperNotFoundException("Developer not found"));
        foundedDeveloper.setStatus(Status.DELETED);
        this.developerRepository.save(foundedDeveloper);
    }

    @Override
    public void hardDeleteById(Integer id) {
        DeveloperEntity foundedDeveloper = this.developerRepository.findById(id)
                .orElseThrow(() -> new DeveloperNotFoundException("Developer not found"));
        this.developerRepository.deleteById(foundedDeveloper.getId());
    }
}
