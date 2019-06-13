package com.bing.service;

import com.bing.common.ServerResponse;
import com.bing.pojo.Category;

public interface ICategory {
    public ServerResponse selectByParentId(Integer categoryId);
    public ServerResponse add_category(Category category);
    public ServerResponse set_category_name(Category category);
    public ServerResponse get_deep_category(Category category);

}
