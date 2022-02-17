package api.allegro.converter;

import jakarta.persistence.AttributeConverter;
import org.json.JSONObject;

import java.util.Map;

public class JSONObjectToMapConverter implements AttributeConverter<Map<String, Object>, String> {
    @Override
    public String convertToDatabaseColumn(Map<String, Object> stringObjectMap) {
        if (stringObjectMap == null) return null;
        return new JSONObject(stringObjectMap).toString();
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String s) {
        if (s == null) return null;
        return new JSONObject(s).toMap();
    }
}
