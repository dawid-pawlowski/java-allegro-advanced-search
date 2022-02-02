package api.allegro.service;

import api.allegro.bean.CategoryBean;
import api.allegro.bean.CategoryParamBean;
import api.allegro.bean.CategoryParamValueBean;
import api.allegro.exception.AllegroNotFoundException;
import api.allegro.exception.AllegroUnauthorizedException;
import api.allegro.resource.CategoryResource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryService {

    private final CategoryResource resource;
    private final CategoryBean rootCategory;

    // TODO: implement cache system
    private final Map<String, List<CategoryParamBean>> cache = new HashMap<>();

    public CategoryService(String pu, String accessToken) {
        resource = new CategoryResource(accessToken);
        rootCategory = new CategoryBean(resource.getMainCategoryId(), "Allegro", null, null);
    }

    private List<CategoryParamBean> getCategoryParameters(CategoryBean category) throws AllegroUnauthorizedException, IOException, InterruptedException, AllegroNotFoundException {
        // TODO: implement cache system
        List<CategoryParamBean> parameters = new ArrayList<>();
        JSONObject response = resource.getCategoryParameters(category.getId());
        JSONArray jsonArray = response.getJSONArray("parameters");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = (JSONObject) jsonArray.get(i);
            CategoryParamBean parameter = new CategoryParamBean();
            parameter.setId(o.getString("id"));
            parameter.setName(o.getString("name"));
            List<CategoryParamValueBean> values = new ArrayList<>();
            parameter.setType(o.getString("type"));

            if (o.getString("type").equals("dictionary")) {
                JSONArray dictArray = o.getJSONArray("dictionary");
                for (int j = 0; j < dictArray.length(); j++) {
                    JSONObject d = (JSONObject) dictArray.get(j);
                    CategoryParamValueBean v = new CategoryParamValueBean();
                    v.setId(d.getString("id"));
                    v.setValue(d.getString("value"));
                    values.add(v);
                }
            }

            parameter.setValues(values);
            parameters.add(parameter);
        }

        return parameters;
    }

    public CategoryBean getMainCategory() throws AllegroUnauthorizedException, IOException, InterruptedException, AllegroNotFoundException {
        rootCategory.setParameters(getCategoryParameters(rootCategory));
        return rootCategory;
    }

    public ArrayList<String> getCategoryPath(String categoryId) throws AllegroUnauthorizedException, IOException, AllegroNotFoundException, InterruptedException {
        ArrayList<String> categories = new ArrayList<>();
        JSONObject category = resource.getCategoryById(categoryId);
        if (!category.isNull("parent")) {
            JSONObject parent = new JSONObject(category.get("parent").toString());
            categories.addAll(getCategoryPath(parent.getString("id")));
        } else {
            categories.add(rootCategory.getId());
        }
        categories.add(category.getString("id"));
        return categories;
    }

    public List<CategoryBean> getCategoryList(CategoryBean root) throws AllegroUnauthorizedException, IOException, InterruptedException, AllegroNotFoundException {
        // TODO: implement cache system
        ArrayList<CategoryBean> category = new ArrayList<>();
        JSONObject response = resource.getCategoryByParentId(root.getId());
        JSONArray jsonArray = response.getJSONArray("categories");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject o = (JSONObject) jsonArray.get(i);
            CategoryBean c = new CategoryBean();
            c.setId(o.getString("id"));
            c.setName(o.getString("name"));
            if (o.isNull("parent")) {
                c.setParent(rootCategory);
            } else {
                c.setParent(root);
            }
            // load recursively all child parents
            /*if (!o.getBoolean("leaf")) {
                c.setChildren(getCategory(c));
            }*/

            // set category parameters
            c.setParameters(getCategoryParameters(c));
            category.add(c);
        }

        return category;
    }

}
