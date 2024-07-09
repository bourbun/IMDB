package org.example;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import static javax.print.attribute.standard.MediaSize.Engineering.C;

public class RegularFrame extends JFrame implements RequestsManager,Observer {

    private ReviewExperience reviewExperience = new ReviewExperience();
    private List<Observer> observers = IMDB.getInstance().getObservers();

    List<Request> requests = IMDB.getInstance().getRequests();
    private User user;
    private JLabel imdbLabel;
    private JLabel userDetails;
    private JLabel experienceLabel;
    private JPanel headerPanel;
    private JButton logoutButton;
    private  JPanel panelMain;
    private JLabel barbieLabel;
    JPanel actionsPanel;

    public RegularFrame(User user) {

        this.user = user;
        setTitle("IMDB");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(10, 10));
        headerPanel.setBackground(new Color(225, 156, 253, 191));

        // Panel for recommendations in the header
        JPanel recommendationsPanel = new JPanel();
        recommendationsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Use FlowLayout for center alignment
        recommendationsPanel.setOpaque(false); // Make it transparent

        // Fetch recommendations and add them to the recommendations panel
        // Note: Replace with actual image icons and titles
        recommendationsPanel.add(createRecommendationComponent("Barbie", new ImageIcon("C:\\Users\\andre\\Desktop\\TEMA_POO\\POO-Tema-2023-checker\\Images\\download.jpg")));
        recommendationsPanel.add(createRecommendationComponent("Oppenheimer", new ImageIcon("C:\\Users\\andre\\Desktop\\TEMA_POO\\POO-Tema-2023-checker\\Images\\Empire-Movie-site-Oppenheimer.jpg")));
        recommendationsPanel.add(createRecommendationComponent("HTGAWM", new ImageIcon("C:\\Users\\andre\\Desktop\\TEMA_POO\\POO-Tema-2023-checker\\Images\\download (1).jpg")));

        // Label for IMDB
        imdbLabel = new JLabel("IMDB");
        imdbLabel.setFont(imdbLabel.getFont().deriveFont(Font.BOLD, 24f));
        headerPanel.add(imdbLabel, BorderLayout.WEST);

        // Add recommendations to the CENTER of the header
        headerPanel.add(recommendationsPanel, BorderLayout.CENTER);
        // Panou pentru detalii utilizator
        JPanel userDetailsPanel = new JPanel();
        userDetailsPanel.setLayout(new BoxLayout(userDetailsPanel, BoxLayout.PAGE_AXIS));
        userDetailsPanel.setOpaque(false); // Face panoul transparent

        // Label pentru numele utilizatorului
        userDetails = new JLabel("User: " + user.getName());
        userDetails.setHorizontalAlignment(SwingConstants.RIGHT);

        // Label pentru experiență
        experienceLabel = new JLabel("Experience: " + user.getExperience());
        experienceLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Adaugă etichetele la panoul userDetailsPanel
        userDetailsPanel.add(userDetails);
        userDetailsPanel.add(experienceLabel);
        userDetailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Adaugă panoul userDetailsPanel la headerPanel
        headerPanel.add(userDetailsPanel, BorderLayout.EAST);

        // Adaugă headerPanel la fereastră
        add(headerPanel, BorderLayout.NORTH);

        // Panou principal
        panelMain = new JPanel();
        panelMain.setBackground(new Color(177, 151, 209, 255));
        add(panelMain, BorderLayout.CENTER);

        actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setBackground(new Color(177, 151, 209, 255)); // Culoarea de fundal pentru panoul de acțiuni
        String[] actions = new String[]{"View productions details", "View actors details", "View notifications",
                "Search for actor/production", "Add/Delete actor/production to/from favorites", "Add/Delete a request",
                "Add/Delete a review for a production", "See favorites"};

