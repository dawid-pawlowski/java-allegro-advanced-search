package api.allegro.converter;

import jakarta.persistence.AttributeConverter;
import org.json.JSONArray;

import java.util.List;

public class JSONArrayToListConverter implements AttributeConverter<List<Object>, String> {
    @Override
    public String convertToDatabaseColumn(List<Object> strings) {
        if (strings == null) return null;
        return new JSONArray(strings).toString();
    }

    @Override
    public List<Object> convertToEntityAttribute(String s) {
        if (s == null) return null;
        return new JSONArray(s).toList();
    }
}
