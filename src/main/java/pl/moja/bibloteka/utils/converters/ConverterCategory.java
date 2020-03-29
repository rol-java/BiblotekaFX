package pl.moja.bibloteka.utils.converters;

import pl.moja.bibloteka.database.models.Category;
import pl.moja.bibloteka.modelFX.CategoryFx;

public class ConverterCategory {

    public static CategoryFx convertToCategoryFx(Category category) {
        CategoryFx categoryFx = new CategoryFx();
        categoryFx.setId(category.getId());
        categoryFx.setName(category.getName());
        return categoryFx;
    }
}