        for (String action : actions) {
            JButton button = new JButton(action);
            button.setAlignmentX(Component.CENTER_ALIGNMENT); // Aliniază butonul pe axa X
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMinimumSize().height)); // Lățime maximă

            // Adaugă efectul de hover
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    button.setBackground(Color.LIGHT_GRAY); // Schimbă culoarea butonului la hover
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    button.setBackground(UIManager.getColor("control")); // Restabilește culoarea implicită
                }
            });
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleAction(e.getActionCommand());
                }
            });

            actionsPanel.add(button);
            actionsPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Adaugă spațiu între butoane
        }

        actionsPanel.setBorder(BorderFactory.createEmptyBorder(80, 20, 20, 20));
        // Adaugă actionsPanel la fereastră
        add(actionsPanel, BorderLayout.EAST);

        // Buton de logout
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> dispose());
        add(logoutButton, BorderLayout.SOUTH);

        // Setează dimensiunea ferestrei la 75% din dimensiunea ecranului
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width * 3 / 4, screenSize.height * 3 / 4);

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
    private JPanel createRecommendationComponent(String title, ImageIcon icon) {
        // Create component to display a recommendation with an image and a title
        JPanel recommendationPanel = new JPanel();
        recommendationPanel.setLayout(new BoxLayout(recommendationPanel, BoxLayout.PAGE_AXIS));
        recommendationPanel.setOpaque(false); // Make it transparent

        // Scale the image icon to fit the header
        Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Adjust size as needed
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

        // Label for the title
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Set font here if needed

        // Add components to the recommendation panel
        recommendationPanel.add(imageLabel);
        recommendationPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Space between image and text
        recommendationPanel.add(titleLabel);

        return recommendationPanel;
    }

    private void handleAction(String actionCommand) {
		switch (actionCommand) {
            case "View productions details":
                viewProductionsDetails();
                break;
            case "View actors details":
                viewActorsDetails();
                break;
            case "View notifications":
                viewNotifications();
                break;
            case "Search for actor/production":
                searchForActorOrProduction();
                break;
            case "Add/Delete actor/production to/from favorites":
                addOrDeleteFromFavorites();
                break;
            case "Add/Delete a request":
                addOrDeleteRequest();
                break;
            case "Add/Delete a review for a production":
                addOrDeleteReview();
                break;
            case "See favorites":
                seeFavorites();
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + actionCommand);
        }
    }
    private void viewProductionsDetails() {
        // Get sorted list of productions
        List<Production> sortedProductions = IMDB.getInstance().getProductions();
        sortedProductions.sort(Comparator.comparing(Production::getTitle));

        // Transformă lista de producții într-un model pentru JList
        DefaultListModel<Production> productionListModel = new DefaultListModel<>();
        for (Production production : sortedProductions) {
            productionListModel.addElement(production);
        }

        // Creează JList-ul și JScrollPane-ul pentru producții
        JList<Production> productionJList = new JList<>(productionListModel);
        JScrollPane productionScrollPane = new JScrollPane(productionJList);
        productionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Creează JTextArea pentru detalii și JScrollPane-ul aferent
        JTextArea productionDetailsTextArea = new JTextArea();
        productionDetailsTextArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(productionDetailsTextArea);
        detailsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JButton filterSortButton = new JButton("Filter/Sort Options");
        filterSortButton.addActionListener(e -> showFilterSortDialog(productionJList));
        filterSortButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionsPanel.add(filterSortButton);


        // Adaugă un listener pentru JList pentru a afișa detalii când o producție este selectată
        productionJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Production selectedProduction = productionJList.getSelectedValue();
                if (selectedProduction != null) {
                    if (selectedProduction instanceof Movie movie) {
                        productionDetailsTextArea.setText("Title: " + movie.getTitle() + "\n" +
                                "Directors: " + Arrays.toString(movie.getDirectors()) + "\n" +
                                "Actors: " + movie.getActors() + "\n" +
                                "Genres: " + movie.getGenres() + "\n" );
                        for (Rating rating : movie.getRatings()) {
                            productionDetailsTextArea.append("Ratings: " + rating.getUsername() + " " + rating.getScore() + " " + rating.getComments() + "\n");
                        }
                        productionDetailsTextArea.append("Description: " + movie.description + "\n" +
                                "Average rating: " + movie.averageRating + "\n" +
                                "Duration: " + movie.duration + "\n" +
                                "Release year: " + movie.releaseYear + "\n");
                    } else {
                        Series series = (Series) selectedProduction;
                        productionDetailsTextArea.setText("Title: " + series.getTitle() + "\n" +
                                "Directors: " + Arrays.toString(series.getDirectors()) + "\n" +
                                "Actors: " + series.getActors() + "\n" +
                                "Genres: " + series.getGenres() + "\n");
                        for (Rating rating : series.getRatings()) {
                            productionDetailsTextArea.append("Ratings: " + rating.getUsername() + " " + rating.getScore() + " " + rating.getComments() + "\n");
                        }
                        productionDetailsTextArea.append("Description: " + series.description + "\n" +
                                "Average rating: " + series.averageRating + "\n");
                        for (String season : series.getSeasons().keySet()) {
                            productionDetailsTextArea.append("Season: " + season + "\n");
                            for (Episode episode : series.getSeasons().get(season)) {
                                productionDetailsTextArea.append("Episode: " + episode.getTitle() + " " + episode.getDuration() + "minutes" + "\n");
                            }
                        }
                    }
                }
            }
        });

        // Creează un JSplitPane pentru a împărți ecranul între lista de producții și detalii
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, productionScrollPane, detailsScrollPane);
        splitPane.setDividerLocation(300); // Setează locația divider-ului după preferințe

        // Adaugă splitPane-ul la frame
        this.getContentPane().add(splitPane, BorderLayout.CENTER);

        // Revalidate și repaint pentru a actualiza UI-ul
        this.revalidate();
        this.repaint();
    }

    private void showFilterSortDialog(JList<Production> productionJList) {
        // Create and setup the dialog
        JDialog filterSortDialog = new JDialog();
        filterSortDialog.setLayout(new FlowLayout());

        // ComboBox for genre selection
        JComboBox<Genre> genreComboBox = new JComboBox<>(Genre.getGenres());
        filterSortDialog.add(genreComboBox);

        // RadioButton for sorting by most ratings
        JRadioButton sortMostRatingsButton = new JRadioButton("Sort by Most Ratings");
        filterSortDialog.add(sortMostRatingsButton);

        // Button to confirm selection
        JButton selectButton = new JButton("Select");
        filterSortDialog.add(selectButton);
        selectButton.addActionListener(e -> {
            if (sortMostRatingsButton.isSelected()) {
                sortProductionsByMostRatings(productionJList);
            } else {
                Genre selectedGenre = (Genre) genreComboBox.getSelectedItem();
                filterProductionsByGenre(selectedGenre, productionJList);
            }
            filterSortDialog.dispose();
        });

        // Show the dialog
        filterSortDialog.pack();
        filterSortDialog.setVisible(true);
    }

    // Methods to filter and sort productions (to be implemented)
    private void filterProductionsByGenre(Genre genre, JList<Production> productionJList) {
        List<Production> filteredProductions = IMDB.getInstance().getProductions().stream()
                .filter(p -> p.getGenres().contains(genre))
                .toList();

        DefaultListModel<Production> filteredListModel = new DefaultListModel<>();
        for (Production production : filteredProductions) {
            filteredListModel.addElement(production);
        }

        productionJList.setModel(filteredListModel);
    }

    private void sortProductionsByMostRatings(JList<Production> productionJList) {
        List<Production> sortedProductions = IMDB.getInstance().getProductions().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getRatings().size(), p1.getRatings().size()))
                .toList();

        DefaultListModel<Production> sortedListModel = new DefaultListModel<>();
        for (Production production : sortedProductions) {
            sortedListModel.addElement(production);
        }

        productionJList.setModel(sortedListModel);
    }


    private void viewActorsDetails() {
        // Get sorted list of actors
        List<Actor> sortedActors = IMDB.getInstance().getActors();
        sortedActors.sort(Comparator.comparing(Actor::getName));

        // Transformă lista de actori într-un model pentru JList
        DefaultListModel<Actor> actorListModel = new DefaultListModel<>();
        for (Actor actor : sortedActors) {
            actorListModel.addElement(actor);
        }

        // Creează JList-ul și JScrollPane-ul pentru actori
        JList<Actor> actorJList = new JList<>(actorListModel);
        JScrollPane actorScrollPane = new JScrollPane(actorJList);
        actorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Creează JTextArea pentru detalii și JScrollPane-ul aferent
        JTextArea actorDetailsTextArea = new JTextArea();
        actorDetailsTextArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(actorDetailsTextArea);
        detailsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Adaugă un listener pentru JList pentru a afișa detalii când un actor este selectat
        actorJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Actor selectedActor = actorJList.getSelectedValue();
                if (selectedActor != null) {
                    // Aici, folosește metodele din clasa Actor pentru a obține informațiile necesare
                    actorDetailsTextArea.setText("Name: " + selectedActor.getName() + "\n" +
                            "Biography: " + selectedActor.getBiography() + "\n" +
                            // Presupunem că ai o metodă care returnează rolurile actorului sub forma unui String
                            "Roles: " + selectedActor.getRolesAsString());
                }
            }
        });

        // Creează un JSplitPane pentru a împărți ecranul între lista de actori și detalii
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, actorScrollPane, detailsScrollPane);
        splitPane.setDividerLocation(300); // Setează locația divider-ului după preferințe

        // Adaugă splitPane-ul la frame
        this.getContentPane().add(splitPane, BorderLayout.CENTER);

        // Revalidate și repaint pentru a actualiza UI-ul
        this.revalidate();
        this.repaint();
    }

    private void viewNotifications() {
        List<String> notifications = user.getNotifications(); // Presupunem că există o metodă care returnează notificările utilizatorului

        // Poziția inițială pentru prima notificare
        int xOffset = Toolkit.getDefaultToolkit().getScreenSize().width - 300; // 300px lățime pentru notificare
        int yOffset = 0;

        for (String notification : notifications) {
            JWindow notificationWindow = new JWindow();
            notificationWindow.setLayout(new BorderLayout());
            notificationWindow.setSize(300, 100); // Setează dimensiunea ferestrei de notificare
            notificationWindow.setLocation(xOffset, yOffset);

            JTextArea notificationTextArea = new JTextArea(notification);
            notificationTextArea.setWrapStyleWord(true);
            notificationTextArea.setLineWrap(true);
            notificationTextArea.setEditable(false);

            JButton closeButton = new JButton("X");
            closeButton.addActionListener(e -> notificationWindow.dispose()); // Închide fereastra la apăsare

            notificationWindow.add(notificationTextArea, BorderLayout.CENTER);
            notificationWindow.add(closeButton, BorderLayout.EAST);

            notificationWindow.setVisible(true);

            // Actualizează offset-ul pentru următoarea notificare
            yOffset += 110; // 100px înălțimea ferestrei + 10px spațiu între notificări
        }

    }

    private void searchForActorOrProduction() {
        // Left panel for search input and results
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBackground(new Color(255, 220, 220)); // Different background color

        // Search bar components
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JComboBox<String> searchTypeComboBox = new JComboBox<>(new String[]{"Actor", "Production"});

        // Add components to the search panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(searchTypeComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        leftPanel.add(searchPanel, BorderLayout.NORTH);

        // Model for search results
        DefaultListModel<String> searchResultsModel = new DefaultListModel<>();
        JList<String> searchResultsList = new JList<>(searchResultsModel);
        searchResultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResultsList.setBackground(new Color(255, 250, 220)); // Different background color for list
        JScrollPane searchResultsScrollPane = new JScrollPane(searchResultsList);
        leftPanel.add(searchResultsScrollPane, BorderLayout.CENTER);

        // Text area for displaying details
        JTextArea detailsTextArea = new JTextArea();
        detailsTextArea.setLineWrap(true);
        detailsTextArea.setWrapStyleWord(true);
        JScrollPane detailsScrollPane = new JScrollPane(detailsTextArea);
        detailsScrollPane.setBackground(new Color(220, 255, 220)); // Different background color

        // Add panels to the main frame
        add(leftPanel, BorderLayout.WEST);
        add(detailsScrollPane, BorderLayout.CENTER);

        // Search button action
        searchButton.addActionListener(e -> {
            // Perform search and update the list model with results
            // This is where you would call your search method from the IMDB instance
            String searchTerm = searchField.getText().trim();
            String searchType = searchTypeComboBox.getSelectedItem().toString();
            searchResultsModel.clear(); // Clear previous results

            // Dummy search results added to the model for illustration
            // Replace with actual search results retrieval logic
            if (!searchTerm.isEmpty()) {
                // More results would be added here
                if (searchType.equals("Actor")) {
                    for (Actor actor : IMDB.getInstance().getActors()) {
                        if (actor.getName().contains(searchTerm)) {
                            searchResultsModel.addElement(actor.getName());
                        }
                    }
                } else {
                    for (Production production : IMDB.getInstance().getProductions()) {
                        if (production.getTitle().contains(searchTerm)) {
                            searchResultsModel.addElement(production.getTitle());
                        }
                    }
                }
            }
        });

        // List selection action to display details
        searchResultsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedResult = searchResultsList.getSelectedValue();
                if (selectedResult != null) {
                    // Display details for the selected search result
                    // Replace with actual details retrieval logic
                    detailsTextArea.setText("Details for \"" + selectedResult + ":\n");
                    if (searchTypeComboBox.getSelectedItem().toString().equals("Actor")) {
                        for (Actor actor : IMDB.getInstance().getActors()) {
                            if (actor.getName().equals(selectedResult)) {
                                detailsTextArea.append("Name: " + actor.getName() + "\n" +
                                        "Biography: " + actor.getBiography() + "\n" +
                                        "Roles: " + actor.getRolesAsString());
                            }
                        }
                    } else {
                        for (Production production : IMDB.getInstance().getProductions()) {
                            if (production.getTitle().equals(selectedResult)) {
                                if (production instanceof Movie movie) {
                                    detailsTextArea.append("Title: " + movie.getTitle() + "\n" +
                                            "Directors: " + Arrays.toString(movie.getDirectors()) + "\n" +
                                            "Actors: " + movie.getActors() + "\n" +
                                            "Genres: " + movie.getGenres() + "\n" +
                                            "Description: " + movie.description + "\n" +
                                            "Average rating: " + movie.averageRating + "\n" +
                                            "Duration: " + movie.duration + "\n" +
                                            "Release year: " + movie.releaseYear + "\n");
                                } else {
                                    Series series = (Series) production;
                                    detailsTextArea.append("Title: " + series.getTitle() + "\n" +
                                            "Directors: " + Arrays.toString(series.getDirectors()) + "\n" +
                                            "Actors: " + series.getActors() + "\n" +
                                            "Genres: " + series.getGenres() + "\n" +
                                            "Description: " + series.description + "\n" +
                                            "Average rating: " + series.averageRating + "\n");
                                    for (String season : series.getSeasons().keySet()) {
                                        detailsTextArea.append("Season: " + season + "\n");
                                        for (Episode episode : series.getSeasons().get(season)) {
                                            detailsTextArea.append("Episode: " + episode.getTitle() + " " + episode.getDuration() + "minutes" + "\n");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        // Finalize frame layout
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }


    private void addOrDeleteFromFavorites() {
        // Create a dialog frame
        JDialog dialog = new JDialog(this, "Add/Delete Favorites", true);
        dialog.setLayout(new BorderLayout());

        // Radio buttons for add or delete choice
        JRadioButton addRadioButton = new JRadioButton("Add to favorites", true);
        JRadioButton deleteRadioButton = new JRadioButton("Delete from favorites");

        // Group the radio buttons
        ButtonGroup group = new ButtonGroup();
        group.add(addRadioButton);
        group.add(deleteRadioButton);

        // Panel for radio buttons
        JPanel radioPanel = new JPanel(new FlowLayout());
        radioPanel.add(addRadioButton);
        radioPanel.add(deleteRadioButton);
        dialog.add(radioPanel, BorderLayout.NORTH);

        // Combo boxes for selecting actors or productions
        JComboBox<String> actorsComboBox = new JComboBox<>();
        JComboBox<String> productionsComboBox = new JComboBox<>();

        // Panel for combo boxes
        JPanel comboPanel = new JPanel();
        comboPanel.add(new JLabel("Actor:"));
        comboPanel.add(actorsComboBox);
        comboPanel.add(new JLabel("Production:"));
        comboPanel.add(productionsComboBox);
        dialog.add(comboPanel, BorderLayout.CENTER);

        // Populate combo boxes
        actorsComboBox.addItem(null); // Add null item for no selection
        for (Actor actor : IMDB.getInstance().getActors()) {
            actorsComboBox.addItem(actor.getName());
        }
        actorsComboBox.setSelectedIndex(0); // Select first actor by default
        productionsComboBox.addItem(null); // Add null item for no selection
        for (Production production : IMDB.getInstance().getProductions()) {
            productionsComboBox.addItem(production.getTitle());
        }
        productionsComboBox.setSelectedIndex(0); // Select first production by default

        // Button for performing the add/delete action
        JButton performActionButton = new JButton("Perform Action");
        dialog.add(performActionButton, BorderLayout.SOUTH);

        // Action listener for the performActionButton
        performActionButton.addActionListener(e -> {
            if (addRadioButton.isSelected()) {
                // TODO: Implement adding to favorites
                if (actorsComboBox.getSelectedItem() != null) {
                    Actor actor = IMDB.getInstance().getActorByName(Objects.requireNonNull(actorsComboBox.getSelectedItem()).toString(),IMDB.getInstance().getActors());
                    user.favorites.add(actor);
                } else if (productionsComboBox.getSelectedItem() != null) {
                    Production production = IMDB.getInstance().getProductionByTitle(Objects.requireNonNull(productionsComboBox.getSelectedItem()).toString(),IMDB.getInstance().getProductions());
                    user.favorites.add(production);
                }
            } else if (deleteRadioButton.isSelected()) {
                // TODO: Implement deleting from favorites
                if (actorsComboBox.getSelectedItem() != null) {
                    Actor actor = IMDB.getInstance().getActorByName(Objects.requireNonNull(actorsComboBox.getSelectedItem()).toString(),IMDB.getInstance().getActors());
                    user.favorites.remove(actor);
                } else if (productionsComboBox.getSelectedItem() != null) {
                    Production production = IMDB.getInstance().getProductionByTitle(Objects.requireNonNull(productionsComboBox.getSelectedItem()).toString(),IMDB.getInstance().getProductions());
                    user.favorites.remove(production);
                }
            }
            dialog.dispose(); // Close the dialog after performing the action
        });

        // Display the dialog
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    private void addOrDeleteRequest() {
        JDialog dialog = new JDialog(this, "Add or Delete Request", true);
        dialog.setLayout(new CardLayout());
        dialog.setSize(400, 300); // Set dialog size

        // Create panel for add/delete choice
        JPanel choicePanel = new JPanel();
        JButton addButton = new JButton("Add a request");
        JButton deleteButton = new JButton("Delete a request");
        choicePanel.add(addButton);
        choicePanel.add(deleteButton);

        // Add action listeners to buttons
        addButton.addActionListener(e -> showRequestForm(dialog, RequestAction.ADD));
        deleteButton.addActionListener(e -> showRequestForm(dialog, RequestAction.DELETE));

        // Add choicePanel to the dialog
        dialog.add(choicePanel, "Choice");

        dialog.setLocationRelativeTo(this); // Center on parent
        dialog.setVisible(true);
    }

    private void showRequestForm(JDialog dialog, RequestAction action) {
        CardLayout cardLayout = (CardLayout) dialog.getContentPane().getLayout();
        switch (action) {
            case ADD:
                // Show add request form
                JPanel addPanel = createAddRequestPanel(dialog);
                dialog.add(addPanel, "Add");
                cardLayout.show(dialog.getContentPane(), "Add");
                break;
            case DELETE:
                // Show delete request form
                JPanel deletePanel = createDeleteRequestPanel(dialog);
                dialog.add(deletePanel, "Delete");
                cardLayout.show(dialog.getContentPane(), "Delete");
                break;
        }
    }

    private JPanel createAddRequestPanel(JDialog dialog) {
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.PAGE_AXIS));

        // Add components for selecting the type of request
        JComboBox<String> requestTypeComboBox = new JComboBox<>(new String[]{"DELETE_ACCOUNT", "ACTOR_ISSUE", "MOVIE_ISSUE", "OTHERS"});
        addPanel.add(new JLabel("Select the type of request:"));
        addPanel.add(requestTypeComboBox);

        // Placeholder for input fields, to be added based on selection
        JPanel inputFieldsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addPanel.add(inputFieldsPanel);

        requestTypeComboBox.addActionListener(e -> {
            inputFieldsPanel.removeAll(); // Clear previous input fields
            String selectedType = (String) requestTypeComboBox.getSelectedItem();
            // Based on selection, add appropriate input fields
            if ("DELETE_ACCOUNT".equals(selectedType)) {
                inputFieldsPanel.removeAll();
                JTextField reasonField = new JTextField(20);
                inputFieldsPanel.add(new JLabel("Enter the reason:"));
                reasonField.setAlignmentY(Component.CENTER_ALIGNMENT);
                inputFieldsPanel.add(reasonField);
                LocalDateTime creationDate = LocalDateTime.now();
                String resolverUsername = "ADMIN"; //all delete account requests are solved by admins
                JButton button = new JButton("Create request");
                button.setAlignmentY(Component.BOTTOM_ALIGNMENT);
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                inputFieldsPanel.add(button);
                inputFieldsPanel.revalidate();
                //after pressing the button, the addPanel will be disposed
                button.addActionListener(f -> {
                    if (!reasonField.getText().isEmpty()) {
                        String description = reasonField.getText();
                        System.out.println(description);
                        Request r = new Request(RequestTypes.DELETE_ACCOUNT, creationDate,null,null,description,user.getName(),resolverUsername);
                        createRequest(r);
                        IMDB.getInstance().addObserver(user);
                        String notificationMessage = "Ai primit o noua cerere de tipul" + r.getRequestType() + " de la " + r.getCreatorUsername() + "pentru tine";
                        IMDB.getInstance().getUserByUsername(resolverUsername).notifications.add(notificationMessage);
                    }

                    dialog.dispose();
                });

                inputFieldsPanel.revalidate();
                inputFieldsPanel.repaint();
            } else if ("ACTOR_ISSUE".equals(selectedType)) {
                inputFieldsPanel.removeAll();

                // Label for actor selection
                inputFieldsPanel.add(new JLabel("Select an actor:"));

                // List of actors
                List<Actor> actors = IMDB.getInstance().getActors();
                DefaultComboBoxModel<Actor> actorModel = new DefaultComboBoxModel<>();
                for (Actor actor : actors) {
                    actorModel.addElement(actor);
                }
                JComboBox<Actor> actorComboBox = new JComboBox<>(actorModel);
                inputFieldsPanel.add(actorComboBox);

                // Field for reason
                JTextField reasonField = new JTextField(20);
                inputFieldsPanel.add(new JLabel("Enter the reason:"));
                reasonField.setAlignmentY(Component.CENTER_ALIGNMENT);
                inputFieldsPanel.add(reasonField);

                // Button to create request
                JButton button = new JButton("Create request");
                button.setEnabled(false); // Button is initially disabled
                button.setAlignmentY(Component.BOTTOM_ALIGNMENT);
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                inputFieldsPanel.add(button);

                // Listener for actor selection
                actorComboBox.addActionListener(g -> button.setEnabled(actorComboBox.getSelectedItem() != null && !reasonField.getText().trim().isEmpty()));

                // Button action listener
                button.addActionListener(f -> {
                    Actor selectedActor = (Actor) actorComboBox.getSelectedItem();
                    String reason = reasonField.getText().trim();
                    if (selectedActor != null && !reason.isEmpty()) {
                        LocalDateTime creationDate = LocalDateTime.now();
                        String resolverUsername = IMDB.getInstance().findUserWhoAddedActor(selectedActor.getName()).getName();
                        Request r = new Request(RequestTypes.ACTOR_ISSUE, creationDate, selectedActor.getName(), null, reason, user.getName(), resolverUsername);
                        createRequest(r);
                        dialog.dispose();
                    } else {
                        // Show error message if the actor or reason is not provided
                        JOptionPane.showMessageDialog(dialog, "Please select an actor and provide a reason for the request.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                inputFieldsPanel.revalidate();
                inputFieldsPanel.repaint();


                // Add more fields as needed
            } else if ("MOVIE_ISSUE".equals(selectedType)) {
                inputFieldsPanel.removeAll();

                // Label for movie selection
                inputFieldsPanel.add(new JLabel("Select a movie:"));

                // Dropdown for movies
                JComboBox<String> movieComboBox = new JComboBox<>();
                for (Production production : IMDB.getInstance().getProductions()) {
                    if (production instanceof Movie) {
                        movieComboBox.addItem(production.getTitle());
                    }
                }
                inputFieldsPanel.add(movieComboBox);

                // Field for reason
                JTextField reasonField = new JTextField(20);
                inputFieldsPanel.add(new JLabel("Enter the reason:"));
                reasonField.setAlignmentY(Component.CENTER_ALIGNMENT);
                inputFieldsPanel.add(reasonField);

                // Button to create request
                JButton button = new JButton("Create request");
                button.setEnabled(false); // Button is initially disabled
                button.setAlignmentY(Component.BOTTOM_ALIGNMENT);
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                inputFieldsPanel.add(button);

                // Listener for movie selection and reason input
                movieComboBox.addActionListener(f -> button.setEnabled(movieComboBox.getSelectedItem() != null && !reasonField.getText().trim().isEmpty()));
                reasonField.addKeyListener(new KeyAdapter() {
                    public void keyReleased(KeyEvent e) {
                        button.setEnabled(movieComboBox.getSelectedItem() != null && !reasonField.getText().trim().isEmpty());
                    }
                });

                // Button action listener
                button.addActionListener(f -> {
                    String selectedMovieTitle = (String) movieComboBox.getSelectedItem();
                    String reason = reasonField.getText().trim();
                    if (selectedMovieTitle != null && !reason.isEmpty()) {
                        LocalDateTime creationDate = LocalDateTime.now();
                        Production selectedProduction = IMDB.getInstance().getProductionByTitle(selectedMovieTitle, IMDB.getInstance().getProductions());
                        String resolverUsername = IMDB.getInstance().findUserWhoAddedProduction(selectedMovieTitle).getName();
                        Request r = new Request(RequestTypes.MOVIE_ISSUE, creationDate, selectedMovieTitle, null, reason, user.getName(), resolverUsername);
                        createRequest(r);
                        dialog.dispose();
                    } else {
                        // Show error message if the movie or reason is not provided
                        JOptionPane.showMessageDialog(dialog, "Please select a movie and provide a reason for the request.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                inputFieldsPanel.revalidate();
                inputFieldsPanel.repaint();
            } else if ("OTHERS".equals(selectedType)) {
                inputFieldsPanel.removeAll();
                JTextField reasonField = new JTextField(20);
                inputFieldsPanel.add(new JLabel("Enter the reason:"));
                reasonField.setAlignmentY(Component.CENTER_ALIGNMENT);
                inputFieldsPanel.add(reasonField);
                LocalDateTime creationDate = LocalDateTime.now();
                String resolverUsername = "ADMIN"; //all delete account requests are solved by admins
                JButton button = new JButton("Create request");
                button.setAlignmentY(Component.BOTTOM_ALIGNMENT);
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                inputFieldsPanel.add(button);
                inputFieldsPanel.revalidate();
                button.addActionListener(f -> {
                    if (!reasonField.getText().isEmpty()) {
                        String description = reasonField.getText();
                        System.out.println(description);
                        Request r = new Request(RequestTypes.OTHERS, creationDate,null,null,description,user.getName(),resolverUsername);
                        createRequest(r);

                    }

                    dialog.dispose();
                });
                // Add more fields as needed
            }


            // Refresh panel to show new input fields
            inputFieldsPanel.revalidate();
            inputFieldsPanel.repaint();
        });

        // Initialize with DELETE_ACCOUNT input fields
        requestTypeComboBox.setSelectedIndex(0);

        return addPanel;
    }

    private JPanel createDeleteRequestPanel(JDialog dialog) {
        JPanel deletePanel = new JPanel();
        deletePanel.setLayout(new BoxLayout(deletePanel, BoxLayout.PAGE_AXIS));

        List<Request> userRequests = IMDB.getInstance().getUserRequests(user.getName()); // You need to implement getUserRequests in IMDB
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (Request request : userRequests) {
            listModel.addElement(request.description);
        }

        JList<String> requestsList = new JList<>(listModel);
        JScrollPane listScroller = new JScrollPane(requestsList);
        listScroller.setPreferredSize(new Dimension(250, 80));
        deletePanel.add(listScroller);

        JButton deleteButton = new JButton("Delete Selected Request");
        deleteButton.addActionListener(e -> {
            int selectedIndex = requestsList.getSelectedIndex();
            if (selectedIndex != -1) {
                Request toDelete = userRequests.get(selectedIndex);
                removeRequest(toDelete); // Assume removeRequest(Request) is a method to remove the request
                listModel.remove(selectedIndex); // Update the JList
                dialog.dispose(); // Close the dialog
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a request to delete.");
            }
        });

        deletePanel.add(deleteButton);
        return deletePanel;
    }

    @Override
    public void update(String message) {
        // Handle the notification message
    }

    enum RequestAction {
        ADD, DELETE
    }

	private void addOrDeleteReview() {
		// Create a dialog
		JDialog dialog = new JDialog(this, "Add or Delete Review", true);
		dialog.setLayout(new BorderLayout());

		// Option pane for choosing between adding or deleting a review
		String[] options = {"Add", "Delete"};
		int choice = JOptionPane.showOptionDialog(dialog,
				"Choose an option",
				"Review Options",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null, options, options[0]);

		// If user chooses to add a review
		if (choice == JOptionPane.YES_OPTION) {
            JPanel panel = new JPanel(new GridLayout(0, 1));

            // Input for production title
            JTextField titleField = new JTextField();
            panel.add(new JLabel("Enter the title of the production:"));
            panel.add(titleField);

            // Input for rating
            JTextField ratingField = new JTextField();
            panel.add(new JLabel("Enter your rating (1-10):"));
            panel.add(ratingField);

            // Input for comment
            JTextArea commentArea = new JTextArea(3, 20);
            panel.add(new JLabel("Enter your comment:"));
            panel.add(new JScrollPane(commentArea));

            int result = JOptionPane.showConfirmDialog(dialog, panel, "Add Review", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String title = titleField.getText();
                int rating = Integer.parseInt(ratingField.getText());
                String comment = commentArea.getText();

                // TODO: Implement the logic to add the review
                // You'll need to find the production by title, check if the user already has a review on it,
                // and then add or update the review accordingly.
                Rating existingRating = IMDB.getInstance().getProductionByTitle(title, IMDB.getInstance().getProductions()).getRatingByUser(user.getName());
                if (existingRating != null) {
                    // ask if they want to update the review
                    String[] options1 = {"Yes", "No"};
                    int choice1 = JOptionPane.showOptionDialog(dialog,
                            "You already have a review for this production. Do you want to update it?",
                            "Review Options",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, options1, options1[0]);
                    if (choice1 == JOptionPane.YES_OPTION) {
                        existingRating.setScore(rating);
                        existingRating.setComments(comment);
                    } else {
                        return;
                    }
                } else {
                    Rating rating1 = new Rating(user.getName(), rating, comment);
                    IMDB.getInstance().getProductionByTitle(title, IMDB.getInstance().getProductions()).ratings.add(rating1);
                    int newExperience = Integer.parseInt(user.experience) + reviewExperience.calculateExperience();
                    user.experience = String.valueOf(newExperience);
                    // go through all the users who reviewed this production and add notification message
                    for (Rating rating2 : IMDB.getInstance().getProductionByTitle(title, IMDB.getInstance().getProductions()).ratings) {
                        if (!rating2.getUsername().equals(user.getName())) {
                            User user1 = IMDB.getInstance().findUserWhoAddedProduction(title);
                            if(IMDB.getInstance().getProductionByTitle(title, IMDB.getInstance().getProductions()) instanceof Movie) {
                                user1.notifications.add("Filmul \"" + title + "\" caruia i-ai adaugat o recenzie a primit un review de la utilizatorul \"" + user.getName() + "\"" + rating2.getScore());
                            } else {
                                user1.notifications.add("Serialul \"" + title + "\" caruia i-ai adaugat o recenzie a primit un review de la utilizatorul \"" + user.getName() + "\"" + rating2.getScore());
                            }
                        }
                    }
                    // go through all the users who added this production to favorites and add notification message
                    for (Comparable favorite : user.favorites) {
                        if (favorite instanceof Production production) {
                            if (production.getTitle().equals(title)) {
                                User user1 = IMDB.getInstance().findUserWhoAddedProduction(title);
                                if(IMDB.getInstance().getProductionByTitle(title, IMDB.getInstance().getProductions()) instanceof Movie) {
                                    user1.notifications.add("Filmul \"" + title + "\" pe care l-ai adaugat la favorite a primit un review de la utilizatorul \"" + user.getName() + "\"" + rating1.getScore());
                                } else {
                                    user1.notifications.add("Serialul \"" + title + "\" pe care l-ai adaugat la favorite a primit un review de la utilizatorul \"" + user.getName() + "\"" + rating1.getScore());
                                }
                            }
                        }
                    }
                }
            }
		} else if (choice == JOptionPane.NO_OPTION) {

            User currentUser = user;
            List<Rating> userRatings = IMDB.getInstance().getUserRatings(currentUser.getName()); // You need to implement getUserRatings in IMDB

            if (userRatings.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "You have no reviews to delete.");
                return;
            }

            JPanel panel = new JPanel(new GridLayout(0, 1));

            JComboBox<String> ratingsComboBox = new JComboBox<>();
            for (Production production : IMDB.getInstance().getProductions()) {
                for (Rating rating : production.getRatings()) {
                    if (rating.getUsername().equals(currentUser.getName())) {
                        ratingsComboBox.addItem(production.title);
                    }
                }
            }
            panel.add(new JLabel("Select a review to delete:"));
            panel.add(ratingsComboBox);

            int result = JOptionPane.showConfirmDialog(dialog, panel, "Delete Review", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String selectedRating = Objects.requireNonNull(ratingsComboBox.getSelectedItem()).toString();

                // TODO: Implement the logic to delete the selected review
                // This might involve removing the rating from the production's ratings list
                // and updating the data store or model.
                List<Production> productions = IMDB.getInstance().getProductions();
                for (Production production : productions) {
                    for (Rating rating : production.ratings) {
                        if (rating.getUsername().equals(currentUser.getName())) {
                            production.ratings.remove(rating);
                        }
                    }
                }

            }


		}
	}

    private void seeFavorites() {
        // Create a dialog frame
        JDialog dialog = new JDialog(this, "Favorites", true);
        dialog.setLayout(new BorderLayout());

        // Create a tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab for favorite actors
        DefaultListModel<String> actorsListModel = new DefaultListModel<>();
        for (Comparable favorite : user.favorites) {
            if (favorite instanceof Actor actor) {
                actorsListModel.addElement(actor.getName());
            }
        }
        JList<String> actorsList = new JList<>(actorsListModel);
        JScrollPane actorsScrollPane = new JScrollPane(actorsList);
        tabbedPane.addTab("Actors", actorsScrollPane);

        // Tab for favorite movies
        DefaultListModel<String> moviesListModel = new DefaultListModel<>();
        for (Comparable favorite : user.favorites) {
            if (favorite instanceof Movie movie) {
                moviesListModel.addElement(movie.getTitle());
            }
        }
        JList<String> moviesList = new JList<>(moviesListModel);
        JScrollPane moviesScrollPane = new JScrollPane(moviesList);
        tabbedPane.addTab("Movies", moviesScrollPane);

        // Tab for favorite series
        DefaultListModel<String> seriesListModel = new DefaultListModel<>();
        for (Comparable favorite : user.favorites) {
            if (favorite instanceof Series series) {
                seriesListModel.addElement(series.getTitle());
            }
        }
        JList<String> seriesList = new JList<>(seriesListModel);
        JScrollPane seriesScrollPane = new JScrollPane(seriesList);
        tabbedPane.addTab("Series", seriesScrollPane);

        // Add tabbed pane to dialog
        dialog.add(tabbedPane, BorderLayout.CENTER);

        // Display the dialog
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    @Override
    public void createRequest(Request r) {
        IMDB.RequestsHolder.addRequest(r);
    }

    @Override
    public void removeRequest(Request r) {
        IMDB.RequestsHolder.removeRequest(r);
    }
}




