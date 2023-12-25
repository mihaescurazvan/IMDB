import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI {
    private JFrame loginFrame;
    private JTextField emailField;
    private JPasswordField passwordField;
    private User userLoggedIn;

    public void displayLoginPage() {
        loginFrame = new JFrame("IMDB Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setPreferredSize(new Dimension(500, 350));

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(61, 76, 102)); // IMDB blue background color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel imdbLogoLabel = new JLabel(new ImageIcon("imdb_logo.png"));
        panel.add(imdbLogoLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 0, 15, 0);

        JLabel titleLabel = new JLabel("Sign In to IMDB");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 255, 255)); // White text color
        panel.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 10, 0);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 10, 15));
        inputPanel.setBackground(new Color(61, 76, 102));

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(new Color(255, 255, 255));
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 30)); // Larger text field
        emailField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white)); // Bottom border

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(new Color(255, 255, 255));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30)); // Larger text field
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.white)); // Bottom border

        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        panel.add(inputPanel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(255, 204, 0)); // IMDB yellow button color
        loginButton.setForeground(new Color(61, 76, 102)); // Dark blue text color
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(loginFrame, "Email field is empty!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(loginFrame, "Password field is empty!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    boolean loginSuccessful = false;
                    for (Object userObj : IMDB.getInstance().users) {
                        User user = (User) userObj;
                        if (user.information.getCredentials().getEmail().equals(email) &&
                                user.information.getCredentials().getPassword().equals(password)) {
                            loginSuccessful = true;
                            userLoggedIn = user;
                            break;
                        }
                    }
                    if (loginSuccessful) {
                        showNextScreen();
                    } else {
                        JOptionPane.showMessageDialog(loginFrame, "Invalid email or password!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        panel.add(loginButton, gbc);

        loginFrame.add(panel);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    private void showNextScreen() {
        // Display options based on the user's account type
        OptionsGUI optionsGUI = new OptionsGUI(userLoggedIn);
        loginFrame.dispose();
        optionsGUI.start();
    }

    public void start() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                displayLoginPage();
            }
        });
    }
}