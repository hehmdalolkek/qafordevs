package ru.hehmdalolkek.qafordevs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hehmdalolkek.qafordevs.enitity.DeveloperEntity;

import java.util.List;

@Repository
public interface DeveloperRepository extends JpaRepository<DeveloperEntity, Integer> {

    DeveloperEntity findByEmail(String email);

    @Query("select d from DeveloperEntity d where d.status = 'ACTIVE' and d.speciality = ?1")
    List<DeveloperEntity> findAllByActiveSpeciality(String speciality);

}
