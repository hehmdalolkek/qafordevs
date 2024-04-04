package ru.hehmdalolkek.qafordevs.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.CollectionUtils;
import ru.hehmdalolkek.qafordevs.enitity.DeveloperEntity;
import ru.hehmdalolkek.qafordevs.util.DataUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeveloperRepositoryTests {

    @Autowired
    DeveloperRepository developerRepository;

    @BeforeEach
    public void setUp() {
        this.developerRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save developer functionality")
    public void givenDeveloperObject_whenSave_thenDeveloperIsCreated() {
        // given
        DeveloperEntity developerToSave = DataUtil.getJohnDoeTransient();

        // when
        DeveloperEntity savedDeveloper = this.developerRepository.save(developerToSave);

        // then
        assertThat(savedDeveloper).isNotNull();
        assertThat(savedDeveloper.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test update developer functionality")
    public void givenDeveloperToUpdate_whenSave_thenDeveloperIsCreated() {
        // given
        DeveloperEntity developerToCreate = DataUtil.getJohnDoeTransient();
        this.developerRepository.save(developerToCreate);
        String updatedEmail = "updated@email.com";

        // when
        DeveloperEntity developerToUpdate = this.developerRepository.findById(developerToCreate.getId())
                .orElse(null);
        developerToUpdate.setEmail(updatedEmail);
        DeveloperEntity updatedDeveloper = this.developerRepository.save(developerToUpdate);

        // then
        assertThat(updatedDeveloper).isNotNull();
        assertThat(updatedDeveloper.getEmail()).isEqualTo(updatedEmail);
    }

    @Test
    @DisplayName("Test find developer by id functionality")
    public void givenDeveloperToCreate_whenFindById_thenDeveloperIsReturned() {
        // given
        DeveloperEntity developerToCreate = DataUtil.getJohnDoeTransient();
        this.developerRepository.save(developerToCreate);

        // when
        DeveloperEntity foundedDeveloper = this.developerRepository.findById(developerToCreate.getId())
                .orElse(null);

        // then
        assertThat(foundedDeveloper).isNotNull();
        assertThat(foundedDeveloper.getId()).isEqualTo(developerToCreate.getId());
    }

    @Test
    @DisplayName("Test developer is not found functionality")
    public void givenDeveloperIsNotCreated_whenFindById_thenOptionalIsEmpty() {
        // given

        // when
        DeveloperEntity foundedDeveloper = this.developerRepository.findById(1)
                .orElse(null);

        // then
        assertThat(foundedDeveloper).isNull();
    }

    @Test
    @DisplayName("Test delete developer by id")
    public void givenDeveloperToCreate_whenDeleteById_thenDeveloperIsDeleted() {
        // given
        DeveloperEntity developerToCreate = DataUtil.getJohnDoeTransient();
        this.developerRepository.save(developerToCreate);

        // when
        this.developerRepository.deleteById(developerToCreate.getId());

        // then
        DeveloperEntity deletedDeveloper = this.developerRepository.findById(developerToCreate.getId())
                .orElse(null);
        assertThat(deletedDeveloper).isNull();
    }

    @Test
    @DisplayName("Test find all developers")
    public void givenThreeDevelopers_whenFindAll_thenReturnedThreeDevelopers() {
        // given
        DeveloperEntity developer1 = DataUtil.getJohnDoeTransient();
        DeveloperEntity developer2 = DataUtil.getMikeSmithTransient();
        DeveloperEntity developer3 = DataUtil.getFrankJonesTransient();
        List<DeveloperEntity> developers = List.of(developer1, developer2, developer3);
        this.developerRepository.saveAll(developers);

        // when
        List<DeveloperEntity> foundedDevelopers = this.developerRepository.findAll();

        // then
        assertThat(CollectionUtils.isEmpty(foundedDevelopers)).isFalse();
        assertThat(foundedDevelopers.size()).isEqualTo(developers.size());
    }

    @Test
    @DisplayName("Test find developer by email")
    public void givenDeveloperToCreate_whenFindByEmail_thenReturnedDeveloper() {
        // given
        DeveloperEntity developerToCreate = DataUtil.getJohnDoeTransient();
        this.developerRepository.save(developerToCreate);

        // when
        DeveloperEntity foundedDeveloper = this.developerRepository.findByEmail(developerToCreate.getEmail());

        // then
        assertThat(foundedDeveloper).isNotNull();
        assertThat(foundedDeveloper.getEmail()).isEqualTo(developerToCreate.getEmail());
    }

    @Test
    @DisplayName("Test find all by active speciality")
    public void givenThreeDevelopers_whenFindAllByActiveSpeciality_thenReturnedTwoDevelopers() {
        // given
        DeveloperEntity developer1 = DataUtil.getJohnDoeTransient();
        DeveloperEntity developer2 = DataUtil.getMikeSmithTransient();
        DeveloperEntity developer3 = DataUtil.getFrankJonesTransient();
        this.developerRepository.saveAll(List.of(developer1, developer2, developer3));

        // when
        List<DeveloperEntity> foundedDevelopers = this.developerRepository.findAllByActiveSpeciality("Java");

        // then
        assertThat(CollectionUtils.isEmpty(foundedDevelopers)).isFalse();
        assertThat(foundedDevelopers.size()).isEqualTo(2);
    }

}
