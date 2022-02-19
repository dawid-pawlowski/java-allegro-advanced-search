package api.allegro.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.json.JSONArray;

import java.util.List;

@Converter(autoApply = true)
public class JSONArrayToListConverter<T> implements AttributeConverter<List<T>, String> {
    @Override
    public String convertToDatabaseColumn(List<T> strings) {
        if (strings == null) return null;
        return new JSONArray(strings).toString();
    }

    @Override
    public List<T> convertToEntityAttribute(String s) {
        if (s == null) return null;
        return (List<T>) new JSONArray(s).toList();
    }
}
