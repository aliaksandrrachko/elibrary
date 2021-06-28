package by.it.academy.grodno.elibrary.rest.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.it.academy.grodno.elibrary.api.constants.Routes;
import by.it.academy.grodno.elibrary.api.services.books.ISubscriptionScheduledTaskExecutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

class ScheduledTaskSubscriptionControllerTest extends ARestControllerTest{

    private MockMvc mockMvc;

    @Autowired
    private ISubscriptionScheduledTaskExecutorService scheduledTaskExecutorService;
    @Autowired
    private ScheduledTaskSubscriptionController scheduledTaskController;
    @Autowired
    private WebApplicationContext wac;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(scheduledTaskController).isNotNull();
        assertThat(scheduledTaskExecutorService).isNotNull();
    }

    @BeforeEach
    public void setup() {
        Mockito.reset(scheduledTaskExecutorService);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void callControllerMethods() throws Exception {
        mockMvc.perform(post(Routes.ScheduledTask.SCHEDULED_TASK_DELETE_COMPLETED_MONT_AGO_SUBSCRIPTIONS))
                .andExpect(status().isOk());
        verify(scheduledTaskExecutorService, times(1))
                .findAllCompletedMonthAgoSubscriptionsAndDeleteIt();

        mockMvc.perform(post(Routes.ScheduledTask.SCHEDULED_TASK_UNDO_EXPIRED_SUBSCRIPTIONS))
                .andExpect(status().isOk());
        verify(scheduledTaskExecutorService, times(1))
                .findAllExpiredBookingSubscriptionAndUndoBookingAndBeforeSendEmailToUser();

        mockMvc.perform(post(Routes.ScheduledTask.SCHEDULED_TASK_WARN_ABOUT_EXPIRATION_PERIOD))
                .andExpect(status().isOk());
        verify(scheduledTaskExecutorService, times(1))
                .findAllExpiredSubscriptionsAndSendEmailToUserAndAdmin();
    }
}
