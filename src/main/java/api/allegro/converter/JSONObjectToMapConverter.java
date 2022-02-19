package api.allegro.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.json.JSONObject;

import java.util.Map;

@Converter(autoApply = true)
public class JSONObjectToMapConverter<X, Y> implements AttributeConverter<Map<X, Y>, String> {
    @Override
    public String convertToDatabaseColumn(Map<X, Y> stringObjectMap) {
        if (stringObjectMap == null) return null;
        return new JSONObject(stringObjectMap).toString();
    }

    @Override
    public Map<X, Y> convertToEntityAttribute(String s) {
        if (s == null) return null;
        return (Map<X, Y>) new JSONObject(s).toMap();
    }
}
