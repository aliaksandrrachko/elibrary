CREATE FUNCTION f_address_update_last_updated() RETURNS TRIGGER AS $x$
BEGIN
    NEW.last_updated=NOW();
RETURN NEW;
END; $x$
    LANGUAGE plpgsql;

CREATE
    TRIGGER p_address_update
    BEFORE UPDATE
    ON address
    FOR EACH ROW
    EXECUTE PROCEDURE f_address_update_last_updated();

CREATE
    TRIGGER p_address_insert
    BEFORE INSERT
    ON address
    FOR EACH ROW
    EXECUTE PROCEDURE f_address_update_last_updated();

CREATE FUNCTION f_book_update_book_updated() RETURNS TRIGGER AS $x$
BEGIN
    NEW.book_updated=NOW();
RETURN NEW;
END; $x$
    LANGUAGE plpgsql;

CREATE
    TRIGGER p_book_update
    BEFORE UPDATE
    ON book
    FOR EACH ROW
    EXECUTE PROCEDURE f_book_update_book_updated();

CREATE FUNCTION f_review_update_review_updated() RETURNS TRIGGER AS $x$
BEGIN
    NEW.review_updated=NOW();
RETURN NEW;
END; $x$
    LANGUAGE plpgsql;

CREATE
    TRIGGER p_review_update
    BEFORE UPDATE
    ON review
    FOR EACH ROW
    EXECUTE PROCEDURE f_review_update_review_updated();