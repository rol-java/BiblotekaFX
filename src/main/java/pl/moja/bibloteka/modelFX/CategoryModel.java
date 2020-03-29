package pl.moja.bibloteka.modelFX;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import pl.moja.bibloteka.database.dao.BookDao;
import pl.moja.bibloteka.database.dao.CategoryDao;
import pl.moja.bibloteka.database.models.Book;
import pl.moja.bibloteka.database.models.Category;
import pl.moja.bibloteka.utils.converters.ConverterCategory;
import pl.moja.bibloteka.utils.exceptions.ApplicationException;

import java.sql.SQLException;
import java.util.List;

public class CategoryModel {

    // ta klasa bedzie zajmowac sie obsługą logiki biznesoawj - widoku
    // warstwa pośrednia oddzielająca baze i aplikacje

    // obsługa ComboBoxa
    private ObservableList<CategoryFx> categoryList = FXCollections.observableArrayList(); // ubsługa
    private ObjectProperty<CategoryFx> category = new SimpleObjectProperty<>(); // przytrzymanie elementu w comboboksie
    private TreeItem<String> root = new TreeItem<>();

    // wypełnieniamy elementami z bazy danych w comboboksie
    public void init() throws ApplicationException {
        CategoryDao categoryDao = new CategoryDao();
        List<Category> categories = categoryDao.queryForAll(Category.class);
        // żeby nie podwajały sie nam elementy w comboboksie po init()
        initCategoryList(categories);
        initRoot(categories);
    }

    private void initRoot(List<Category> categories) {
        this.root.getChildren().clear();
        categories.forEach(c->{
            TreeItem<String> categoryItem = new TreeItem<>(c.getName());
            c.getBooks().forEach(b->{
                categoryItem.getChildren().add(new TreeItem<>(b.getTitle()));
            });
            root.getChildren().add(categoryItem);
        });
    }

    private void initCategoryList(List<Category> categories) {
        this.categoryList.clear();
        // pobieramy liste z CategoryFX
        categories.forEach(c->{
            CategoryFx categoryFX = ConverterCategory.convertToCategoryFx(c);
            // dodajemy do listy metodą add
            this.categoryList.add(categoryFX);
        });
    }

    // do obsłygi usuwania comboboksa
    public void deleteCategoryById() throws ApplicationException, SQLException {
        CategoryDao categoryDao = new CategoryDao();
        categoryDao.deleteById(Category.class, category.getValue().getId());
        BookDao bookDao = new BookDao();
        bookDao.deleteByColumnName(Book.CATEGORY_ID, category.getValue().getId());
        init();
    }

    public void saveCategoryInDataBase(String name) throws ApplicationException {
        CategoryDao categoryDao = new CategoryDao();
        Category category = new Category();
        category.setName(name);
        categoryDao.createOrUpdate(category);
        // odświerzenie comboboksa po dodaniu nowych elementów, init() połączy sie jeszcze raz do bazy i zaczyta dane
        init();
    }
    public void updateCategoryInDataBase() throws ApplicationException {
        CategoryDao categoryDao = new CategoryDao();
        Category tempCategory = categoryDao.findById(Category.class, getCategory().getId());
        tempCategory.setName(getCategory().getName());
        categoryDao.createOrUpdate(tempCategory);
        init();
    }

    public ObservableList<CategoryFx> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ObservableList<CategoryFx> categoryList) {
        this.categoryList = categoryList;
    }

    public CategoryFx getCategory() {
        return category.get();
    }

    public ObjectProperty<CategoryFx> categoryProperty() {
        return category;
    }

    public void setCategory(CategoryFx category) {
        this.category.set(category);
    }

    public TreeItem<String> getRoot() { return root; }

    public void setRoot(TreeItem<String> root) { this.root = root; }
}
