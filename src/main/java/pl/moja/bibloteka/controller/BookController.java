package pl.moja.bibloteka.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import pl.moja.bibloteka.modelFX.AuthorFx;
import pl.moja.bibloteka.modelFX.BookModel;
import pl.moja.bibloteka.modelFX.CategoryFx;
import pl.moja.bibloteka.utils.DialogsUtils;
import pl.moja.bibloteka.utils.exceptions.ApplicationException;

public class BookController {

    @FXML
    public Button addButton;
    @FXML
    private ComboBox<CategoryFx> categoryComboBox;

    @FXML
    private ComboBox<AuthorFx> authorComboBox;

    @FXML
    private TextArea descTextArea;

    @FXML
    private Slider ratingSlider;

    @FXML
    private TextField isbnTextField;

    @FXML
    private DatePicker releaseDatePicker;

    @FXML
    private TextField titleTextField;

    @FXML
    private BookModel bookModel;

    // bindujemy nasze elementy z propertkami
    @FXML
    public void initialize() {

       this.bookModel = new BookModel();
        try {
            this.bookModel.init();
        } catch (ApplicationException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
        bindings();
        validation();
    }

    private void validation() {
        this.addButton.disableProperty().bind(this.authorComboBox.valueProperty().isNull()
        .or(this.categoryComboBox.valueProperty().isNull())
        .or(this.titleTextField.textProperty().isEmpty())
        .or(this.descTextArea.textProperty().isEmpty())
        .or(this.isbnTextField.textProperty().isEmpty())
        .or(this.releaseDatePicker.valueProperty().isNull())
        );
    }

    public void bindings() {
        this.authorComboBox.setItems(this.bookModel.getAuthorFxObservableList());
        this.categoryComboBox.setItems(this.bookModel.getCategoryFxObservableList());
        // dwustronne bindowanie przykład - zmieniamy wszystko zeby w formatce z przyciskiem edycja zaczytaly sie na rekordy
        this.authorComboBox.valueProperty().bindBidirectional(this.bookModel.getBookFxObjectProperty().authorFxProperty());
        this.categoryComboBox.valueProperty().bindBidirectional(this.bookModel.getBookFxObjectProperty().categoryFxProperty());
        this.titleTextField.textProperty().bindBidirectional(this.bookModel.getBookFxObjectProperty().titleProperty());
        this.descTextArea.textProperty().bindBidirectional(this.bookModel.getBookFxObjectProperty().descriptionProperty());
        this.ratingSlider.valueProperty().bindBidirectional(this.bookModel.getBookFxObjectProperty().ratingProperty());
        this.isbnTextField.textProperty().bindBidirectional(this.bookModel.getBookFxObjectProperty().isbnProperty());
        this.releaseDatePicker.valueProperty().bindBidirectional(this.bookModel.getBookFxObjectProperty().releaseDateProperty());

        // wczesniej przed obustronnym bindowaniem było tak:
        //this.bookModel.getBookFxObjectProperty().categoryFxProperty().bind(this.categoryComboBox.valueProperty());
        //this.bookModel.getBookFxObjectProperty().authorFxProperty().bind(this.authorComboBox.valueProperty());
        //this.bookModel.getBookFxObjectProperty().titleProperty().bind(this.titleTextField.textProperty());
        //this.bookModel.getBookFxObjectProperty().descriptionProperty().bind(this.descTextArea.textProperty());
        //this.bookModel.getBookFxObjectProperty().ratingProperty().bind(ratingSlider.valueProperty());
        //this.bookModel.getBookFxObjectProperty().isbnProperty().bind(this.isbnTextField.textProperty());
        //this.bookModel.getBookFxObjectProperty().releaseDateProperty().bind(this.releaseDatePicker.valueProperty());
    }

    public void addBookOnAction() {

        try {
            this.bookModel.saveBookInDataBase();
            clearFields();
        } catch (ApplicationException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
    }

    private void clearFields() {
        this.authorComboBox.getSelectionModel().clearSelection();
        this.categoryComboBox.getSelectionModel().clearSelection();
        this.titleTextField.clear();
        this.descTextArea.clear();
        this.ratingSlider.setValue(1);
        this.isbnTextField.clear();
        this.releaseDatePicker.getEditor().clear();
    }

    public BookModel getBookModel() {
        return bookModel;
    }
}
