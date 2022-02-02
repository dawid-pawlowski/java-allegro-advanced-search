package api.allegro.bean;


import java.util.List;

public class CategoryBean {

    public String id;
    public String name;
    public CategoryBean parent;
    public List<CategoryParamBean> parameters;

    public CategoryBean() {
    }

    public CategoryBean(String id, String name, CategoryBean parent, List<CategoryParamBean> parameters) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.parameters = parameters;
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

    public CategoryBean getParent() {
        return parent;
    }

    public void setParent(CategoryBean parent) {
        this.parent = parent;
    }

    public List<CategoryParamBean> getParameters() {
        return parameters;
    }

    public void setParameters(List<CategoryParamBean> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return name;
    }
}
