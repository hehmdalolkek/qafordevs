package ru.hehmdalolkek.qafordevs.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hehmdalolkek.qafordevs.dto.DeveloperDto;
import ru.hehmdalolkek.qafordevs.enitity.DeveloperEntity;
import ru.hehmdalolkek.qafordevs.exception.DeveloperNotFoundException;
import ru.hehmdalolkek.qafordevs.exception.DeveloperWithDuplicateEmailException;
import ru.hehmdalolkek.qafordevs.service.DeveloperService;
import ru.hehmdalolkek.qafordevs.util.DataUtil;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class DeveloperRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeveloperService developerService;

    @Test
    @DisplayName("Test find developer by id functionality")
    public void givenDeveloperId_whenFindDeveloperById_thenSuccessResponse() throws Exception {
        // given
        doReturn(DataUtil.getJohnDoePersisted()).when(developerService).findDeveloperById(anyInt());

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/developers/1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()),
                        MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is("John")),
                        MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is("Doe")),
                        MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("ACTIVE"))
                );
    }

    @Test
    @DisplayName("Test find developer by id functionality")
    public void givenIncorrectDeveloperId_whenFindDeveloperById_thenErrorResponse() throws Exception {
        // given
        doThrow(new DeveloperNotFoundException("Developer not found"))
                .when(developerService).findDeveloperById(anyInt());

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/developers/1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isNotFound(),
                        MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)),
                        MockMvcResultMatchers.jsonPath("$.detail", CoreMatchers.is("Developer not found"))
                );
    }

    @Test
    @DisplayName("Test create developer functionality")
    public void givenDeveloperDto_whenCreateDeveloper_thenSuccessResponse() throws Exception {
        // given
        DeveloperDto dto = DataUtil.getDtoJohnDoeTransient();
        DeveloperEntity entity = DataUtil.getJohnDoePersisted();
        doReturn(entity).when(developerService).saveDeveloper(any(DeveloperEntity.class));

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()),
                        MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is("John")),
                        MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is("Doe")),
                        MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("ACTIVE"))
                );
    }

    @Test
    @DisplayName("Test create developer functionality")
    public void givenDeveloperDtoWithDuplicateEmail_whenCreateDeveloper_thenErrorResponse() throws Exception {
        // given
        DeveloperDto dto = DataUtil.getDtoJohnDoeTransient();
        doThrow(new DeveloperWithDuplicateEmailException("Developer with this email is already exists"))
                .when(developerService).saveDeveloper(any(DeveloperEntity.class));

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isConflict(),
                        MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(409)),
                        MockMvcResultMatchers.jsonPath("$.detail",
                                CoreMatchers.is("Developer with this email is already exists"))
                );
    }

    @Test
    @DisplayName("Test update developer functionality")
    public void givenDeveloperDto_whenUpdateDeveloper_thenSuccessResponse() throws Exception {
        // given
        DeveloperDto dto = DataUtil.getDtoJohnDoeTransient();
        DeveloperEntity entity = DataUtil.getJohnDoePersisted();
        doReturn(entity).when(developerService).updateDeveloper(any(DeveloperEntity.class));

        // when
        ResultActions result = mockMvc.perform(put("/api/v1/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()),
                        MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is("John")),
                        MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is("Doe")),
                        MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("ACTIVE"))
                );
    }

    @Test
    @DisplayName("Test update developer functionality")
    public void givenIncorrectDeveloperDto_whenUpdateDeveloper_thenErrorResponse() throws Exception {
        // given
        DeveloperDto dto = DataUtil.getDtoJohnDoeTransient();
        doThrow(new DeveloperNotFoundException("Developer not found"))
                .when(developerService).updateDeveloper(any(DeveloperEntity.class));

        // when
        ResultActions result = mockMvc.perform(put("/api/v1/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(400)),
                        MockMvcResultMatchers.jsonPath("$.detail", CoreMatchers.is("Developer not found"))
                );
    }

    @Test
    @DisplayName("Test find all developers by id functionality")
    public void givenDevelopers_whenFindAllDevelopers_thenSuccessResponse() throws Exception {
        // given
        List<DeveloperEntity> developers = List.of(DataUtil.getJohnDoePersisted(), DataUtil.getMikeSmithPersisted());
        doReturn(developers).when(developerService).findAllDevelopers();

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/developers")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("$.[0].id", CoreMatchers.notNullValue()),
                        MockMvcResultMatchers.jsonPath("$.[0].firstName", CoreMatchers.is("John")),
                        MockMvcResultMatchers.jsonPath("$.[1].id", CoreMatchers.notNullValue()),
                        MockMvcResultMatchers.jsonPath("$.[1].firstName", CoreMatchers.is("Mike"))
                );
    }

    @Test
    @DisplayName("Test find all developers by id functionality")
    public void givenDevelopers_whenFindAllBySpecialityDevelopers_thenSuccessResponse() throws Exception {
        // given
        List<DeveloperEntity> developers = List.of(DataUtil.getJohnDoePersisted(), DataUtil.getMikeSmithPersisted());
        doReturn(developers).when(developerService).findActiveDevelopersBySpeciality(anyString());

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/developers/speciality/Java")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("$.[0].id", CoreMatchers.notNullValue()),
                        MockMvcResultMatchers.jsonPath("$.[0].firstName", CoreMatchers.is("John")),
                        MockMvcResultMatchers.jsonPath("$.[1].id", CoreMatchers.notNullValue()),
                        MockMvcResultMatchers.jsonPath("$.[1].firstName", CoreMatchers.is("Mike"))
                );
    }

    @Test
    @DisplayName("Test soft delete developer by id functionality")
    public void givenDeveloperId_whenDeleteDeveloperSoft_thenSuccessResponse() throws Exception {
        // given
        doNothing().when(developerService).softDeleteById(anyInt());

        // when
        ResultActions result = mockMvc.perform(delete("/api/v1/developers/1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        verify(developerService, times(1)).softDeleteById(anyInt());
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test soft delete developer by id functionality")
    public void givenIncorrectDeveloperId_whenDeleteDeveloperSoft_thenErrorResponse() throws Exception {
        // given
        doThrow(new DeveloperNotFoundException("Developer not found"))
                .when(developerService).softDeleteById(anyInt());

        // when
        ResultActions result = mockMvc.perform(delete("/api/v1/developers/1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        verify(developerService, times(1)).softDeleteById(anyInt());
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(400)),
                        MockMvcResultMatchers.jsonPath("$.detail", CoreMatchers.is("Developer not found"))
                );
    }


    @Test
    @DisplayName("Test hard delete developer by id functionality")
    public void givenDeveloperId_whenDeleteDeveloperHard_thenSuccessResponse() throws Exception {
        // given
        doNothing().when(developerService).hardDeleteById(anyInt());

        // when
        ResultActions result = mockMvc.perform(delete("/api/v1/developers/1?isHard=true")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        verify(developerService, times(1)).hardDeleteById(anyInt());
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test hard delete developer by id functionality")
    public void givenIncorrectDeveloperId_whenDeleteDeveloperHard_thenErrorResponse() throws Exception {
        // given
        doThrow(new DeveloperNotFoundException("Developer not found"))
                .when(developerService).hardDeleteById(anyInt());

        // when
        ResultActions result = mockMvc.perform(delete("/api/v1/developers/1?isHard=true")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        verify(developerService, times(1)).hardDeleteById(anyInt());
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(400)),
                        MockMvcResultMatchers.jsonPath("$.detail", CoreMatchers.is("Developer not found"))
                );
    }

}
