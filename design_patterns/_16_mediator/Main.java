package design_patterns._16_mediator;

public class Main {
    public static void main(String[] args) {
        FormMediator mediator = new FormMediator();

        TextField usernameField = new TextField(mediator);
        TextField passwordField = new TextField(mediator);
        Button loginButton = new Button(mediator);
        Label statusLabel = new Label(mediator);

        mediator.setUsernameField(usernameField);
        mediator.setPasswordField(passwordField);
        mediator.setLoginButton(loginButton);
        mediator.setStatusLabel(statusLabel);

        // siumulation
        usernameField.setText("admin");
        passwordField.setText("1234");
        loginButton.click(); // Should succeed

        System.out.println("\n--- New Attempt with Wrong Password ---");
        passwordField.setText("wrong");
        loginButton.click(); // Should fail
    }
}
/*
TextField updated: admin
Login Button is now DISABLED
TextField updated: 1234
Login Button is now ENABLED
Login Button clicked!
Status: Login successful!

--- New Attempt with Wrong Password ---
TextField updated: wrong
Login Button is now ENABLED
Login Button clicked!
Status: Invalid credentials.
PS C:\Users\suhai\Desktop\mySD> 
*/