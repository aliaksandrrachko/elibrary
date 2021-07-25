package by.it.academy.grodno.elibrary.api.constants;

/**
 * Class containing routes Rest API constants
 */
public final class Routes {
    private Routes(){}

    private static final String REST = "/rest";
    private static final String ID = "/{id}";
    private static final String ANY = "/**";
    private static final String ADMIN = "/admin";

    public static final String REST_ADMIN = REST + ADMIN;

    public static final class Author {
        private Author(){}
        public static final String AUTHORS = REST + "/authors";
        public static final String AUTHORS_ID = AUTHORS + ID;
        public static final String AUTHORS_ANY = AUTHORS + ANY;
        public static final String AUTHORS_HAS_THE_MOST_BOOK = AUTHORS + "/whohasmostbook";

        public static final String ADMIN_AUTHORS = REST + ADMIN + "/authors";
        public static final String ADMIN_AUTHORS_ID = ADMIN_AUTHORS + ID;
    }

    public static final class Category {
        private Category(){}
        public static final String CATEGORIES = REST + "/categories";
        public static final String CATEGORIES_ID = CATEGORIES + ID;
        public static final String CATEGORIES_ANY = CATEGORIES + ANY;

        public static final String ADMIN_CATEGORIES = REST + ADMIN + "/categories";
        public static final String ADMIN_CATEGORIES_ID = ADMIN_CATEGORIES + ID;
    }

    public static final class Publisher {
        private Publisher(){}
        public static final String PUBLISHERS = REST + "/publishers";
        public static final String PUBLISHERS_ID = PUBLISHERS + ID;
        public static final String PUBLISHERS_ANY = PUBLISHERS + ANY;
        public static final String PUBLISHERS_NAME_LIKE = PUBLISHERS + "/{publisherName}";

        public static final String ADMIN_PUBLISHERS = REST + ADMIN + "/publishers";
        public static final String ADMIN_PUBLISHERS_ID = ADMIN_PUBLISHERS + ID;
    }

    public static final class Book {
        private Book(){}
        private static final String ISBN = "/isbn/{isbn}";

        public static final String BOOKS = REST + "/books";
        public static final String BOOKS_ID = BOOKS + ID;
        public static final String BOOKS_ANY = BOOKS + ANY;
        public static final String BOOKS_ISBN = BOOKS + ISBN;
        public static final String BOOKS_LANGUAGE = BOOKS + "/language/{language}";

        public static final String ADMIN_BOOKS = REST + ADMIN + "/books";
        public static final String ADMIN_BOOKS_ID = ADMIN_BOOKS + ID;
    }

    public static final class Review {
        private Review() {}

        public static final String REVIEWS = REST + "/reviews";
        public static final String REVIEWS_ID = REVIEWS + ID;
        public static final String REVIEWS_ID_AVERAGE_GRADE = REVIEWS_ID + "/averagegrade";
        public static final String REVIEWS_ANY = REVIEWS + ANY;
    }

    public static final class ScheduledTask{
        private ScheduledTask() { }

        public static final String SCHEDULED_TASK = REST + "/scheduledtask";
        public static final String SCHEDULED_TASK_UNDO_EXPIRED_SUBSCRIPTIONS = SCHEDULED_TASK + "/undoExpiredSubscriptions";
        public static final String SCHEDULED_TASK_WARN_ABOUT_EXPIRATION_PERIOD = SCHEDULED_TASK + "/warnAboutExpirationPeriod";
        public static final String SCHEDULED_TASK_DELETE_COMPLETED_MONT_AGO_SUBSCRIPTIONS = SCHEDULED_TASK + "/deleteCompletedMontAgoSubscriptions";
        public static final String SCHEDULED_TASK_ANY = SCHEDULED_TASK + ANY;
    }

    public static final class Subscription{
        private Subscription() {}
        private static final String STATUS = "/status/{status}";

        public static final String SUBSCRIPTIONS = REST + "/subscriptions";
        public static final String SUBSCRIPTIONS_ID = SUBSCRIPTIONS + ID;
        public static final String SUBSCRIPTIONS_STATUS = SUBSCRIPTIONS + STATUS;
        public static final String SUBSCRIPTIONS_ANY = SUBSCRIPTIONS + ANY;

        public static final String ADMIN_SUBSCRIPTIONS = REST + ADMIN +"/subscriptions";
        public static final String ADMIN_SUBSCRIPTIONS_ID = ADMIN_SUBSCRIPTIONS + ID;
    }

    public static final class User {
        private User(){}

        public static final String USER_PROFILE = REST + "/profile";
        public static final String ADMIN_USERS = REST + "/users";
        public static final String USERS_PUBLIC_PROFILE = REST + "/users/public";
        public static final String ADMIN_USERS_HAS_BIRTHDAY = ADMIN_USERS + "/havebirthday";
        public static final String ADMIN_USERS_HAS_LONGER_TERM = ADMIN_USERS + "/havelongerterm";
        public static final String ADMIN_USERS_ID = ADMIN_USERS + ID;
        public static final String ADMIN_USERS_EMAIL = ADMIN_USERS + "/email/{email}";
    }

    public static final class Auth {
        private Auth(){}

        public static final String LOGIN = REST + "/login";
        public static final String SIGN_UP = REST + "/signup";
    }
}
