package api.allegro.entity;

import api.allegro.converter.JSONArrayToListConverter;
import api.allegro.converter.JSONObjectToMapConverter;
import jakarta.persistence.*;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "offer")
public class OfferEntity {
    @Id
    public String id;
    public String name;
    @Convert(converter = JSONArrayToListConverter.class)
    public List<? extends String> categories;
    @Convert(converter = JSONObjectToMapConverter.class)
    public Map<String, List<String>> parameters;

    @Column(name = "param_backup")
    public String paramBackup;

    public OfferEntity() {
    }

    public String getParamBackup() {
        return paramBackup;
    }

    public void setParamBackup(String paramBackup) {
        this.paramBackup = paramBackup;
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

    public List<? extends String> getCategories() {
        return categories;
    }

    public void setCategories(List<? extends String> categories) {
        this.categories = categories;
    }

    public Map<String, List<String>> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, List<String>> parameters) {
        this.parameters = parameters;
    }
}
