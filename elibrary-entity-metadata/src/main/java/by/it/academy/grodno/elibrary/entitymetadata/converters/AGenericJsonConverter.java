package by.it.academy.grodno.elibrary.entitymetadata.converters;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;

@Slf4j
public abstract class AGenericJsonConverter<X> implements AttributeConverter<X, String> {

    protected static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private final Class<X> clazz;

    protected AGenericJsonConverter(Class<X> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String convertToDatabaseColumn(X attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("Exception converting to database column", e);
            return null;
        }
    }

    @Override
    public X convertToEntityAttribute(String dbData) {
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
