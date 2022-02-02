package api.allegro.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.json.JSONObject;

@Converter(autoApply = true)
public class JSONObjectConverter implements AttributeConverter<JSONObject, String> {
    @Override
    public String convertToDatabaseColumn(JSONObject jsonObject) {
        if (jsonObject != null) {
            return jsonObject.toString();
        }

        return null;
    }

    @Override
    public JSONObject convertToEntityAttribute(String s) {
        return new JSONObject(s);
    }
}
