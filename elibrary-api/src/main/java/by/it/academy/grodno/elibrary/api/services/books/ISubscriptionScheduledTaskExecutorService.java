package by.it.academy.grodno.elibrary.api.services.books;

public interface ISubscriptionScheduledTaskExecutorService {

    void findAllExpiredSubscriptionsAndSendEmailToUserAndAdmin();
    void findAllExpiredBookingSubscriptionAndUndoBookingAndBeforeSendEmailToUser();
}
