package api.allegro.entity;

import api.allegro.converter.JSONArrayConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.json.JSONArray;

@Entity
@Table(name = "offer")
public class OfferEntity {
    @Id
    public String id;
    public String name;
    @Convert(converter = JSONArrayConverter.class)
    public JSONArray categories;
    @Convert(converter = JSONArrayConverter.class)
    public JSONArray parameters;
    public String category;

    public OfferEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONArray getCategories() {
        return categories;
    }

    public void setCategories(JSONArray categories) {
        this.categories = categories;
    }

    public JSONArray getParameters() {
        return parameters;
    }

    public void setParameters(JSONArray parameters) {
        this.parameters = parameters;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
