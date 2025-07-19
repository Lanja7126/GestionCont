package com.example.gestioncont;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.sql.*;
import java.util.Comparator;

public class ContactController {

    @FXML private TextField nomField, telField, mailField;
    @FXML private Label statusLabel;
    @FXML private TableView<Contact> tableView;
    @FXML private TableColumn<Contact, String> nomCol;
    @FXML private TableColumn<Contact, String> telCol;
    @FXML private TableColumn<Contact, String> mailCol;
    @FXML private Button deleteButton;

    private ObservableList<Contact> contacts = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialiser les colonnes
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        telCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        mailCol.setCellValueFactory(new PropertyValueFactory<>("mail"));

        // Charger les contacts depuis la base
        loadContacts();

        // Créer une liste triée alphabétiquement par nom
        SortedList<Contact> sortedContacts = new SortedList<>(contacts);
        sortedContacts.setComparator(Comparator.comparing(Contact::getNom, String.CASE_INSENSITIVE_ORDER));

        // Lier au TableView
        tableView.setItems(sortedContacts);

        deleteButton.setDisable(true);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            deleteButton.setDisable(newSelection == null);
        });
    }

    private void loadContacts() {
        contacts.clear(); // Pour éviter les doublons si rechargé
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM contacts")) {

            while (rs.next()) {
                String nom = rs.getString("nom");
                String tel = rs.getString("telephone");
                String mail = rs.getString("mail");
                contacts.add(new Contact(nom, tel, mail));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleOpenAdd() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add_contact.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setMinWidth(400);
            stage.setMinHeight(400);
            stage.setTitle("Ajouter un contact");
            stage.showAndWait();

            // Si on a ajouté un contact, on recharge la liste
            if (shouldRefresh) {
                loadContacts();
                shouldRefresh = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEdit() {
        Contact selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Aucun contact sélectionné");
            alert.setContentText("Veuillez sélectionner un contact à modifier.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_contact.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setMinWidth(400);
            stage.setMinHeight(400);
            stage.setTitle("Modifier un contact");

            // Passer le contact sélectionné au contrôleur d'édition
            AddContactController controller = loader.getController();
            controller.setContactToEdit(selected); // méthode qu'on crée plus bas
            stage.showAndWait();

            // Rafraîchir après modification
            if (shouldRefresh) {
                loadContacts();
                shouldRefresh = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        Contact selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un contact à supprimer.");
            alert.showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer ce contact ?", ButtonType.YES, ButtonType.NO);
        confirmation.setTitle("Confirmer la suppression");
        confirmation.showAndWait();

        if (confirmation.getResult() == ButtonType.YES) {
            try (Connection conn = DBConnection.connect()) {
                String sql = "DELETE FROM contacts WHERE nom = ? AND telephone = ? AND mail = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, selected.getNom());
                stmt.setString(2, selected.getTelephone());
                stmt.setString(3, selected.getMail());
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    contacts.remove(selected);
                    Alert success = new Alert(Alert.AlertType.INFORMATION, "Contact supprimé avec succès.");
                    success.showAndWait();
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression.");
                    error.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression.");
                error.showAndWait();
            }
        }
    }


    // Variable static partagée entre les fenêtres
    public static boolean shouldRefresh = false;

}
