package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.UUID;

public class AdminFrame extends JFrame implements RequestsManager,Observer,StaffInterface{

	ContributionExperience contributionExperience = new ContributionExperience();

	RequestExperience requestExperience = new RequestExperience();

	private List<Observer> observers = IMDB.getInstance().getObservers();
	List<Request> requests = IMDB.getInstance().getRequests();
	private User user;
	private JLabel imdbLabel;
	private JLabel userDetails;
	private JLabel experienceLabel;
	private JPanel headerPanel;
	private JButton logoutButton;
	private  JPanel panelMain;
	JPanel actionsPanel;

	public AdminFrame(User user) {
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
				"Search for actor/production","Add/Delete actor/production to/from favorites","Add/Delete actor/movie/series from system","Solve a request","Update Movie/Actor Details","Add/Delete user","See favorites"};

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
			case "Add/Delete actor/movie/series from system":
				addOrDeleteActorOrMovieOrSeries();
				break;
			case "Solve a request":
				solveRequest();
				break;
			case "Update Movie/Actor Details":
				updateMovieOrActorDetails();
				break;
			case "Add/Delete user":
				addOrDeleteUser();
				break;
			case "See favorites":
				seeFavorites();
				break;
			default:
				throw new IllegalArgumentException("Unknown action: " + actionCommand);
		}
	}

	@Override
	public void update(String message) {
		// Handle the notification message

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


	private void showRequestForm(JDialog dialog, RegularFrame.RequestAction action) {
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
	public void addProductionToSystem(Production p) {
		List<Production> productions = IMDB.getInstance().getProductions();
		if(productions.contains(p)){
			JOptionPane.showMessageDialog(this, "Production already exists in the system.");
			return;
		} else {
			productions.add(p);
			JOptionPane.showMessageDialog(this, "Production added successfully.");
		}
	}

	@Override
	public void addActorToSystem(Actor a) {
		List<Actor> actors = IMDB.getInstance().getActors();
		if(actors.contains(a)){
			JOptionPane.showMessageDialog(this, "Actor already exists in the system.");
			return;
		} else {
			actors.add(a);
			JOptionPane.showMessageDialog(this, "Actor added successfully.");
		}
	}

	@Override
	public void removeProductionFromSystem(String name) {
		List<Production> productions = IMDB.getInstance().getProductions();
		Production productionToRemove = IMDB.getInstance().getProductionByTitle(name, productions);

		// Check if the production exists in the system
		if (productionToRemove == null) {
			JOptionPane.showMessageDialog(this, "Production does not exist in the system.");
			return;
		} else {
			productions.remove(productionToRemove);
			JOptionPane.showMessageDialog(this, "Production removed successfully.");
		}
	}

	@Override
	public void removeActorFromSystem(String name) {
		List<Actor> actors = IMDB.getInstance().getActors();
		Actor actorToRemove = IMDB.getInstance().getActorByName(name, actors);

		// Check if the actor exists in the system
		if (actorToRemove == null) {
			JOptionPane.showMessageDialog(this, "Actor does not exist in the system.");
			return;
		} else {
			actors.remove(actorToRemove);
			JOptionPane.showMessageDialog(this, "Actor removed successfully.");
		}
	}

	@Override
	public void updateProduction(Production p) {
		if (p instanceof Movie movie) {
			// let the user choose what to update
			String[] options = {"Title", "Directors", "Actors", "Genres", "Description", "Average rating", "Duration", "Release year"};
			int response = JOptionPane.showOptionDialog(this,
					"What would you like to update?",
					"Select Type",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null,
					options,
					options[0]);

			if (response == 0) { // Title selected
				String newTitle = JOptionPane.showInputDialog(this, "Enter the new title of the movie:");
				if (newTitle != null && !newTitle.trim().isEmpty()) {
					movie.setTitle(newTitle);
					JOptionPane.showMessageDialog(this, "Movie title updated successfully.");
				}
			} else if (response == 1) { // Directors selected
				String newDirectors = JOptionPane.showInputDialog(this, "Enter the new directors of the movie:");
				if (newDirectors != null && !newDirectors.trim().isEmpty()) {
					String[] directors = newDirectors.split(",");
					movie.setDirectors(directors);
					JOptionPane.showMessageDialog(this, "Movie directors updated successfully.");
				}
			} else if (response == 2) { // Actors selected
				String newActors = JOptionPane.showInputDialog(this, "Enter the new actors of the movie:");
				if (newActors != null && !newActors.trim().isEmpty()) {
					String[] actors = newActors.split(",");
					movie.setActors(actors);
					JOptionPane.showMessageDialog(this, "Movie actors updated successfully.");
				}
			} else if (response == 3) { // Genres selected
				String newGenres = JOptionPane.showInputDialog(this, "Enter the new genres of the movie:");
				if (newGenres != null && !newGenres.trim().isEmpty()) {
					String[] genres = newGenres.split(",");
					List<Genre> genresList = new ArrayList<>();
					for (String genre : genres) {
						genresList.add(Genre.valueOf(genre));
					}
					movie.setGenres(genresList);
					JOptionPane.showMessageDialog(this, "Movie genres updated successfully.");
				}
			} else if (response == 4) { // Description selected
				String newDescription = JOptionPane.showInputDialog(this, "Enter the new description of the movie:");
				if (newDescription != null && !newDescription.trim().isEmpty()) {
					movie.setDescription(newDescription);
					JOptionPane.showMessageDialog(this, "Movie description updated successfully.");
				}
			} else if (response == 6) { // Duration selected
				String newDuration = JOptionPane.showInputDialog(this, "Enter the new duration of the movie:");
				if (newDuration != null && !newDuration.trim().isEmpty()) {
					movie.setDuration(Integer.parseInt(newDuration));
					JOptionPane.showMessageDialog(this, "Movie duration updated successfully.");
				}
			} else if (response == 7) { // Release year selected
				String newReleaseYear = JOptionPane.showInputDialog(this, "Enter the new release year of the movie:");
				if (newReleaseYear != null && !newReleaseYear.trim().isEmpty()) {
					movie.setReleaseYear(Integer.parseInt(newReleaseYear));
					JOptionPane.showMessageDialog(this, "Movie release year updated successfully.");
				}
			}
		} else {
			Series series = (Series) p;
			// let the user choose what to update
			String[] options = {"Title", "Directors", "Actors", "Genres", "Description", "Average rating", "Seasons"};
			int response = JOptionPane.showOptionDialog(this,
					"What would you like to update?",
					"Select Type",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null,
					options,
					options[0]);

			if (response == 0) { // Title selected
				String newTitle = JOptionPane.showInputDialog(this, "Enter the new title of the series:");
				if (newTitle != null && !newTitle.trim().isEmpty()) {
					series.setTitle(newTitle);
					JOptionPane.showMessageDialog(this, "Series title updated successfully.");
				}
			} else if (response == 1) { // Directors selected
				String newDirectors = JOptionPane.showInputDialog(this, "Enter the new directors of the series:");
				if (newDirectors != null && !newDirectors.trim().isEmpty()) {
					String[] directors = newDirectors.split(",");
					series.setDirectors(directors);
					JOptionPane.showMessageDialog(this, "Series directors updated successfully.");
				}
			} else if (response == 2) { // Actors selected
				String newActors = JOptionPane.showInputDialog(this, "Enter the new actors of the series:");
				if (newActors != null && !newActors.trim().isEmpty()) {
					String[] actors = newActors.split(",");
					series.setActors(actors);
					JOptionPane.showMessageDialog(this, "Series actors updated successfully.");
				}
			} else if (response == 3) { // Genres selected
				String newGenres = JOptionPane.showInputDialog(this, "Enter the new genres of the series:");
				if (newGenres != null && !newGenres.trim().isEmpty()) {
					String[] genres = newGenres.split(",");
					List<Genre> genresList = new ArrayList<>();
					for (String genre : genres) {
						genresList.add(Genre.valueOf(genre));
					}
					series.setGenres(genresList);
					JOptionPane.showMessageDialog(this, "Series genres updated successfully.");
				}
			} else if (response == 4) { // Description selected
				String newDescription = JOptionPane.showInputDialog(this, "Enter the new description of the series:");
				if (newDescription != null && !newDescription.trim().isEmpty()) {
					series.setDescription(newDescription);
					JOptionPane.showMessageDialog(this, "Series description updated successfully.");
				}
			} else if (response == 6) { // Seasons selected
				// let the user choose what to update
				String[] seasonOptions = {"Add", "Delete"};
				int seasonResponse = JOptionPane.showOptionDialog(this,
						"What would you like to do?",
						"Select Type",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE,
						null,
						seasonOptions,
						seasonOptions[0]);
				if (seasonResponse == 0) { // Add selected
					String season = JOptionPane.showInputDialog(this, "Enter the season of the series:");
					if (season != null && !season.trim().isEmpty()) {
						String episode = JOptionPane.showInputDialog(this, "Enter the episode of the series:");
						if (episode != null && !episode.trim().isEmpty()) {
							String duration = JOptionPane.showInputDialog(this, "Enter the duration of the series:");
							if (duration != null && !duration.trim().isEmpty()) {
								Episode newEpisode = new Episode(episode, Integer.parseInt(duration));
								series.addEpisode(season, newEpisode);
								JOptionPane.showMessageDialog(this, "Series episode added successfully.");
							}
						}
					}
				} else if (seasonResponse == 1) { // Delete selected
					String season = JOptionPane.showInputDialog(this, "Enter the season of the series:");
					if (season != null && !season.trim().isEmpty()) {
						String episode = JOptionPane.showInputDialog(this, "Enter the episode of the series:");
						if (episode != null && !episode.trim().isEmpty()) {
							series.deleteEpisode(season, episode);
							JOptionPane.showMessageDialog(this, "Series episode deleted successfully.");
						}
					}
				}
			}
		}
	}

	@Override
	public void updateActor(Actor a) {
		// let the user choose what to update
		String[] options = {"Name", "Biography", "Roles"};
		int response = JOptionPane.showOptionDialog(this,
				"What would you like to update?",
				"Select Type",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null,
				options,
				options[0]);

		if (response == 0) { // Name selected
			String newName = JOptionPane.showInputDialog(this, "Enter the new name of the actor:");
			if (newName != null && !newName.trim().isEmpty()) {
				a.setName(newName);
				JOptionPane.showMessageDialog(this, "Actor name updated successfully.");
			}
		} else if (response == 1) { // Biography selected
			String newBiography = JOptionPane.showInputDialog(this, "Enter the new biography of the actor:");
			if (newBiography != null && !newBiography.trim().isEmpty()) {
				a.setBiography(newBiography);
				JOptionPane.showMessageDialog(this, "Actor biography updated successfully.");
			}
		} else if (response == 2) { // Roles selected
			// ask if the user wants to add or delete a role
			String[] roleOptions = {"Add", "Delete"};
			int roleResponse = JOptionPane.showOptionDialog(this,
					"What would you like to do?",
					"Select Type",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null,
					roleOptions,
					roleOptions[0]);
			if (roleResponse == 0) { // Add selected
				String role = JOptionPane.showInputDialog(this, "Enter the role of the actor:");
				if (role != null && !role.trim().isEmpty()) {
					String type = JOptionPane.showInputDialog(this, "Enter the type of the role:");
					if (type != null && !type.trim().isEmpty()) {
						Map<String, String> roles = new HashMap<>();
						roles.put(role,type);
						a.addRole(roles);
						JOptionPane.showMessageDialog(this, "Actor role added successfully.");
					}
				}
			} else if (roleResponse == 1) { // Delete selected
				String role = JOptionPane.showInputDialog(this, "Enter the role of the actor:");
				if (role != null && !role.trim().isEmpty()) {
					a.deleteRole(role);
					JOptionPane.showMessageDialog(this, "Actor role deleted successfully.");
				}
			}
		}
	}




	enum RequestAction {
		ADD, DELETE
	}

	private void addOrDeleteActorOrMovieOrSeries() {
		JDialog dialog = new JDialog(this, "Add/Delete Actor/Production", true);
		dialog.setSize(400, 300); // Set dialog size
		dialog.setLayout(new BorderLayout());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = screenSize.width / 2 - dialog.getWidth() / 2;
		int y = screenSize.height / 2 - dialog.getHeight() / 2;

		dialog.setLocation(x, y);

// Radio buttons for add or delete choice
		JRadioButton addRadioButton = new JRadioButton("Add to system", true);
		JRadioButton deleteRadioButton = new JRadioButton("Delete from system");

// Group the radio buttons
		ButtonGroup group = new ButtonGroup();
		group.add(addRadioButton);
		group.add(deleteRadioButton);

// Panel for radio buttons
		JPanel radioPanel = new JPanel(new FlowLayout());
		radioPanel.add(addRadioButton);
		radioPanel.add(deleteRadioButton);
		dialog.add(radioPanel, BorderLayout.NORTH);

		JButton performActionButton = new JButton("Perform Action");
		dialog.add(performActionButton, BorderLayout.SOUTH);


		// Action listener for the performActionButton
		performActionButton.addActionListener(e -> {
			if (addRadioButton.isSelected()) {
				// Add code here to add an actor or production to the system
				String[] options = {"Actor", "Production"};
				int response = JOptionPane.showOptionDialog(dialog,
						"What would you like to add?",
						"Select Type",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE,
						null,
						options,
						options[0]);

				if (response == 0) { // Actor selected
					// Dialogs for adding actor
					String actorName = JOptionPane.showInputDialog(dialog, "Enter the name of the actor:");
					// ... additional dialogs for actor's details
					if (actorName != null && !actorName.trim().isEmpty()) {
						// Create and add actor to the system
						String actorBiography = JOptionPane.showInputDialog(dialog, "Enter the biography of the actor:");
						if (actorBiography != null && !actorBiography.trim().isEmpty()) {
							String actorPerfomance = JOptionPane.showInputDialog(dialog, "Enter the perfomance of the actor:");
							if (actorPerfomance != null && !actorPerfomance.trim().isEmpty()) {
								String typePerfomance = JOptionPane.showInputDialog(dialog, "Enter the type of perfomance of the actor:");
								if (typePerfomance != null && !typePerfomance.trim().isEmpty()) {
									Map<String, String> roles = new HashMap<>();
									roles.put(actorPerfomance, typePerfomance);
									Actor actor = new Actor(actorName, roles, actorBiography);
									user.contributions.add(actor);
									addActorToSystem(actor);
									int newExperience = Integer.parseInt(user.getExperience()) + contributionExperience.calculateExperience();
									user.setExperience(String.valueOf(newExperience));
									dialog.dispose();
								}
							}
						}
					}
				} else if (response == 1) { // Production selected
					String[] prodOptions = {"Movie", "Series"};
					int prodResponse = JOptionPane.showOptionDialog(dialog,
							"What would you like to add?",
							"Select Type",
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE,
							null,
							prodOptions,
							prodOptions[0]);
					if (prodResponse == 0) { // Movie selected
						// Dialogs for adding movie
						String movieTitle = JOptionPane.showInputDialog(dialog, "Enter the title of the movie:");
						// ... additional dialogs for movie's details
						if (movieTitle != null && !movieTitle.trim().isEmpty()) {
							// Create and add movie to the system
							List<String> directors = new ArrayList<>();
							// read directors
							String[] directorOptions = {"Yes", "No"};
							int directorResponse = JOptionPane.showOptionDialog(dialog,
									"Would you like to add a director?",
									"Select Type",
									JOptionPane.DEFAULT_OPTION,
									JOptionPane.INFORMATION_MESSAGE,
									null,
									directorOptions,
									directorOptions[0]);
							while (directorResponse == 0) {
								String directorName = JOptionPane.showInputDialog(dialog, "Enter the name of the director:");
								if (directorName != null && !directorName.trim().isEmpty()) {
									directors.add(directorName);
								}
								directorResponse = JOptionPane.showOptionDialog(dialog,
										"Would you like to add another director?",
										"Select Type",
										JOptionPane.DEFAULT_OPTION,
										JOptionPane.INFORMATION_MESSAGE,
										null,
										directorOptions,
										directorOptions[0]);
							}
							List<String> actors = new ArrayList<>();
							// read actors
							String[] actorOptions = {"Yes", "No"};
							int actorResponse = JOptionPane.showOptionDialog(dialog,
									"Would you like to add an actor?",
									"Select Type",
									JOptionPane.DEFAULT_OPTION,
									JOptionPane.INFORMATION_MESSAGE,
									null,
									actorOptions,
									actorOptions[0]);
							while (actorResponse == 0) {
								String actorName = JOptionPane.showInputDialog(dialog, "Enter the name of the actor:");
								if (actorName != null && !actorName.trim().isEmpty()) {
									actors.add(actorName);
								}
								actorResponse = JOptionPane.showOptionDialog(dialog,
										"Would you like to add another actor?",
										"Select Type",
										JOptionPane.DEFAULT_OPTION,
										JOptionPane.INFORMATION_MESSAGE,
										null,
										actorOptions,
										actorOptions[0]);

							}
							List<Genre> genres = new ArrayList<>();
							// read genres
							String[] genreOptions = {"Yes", "No"};
							int genreResponse = JOptionPane.showOptionDialog(dialog,
									"Would you like to add a genre?",
									"Select Type",
									JOptionPane.DEFAULT_OPTION,
									JOptionPane.INFORMATION_MESSAGE,
									null,
									genreOptions,
									genreOptions[0]);
							while (genreResponse == 0) {
								String genreName = JOptionPane.showInputDialog(dialog, "Enter the name of the genre:");
								if (genreName != null && !genreName.trim().isEmpty()) {
									genres.add(Genre.valueOf(genreName));
								}
								genreResponse = JOptionPane.showOptionDialog(dialog,
										"Would you like to add another genre?",
										"Select Type",
										JOptionPane.DEFAULT_OPTION,
										JOptionPane.INFORMATION_MESSAGE,
										null,
										genreOptions,
										genreOptions[0]);
							}
							String description = JOptionPane.showInputDialog(dialog, "Enter the description of the movie:");
							if (description != null && !description.trim().isEmpty()) {
								String duration = JOptionPane.showInputDialog(dialog, "Enter the duration of the movie:");
								if (duration != null && !duration.trim().isEmpty()) {
									String releaseYear = JOptionPane.showInputDialog(dialog, "Enter the release year of the movie:");
									if (releaseYear != null && !releaseYear.trim().isEmpty()) {
										// average rating is 0 by default
										Movie movie = new Movie(movieTitle, directors, actors, genres,null, description, (double) 0, Integer.parseInt(duration), Integer.parseInt(releaseYear));
										user.contributions.add(movie);
										addProductionToSystem(movie);
										int newExperience = Integer.parseInt(user.getExperience()) + contributionExperience.calculateExperience();
										user.setExperience(String.valueOf(newExperience));
										dialog.dispose();
									}
								}
							}


						}
					} else if (prodResponse == 1) { // Series selected
						// Dialogs for adding series
						String seriesTitle = JOptionPane.showInputDialog(dialog, "Enter the title of the series:");
						// ... additional dialogs for series' details
						if (seriesTitle != null && !seriesTitle.trim().isEmpty()) {
							// Create and add series to the system
							List<String> directors = new ArrayList<>();
							// read directors
							String[] directorOptions = {"Yes", "No"};
							int directorResponse = JOptionPane.showOptionDialog(dialog,
									"Would you like to add a director?",
									"Select Type",
									JOptionPane.DEFAULT_OPTION,
									JOptionPane.INFORMATION_MESSAGE,
									null,
									directorOptions,
									directorOptions[0]);
							while (directorResponse == 0) {
								String directorName = JOptionPane.showInputDialog(dialog, "Enter the name of the director:");
								if (directorName != null && !directorName.trim().isEmpty()) {
									directors.add(directorName);
								}
								directorResponse = JOptionPane.showOptionDialog(dialog,
										"Would you like to add another director?",
										"Select Type",
										JOptionPane.DEFAULT_OPTION,
										JOptionPane.INFORMATION_MESSAGE,
										null,
										directorOptions,
										directorOptions[0]);
							}
							List<String> actors = new ArrayList<>();
							// read actors
							String[] actorOptions = {"Yes", "No"};
							int actorResponse = JOptionPane.showOptionDialog(dialog,
									"Would you like to add an actor?",
									"Select Type",
									JOptionPane.DEFAULT_OPTION,
									JOptionPane.INFORMATION_MESSAGE,
									null,
									actorOptions,
									actorOptions[0]);
							while (actorResponse == 0) {
								String actorName = JOptionPane.showInputDialog(dialog, "Enter the name of the actor:");
								if (actorName != null && !actorName.trim().isEmpty()) {
									actors.add(actorName);
								}
								actorResponse = JOptionPane.showOptionDialog(dialog,
										"Would you like to add another actor?",
										"Select Type",
										JOptionPane.DEFAULT_OPTION,
										JOptionPane.INFORMATION_MESSAGE,
										null,
										actorOptions,
										actorOptions[0]);

							}
							List<Genre> genres = new ArrayList<>();
							// read genres
							String[] genreOptions = {"Yes", "No"};
							int genreResponse = JOptionPane.showOptionDialog(dialog,
									"Would you like to add a genre?",
									"Select Type",
									JOptionPane.DEFAULT_OPTION,
									JOptionPane.INFORMATION_MESSAGE,
									null,
									genreOptions,
									genreOptions[0]);
							while (genreResponse == 0) {
								String genreName = JOptionPane.showInputDialog(dialog, "Enter the name of the genre:");
								if (genreName != null && !genreName.trim().isEmpty()) {
									genres.add(Genre.valueOf(genreName));
								}
								genreResponse = JOptionPane.showOptionDialog(dialog,
										"Would you like to add another genre?",
										"Select Type",
										JOptionPane.DEFAULT_OPTION,
										JOptionPane.INFORMATION_MESSAGE,
										null,
										genreOptions,
										genreOptions[0]);
							}
							String releaseYear = JOptionPane.showInputDialog(dialog, "Enter the release year of the series:");
							if (releaseYear != null && !releaseYear.trim().isEmpty()) {
								String description = JOptionPane.showInputDialog(dialog, "Enter the description of the series:");
								if (description != null && !description.trim().isEmpty()) {
									String numberOfSeasons = JOptionPane.showInputDialog(dialog, "Enter the number of seasons of the series:");
									if (numberOfSeasons != null && !numberOfSeasons.trim().isEmpty()) {
										Map<String, List<Episode>> seasons = new HashMap<>();
										for (int i = 0; i < Integer.parseInt(numberOfSeasons); i++) {
											String seasonName = JOptionPane.showInputDialog(dialog, "Enter the name of the season:");
											if (seasonName != null && !seasonName.trim().isEmpty()) {
												String numberOfEpisodes = JOptionPane.showInputDialog(dialog, "Enter the number of episodes of the season:");
												if (numberOfEpisodes != null && !numberOfEpisodes.trim().isEmpty()) {
													List<Episode> episodes = new ArrayList<>();
													for (int j = 0; j < Integer.parseInt(numberOfEpisodes); j++) {
														String episodeName = JOptionPane.showInputDialog(dialog, "Enter the name of the episode:");
														if (episodeName != null && !episodeName.trim().isEmpty()) {
															String episodeDuration = JOptionPane.showInputDialog(dialog, "Enter the duration of the episode:");
															if (episodeDuration != null && !episodeDuration.trim().isEmpty()) {
																Episode episode = new Episode(episodeName, Integer.parseInt(episodeDuration));
																episodes.add(episode);
															}
														}
													}
													seasons.put(seasonName, episodes);
												}
											}
										}
										// average rating is 0 by default
										Series series = new Series(seriesTitle, directors, actors, genres, null, description, (double) 0, Integer.parseInt(releaseYear), Integer.parseInt(numberOfSeasons), seasons);
										user.contributions.add(series);
										addProductionToSystem(series);
										int newExperience = Integer.parseInt(user.getExperience()) + contributionExperience.calculateExperience();
										user.setExperience(String.valueOf(newExperience));
										dialog.dispose();
									}
								}
							}
						}
					}
				}
			} else if (deleteRadioButton.isSelected()) {
				// Add code here to delete an actor or production from the system
				String[] options = {"Actor", "Production"};
				int response = JOptionPane.showOptionDialog(dialog,
						"What would you like to delete?",
						"Select Type",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE,
						null,
						options,
						options[0]);

				if (response == 0) { // Actor selected
					// Dialogs for deleting actor, let him choose from a list of actors
					List<Actor> actors = IMDB.getInstance().getActors();
					DefaultComboBoxModel<Actor> actorModel = new DefaultComboBoxModel<>();
					for (Actor actor : actors) {
						actorModel.addElement(actor);
					}
					JComboBox<Actor> actorComboBox = new JComboBox<>(actorModel);
					JOptionPane.showMessageDialog(dialog, actorComboBox, "Select an actor to delete", JOptionPane.QUESTION_MESSAGE);
					Actor selectedActor = (Actor) actorComboBox.getSelectedItem();
					if (selectedActor != null) {
						// Delete actor from the system
						// check if the actor was added by this user
						if (IMDB.getInstance().findUserWhoAddedActor(selectedActor.getName()).getName().equals(user.getName())) {
							removeActorFromSystem(selectedActor.getName());
							dialog.dispose();
						} else {
							JOptionPane.showMessageDialog(dialog, "You can't delete this actor because you didn't add it to the system.");
						}
					}
				} else if (response == 1) { // Production selected
					// same as actors, just let him choose from a list of productions
					List<Production> productions = IMDB.getInstance().getProductions();
					DefaultComboBoxModel<Production> productionModel = new DefaultComboBoxModel<>();
					for (Production production : productions) {
						productionModel.addElement(production);
					}
					JComboBox<Production> productionComboBox = new JComboBox<>(productionModel);
					JOptionPane.showMessageDialog(dialog, productionComboBox, "Select a production to delete", JOptionPane.QUESTION_MESSAGE);
					Production selectedProduction = (Production) productionComboBox.getSelectedItem();
					if (selectedProduction != null) {
						// Delete production from the system
						// check if the production was added by this user
						if (IMDB.getInstance().findUserWhoAddedProduction(selectedProduction.getTitle()).getName().equals(user.getName())) {
							removeProductionFromSystem(selectedProduction.getTitle());
							dialog.dispose();
						} else {
							JOptionPane.showMessageDialog(dialog, "You can't delete this production because you didn't add it to the system.");
						}
					}
				}
			}

		});

		dialog.pack();
		dialog.setVisible(true);
	}

	private void solveRequest() {
		JDialog dialog = new JDialog(this, "Solve Request", true);
		dialog.setLayout(new BorderLayout());
		dialog.setSize(300, 400);

		List<Request> userRequests = IMDB.getInstance().getRequestsForUserIncludingAdmin(user.name);
		DefaultListModel<String> listModel = new DefaultListModel<>();
		for (Request request : userRequests) {
			listModel.addElement(request.getDescription());
		}

		JList<String> requestsList = new JList<>(listModel);
		JScrollPane listScroller = new JScrollPane(requestsList);
		dialog.add(listScroller, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton getDetailsButton = new JButton("Get Details");
		JButton solveButton = new JButton("Solve Request");
		JButton rejectButton = new JButton("Reject Request"); // New button for rejecting the request
		buttonPanel.add(getDetailsButton);
		buttonPanel.add(solveButton);
		buttonPanel.add(rejectButton); // Add the reject button to the panel
		dialog.add(buttonPanel, BorderLayout.SOUTH);

		getDetailsButton.addActionListener(e -> {
			int selectedIndex = requestsList.getSelectedIndex();
			if (selectedIndex != -1) {
				Request selectedRequest = userRequests.get(selectedIndex);
				JOptionPane.showMessageDialog(dialog, selectedRequest.toString(), "Request Details", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		solveButton.addActionListener(e -> {
			int selectedIndex = requestsList.getSelectedIndex();
			if (selectedIndex != -1) {
				Request selectedRequest = userRequests.get(selectedIndex);
				String notificationMessage = "Cererea \"" + selectedRequest.getRequestType() + "\" a fost rezolvata de utilizatorul \"" + user.name + "\"";
				userRequests.remove(selectedRequest); // Simply remove the request without further processing
				listModel.removeElementAt(selectedIndex); // Update the list model
				// add experience to the user who made the request, first get the user who made the request
				User requestUser = IMDB.getInstance().findUserWhoAddedRequest(selectedRequest);
				int newExperience = Integer.parseInt(requestUser.getExperience()) + requestExperience.calculateExperience();
				requestUser.setExperience(String.valueOf(newExperience));
				requestUser.notifications.add(notificationMessage);
				// Add logic to mark the request as solved
				dialog.dispose();
			}
		});

		rejectButton.addActionListener(e -> {
			int selectedIndex = requestsList.getSelectedIndex();
			if (selectedIndex != -1) {
				Request selectedRequest = userRequests.get(selectedIndex);
				String notificationMessage = "Cererea \"" + selectedRequest.getRequestType() + "\" a fost respinsa de utilizatorul \"" + user.name + "\"";
				User requestUser = IMDB.getInstance().findUserWhoAddedRequest(selectedRequest);
				requestUser.notifications.add(notificationMessage);
				userRequests.remove(selectedRequest); // Simply remove the request without further processing
				listModel.removeElementAt(selectedIndex); // Update the list model
			}
		});

		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}


	private void updateMovieOrActorDetails() {
		JDialog dialog = new JDialog(this, "Update Movie/Actor Details", true);
		dialog.setSize(400, 300); // Set dialog size
		dialog.setLayout(new BorderLayout());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = screenSize.width / 2 - dialog.getWidth() / 2;
		int y = screenSize.height / 2 - dialog.getHeight() / 2;

		dialog.setLocation(x, y);

		String[] options = {"Actor", "Production"};
		int response = JOptionPane.showOptionDialog(dialog,
				"What would you like to update?",
				"Select Type",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null,
				options,
				options[0]);

		if (response == 0) { // Actor selected
			// Dialogs for updating actor, let him choose from a list of actors
			List<Actor> actors = IMDB.getInstance().getActors();
			DefaultComboBoxModel<Actor> actorModel = new DefaultComboBoxModel<>();
			for (Actor actor : actors) {
				actorModel.addElement(actor);
			}
			JComboBox<Actor> actorComboBox = new JComboBox<>(actorModel);
			JOptionPane.showMessageDialog(dialog, actorComboBox, "Select an actor to update", JOptionPane.QUESTION_MESSAGE);
			Actor selectedActor = (Actor) actorComboBox.getSelectedItem();
			if (selectedActor != null) {
				// check if the actor was added by this user
				if (IMDB.getInstance().findUserWhoAddedActor(selectedActor.getName()).getName().equals(user.getName())) {
					updateActor(selectedActor);
				} else {
					JOptionPane.showMessageDialog(dialog, "You can't update this actor because you didn't add it to the system.");
				}

			}

		} else if (response == 1) { // Production selected
			// same as actors, just let him choose from a list of productions
			List<Production> productions = IMDB.getInstance().getProductions();
			DefaultComboBoxModel<Production> productionModel = new DefaultComboBoxModel<>();
			for (Production production : productions) {
				productionModel.addElement(production);
			}
			JComboBox<Production> productionComboBox = new JComboBox<>(productionModel);
			JOptionPane.showMessageDialog(dialog, productionComboBox, "Select a production to update", JOptionPane.QUESTION_MESSAGE);
			Production selectedProduction = (Production) productionComboBox.getSelectedItem();
			if (selectedProduction != null) {
				// Update production from the system
				// check if the production was added by this user
				if (IMDB.getInstance().findUserWhoAddedProduction(selectedProduction.getTitle()).getName().equals(user.getName())) {
					// Dialogs for updating production
					updateProduction(selectedProduction);
				}
			}
		}

	}

	private void addOrDeleteUser() {
		JDialog dialog = new JDialog(this, "Add or Delete User", true);
		dialog.setLayout(new FlowLayout());

		JButton addButton = new JButton("Add User");
		JButton deleteButton = new JButton("Delete User");

		dialog.add(addButton);
		dialog.add(deleteButton);

		addButton.addActionListener(e -> {
			// Crează un nou dialog pentru adăugarea unui utilizator
			JDialog addUserDialog = new JDialog(dialog, "Add New User", true);
			addUserDialog.setLayout(new FlowLayout());

			// Opțiuni pentru tipul de utilizator
			String[] userTypes = {"Regular", "Admin", "Contributor"};
			JComboBox<String> userTypeComboBox = new JComboBox<>(userTypes);
			addUserDialog.add(new JLabel("Select User Type:"));
			addUserDialog.add(userTypeComboBox);

			// Câmp pentru numele complet al utilizatorului
			JTextField nameField = new JTextField(20);
			addUserDialog.add(new JLabel("Enter Full Name:"));
			addUserDialog.add(nameField);

			// Buton pentru finalizarea adăugării
			JButton finalizeAddButton = new JButton("Add User");
			addUserDialog.add(finalizeAddButton);

			finalizeAddButton.addActionListener(f -> {
				String selectedUserType = (String) userTypeComboBox.getSelectedItem();
				String fullName = nameField.getText().trim();

				// Generați un username unic și o parolă puternică
				String username = generateUniqueUsername(fullName);
				String password = generateStrongPassword();

				User.Information info = new User.Information.Builder(username, password).build();
				User user = UserFactory.createUser(AccountType.valueOf(selectedUserType), fullName, String.valueOf(0), info);
				IMDB.getInstance().getUsers().add(user);
				JOptionPane.showMessageDialog(addUserDialog, "User added successfully.");
				addUserDialog.dispose();
			});

			addUserDialog.pack();
			addUserDialog.setVisible(true);
		});
		deleteButton.addActionListener(e -> {
			// Crează un nou dialog pentru ștergerea unui utilizator
			JDialog deleteUserDialog = new JDialog(dialog, "Delete User", true);
			deleteUserDialog.setLayout(new FlowLayout());

			// Obțineți lista de utilizatori existenți
			List<User> users = IMDB.getInstance().getUsers();

			// Model pentru JComboBox
			DefaultComboBoxModel<String> userModel = new DefaultComboBoxModel<>();
			for (User user : users) {
				userModel.addElement(user.name);
			}

			// Combobox pentru selectarea utilizatorilor
			JComboBox<String> userComboBox = new JComboBox<>(userModel);
			deleteUserDialog.add(new JLabel("Select User:"));
			deleteUserDialog.add(userComboBox);

			// Buton pentru confirmarea ștergerii
			JButton confirmDeleteButton = new JButton("Delete User");
			deleteUserDialog.add(confirmDeleteButton);

			confirmDeleteButton.addActionListener(f -> {
				String selectedUsername = (String) userComboBox.getSelectedItem();
				User userToDelete = IMDB.getInstance().getUserByUsername(selectedUsername);

				// Ștergeți utilizatorul selectat și actualizați referințele
				deleteUserAndUpdateReferences(userToDelete);

				deleteUserDialog.dispose();
			});

			deleteUserDialog.pack();
			deleteUserDialog.setVisible(true);
		});

		dialog.pack();
		dialog.setVisible(true);

	}

	private void deleteUserAndUpdateReferences(User userToDelete) {
		if (userToDelete instanceof Contributor) {
			reassignContributionsToAdmin(userToDelete);
		}

		// Ștergeți utilizatorul din lista de utilizatori
		IMDB.getInstance().getUsers().remove(userToDelete);

		List<Production> productions = IMDB.getInstance().getProductions();
		// Ștergeți review-urile utilizatorului din producții
		for (Production production : productions) {
			production.ratings.removeIf(rating -> rating.getUsername().equals(userToDelete.name));
		}

		//sterge requesturile utilizatorului
		List<Request> requests = IMDB.getInstance().getRequests();
		for (Request request : requests) {
			if (request.getCreatorUsername().equals(userToDelete.name)) {
				IMDB.getInstance().getRequests().remove(request);
			}
		}


	}

	private void reassignContributionsToAdmin(User userToDelete) {
		// Găsiți toti utilizatorii ADMIN
		List<User> admins = IMDB.getInstance().getUsers().stream().filter(user -> user instanceof Admin).toList();

		// atribuiți contribuțiile utilizatorului sters tuturor utilizatorilor ADMIN
		for (User admin : admins) {
			admin.contributions.addAll(userToDelete.contributions);
		}

		// Ștergeți contribuțiile utilizatorului șters
		userToDelete.contributions.clear();
	}

	private String generateUniqueUsername(String fullName) {
		UUID uuid = UUID.randomUUID();
		String username = fullName.toLowerCase().replaceAll("\\s+", "") + uuid.toString().substring(0, 8);
		return username;
	}

	public String generateStrongPassword(){
		Random random = new Random();
		String password = "";
		for (int i = 0; i < 10; i++) {
			int randomNumber = random.nextInt(128); // 128 is the number of ASCII characters
			char randomCharacter = (char) randomNumber;
			password += randomCharacter;
		}
		return password;
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
