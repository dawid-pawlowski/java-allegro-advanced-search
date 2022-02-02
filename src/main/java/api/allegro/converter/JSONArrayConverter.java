package api.allegro.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.json.JSONArray;

@Converter(autoApply = true)
public class JSONArrayConverter implements AttributeConverter<JSONArray, String> {
    @Override
    public String convertToDatabaseColumn(JSONArray jsonArray) {
        if (jsonArray != null) {
            return jsonArray.toString();
        }

        return null;
    }

    @Override
    public JSONArray convertToEntityAttribute(String s) {
        return new JSONArray(s);
    }
}
