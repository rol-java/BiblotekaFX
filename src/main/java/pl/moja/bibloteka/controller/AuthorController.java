package pl.moja.bibloteka.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import pl.moja.bibloteka.modelFX.AuthorFx;
import pl.moja.bibloteka.modelFX.AuthorModel;
import pl.moja.bibloteka.utils.DialogsUtils;
import pl.moja.bibloteka.utils.exceptions.ApplicationException;

import java.sql.SQLException;

public class AuthorController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private Button addButton;

    @FXML
    private TableView<AuthorFx> authorTableView;

    @FXML
    private TableColumn<AuthorFx, String> nameColumn;

    @FXML
    private TableColumn<AuthorFx, String> surnameColumn;

    @FXML
    MenuItem deleteMenuItem;


    private AuthorModel authorModel;

    public void initialize() {

        this.authorModel = new AuthorModel();

        // inicjacja tabeli danych author
        try {
            this.authorModel.init();
        } catch (ApplicationException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
        //
        bindings();
        bindingsTableView();
    }

    private void bindings() {
        this.authorModel.authorFxObjectPropertyProperty().get().nameProperty().bind(this.nameTextField.textProperty());
        this.authorModel.authorFxObjectPropertyProperty().get().surnameProperty().bind(this.surnameTextField.textProperty());
        this.addButton.disableProperty().bind(this.nameTextField.textProperty().isEmpty().or(this.surnameTextField.textProperty().isEmpty()));
        // blokowanie usun prawym jak nie bedzie co usunać ma sie wyszarzyć
        this.deleteMenuItem.disableProperty().bind(this.authorTableView.getSelectionModel().selectedItemProperty().isNull());
        ///////////////////////////////////////////////////////////////////
    }

    private void bindingsTableView() {
        this.authorTableView.setItems(this.authorModel.getAuthorFxObservableList());
        // odwołujemy sie do każdej kolumny name i surname
        this.nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        this.surnameColumn.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
        // dodanie mozliwosci edytowania rekordów //////////////////////////////
        this.nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        // dodawanie do bazy danych author
        this.authorTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.authorModel.setAuthorFxObjectPropertyEdit(newValue);
        });
        ////////////////////////////////////////////////////////////////////////
    }


    public void addAuthorOnAction() {
        try {
            this.authorModel.saveAuthorInDataBase();
        } catch (ApplicationException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
        this.nameTextField.clear();
        this.surnameTextField.clear();
    }
    // dodanie edycji w kolumanch author - event
    public void onEditCommitName(TableColumn.CellEditEvent<AuthorFx, String> authorFxStringCellEditEvent) {
        this.authorModel.getAuthorFxObjectPropertyEdit().setName(authorFxStringCellEditEvent.getNewValue());
        updateInDataBase();
    }

    public void onEditCommitSurname(TableColumn.CellEditEvent<AuthorFx, String> authorFxStringCellEditEvent) {
        this.authorModel.getAuthorFxObjectPropertyEdit().setSurname(authorFxStringCellEditEvent.getNewValue());
        updateInDataBase();
    }

    private void updateInDataBase() {
        try {
            this.authorModel.saveAuthorEditInDataBase();
        } catch (ApplicationException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
    }
    /////////////////////////////////////////////

    public void deleteAuthorOnAction() {
        try {
            this.authorModel.deleteAuthorInDataBase();
        } catch (ApplicationException | SQLException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
    }

}
