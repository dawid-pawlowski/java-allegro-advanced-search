package api.allegro.bean;


import java.io.Serializable;
import java.util.List;

public class CategoryParamBean implements Serializable {
    private String id;
    private String name;
    // TODO: use enum instead for "type" property (?)
    private String type;
    private List<CategoryParamValueBean> values;

    public CategoryParamBean() {
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

    public List<CategoryParamValueBean> getValues() {
        return values;
    }

    public void setValues(List<CategoryParamValueBean> values) {
        this.values = values;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }
}
