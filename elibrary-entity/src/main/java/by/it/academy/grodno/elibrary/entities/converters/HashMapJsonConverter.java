package by.it.academy.grodno.elibrary.entities.converters;

import com.fasterxml.jackson.databind.type.CollectionType;

import javax.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

@Converter
public class HashMapJsonConverter extends AGenericJsonConverter<Map<String, Object>>{

//    private static CollectionType collectionType = OBJECT_MAPPER.getTypeFactory()
//            .constructCollectionType(Map.class, Object.class);

    private static Map<String, Object> MAP_INSTANCE = new HashMap<>();

    @SuppressWarnings("unchecked")
    private static final Class<Map<String, Object>> PARAMETERIZED_CLASS =
         (Class<Map<String, Object>>) MAP_INSTANCE.getClass();

    protected HashMapJsonConverter() {
        super(PARAMETERIZED_CLASS);
    }
}
