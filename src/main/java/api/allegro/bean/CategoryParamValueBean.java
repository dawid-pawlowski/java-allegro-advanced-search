package api.allegro.bean;

import java.io.Serializable;

public class CategoryParamValueBean implements Serializable {
    private String id;
    private String value;

    public CategoryParamValueBean() {
    }

    public CategoryParamValueBean(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
