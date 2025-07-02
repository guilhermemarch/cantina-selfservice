package com.cantinasa.cantinasa.UI.controllers;

import com.cantinasa.cantinasa.model.Usuario;
import com.cantinasa.cantinasa.model.enums.role;
import com.cantinasa.cantinasa.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import static com.cantinasa.cantinasa.model.enums.role.ADMIN;

@Controller
public class AdminLoginController {

    @Autowired
    private UsuarioService usuarioService;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Por favor, preencha todos os campos.");
            return;
        }

        try {
            Usuario usuario = usuarioService.autenticar(username, password);

            if (usuario != null && usuario.getRole() == role.ADMIN) {
                MainController.getInstance().loadView("shopping-cart");
                MainController.getInstance().loadView("admin-dashboard");
            } else {
                showError("Acesso restrito: apenas administradores podem acessar este painel.");
            }
        } catch (Exception e) {
            showError("Erro ao realizar login. Tente novamente.");
        }
    }

    @FXML
    private void handleBack() {
        com.cantinasa.cantinasa.UI.controllers.MainController.getInstance().loadView("welcome");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
