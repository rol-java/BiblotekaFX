package pl.moja.bibloteka.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.moja.bibloteka.modelFX.AuthorFx;
import pl.moja.bibloteka.modelFX.BookFx;
import pl.moja.bibloteka.modelFX.CategoryFx;
import pl.moja.bibloteka.modelFX.ListBooksModel;
import pl.moja.bibloteka.utils.DialogsUtils;
import pl.moja.bibloteka.utils.FxmlUtils;
import pl.moja.bibloteka.utils.exceptions.ApplicationException;

import java.io.IOException;
import java.time.LocalDate;

public class ListBooksController {

    @FXML
    private ComboBox categoryComboBox;

    @FXML
    private ComboBox authorComboBox;

    @FXML
    private TableView<BookFx> booksTableView;

    @FXML
    private TableColumn<BookFx, String> titleColumn;

    @FXML
    private TableColumn<BookFx, String> descColumn;

    @FXML
    private TableColumn<BookFx, AuthorFx> authorColumn;

    @FXML
    private TableColumn<BookFx, CategoryFx> categoryColumn;

    @FXML
    private TableColumn<BookFx, Number> ratingColumn;

    @FXML
    private TableColumn<BookFx, String> isbnColumn;

    @FXML
    private TableColumn<BookFx, LocalDate> releaseColumn;

    @FXML
    private TableColumn<BookFx, BookFx> deleteColumn;

    @FXML
    private TableColumn<BookFx, BookFx> editColumn;

    private ListBooksModel listBooksModel;

    @FXML
    void initialize() {
        //bindujemy elementy FXML z BookFx
        this.listBooksModel = new ListBooksModel();
        try {
            this.listBooksModel.init();
        } catch (ApplicationException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }

        // odwołanie do filtru comboboxu
        this.categoryComboBox.setItems(this.listBooksModel.getCategoryFxObservableList());
        this.authorComboBox.setItems(this.listBooksModel.getAuthorFxObservableList());
        // powiązenie z comboboxem - filtr
        this.listBooksModel.categoryFxObjectPropertyProperty().bind(this.categoryComboBox.valueProperty());
        this.listBooksModel.authorFxObjectPropertyProperty().bind(this.authorComboBox.valueProperty());

        this.booksTableView.setItems(this.listBooksModel.getBookFxObservableList());
        this.titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        this.descColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        this.ratingColumn.setCellValueFactory(cellData -> cellData.getValue().ratingProperty());
        this.isbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
        this.releaseColumn.setCellValueFactory(cellData -> cellData.getValue().releaseDateProperty());
        this.authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorFxProperty());
        this.categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryFxProperty());
        this.deleteColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        this.editColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));

        this.deleteColumn.setCellFactory(param -> new TableCell<BookFx, BookFx>() {
            Button button = createButton("/icons/delete.png");

            @Override
            protected void updateItem(BookFx item, boolean empty) {
                super.updateItem(item, empty);

                if(empty) {
                    setGraphic(null);
                    return;
                }

                if(!empty) {
                    setGraphic(button);
                    button.setOnAction(event -> {
                        try {
                            listBooksModel.deleteBook(item);
                        } catch (ApplicationException e) {
                            DialogsUtils.errorDialog(e.getMessage());
                        }
                    });
                }
            }
        });

        this.editColumn.setCellFactory(param -> new TableCell<BookFx, BookFx>() {
            Button button = createButton("/icons/edit.png");

            @Override
            protected void updateItem(BookFx item, boolean empty) {
                super.updateItem(item, empty);

                if(empty) {
                    setGraphic(null);
                    return;
                }

                if(!empty) {
                    setGraphic(button);
                    button.setOnAction(event -> {
                     // uzyjemy tej samej formatki co do edycji danych book z editbook.fxml
                        FXMLLoader loader = FxmlUtils.getLoader("/fxml/AddBook.fxml");
                        Scene scene = null;
                        try {
                            scene = new Scene(loader.load());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        BookController controller = loader.getController();
                        controller.getBookModel().setBookFxObjectProperty(item);
                        controller.bindings();
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.initModality(Modality.APPLICATION_MODAL); //okno modalne bedzie nad aplikacją - zawsze na wierzchu
                        stage.showAndWait();
                    });
                }
            }
        });
    }
    // obsługa przycisku delete z books

    private Button createButton(String path) {
        Button button = new Button();
        Image image = new Image(this.getClass().getResource(path).toString());
        ImageView imageView = new ImageView(image);
        button.setGraphic(imageView);
        return button;
    }

    public void filterOnActionComboBox() {
        this.listBooksModel.filterBooksList();
    }

    public void clearCategoryComboBox() {
        this.categoryComboBox.getSelectionModel().clearSelection();
    }

    public void clearAuthorComboBox() {
        this.authorComboBox.getSelectionModel().clearSelection();
    }
}
