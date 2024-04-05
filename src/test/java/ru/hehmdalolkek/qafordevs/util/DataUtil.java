package ru.hehmdalolkek.qafordevs.util;

import ru.hehmdalolkek.qafordevs.dto.DeveloperDto;
import ru.hehmdalolkek.qafordevs.enitity.DeveloperEntity;
import ru.hehmdalolkek.qafordevs.enitity.Status;

public class DataUtil {

    public static DeveloperEntity getJohnDoeTransient() {
        return DeveloperEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .speciality("Java")
                .status(Status.ACTIVE)
                .build();
    }

    public static DeveloperEntity getJohnDoePersisted() {
        return DeveloperEntity.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .speciality("Java")
                .status(Status.ACTIVE)
                .build();
    }

    public static DeveloperEntity getMikeSmithTransient() {
        return DeveloperEntity.builder()
                .firstName("Mike")
                .lastName("Smith")
                .email("smith@email.com")
                .speciality("Java")
                .status(Status.ACTIVE)
                .build();
    }

    public static DeveloperEntity getMikeSmithPersisted() {
        return DeveloperEntity.builder()
                .id(2)
                .firstName("Mike")
                .lastName("Smith")
                .email("smith@email.com")
                .speciality("Java")
                .status(Status.ACTIVE)
                .build();
    }

    public static DeveloperEntity getFrankJonesTransient() {
        return DeveloperEntity.builder()
                .firstName("Frank")
                .lastName("Jones")
                .email("frank@email.com")
                .speciality("Java")
                .status(Status.DELETED)
                .build();
    }

    public static DeveloperEntity getFrankJonesPersisted() {
        return DeveloperEntity.builder()
                .id(3)
                .firstName("Frank")
                .lastName("Jones")
                .email("frank@email.com")
                .speciality("Java")
                .status(Status.DELETED)
                .build();
    }

    public static DeveloperDto getDtoJohnDoeTransient() {
        return DeveloperDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .speciality("Java")
                .status(Status.ACTIVE)
                .build();
    }

    public static DeveloperDto getDtoJohnDoePersisted() {
        return DeveloperDto.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .speciality("Java")
                .status(Status.ACTIVE)
                .build();
    }

    public static DeveloperDto getDtoMikeSmithTransient() {
        return DeveloperDto.builder()
                .firstName("Mike")
                .lastName("Smith")
                .email("smith@email.com")
                .speciality("Java")
                .status(Status.ACTIVE)
                .build();
    }

    public static DeveloperDto getDtoMikeSmithPersisted() {
        return DeveloperDto.builder()
                .id(2)
                .firstName("Mike")
                .lastName("Smith")
                .email("smith@email.com")
                .speciality("Java")
                .status(Status.ACTIVE)
                .build();
    }

    public static DeveloperDto getDtoFrankJonesTransient() {
        return DeveloperDto.builder()
                .firstName("Frank")
                .lastName("Jones")
                .email("frank@email.com")
                .speciality("Java")
                .status(Status.DELETED)
                .build();
    }

    public static DeveloperDto getDtoFrankJonesPersisted() {
        return DeveloperDto.builder()
                .id(3)
                .firstName("Frank")
                .lastName("Jones")
                .email("frank@email.com")
                .speciality("Java")
                .status(Status.DELETED)
                .build();
    }
}
