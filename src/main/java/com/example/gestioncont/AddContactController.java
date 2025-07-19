package com.example.gestioncont;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddContactController {
    @FXML private TextField nomField, telField, mailField;
    @FXML private Label statusLabel;
    private boolean isUpdateMode = false;
    private Contact contactToEdit;
    private ContactController parentController;

    @FXML
    private void handleSave() {
        String nom = nomField.getText().trim();
        String tel = telField.getText().trim();
        String mail = mailField.getText().trim();

        if (nom.isEmpty() || tel.isEmpty() || mail.isEmpty()) {
            statusLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        if (!mail.contains("@") || !mail.contains(".")) {
            statusLabel.setText("Adresse email invalide !");
            return;
        }

        try (Connection conn = DBConnection.connect()) {
            if (isUpdateMode) {
                String sql = "UPDATE contacts SET nom = ?, telephone = ?, mail = ? WHERE nom = ? AND telephone = ? AND mail = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nom);
                stmt.setString(2, tel);
                stmt.setString(3, mail);
                stmt.setString(4, contactToEdit.getNom());
                stmt.setString(5, contactToEdit.getTelephone());
                stmt.setString(6, contactToEdit.getMail());
                stmt.executeUpdate();
            } else {
                String sql = "INSERT INTO contacts (nom, telephone, mail) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nom);
                stmt.setString(2, tel);
                stmt.setString(3, mail);
                stmt.executeUpdate();
            }

            ContactController.shouldRefresh = true;

            // Fermer la fenêtre
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Erreur lors de l'enregistrement.");
        }
    }

    @FXML
    private void handleCancel() {
        // Ferme simplement la fenêtre actuelle
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }


    public void setContactToEdit(Contact contact) {
        this.contactToEdit = contact;
        this.isUpdateMode = true;

        // Pré-remplir le formulaire
        nomField.setText(contact.getNom());
        telField.setText(contact.getTelephone());
        mailField.setText(contact.getMail());
    }

}
