package ru.hehmdalolkek.qafordevs.service;

import ru.hehmdalolkek.qafordevs.enitity.DeveloperEntity;

import java.util.List;

public interface DeveloperService {

    DeveloperEntity findDeveloperById(Integer id);

    DeveloperEntity findDeveloperByEmail(String email);

    DeveloperEntity saveDeveloper(DeveloperEntity developer);

    DeveloperEntity updateDeveloper(DeveloperEntity developer);

    List<DeveloperEntity> findAllDevelopers();

    List<DeveloperEntity> findActiveDevelopersBySpeciality(String spectiality);

    void softDeleteById(Integer id);

    void hardDeleteById(Integer id);

}
