# Trigger on update user
CREATE
    TRIGGER p_user_update
    BEFORE UPDATE
    ON user
    FOR EACH ROW
    SET NEW.user_updated = NOW();

# Trigger by insert or update address
CREATE
    TRIGGER p_address_update
    BEFORE UPDATE
    ON address
    FOR EACH ROW
    SET NEW.last_updated = NOW();

# Trigger by insert or insert address
CREATE
    TRIGGER p_address_insert
    BEFORE INSERT
    ON address
    FOR EACH ROW
    SET NEW.last_updated = NOW();

# Trigger on update user
CREATE
    TRIGGER p_book_update
    BEFORE UPDATE
    ON book
    FOR EACH ROW
    SET NEW.book_updated = NOW();

# Trigger on update review
CREATE
    TRIGGER p_review_update
    BEFORE UPDATE
    ON review
    FOR EACH ROW
    SET NEW.review_updated = NOW();