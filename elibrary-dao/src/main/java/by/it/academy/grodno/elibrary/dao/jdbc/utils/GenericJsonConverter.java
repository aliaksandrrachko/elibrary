package by.it.academy.grodno.elibrary.dao.jdbc.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class GenericJsonConverter {

    private GenericJsonConverter(){}

    protected static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static  <T> String convertToDatabaseColumn(T entity) {
        if (entity == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            log.error("Exception converting to database column", e);
            return null;
        }
    }

    public static <T> T convertToEntity(String dbData, Class<T> clazz) {
        try {
            if (!StringUtils.hasText(dbData)) {
                return null;
            }
            return OBJECT_MAPPER.readValue(dbData, clazz);
        } catch (Exception e) {
            log.error("Exception converting to entity attribute", e);
            return null;
        }
    }
}
