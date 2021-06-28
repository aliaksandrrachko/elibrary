package by.it.academy.grodno.elibrary.rest.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.it.academy.grodno.elibrary.api.Role;
import by.it.academy.grodno.elibrary.api.constants.Routes;
import by.it.academy.grodno.elibrary.api.dto.books.BookDetailsDto;
import by.it.academy.grodno.elibrary.api.dto.books.ReviewDto;
import by.it.academy.grodno.elibrary.api.services.books.IReviewService;
import by.it.academy.grodno.elibrary.rest.utils.EntityJsonConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ReviewRestControllerTest extends ARestControllerTest {

    @Autowired
    private IReviewService reviewService;
    @Autowired
    private ReviewRestController reviewRestController;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private EntityJsonConverter jsonConverter;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(reviewRestController).isNotNull();
        assertThat(reviewService).isNotNull();
    }

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        Mockito.reset(reviewService);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void findAll() throws Exception {
        final Page<ReviewDto> reviewDtoPage = getTestPageReviewDto();
        when(reviewService.findAll(any())).thenReturn(reviewDtoPage);
       mockMvc.perform(get(Routes.Review.REVIEWS))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content[0].id", is(1)))
               .andExpect(jsonPath("$.content[0].username", is(reviewDtoPage.getContent().get(0).getUsername())))
               .andReturn().getResponse();
        verify(reviewService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void findById() throws Exception {
        final ReviewDto reviewDto = getTestReviewDto();
        when(reviewService.findById(any())).thenReturn(reviewDto);
        mockMvc.perform(get(Routes.Review.REVIEWS_ID, reviewDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("username")))
                .andExpect(jsonPath("$.id", is(1)))
                .andReturn().getResponse();
        verify(reviewService, times(1)).findById(any());
    }

    @Test
    void createReview() throws Exception {
        final ReviewDto reviewDto = getTestReviewDtoForCreateOrUpdate();
        when(reviewService.create(any(ReviewDto.class))).thenReturn(getTestReviewDto());
        MockHttpServletResponse response = mockMvc.perform(post(Routes.Review.REVIEWS)
                .contentType(APPLICATION_JSON)
                .content(jsonConverter.getObjectJsonString(reviewDto))
                .principal(getTestPrincipal()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.bookId", is(15)))
                .andExpect(jsonPath("$.text", is("Review text")))
                .andExpect(jsonPath("$.grade", is(1)))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(reviewService, times(1)).create(any(ReviewDto.class));
    }

    @Test
    void updateReview() throws Exception {
        final ReviewDto reviewDtoForUpdate = getTestReviewDtoForCreateOrUpdate();
        when(reviewService.update(any(Long.class), any(ReviewDto.class), any(Long.class))).thenReturn(reviewDtoForUpdate);
        MockHttpServletResponse response = mockMvc.perform(put(Routes.Review.REVIEWS_ID, 1)
                .contentType(APPLICATION_JSON)
                .content(jsonConverter.getObjectJsonString(reviewDtoForUpdate))
                .principal(getTestPrincipal()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.bookId", is(15)))
                .andExpect(jsonPath("$.text", is("Some text")))
                .andExpect(jsonPath("$.grade", is(5)))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(reviewService, times(1)).update(any(Long.class), any(ReviewDto.class), any(Long.class));
    }

    @Test
    void deleteReview() throws Exception {
        final Long reviewId = 1L;
        mockMvc.perform(delete(Routes.Review.REVIEWS_ID, reviewId)
                .principal(getTestPrincipal()))
                .andExpect(status().isOk());
        verify(reviewService, times(1)).delete(any(Long.class), any(Long.class));
    }

    private ReviewDto getTestReviewDtoForCreateOrUpdate(){
        return ReviewDto.builder().userId(1L).bookId(15L).text("Some text").grade((byte) 5).build();
    }

    private ReviewDto getTestReviewDto() {
        return ReviewDto.builder()
                .bookDetails(
                        new BookDetailsDto("Books title",
                                Collections.singletonList("The Strange Author"),
                                LocalDate.now(), "picture"))
                .bookId(15L)
                .grade((byte) 1).id(1L).text("Review text")
                .updated(LocalDateTime.now().withNano(0))
                .created(LocalDateTime.now().withNano(0))
                .userEmail("user@mail.ru")
                .userId(1L)
                .username("username")
                .userPictureUrl(null)
                .build();
    }

    private Page<ReviewDto> getTestPageReviewDto(){
        return new PageImpl<>(Collections.singletonList(getTestReviewDto()));
    }

    private Authentication getTestPrincipal(){
        return new UsernamePasswordAuthenticationToken("1", null, getTestFullRoleGrantedAuthority());
    }

    private Collection<? extends GrantedAuthority> getTestFullRoleGrantedAuthority(){
        return Stream.of(Role.ROLE_ADMIN.name(), Role.ROLE_USER.name())
                .map(s -> (GrantedAuthority) () -> s).collect(Collectors.toSet());
    }
}
