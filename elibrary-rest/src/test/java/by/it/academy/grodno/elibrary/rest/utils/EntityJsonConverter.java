package by.it.academy.grodno.elibrary.rest.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class EntityJsonConverter {

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T jsonStringToObject(String jsonString, Class<T> clazz){
        if (StringUtils.hasText(jsonString) && clazz != null){
            try {
                return objectMapper.readValue(jsonString, clazz);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getObjectJsonString(Object entity){
        try {
            return objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
