package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passField;
    private JButton loginButton;
    RegularFrame regularFrame;
    ContributorFrame contributorFrame;
    AdminFrame adminFrame;
    public LoginFrame() {
        setTitle("Autentificare");
        setSize(600, 400); // Setează dimensiunea ferestrei
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        // Setează culoarea de fundal a ferestrei
        getContentPane().setBackground(new Color(128, 128, 128)); // Gri

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(225, 156, 253, 191)); // Gri
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Crează și adaugă câmpurile de text și butonul în panou
        emailField = new JTextField(10);
        emailField.setMaximumSize(new Dimension(200, 20));
        passField = new JPasswordField(10);
        passField.setMaximumSize(new Dimension(200, 20));


        loginButton = new JButton("Autentificare");
        loginButton.setBackground(Color.RED);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        // Adaugă componentele la panou
        panel.add(Box.createVerticalGlue());
        addLabelAndComponent(panel, "Email:", emailField);
        addLabelAndComponent(panel, "Password:", passField);
        panel.add(loginButton);
        panel.add(Box.createVerticalGlue());

        // Adaugă panoul în fereastră
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(panel, gbc);

        setLocationRelativeTo(null); // Centrează fereastra pe ecran
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    IMDB.getInstance().saveToJsonFile("C:\\Users\\andre\\Desktop\\TEMA_POO\\POO-Tema-2023-checker\\src\\main\\java\\org\\example\\data.json"); // Save to a file named data.json
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error saving data.");
                }
            }
        });

    }

    private void handleLogin ()
    {
        String email = emailField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Toate câmpurile trebuie completate!", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = IMDB.getInstance().authenticateUser(email, password);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Credentiale incorecte!", "Autentificare esuată", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Autentificare reusita!", "Succes", JOptionPane.INFORMATION_MESSAGE);
            openUserSpecificWindow(user);
        }
    }

    private void openUserSpecificWindow (User user){
        // Închide fereastra de login


        // Deschide o nouă fereastră în funcție de tipul utilizatorului
        // Exemplu: dacă este Admin, deschideți fereastra de admin
        if (user instanceof Admin) {
            // Deschide fereastra pentru Admin
            adminFrame = new AdminFrame((Admin) user);
        } else if (user instanceof Contributor) {
            // Deschide fereastra pentru Contributor
            contributorFrame = new ContributorFrame((Contributor) user);
        } else if (user instanceof Regular) {
            // Deschide fereastra pentru Regular
            regularFrame = new RegularFrame((Regular) user);
        }
    }

    private void addLabelAndComponent(JPanel panel, String labelText, JComponent component) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE); // Setează culoarea textului la alb
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(component);
        panel.add(Box.createVerticalStrut(10));
    }


}
