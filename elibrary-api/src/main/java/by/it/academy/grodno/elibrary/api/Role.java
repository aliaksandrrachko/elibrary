package by.it.academy.grodno.elibrary.api;

public enum Role {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_USER_FACEBOOK,
    ROLE_USER_GITHUB,
    ROLE_USER_GOOGLE;

    private static final String ROLE_PREFIX = "ROLE_";

    public String getShortRole(){
        return this.name().replace(ROLE_PREFIX, "");
    }
}
