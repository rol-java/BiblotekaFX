package pl.moja.bibloteka.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import pl.moja.bibloteka.modelFX.CategoryFx;
import pl.moja.bibloteka.modelFX.CategoryModel;
import pl.moja.bibloteka.utils.DialogsUtils;
import pl.moja.bibloteka.utils.exceptions.ApplicationException;

import java.sql.SQLException;

public class CategoryController {

    @FXML
    private Button addCategoryButton;

    @FXML
    private Button deleteCategoryButton;

    @FXML
    private Button editCategoryButton;

    @FXML
    private TextField categoryTextField;

    @FXML
    private ComboBox<CategoryFx> categoryComboBox;

    @FXML
    private TreeView<String> categoryTreeView;

    private CategoryModel categoryModel;

    @FXML
    public void initialize() {
        this.categoryModel = new CategoryModel();  //tworzy instancje naszego modelu, po konstruktorze inicjalizujemy
        try {
            this.categoryModel.init();
        } catch (ApplicationException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
        this.categoryComboBox.setItems(this.categoryModel.getCategoryList());
        this.categoryTreeView.setRoot(this.categoryModel.getRoot());
        initBindings();
    }

    private void initBindings() {
        // dodatkowo ten zapis usuwa problem przy usuwaniu pustej listy w comboboksie czyli wyszarzamy jeśli nic nie wybrane
        this.addCategoryButton.disableProperty().bind(categoryTextField.textProperty().isEmpty()); // pole jest puste to button wygaszony
        this.deleteCategoryButton.disableProperty().bind(this.categoryModel.categoryProperty().isNull());
        this.editCategoryButton.disableProperty().bind(this.categoryModel.categoryProperty().isNull());
    }

    public void addCategoryOnAction(){

        try {
            categoryModel.saveCategoryInDataBase(categoryTextField.getText());
        } catch (ApplicationException e) {
           DialogsUtils.errorDialog(e.getMessage());
        }
        categoryTextField.clear(); // czyszczenie pola tekstowego

    }

    public void onActionDeleteButton() {
        try {
            this.categoryModel.deleteCategoryById();
        } catch (ApplicationException | SQLException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
    }

    public void onActionComboBox() {
        // dobieramy sie do wybranego aktualnie elementu z comboboxa zeby możnabyło go usunąć
        this.categoryModel.setCategory(this.categoryComboBox.getSelectionModel().getSelectedItem());
    }

    public void onActionEditCategory() {

        // nowa zmienna przechowująca nową nazwe do popapu (obsługa comboboxa)
        String newCategoryName = DialogsUtils.editDialog(this.categoryModel.getCategory().getName());
        if (newCategoryName != null) {
            this.categoryModel.getCategory().setName(newCategoryName);
            try {
                this.categoryModel.updateCategoryInDataBase();
            } catch (ApplicationException e) {
                DialogsUtils.errorDialog(e.getMessage());
            }
        }
        }
}

