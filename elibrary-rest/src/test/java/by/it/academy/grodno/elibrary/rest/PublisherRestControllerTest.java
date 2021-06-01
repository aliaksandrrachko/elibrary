package by.it.academy.grodno.elibrary.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import by.it.academy.grodno.elibrary.api.dto.books.PublisherDto;
import by.it.academy.grodno.elibrary.api.services.books.IPublisherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

class PublisherRestControllerTest {

    private MockMvc mockMvc;
    @Mock
    private IPublisherService publisherService;
    @InjectMocks
    private PublisherRestController publisherRestController;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(publisherRestController).isNotNull();
        assertThat(publisherService).isNotNull();
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(publisherRestController).build();
    }
/*

    @Test
    void findPublisherById() throws Exception {
        final PublisherDto publisherDto = getTestsPublisherDto();
        final int publisherId = 15;
        when(publisherService.findById(any())).thenReturn(publisherDto);
        MockHttpServletResponse response = mockMvc.perform(get("/rest/publishers/" + publisherId)
                .accept(APPLICATION_JSON))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        //assertThat(response.getContentAsString()).isEqualTo(jsonPublisherDto.write(publisherDto).getJson());
        verify(publisherService, times(1)).findById(any(Integer.class));
    }

    @Test
    void updatePublisher() throws Exception {
        final PublisherDto publisherDtoForUpdate = new PublisherDto("AST");
        final int publisherId = 23;
        when(publisherService.update(any(Integer.class), any(PublisherDto.class))).thenReturn(getTestsPublisherDto());
        MockHttpServletResponse response = mockMvc.perform(put("/rest/publishers/" + publisherId)
                        .contentType(APPLICATION_JSON)
                        .content("")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        //assertThat(response.getContentAsString()).isEqualTo(jsonPublisherDto.write(getTestsPublisherDto()).getJson());
        verify(publisherService, times(1)).update(any(Integer.class), any(PublisherDto.class));
    }

    @Test
    void createPublisher() throws Exception {
        final PublisherDto publisherForCreating = new PublisherDto("TEXT");
        when(publisherService.create(any(PublisherDto.class))).thenAnswer((Answer<PublisherDto>) invocation -> {
            PublisherDto publisherDto = invocation.getArgument(0);
            publisherDto.setId((int) (Math.random() * 100));
            return publisherDto;
        });
        MockHttpServletResponse response = mockMvc.perform(post("/rest/publishers")
        .contentType(APPLICATION_JSON)
        .content(""
                //jsonPublisherDto.write(publisherForCreating).getJson()
        )).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotNull();
        verify(publisherService, times(1)).create(any(PublisherDto.class));
    }

    @Test
    void findAllPublisher() throws Exception {
        final List<PublisherDto> testPublisherDtoList = getTestPublisherDtoList();
        when(publisherService.findAll()).thenReturn(testPublisherDtoList);
        MockHttpServletResponse response = mockMvc.perform(get("/rest/publishers")
                .accept(APPLICATION_JSON))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotNull();
        verify(publisherService, times(1)).findAll();
    }
*/

    private PublisherDto getTestsPublisherDto() {
        PublisherDto publisherDto = new PublisherDto("AST");
        publisherDto.setId(15);
        return publisherDto;
    }

    private List<PublisherDto> getTestPublisherDtoList() {
        List<PublisherDto> testPublisherDtoList = new ArrayList<>();
        testPublisherDtoList.add(new PublisherDto("TEXT"));
        testPublisherDtoList.add(new PublisherDto("AST"));
        testPublisherDtoList.add(new PublisherDto("SUNRISE"));
        return testPublisherDtoList;
    }

}
