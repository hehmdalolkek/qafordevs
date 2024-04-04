package ru.hehmdalolkek.qafordevs.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hehmdalolkek.qafordevs.enitity.DeveloperEntity;
import ru.hehmdalolkek.qafordevs.exception.DeveloperNotFoundException;
import ru.hehmdalolkek.qafordevs.exception.DeveloperWithDuplicateEmailException;
import ru.hehmdalolkek.qafordevs.repository.DeveloperRepository;
import ru.hehmdalolkek.qafordevs.util.DataUtil;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeveloperServiceImplTests {

    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private DeveloperServiceImpl serviceUnderTest;

    @Test
    @DisplayName("Test find developer by id functionality")
    public void givenDeveloperId_whenFindDeveloperById_thenReturnedDeveloper() {
        // given
        DeveloperEntity developerToFind = DataUtil.getJohnDoePersisted();
        doReturn(Optional.of(developerToFind)).when(developerRepository).findById(anyInt());

        // when
        DeveloperEntity foundedDeveloper = serviceUnderTest.findDeveloperById(developerToFind.getId());

        // then
        assertThat(foundedDeveloper).isNotNull();
        assertThat(foundedDeveloper).isEqualTo(developerToFind);
    }

    @Test
    @DisplayName("Test find developer by id functionality")
    public void givenIncorrectDeveloperId_whenFindDeveloperById_thenThrowsDeveloperNotFoundException() {
        // given
        doReturn(Optional.empty()).when(developerRepository).findById(anyInt());

        // when
        // then
        assertThrows(DeveloperNotFoundException.class,
                () -> serviceUnderTest.findDeveloperById(1));
    }

    @Test
    @DisplayName("Test find developer by email functionality")
    public void givenDeveloperEmail_whenFindDeveloperByEmail_thenReturnedDeveloper() {
        // given
        DeveloperEntity developerToFind = DataUtil.getJohnDoePersisted();
        doReturn(developerToFind).when(developerRepository).findByEmail(anyString());

        // when
        DeveloperEntity foundedDeveloper = serviceUnderTest.findDeveloperByEmail(developerToFind.getEmail());

        // then
        assertThat(foundedDeveloper).isNotNull();
        assertThat(foundedDeveloper).isEqualTo(developerToFind);
    }

    @Test
    @DisplayName("Test find developer by email functionality")
    public void givenIncorrectDeveloperEmail_whenFindDeveloperByEmail_thenThrowsDeveloperNotFoundException() {
        // given
        doReturn(null).when(developerRepository).findByEmail(anyString());

        // when
        // then
        assertThrows(DeveloperNotFoundException.class,
                () -> serviceUnderTest.findDeveloperByEmail("email@email.com"));
    }

    @Test
    @DisplayName("Test save developer functionality")
    public void givenDeveloperToSave_whenSaveDeveloper_thenReturnedSavedDeveloper() {
        // given
        DeveloperEntity developerToSave = DataUtil.getJohnDoeTransient();
        doReturn(null).when(developerRepository).findByEmail(anyString());
        doReturn(DataUtil.getJohnDoePersisted())
                .when(developerRepository).save(any(DeveloperEntity.class));

        // when
        DeveloperEntity savedDeveloper = serviceUnderTest.saveDeveloper(developerToSave);

        // then
        assertThat(savedDeveloper).isNotNull();
        assertThat(savedDeveloper).isEqualTo(DataUtil.getJohnDoePersisted());
    }

    @Test
    @DisplayName("Test save developer functionality")
    public void givenDeveloperToSaveWithDuplicateEmail_whenSaveDeveloper_thenThrowsDeveloperWithDuplicateEmail() {
        // given
        DeveloperEntity developerToSave = DataUtil.getJohnDoeTransient();
        doReturn(DataUtil.getFrankJonesPersisted())
                .when(developerRepository).findByEmail(anyString());

        // when
        assertThrows(DeveloperWithDuplicateEmailException.class,
                () -> serviceUnderTest.saveDeveloper(developerToSave));

        // then
        verify(developerRepository, never()).save(any(DeveloperEntity.class));
    }

    @Test
    @DisplayName("Test update developer functionality")
    public void givenDeveloperToUpdate_whenUpdateDeveloper_thenRepositoryIsCalled() {
        // given
        DeveloperEntity developerToUpdate = DataUtil.getJohnDoePersisted();
        doReturn(Optional.of(developerToUpdate)).when(developerRepository).findById(anyInt());

        // when
        serviceUnderTest.updateDeveloper(developerToUpdate);

        // then
        verify(developerRepository, times(1))
                .save(any(DeveloperEntity.class));
    }

    @Test
    @DisplayName("Test update developer functionality")
    public void givenDeveloperToUpdateWithIncorrectEmail_whenUpdateDeveloper_thenThrowsDeveloperNotFoundException() {
        // given
        DeveloperEntity developerToUpdate = DataUtil.getJohnDoePersisted();
        doReturn(Optional.empty()).when(developerRepository).findById(anyInt());

        // when
        assertThrows(DeveloperNotFoundException.class,
                () -> serviceUnderTest.updateDeveloper(developerToUpdate));

        // then
        verify(developerRepository, never())
                .save(any(DeveloperEntity.class));
    }

    @Test
    @DisplayName("Test find all developers functionality")
    public void givenThreeDevelopers_whenFindAllDevelopers_thenReturnedTwoActiveDevelopers() {
        // given
        DeveloperEntity developer1 = DataUtil.getJohnDoePersisted();
        DeveloperEntity developer2 = DataUtil.getMikeSmithPersisted();
        DeveloperEntity developer3 = DataUtil.getFrankJonesPersisted();
        List<DeveloperEntity> developers = List.of(developer1, developer2, developer3);
        doReturn(developers).when(developerRepository).findAll();

        // when
        List<DeveloperEntity> foundedDevelopers = serviceUnderTest.findAllDevelopers();

        // then
        assertThat(foundedDevelopers).isNotEmpty();
        assertThat(foundedDevelopers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test find actual developers by speciality functionality")
    public void givenTwoActiveDevelopers_whenFindActiveDevelopersBySpeciality_thenReturnedTwoDevelopers() {
        // given
        DeveloperEntity developer1 = DataUtil.getJohnDoePersisted();
        DeveloperEntity developer2 = DataUtil.getMikeSmithPersisted();
        List<DeveloperEntity> developers = List.of(developer1, developer2);
        doReturn(developers).when(developerRepository).findAllByActiveSpeciality(anyString());

        // when
        List<DeveloperEntity> foundedDevelopers = serviceUnderTest.findActiveDevelopersBySpeciality("Java");

        // then
        assertThat(foundedDevelopers).isNotEmpty();
        assertThat(foundedDevelopers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test soft delete developer by id functionality")
    public void givenDeveloperId_whenSoftDeleteById_thenDeveloperStatusUpdatedToDeleted() {
        // given
        DeveloperEntity developerToDelete = DataUtil.getJohnDoePersisted();
        doReturn(Optional.of(developerToDelete)).when(developerRepository).findById(anyInt());

        // when
        serviceUnderTest.softDeleteById(developerToDelete.getId());

        // then
        verify(developerRepository, times(1)).save(developerToDelete);
        verify(developerRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Test soft delete developer by id functionality")
    public void givenIncorrectDeveloperId_whenSoftDeleteById_thenThrowsDeveloperNotFoundException() {
        // given
        doReturn(Optional.empty()).when(developerRepository).findById(anyInt());

        // when
        assertThrows(DeveloperNotFoundException.class,
                () -> serviceUnderTest.softDeleteById(1));

        // then
        verify(developerRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Test hard delete developer by id functionality")
    public void givenDeveloperId_whenHardDeleteById_thenRepositoryMethodDeleteIsCalled() {
        // given
        DeveloperEntity developerToDelete = DataUtil.getJohnDoePersisted();
        doReturn(Optional.of(developerToDelete)).when(developerRepository).findById(anyInt());

        // when
        serviceUnderTest.hardDeleteById(developerToDelete.getId());

        // then
        verify(developerRepository, times(1)).deleteById(anyInt());
        verify(developerRepository, never()).save(any(DeveloperEntity.class));
    }

    @Test
    @DisplayName("Test hard delete developer by id functionality")
    public void givenIncorrectDeveloperId_whenHardDeleteById_thenThrowsDeveloperNotFoundException() {
        // given
        doReturn(Optional.empty()).when(developerRepository).findById(anyInt());

        // when
        assertThrows(DeveloperNotFoundException.class,
                () -> serviceUnderTest.hardDeleteById(1));

        // then
        verify(developerRepository, never()).deleteById(anyInt());
    }

}