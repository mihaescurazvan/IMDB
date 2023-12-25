import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OptionsGUI {
    private JFrame optionsFrame;
    private JFrame productionsFrame;
    private JScrollPane productionsScrollPane;
    private JLabel experienceLabel;
    private User user;

    public OptionsGUI(User user) {
        this.user = user;
    }

    public void displayOptions() {
        if (optionsFrame != null) {
            optionsFrame.dispose();
        }
        optionsFrame = new JFrame("IMDb User Options");
        optionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        optionsFrame.setPreferredSize(new Dimension(600, 800));
        optionsFrame.getContentPane().setBackground(new Color(61, 76, 102));

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(61, 76, 102));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;

        JLabel accountTypeLabel = new JLabel("Account Type: " + user.accountType);
        accountTypeLabel.setForeground(new Color(255, 255, 255));
        accountTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(accountTypeLabel, gbc);

        gbc.gridy++;
        experienceLabel = new JLabel("Experience: " + user.experience);
        experienceLabel.setForeground(new Color(255, 255, 255));
        experienceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        panel.add(experienceLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 10, 0);

        JButton[] optionButtons = createOptionButtons();
        for (JButton button : optionButtons) {
            panel.add(button, gbc);
            gbc.gridy++;
        }

        optionsFrame.add(panel);
        optionsFrame.pack();
        optionsFrame.setLocationRelativeTo(null);
        optionsFrame.setVisible(true);
    }


    private JButton[] createOptionButtons() {
        JButton[] optionButtons;
        if (user.accountType.equals(AccountType.ADMIN)) {
            optionButtons = new JButton[]{
                    createButton("View Productions"),
                    createButton("View Actors"),
                    createButton("View Notifications"),
                    createButton("Search Actor/Movie/Series"),
                    createButton("Manage Favorites"),
                    createButton("Manage System"),
                    createButton("View/Solve Requests"),
                    createButton("Update Details"),
                    createButton("Manage Users"),
                    createLogoutButton()
            };
        } else if (user.accountType.equals(AccountType.CONTRIBUTOR)) {
            optionButtons = new JButton[]{
                    createButton("View Productions"),
                    createButton("View Actors"),
                    createButton("View Notifications"),
                    createButton("Search Actor/Movie/Series"),
                    createButton("Manage Favorites"),
                    createButton("Create/Delete Request"),
                    createButton("Manage System"),
                    createButton("View/Solve Requests"),
                    createButton("Update Details"),
                    createLogoutButton()
            };
        } else {
            optionButtons = new JButton[]{
                    createButton("View Productions"),
                    createButton("View Actors"),
                    createButton("View Notifications"),
                    createButton("Search Actor/Movie/Series"),
                    createButton("Manage Favorites"),
                    createButton("Create/Delete Request"),
                    createButton("Add/Delete Rating"),
                    createLogoutButton()
            };
        }

        return optionButtons;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(new Color(61, 76, 102));
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(300, 40));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleOptionClick(text);
            }
        });
        return button;
    }

    private JButton createLogoutButton() {
        JButton button = new JButton("Logout");
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(new Color(61, 76, 102));
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(250, 40));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleOptionClick("Logout");
            }
        });
        return button;
    }

    private List<Production> getProductions() {
        return IMDB.getInstance().productions;
    }

    private void handleOptionClick(String option) {
        if (option.equals("Logout")) {
            optionsFrame.dispose();
            // give the user the option to sign in to another account or to exit the application
            String[] options = {"Sign in to another account", "Exit"};
            int optionIndex = JOptionPane.showOptionDialog(
                    optionsFrame,
                    "Select one of the following options:",
                    "Logout",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (optionIndex == 0) {
                GUI gui = new GUI();
                gui.start();
            } else {
                System.exit(0);
            }
        } else if (option.equals("View Productions")) {
            showProductions(getProductions());
        } else if (option.equals("View Actors")) {
            showActors(getActors());
        } else if (option.equals("View Notifications")) {
            viewNotifications();
        } else if (option.equals("Search Actor/Movie/Series")) {
            searchActorMovieSeries();
        } else if (option.equals("Manage Favorites")) {
            manageFavorites();
        } else if (option.equals("Create/Delete Request")) {
            handleRequestButtonClick();
        } else if (option.equals("Add/Delete Rating")) {
            handleRatingButtonClick();
        } else if (option.equals("Manage System")) {
            handleManageSystemButtonClick();
        } else if (option.equals("View/Solve Requests")) {
            handleViewSolveRequestsButtonClick();
        } else if (option.equals("Update Details")) {
            handleUpdateDetailsButtonClick();
        } else if (option.equals("Manage Users")) {
            handleManageUsersButtonClick();
        } else {
            JOptionPane.showMessageDialog(optionsFrame, option + " selected.");
        }
        experienceLabel.setText("Experience: " + user.experience);
    }

    private List<Actor> getActors() {
        return IMDB.getInstance().actors;
    }

    private void showActors(List<Actor> actors) {
        productionsFrame = new JFrame("Actors Information");
        productionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        productionsFrame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Build the text content for all actors
        StringBuilder actorsInfo = new StringBuilder();
        for (Actor actor : actors) {
            actorsInfo.append(actor.getDisplayInfo()).append("\n\n");
        }
        textArea.setText(actorsInfo.toString());
        productionsScrollPane = new JScrollPane(textArea);
        productionsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        productionsFrame.add(createSortingPanelForActors(), BorderLayout.NORTH);
        productionsFrame.add(productionsScrollPane, BorderLayout.CENTER);

        productionsFrame.setSize(800, 600);
        productionsFrame.setLocationRelativeTo(optionsFrame);
        productionsFrame.setVisible(true);
    }

    private JPanel createSortingPanelForActors() {
        JPanel sortingPanel = new JPanel();
        sortingPanel.setBackground(new Color(61, 76, 102));

        JButton sortByNameButton = createSortButtonForActors("Sort by Name");

        sortingPanel.add(sortByNameButton);

        return sortingPanel;
    }

    private JButton createSortButtonForActors(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(new Color(61, 76, 102));
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSortOptionClickForActors(text);
            }
        });
        return button;
    }

    private void handleSortOptionClickForActors(String option) {
        List<Actor> actors = getActors();
        List<Actor> filteredActors = new ArrayList<>(actors); // Create a copy

        switch (option) {
            case "Sort by Name":
                filteredActors.sort(Comparator.comparing(actor -> actor.name));
                break;
        }

        // Update the text content for all actors
        StringBuilder actorsInfo = new StringBuilder();
        for (Actor actor : filteredActors) {
            actorsInfo.append(actor.getDisplayInfo()).append("\n\n");
        }

        JTextArea textArea = (JTextArea) productionsScrollPane.getViewport().getView();
        textArea.setText(actorsInfo.toString());

        productionsFrame.revalidate();
        productionsFrame.repaint();
    }

    private JButton createSortButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(new Color(61, 76, 102));
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSortOptionClick(text);
            }
        });
        return button;
    }
    private JPanel createSortingPanel() {
        JPanel sortingPanel = new JPanel();
        sortingPanel.setBackground(new Color(61, 76, 102));

        JButton sortByNameButton = createSortButton("Sort by Name");
        JButton sortByRateButton = createSortButton("Sort by Rate");
        JButton sortByGenreButton = createSortButton("Sort by Genre");

        sortingPanel.add(sortByNameButton);
        sortingPanel.add(sortByRateButton);
        sortingPanel.add(sortByGenreButton);

        return sortingPanel;
    }
    private void showProductions(List<Production> productions) {
        productionsFrame = new JFrame("Productions Information");
        productionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        productionsFrame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Build the text content for all productions
        StringBuilder productionsInfo = new StringBuilder();
        for (Production production : productions) {
            productionsInfo.append(production.getDisplayInfo()).append("\n\n");
        }
        textArea.setText(productionsInfo.toString());
        productionsScrollPane = new JScrollPane(textArea);
        productionsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        productionsFrame.add(createSortingPanel(), BorderLayout.NORTH);
        productionsFrame.add(productionsScrollPane, BorderLayout.CENTER);

        productionsFrame.setSize(800, 600);
        productionsFrame.setLocationRelativeTo(optionsFrame);
        productionsFrame.setVisible(true);
    }

    private void handleSortOptionClick(String option) {
        List<Production> productions = getProductions();
        List<Production> filteredProductions = new ArrayList<>(productions); // Create a copy

        switch (option) {
            case "Sort by Name":
                filteredProductions.sort(Comparator.comparing(production -> production.productionName));
                break;
            case "Sort by Rate":
                filteredProductions.sort((p1, p2) -> Double.compare(p2.rate, p1.rate));
                break;
            case "Sort by Genre":
                String selectedGenre = chooseGenre(); // Get the selected genre from the user
                filterProductionsByGenre(filteredProductions, selectedGenre);
                break;
        }

        // Update the text content for all productions
        StringBuilder productionsInfo = new StringBuilder();
        for (Production production : filteredProductions) {
            productionsInfo.append(production.getDisplayInfo()).append("\n\n");
        }

        JTextArea textArea = (JTextArea) productionsScrollPane.getViewport().getView();
        textArea.setText(productionsInfo.toString());

        productionsFrame.revalidate();
        productionsFrame.repaint();
    }

    private String chooseGenre() {
        String[] genres = Arrays.stream(Genre.values()).map(Enum::name).toArray(String[]::new);
        genres = Arrays.copyOf(genres, genres.length + 1);
        genres[genres.length - 1] = "ALL";
        return (String) JOptionPane.showInputDialog(
                optionsFrame,
                "Choose a Genre:",
                "Genre Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                genres,
                genres[0]
        );
    }
    private void filterProductionsByGenre(List<Production> productions, String genre) {
        if ("ALL".equals(genre)) {
            return;
        }

        // Remove productions that do not belong to the selected genre
        productions.removeIf(production -> !production.genres.contains(Genre.valueOf(genre)));
    }
    private void viewNotifications() {
        productionsFrame = new JFrame("Notifications");
        productionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        productionsFrame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Build the text content for all notifications
        StringBuilder notificationsInfo = new StringBuilder();
        for (Object notificationObj : user.notifications) {
            String notification = (String) notificationObj;
            notificationsInfo.append(notification).append("\n\n");
        }
        textArea.setText(notificationsInfo.toString());
        productionsScrollPane = new JScrollPane(textArea);
        productionsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        productionsFrame.add(productionsScrollPane, BorderLayout.CENTER);

        productionsFrame.setSize(800, 600);
        productionsFrame.setLocationRelativeTo(optionsFrame);
        productionsFrame.setVisible(true);
    }
    private void searchActorMovieSeries() {
        String name = JOptionPane.showInputDialog(optionsFrame, "Enter the name of the actor/movie/series you want to search:");
        if (name != null && !name.isEmpty()) {
            Actor actor = findActor(name);
            Production production = findProduction(name);

            if (actor != null) {
                showActorInfo(actor);
            } else if (production != null) {
                showProductionInfo(production);
            } else {
                JOptionPane.showMessageDialog(optionsFrame, "No actor/movie/series with this name!");
            }
        }
    }
    private Actor findActor(String name) {
        for (Object actorObj : IMDB.getInstance().actors) {
            Actor actor = (Actor) actorObj;
            if (actor.name.equals(name)) {
                return actor;
            }
        }
        return null;
    }
    private Production findProduction(String name) {
        for (Object productionObj : IMDB.getInstance().productions) {
            Production production = (Production) productionObj;
            if (production.productionName.equals(name)) {
                return production;
            }
        }
        return null;
    }
    private void showActorInfo(Actor actor) {
        productionsFrame = new JFrame("Actor Information");
        productionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        productionsFrame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.setText(actor.getDisplayInfo());
        productionsScrollPane = new JScrollPane(textArea);
        productionsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        productionsFrame.add(productionsScrollPane, BorderLayout.CENTER);

        productionsFrame.setSize(800, 600);
        productionsFrame.setLocationRelativeTo(optionsFrame);
        productionsFrame.setVisible(true);
    }
    private void showProductionInfo(Production production) {
        productionsFrame = new JFrame("Production Information");
        productionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        productionsFrame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.setText(production.getDisplayInfo());
        productionsScrollPane = new JScrollPane(textArea);
        productionsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        productionsFrame.add(productionsScrollPane, BorderLayout.CENTER);

        productionsFrame.setSize(800, 600);
        productionsFrame.setLocationRelativeTo(optionsFrame);
        productionsFrame.setVisible(true);
    }
    private void manageFavorites() {
        String[] options = {
                "Add actor/production to favorites",
                "Delete actor/production from favorites",
                "View your favorites",
                "Back"
        };

        int optionIndex = JOptionPane.showOptionDialog(
                optionsFrame,
                "Select one of the following options:",
                "Manage Favorites",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (optionIndex == -1 || optionIndex == 3) {
            return;
        }

        int subOption = optionIndex + 1; // Convert to 1-based index

        switch (subOption) {
            case 1:
                addActorProductionToFavorites();
                break;
            case 2:
                deleteActorProductionFromFavorites();
                break;
            case 3:
                displayUserFavorites();
                break;
        }

    }

    private void addActorProductionToFavorites() {
        String name = JOptionPane.showInputDialog(optionsFrame, "Enter the name of the actor/production you want to add to favorites:");
        if (name != null && !name.isEmpty()) {
            boolean added = false;
            for (Actor actor : getActors()) {
                if (actor.name.equals(name)) {
                    user.addActorToPreferences(actor);
                    added = true;
                    break;
                }
            }
            if (!added) {
                for (Production production : getProductions()) {
                    if (production.productionName.equals(name)) {
                        if (production instanceof Movie) {
                            user.addMovieToPreferences((Movie) production);
                        } else if (production instanceof Series) {
                            user.addSeriesToPreferences((Series) production);
                        }
                        added = true;
                        break;
                    }
                }
            }
            if (!added) {
                JOptionPane.showMessageDialog(optionsFrame, "No actor/production with this name!");
            }
        }
    }

    private void deleteActorProductionFromFavorites() {
        String name = JOptionPane.showInputDialog(optionsFrame, "Enter the name of the actor/production you want to delete from favorites:");
        if (name != null && !name.isEmpty()) {
            boolean deleted = false;
            for (Actor actor : getActors()) {
                if (actor.name.equals(name)) {
                    user.deleteActorFromPreferences(actor);
                    deleted = true;
                    break;
                }
            }
            if (!deleted) {
                for (Production production : getProductions()) {
                    if (production.productionName.equals(name)) {
                        if (production instanceof Movie) {
                            user.deleteMovieFromPreferences((Movie) production);
                        } else if (production instanceof Series) {
                            user.deleteSeriesFromPreferences((Series) production);
                        }
                        deleted = true;
                        break;
                    }
                }
            }
            if (!deleted) {
                JOptionPane.showMessageDialog(optionsFrame, "No actor/production with this name!");
            }
        }
    }

    private void displayUserFavorites() {
        showUserFavorites(user.getFavorites());
    }

    private void showUserFavorites(List favorites) {
        JFrame favoritesFrame = new JFrame("Your Favorites");
        favoritesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // Build the text content for user favorites
        StringBuilder favoritesInfo = new StringBuilder("Your Favorites:\n\n");
        for (Object favorite : favorites) {
            if (favorite instanceof Actor) {
                favoritesInfo.append(((Actor) favorite).getDisplayInfo()).append("\n\n");
            } else if (favorite instanceof Movie) {
                favoritesInfo.append(((Movie) favorite).getDisplayInfo()).append("\n\n");
            } else if (favorite instanceof Series) {
                favoritesInfo.append(((Series) favorite).getDisplayInfo()).append("\n\n");
            }
        }
        textArea.setText(favoritesInfo.toString());

        JScrollPane favoritesScrollPane = new JScrollPane(textArea);
        favoritesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        favoritesFrame.add(favoritesScrollPane);
        favoritesFrame.setSize(800, 600);
        favoritesFrame.setLocationRelativeTo(optionsFrame);
        favoritesFrame.setVisible(true);
    }
    private void handleRequestButtonClick() {
        JFrame requestFrame = new JFrame("Request Options");
        requestFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JButton createRequestButton = new JButton("Create a Request");
        JButton deleteRequestButton = new JButton("Delete a Request");

        createRequestButton.addActionListener(e -> handleCreateRequestClick());
        deleteRequestButton.addActionListener(e -> handleDeleteRequestClick());

        panel.add(createRequestButton);
        panel.add(deleteRequestButton);

        requestFrame.add(panel);
        requestFrame.setSize(400, 200);
        requestFrame.setLocationRelativeTo(optionsFrame);
        requestFrame.setVisible(true);
    }

    private void handleCreateRequestClick() {
        JFrame createRequestFrame = new JFrame("Create Request");
        createRequestFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        String[] requestTypes = {"Delete Account", "Actor Issue", "Movie Issue", "Others"};
        JComboBox<String> requestTypeComboBox = new JComboBox<>(requestTypes);
        JTextField descriptionTextField = new JTextField();
        JButton submitButton = new JButton("Submit");

        panel.add(new JLabel("Select Request Type:"));
        panel.add(requestTypeComboBox);
        panel.add(new JLabel("Enter Description:"));
        panel.add(descriptionTextField);
        panel.add(submitButton);

        submitButton.addActionListener(e -> handleSubmitRequest(
                requestTypeComboBox.getSelectedItem().toString(),
                descriptionTextField.getText()
        ));

        createRequestFrame.add(panel);
        createRequestFrame.setSize(400, 200);
        createRequestFrame.setLocationRelativeTo(optionsFrame);
        createRequestFrame.setVisible(true);
    }

    private void handleDeleteRequestClick() {
        JFrame deleteRequestFrame = new JFrame("Delete Request");
        deleteRequestFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        List<Request> requests = IMDB.getInstance().requests;

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(requests.size() + 1, 1));

        List<JCheckBox> checkBoxes = new ArrayList<>();

        for (Request request : requests) {
            if (request.username.equals(user.username)) {
                String text = requests.indexOf(request) + 1 + ". " + request.toString();
                JCheckBox checkBox = new JCheckBox(text);
                checkBoxes.add(checkBox);
                panel.add(checkBox);
            }
        }

        JButton deleteButton = new JButton("Delete Selected Requests");
        deleteButton.addActionListener(e -> handleDeleteSelectedRequests(checkBoxes));

        panel.add(deleteButton);

        deleteRequestFrame.add(panel);
        deleteRequestFrame.setSize(400, 200);
        deleteRequestFrame.setLocationRelativeTo(optionsFrame);
        deleteRequestFrame.setVisible(true);
    }

    private void handleDeleteSelectedRequests(List<JCheckBox> checkBoxes) {
        List<Request> requests = IMDB.getInstance().requests;
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                String text = checkBox.getText();
                // Parse the request ID from the checkbox text
                int requestId = parseRequestId(text);
                if (requestId != -1 && requestId <= requests.size()) {
                    Request request = requests.get(requestId - 1);
                    requests.remove(request);
                    // display a success message
                    JOptionPane.showMessageDialog(
                            optionsFrame,
                            "Request successfully deleted!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    checkBoxes.clear();
                    break;
                }
            }
        }
    }
    private int parseRequestId(String text) {
        String[] parts = text.split("\\.");
        if (parts.length == 0) {
            return -1;
        }
        return Integer.parseInt(parts[0]);
    }
    private void handleSubmitRequest(String requestType, String description) {
        RequestType type = RequestType.valueOf(requestType.replace(" ", "_").toUpperCase());

        Request request = new Request(
                type,
                user.username,
                description,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                null,
                null
        );

        if (type == RequestType.ACTOR_ISSUE || type == RequestType.MOVIE_ISSUE) {
            String itemName = JOptionPane.showInputDialog(
                    optionsFrame,
                    "Enter the name of the " + (type == RequestType.ACTOR_ISSUE ? "actor:" : "movie:"),
                    "Item Name"
            );
            if (itemName != null && !itemName.isEmpty()) {
                if (type == RequestType.ACTOR_ISSUE) {
                    request.actorName = itemName;
                } else {
                    request.movieName = itemName;
                }
            }
        }
        List<User> users = IMDB.getInstance().users;
        if (request.getRequestType().equals(RequestType.DELETE_ACCOUNT)) {
            Admin.RequestsHolder.addRequest(request);
            for (User u : users) {
                if (u instanceof Admin){
                    request.addObserver(u);
                }
            }
            IMDB.getInstance().requests.add(request);
            request.notifyObservers("The user " + user.username +
                    " wants to delete his account!", 1);
        } else if(request.getRequestType().equals(RequestType.OTHERS)) {
            request.addObserver(user);
            Admin.RequestsHolder.addRequest(request);
            for (User u : users) {
                if (u instanceof Admin){
                    request.addObserver(u);
                }
            }
            IMDB.getInstance().requests.add(request);
            request.notifyObservers("The user " + user.username +
                    " has created a new request!", 1);
        } else if (request.getRequestType().equals(RequestType.ACTOR_ISSUE)) {
            String actorName = request.actorName;
            for (Object actorObj : IMDB.getInstance().actors) {
                Actor actor = (Actor) actorObj;
                if (actor.name.equals(actorName)) {
                    request.addObserver(user);
                    boolean ok = false;
                    for (User u: users) {
                        if (u.username.equals(actor.responsability)) {
                            request.responsableUsername = u.username;
                            ((Staff) u).requests.add(request);
                            request.addObserver(u);
                            ok = true;
                            break;
                        }
                    }
                    if (!ok) {
                        Admin.RequestsHolder.addRequest(request);
                        for (User u : users) {
                            if (u instanceof Admin){
                                request.addObserver(u);
                            }
                        }
                    }
                    IMDB.getInstance().requests.add(request);
                    request.notifyObservers("The user " + user.username +
                            " has a request regarding actor " + actorName + "!", 1);
                    break;
                }
            }
        } else {
            String movieName = request.movieName;
            for (Object productionObj : IMDB.getInstance().productions) {
                Production p = (Production) productionObj;
                if (p.productionName.equals(movieName)) {
                    request.addObserver(user);
                    boolean ok = false;
                    for (User u: users) {
                        if (u.username.equals(p.resposability)) {
                            request.responsableUsername = u.username;
                            ((Staff) u).requests.add(request);
                            request.addObserver(u);
                            ok = true;
                            break;
                        }
                    }
                    if (!ok) {
                        Admin.RequestsHolder.addRequest(request);
                        for (User u : users) {
                            if (u instanceof Admin){
                                request.addObserver(u);
                            }
                        }
                    }
                    IMDB.getInstance().requests.add(request);
                    request.notifyObservers("The user " + user.username +
                            " has a request regarding movie " + movieName + "!", 1);
                    break;
                }
            }
        }
        JOptionPane.showMessageDialog(
                optionsFrame,
                "Request successfully submitted!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
    private void handleRatingButtonClick() {
        JFrame ratingFrame = new JFrame("Rating Options");
        ratingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JButton addRatingButton = new JButton("Add Rating to Production");
        JButton deleteRatingButton = new JButton("Delete Rating from Production");

        addRatingButton.addActionListener(e -> handleAddRatingClick());
        deleteRatingButton.addActionListener(e -> handleDeleteRatingClick());

        panel.add(addRatingButton);
        panel.add(deleteRatingButton);

        ratingFrame.add(panel);
        ratingFrame.setSize(400, 200);
        ratingFrame.setLocationRelativeTo(optionsFrame);
        ratingFrame.setVisible(true);
    }
    private void handleAddRatingClick() {
        String productionName = JOptionPane.showInputDialog(optionsFrame, "Enter the name of the production:");
        if (productionName == null || productionName.isEmpty()) {
            return;
        }

        int rating = -1;
        while (rating < 1 || rating > 10) {
            try {
                String ratingStr = JOptionPane.showInputDialog(optionsFrame, "Enter the rating (1-10):");
                if (ratingStr == null) {
                    return;
                }
                rating = Integer.parseInt(ratingStr);
            } catch (NumberFormatException e) {
                // Handle invalid input
                JOptionPane.showMessageDialog(optionsFrame, "Invalid rating. Please enter a number between 1 and 10.");
            }
        }

        String comment = JOptionPane.showInputDialog(optionsFrame, "Enter the comment:");
        boolean productionExists = false;
        for (Object prodNameObj : user.ratedAndDeletedProductions) {
            String prodName = (String) prodNameObj;
            if (prodName.equals(productionName)) {
                // display a message that the production has already been rated
                JOptionPane.showMessageDialog(
                        optionsFrame,
                        "You have already rated this production!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                productionExists = true;
                break;
            }
        }
        if (productionExists) {
            return;
        }
        for (Object productionObj : IMDB.getInstance().productions) {
            Production p = (Production) productionObj;
            if (p.productionName.equals(productionName)) {
                p.ratings.add(new Rating(user.username, rating, comment));
                Double averageRate = 0.0;
                for (Rating r : p.ratings) {
                    averageRate += r.rating;
                }
                averageRate /= p.ratings.size();
                p.rate = averageRate;
                p.notifyObservers("The production you added in the system " +
                        p.productionName + " received a new rating from the user " +
                        user.username + " -> " + rating, 1);
                p.notifyObservers("The production you also rated " +
                        p.productionName + " received a new rating from the user " +
                        user.username + " -> " + rating, 1);
                p.addObserver(user);
                break;
            }
        }
        boolean ok = false;
        for (Object prodNameObj : user.ratedProductions) {
            String prodName = (String) prodNameObj;
            if (prodName.equals(productionName)) {
                ok = true;
                break;
            }
        }
        if (!ok) {
            user.updateExperience(new RateProduction());
            user.ratedProductions.add(productionName);
            user.ratedAndDeletedProductions.add(productionName);
            displayOptions();
        }
    }
    private void handleDeleteRatingClick() {
        boolean allGood = false;
        String productionName = JOptionPane.showInputDialog(optionsFrame, "Enter the name of the production:");
        if (productionName == null || productionName.isEmpty()) {
            return;
        }

        boolean productionExists = false;
        for (Object prodObj : IMDB.getInstance().productions) {
            Production p = (Production) prodObj;
            if (p.productionName.equals(productionName)) {
                productionExists = true;
                boolean ratingExists = false;
                for (Rating r : p.ratings) {
                    if (r.username.equals(user.username)) {
                        ratingExists = true;
                        allGood = true;
                        p.ratings.remove(r);
                        p.removeObserver(user);
                        user.ratedAndDeletedProductions.remove(productionName);
                        Double averageRate = 0.0;
                        for (Rating rating : p.ratings) {
                            averageRate += rating.rating;
                        }
                        averageRate /= p.ratings.size();
                        p.rate = averageRate;
                        break;
                    }
                }
                if (!ratingExists) {
                    JOptionPane.showMessageDialog(
                            optionsFrame,
                            "You have not rated this production!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                break;
            }
        }
        if (!productionExists) {
            JOptionPane.showMessageDialog(
                    optionsFrame,
                    "No production with this name!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        if (allGood) {
            JOptionPane.showMessageDialog(
                    optionsFrame,
                    "Rating successfully deleted!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    public void start () {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                displayOptions();
            }
        });
    }
    private void handleManageSystemButtonClick() {
        String[] options = {
                "Add production to system",
                "Delete production from system",
                "Add actor to system",
                "Delete actor from system"
        };

        String selectedOption = (String) JOptionPane.showInputDialog(
                optionsFrame,
                "Choose one of the following options:",
                "Manage System",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (selectedOption != null) {
            try {
                addDeleteActorsProductionToFromSystem(selectedOption);
            } catch (InvalidCommandException e) {
                JOptionPane.showMessageDialog(optionsFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void addDeleteActorsProductionToFromSystem(String selectedOption) throws InvalidCommandException {
        switch (selectedOption) {
            case "Add production to system":
                addProductionToSystem();
                break;
            case "Delete production from system":
                deleteProductionFromSystem();
                break;
            case "Add actor to system":
                addActorToSystem();
                break;
            case "Delete actor from system":
                deleteActorFromSystem();
                break;
        }
    }

    private void addProductionToSystem() {
        String productionName = JOptionPane.showInputDialog(optionsFrame, "Enter the name of the production:");
        if (productionName == null || productionName.isEmpty()) {
            return;
        }
        List <Production> productions = IMDB.getInstance().productions;
        for (Production p : productions) {
            if (p.productionName.equals(productionName)) {
                JOptionPane.showMessageDialog(
                        optionsFrame,
                        "Production already exists!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        String productionType = JOptionPane.showInputDialog(optionsFrame, "Enter the type of the production (movie/series):");
        if (productionType == null || productionType.isEmpty()) {
            return;
        }
        String productionDescription = JOptionPane.showInputDialog(optionsFrame, "Enter the description of the production:");
        if (productionDescription == null || productionDescription.isEmpty()) {
            return;
        }
        String productionDirectors = JOptionPane.showInputDialog(optionsFrame, "Enter the directors of the production:");
        if (productionDirectors == null || productionDirectors.isEmpty()) {
            return;
        }
        List<String> directors = Arrays.asList(productionDirectors.split(","));
        String productionActors = JOptionPane.showInputDialog(optionsFrame, "Enter the actors of the production:");
        if (productionActors == null || productionActors.isEmpty()) {
            return;
        }
        List<String> actors = Arrays.asList(productionActors.split(","));
        List<Genre> genres = new ArrayList<>();
        String productionGenres = JOptionPane.showInputDialog(optionsFrame, "Enter the genres of the production:");
        if (productionGenres == null || productionGenres.isEmpty()) {
            return;
        }
        for (String genre : productionGenres.split(",")) {
            try {
                genres.add(Genre.valueOf(genre));
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(
                        optionsFrame,
                        "Invalid genre!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        List<Rating> ratings = new ArrayList<>();
        Double rate = 0.0;
        if (productionType.equals("Movie") || productionType.equals("movie")) {
            String movieDuration = JOptionPane.showInputDialog(optionsFrame, "Enter the duration of the movie:");
            if (movieDuration == null || movieDuration.isEmpty()) {
                return;
            }
            String movieReleaseDate = JOptionPane.showInputDialog(optionsFrame, "Enter the release date of the movie:");
            if (movieReleaseDate == null || movieReleaseDate.isEmpty()) {
                return;
            }
            int intReleaseDate = 0;
            try {
                intReleaseDate = Integer.parseInt(movieReleaseDate);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        optionsFrame,
                        "Invalid release date!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            IMDB.getInstance().productions.add(
                    new Movie(
                            productionName,
                            directors,
                            actors,
                            genres,
                            ratings,
                            productionDescription,
                            movieDuration,
                            intReleaseDate,
                            rate,
                            user.username
                    ));
            ((Staff) user).addProductionSystem((Production) IMDB.getInstance().productions.get(
                    IMDB.getInstance().productions.size() - 1)
            );
            for (String actor : actors) {
                boolean ok = false;
                for (Object aObj : IMDB.getInstance().actors) {
                    Actor a = (Actor) aObj;
                    if (a.name.equals(actor)) {
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    Actor newActor = new Actor(actor, new ArrayList<Pair>(), null, user.username);
                    newActor.movies.add(new Pair(productionName, productionType));
                    IMDB.getInstance().actors.add(newActor);
                    ((Staff) user).addActorSystem(newActor);
                }
            }
            ((Production) IMDB.getInstance().productions.get(IMDB.getInstance().productions.size() - 1)).addObserver(user);
        } else if (productionType.equals("Series") || productionType.equals("series")) {
            String releaseYear = JOptionPane.showInputDialog(optionsFrame, "Enter the release year of the series:");
            if (releaseYear == null || releaseYear.isEmpty()) {
                return;
            }
            int intReleaseYear = 0;
            try {
                intReleaseYear = Integer.parseInt(releaseYear);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        optionsFrame,
                        "Invalid release year!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            String numberOfSeasons = JOptionPane.showInputDialog(optionsFrame, "Enter the number of seasons of the series:");
            if (numberOfSeasons == null || numberOfSeasons.isEmpty()) {
                return;
            }
            int intNumberOfSeasons = 0;
            try {
                intNumberOfSeasons = Integer.parseInt(numberOfSeasons);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        optionsFrame,
                        "Invalid number of seasons!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            Map<String, List<Episode>> seasons = new HashMap<String, List<Episode>>();
            for (int i = 1; i <= intNumberOfSeasons; i++) {
                String numberOfEpisodes = JOptionPane.showInputDialog(optionsFrame, "Enter the number of episodes of season " + i + ":");
                if (numberOfEpisodes == null || numberOfEpisodes.isEmpty()) {
                    return;
                }
                int intNumberOfEpisodes = 0;
                try {
                    intNumberOfEpisodes = Integer.parseInt(numberOfEpisodes);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                            optionsFrame,
                            "Invalid number of episodes!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                List<Episode> episodes = new ArrayList<>();
                for (int j = 1; j <= intNumberOfEpisodes; j++) {
                    String episodeName = JOptionPane.showInputDialog(optionsFrame, "Enter the name of episode " + j + ":");
                    if (episodeName == null || episodeName.isEmpty()) {
                        return;
                    }
                    String episodeDuration = JOptionPane.showInputDialog(optionsFrame, "Enter the duration of episode " + j + ":");
                    if (episodeDuration == null || episodeDuration.isEmpty()) {
                        return;
                    }
                    episodes.add(new Episode(episodeName, episodeDuration));
                }
                seasons.put("Season " + i, episodes);
            }
            IMDB.getInstance().productions.add(
                    new Series(
                            productionName,
                            directors,
                            actors,
                            genres,
                            ratings,
                            productionDescription,
                            intNumberOfSeasons,
                            intReleaseYear,
                            seasons,
                            rate,
                            user.username
                    ));
            ((Staff) user).addProductionSystem((Production) IMDB.getInstance().productions.get(
                    IMDB.getInstance().productions.size() - 1)
            );
            for (String actor : actors) {
                boolean ok = false;
                for (Object aObj : IMDB.getInstance().actors) {
                    Actor a = (Actor) aObj;
                    if (a.name.equals(actor)) {
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    Actor newActor = new Actor(actor, new ArrayList<Pair>(), null, user.username);
                    newActor.movies.add(new Pair(productionName, productionType));
                    IMDB.getInstance().actors.add(newActor);
                    ((Staff) user).addActorSystem(newActor);
                }
            }
            ((Production) IMDB.getInstance().productions.get(IMDB.getInstance().productions.size() - 1)).addObserver(user);
        }
        for (Object deleted : ((Staff) user).deletedProductionsActors) {
            String deletedName = (String) deleted;
            if (deletedName.equals(productionName)) {
                return;
            }
        }
        user.updateExperience(new AddProductionActor());
        displayOptions();
    }

    private void deleteProductionFromSystem() {
        String productionName = JOptionPane.showInputDialog(optionsFrame, "Enter the name of the production:");
        if (productionName == null || productionName.isEmpty()) {
            return;
        }
        List <Production> productions = IMDB.getInstance().productions;
        boolean productionExists = false;
        for (Production p : productions) {
            if (p.productionName.equals(productionName)) {
                if (p.resposability.equals(user.username) ||
                        (user instanceof Admin && p.resposability.equals("Admin"))) {
                    productionExists = true;
                    IMDB.getInstance().productions.remove(p);
                    ((Staff) user).removeProductionSystem(productionName);
                    ((Staff) user).deletedProductionsActors.add(productionName);
                    break;
                } else {
                    JOptionPane.showMessageDialog(
                            optionsFrame,
                            "You are not allowed to delete this production!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }
        }
        if (!productionExists) {
            JOptionPane.showMessageDialog(
                    optionsFrame,
                    "No production with this name!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void addActorToSystem() {
        String actorName = JOptionPane.showInputDialog(optionsFrame, "Enter the name of the actor:");
        if (actorName == null || actorName.isEmpty()) {
            return;
        }
        List <Actor> actors = IMDB.getInstance().actors;
        for (Actor a : actors) {
            if (a.name.equals(actorName)) {
                JOptionPane.showMessageDialog(
                        optionsFrame,
                        "Actor already exists!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        String biography = JOptionPane.showInputDialog(optionsFrame, "Enter the biography of the actor:");
        if (biography == null || biography.isEmpty()) {
            return;
        }
        List<Pair> movies = new ArrayList<>();
        String actorMovies = JOptionPane.showInputDialog(optionsFrame, "Enter the movies of the actor:");
        if (actorMovies == null || actorMovies.isEmpty()) {
            return;
        }
        for (String movie : actorMovies.split(",")) {
            String[] parts = movie.split("-");
            if (parts.length != 2) {
                JOptionPane.showMessageDialog(
                        optionsFrame,
                        "Invalid movie!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            movies.add(new Pair(parts[0], parts[1]));
        }
        IMDB.getInstance().actors.add(new Actor(actorName, movies, biography, user.username));
        ((Staff) user).addActorSystem((Actor) IMDB.getInstance().actors.get(
                IMDB.getInstance().actors.size() - 1)
        );
        for (Object deleted : ((Staff) user).deletedProductionsActors) {
            String deletedName = (String) deleted;
            if (deletedName.equals(actorName)) {
                return;
            }
        }
        user.updateExperience(new AddProductionActor());
        displayOptions();
    }

    private void deleteActorFromSystem() {
        String actorName = JOptionPane.showInputDialog(optionsFrame, "Enter the name of the actor:");
        if (actorName == null || actorName.isEmpty()) {
            return;
        }
        List <Actor> actors = IMDB.getInstance().actors;
        boolean actorExists = false;
        for (Actor a : actors) {
            if (a.name.equals(actorName)) {
                if (a.responsability.equals(user.username) ||
                        (user instanceof Admin && a.responsability.equals("Admin"))) {
                    actorExists = true;
                    IMDB.getInstance().actors.remove(a);
                    ((Staff) user).removeActorSystem(actorName);
                    ((Staff) user).deletedProductionsActors.add(actorName);
                    break;
                } else {
                    JOptionPane.showMessageDialog(
                            optionsFrame,
                            "You are not allowed to delete this actor!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }
        }
        if (!actorExists) {
            JOptionPane.showMessageDialog(
                    optionsFrame,
                    "No actor with this name!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private void handleViewSolveRequestsButtonClick() {
        if (user instanceof Admin<?>) {
            // make him choose between personal and team requests
            String[] options = {"Personal Requests", "Team Requests"};
            String selectedOption = (String) JOptionPane.showInputDialog(
                    optionsFrame,
                    "Choose one of the following options:",
                    "View/Solve Requests",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (selectedOption != null) {
                if (selectedOption.equals("Personal Requests")) {
                    resolvePersonalRequests();
                } else {
                    resolveAdminTeamRequests();
                }
            } else {
                return;
            }
        } else {
            // make him choose between personal and team requests
            String[] options = {"Personal Requests"};
            String selectedOption = (String) JOptionPane.showInputDialog(
                    optionsFrame,
                    "Choose one of the following options:",
                    "View/Solve Requests",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (selectedOption != null) {
                resolvePersonalRequests();
            } else {
                return;
            }
        }
    }
    private void resolvePersonalRequests() {
        Map<Integer, Request> map = new LinkedHashMap<>();
        int i = 1;
        for (Object requestObj : ((Staff) user).requests) {
            Request request = (Request) requestObj;
            map.put(i, request);
            i++;
        }
        if (map.size() == 0) {
            JOptionPane.showMessageDialog(
                    optionsFrame,
                    "You have no requests!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        String[] options = new String[map.size()];
        for (int j = 1; j <= map.size(); j++) {
            options[j - 1] = j + ". " + map.get(j).toString();
        }
        String selectedOption = (String) JOptionPane.showInputDialog(
                optionsFrame,
                "Choose one of the following options:",
                "View/Solve Requests",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        if (selectedOption != null) {
            int requestId = parseRequestId(selectedOption);
            if (requestId != -1 && requestId <= map.size()) {
                Request request = map.get(requestId);
                String[] options2 = {"Accept", "Reject"};
                String selectedOption2 = (String) JOptionPane.showInputDialog(
                        optionsFrame,
                        "Choose one of the following options:",
                        "View/Solve Requests",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options2,
                        options2[0]);
                if (selectedOption2 != null) {
                    if (selectedOption2.equals("Accept")) {
                        acceptRequest(request);
                    } else {
                        rejectRequest(request);
                    }
                    IMDB.getInstance().requests.remove(request);
                    ((Staff) user).requests.remove(request);
                }
            }
        }
    }

    private void resolveAdminTeamRequests() {
        Map<Integer, Request> map = new LinkedHashMap<>();
        int i = 1;
        for (Object requestObj : Admin.RequestsHolder.requests) {
            Request request = (Request) requestObj;
            map.put(i, request);
            i++;
        }
        if (map.size() == 0) {
            JOptionPane.showMessageDialog(
                    optionsFrame,
                    "You have no requests!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        String[] options = new String[map.size()];
        for (int j = 1; j <= map.size(); j++) {
            options[j - 1] = j + ". " + map.get(j).toString();
        }
        String selectedOption = (String) JOptionPane.showInputDialog(
                optionsFrame,
                "Choose one of the following options:",
                "View/Solve Requests",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        if (selectedOption != null) {
            int requestId = parseRequestId(selectedOption);
            if (requestId != -1 && requestId <= map.size()) {
                Request request = map.get(requestId);
                String[] options2 = {"Accept", "Reject"};
                String selectedOption2 = (String) JOptionPane.showInputDialog(
                        optionsFrame,
                        "Choose one of the following options:",
                        "View/Solve Requests",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options2,
                        options2[0]);
                if (selectedOption2 != null) {
                    if (selectedOption2.equals("Accept")) {
                        acceptRequest(request);
                    } else {
                        rejectRequest(request);
                    }
                    Admin.RequestsHolder.requests.remove(request);
                    IMDB.getInstance().requests.remove(request);
                }
            }
        }
    }

    private void acceptRequest(Request request) {
        for (Object userObj : IMDB.getInstance().users) {
            User u = (User) userObj;
            if (u.username.equals(request.username)) {
                if (request.getRequestType().equals(RequestType.MOVIE_ISSUE) ||
                        request.getRequestType().equals(RequestType.ACTOR_ISSUE)) {
                    u.updateExperience(new CreatedRequest());
                    displayOptions();
                }
                if (request.getRequestType().equals(RequestType.MOVIE_ISSUE)) {
                    request.notifyObservers("Your request regarding the movie " + request.movieName +
                            " has been accepted!", 2);
                }
                if (request.getRequestType().equals(RequestType.ACTOR_ISSUE)) {
                    request.notifyObservers("Your request regarding the actor " + request.actorName +
                            " has been accepted!", 2);
                }
                if (request.getRequestType().equals(RequestType.OTHERS)) {
                    request.notifyObservers("Your request regarding " + request.description +
                            " has been accepted!", 2);
                }
                break;
            }
        }
    }

    private void rejectRequest(Request request) {
        for (Object userObj : IMDB.getInstance().users) {
            User u = (User) userObj;
            if (u.username.equals(request.username)) {
                if (request.getRequestType().equals(RequestType.MOVIE_ISSUE)) {
                    request.notifyObservers("Your request regarding the movie " + request.movieName +
                            " has been rejected!", 2);
                }
                if (request.getRequestType().equals(RequestType.ACTOR_ISSUE)) {
                    request.notifyObservers("Your request regarding the actor " + request.actorName +
                            " has been rejected!", 2);
                }
                if (request.getRequestType().equals(RequestType.OTHERS)) {
                    request.notifyObservers("Your request regarding " + request.description +
                            " has been rejected!", 2);
                }
                break;
            }
        }
    }
    private void handleUpdateDetailsButtonClick() {
        String[] options = {"Update Production Details", "Update Actor Details"};

        int option2 = JOptionPane.showOptionDialog(
                optionsFrame,
                "Select one of the following options:",
                "Update Details",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                "Update Production Details");

        if (option2 == 0) {
            // Update Production Details
            updateProductionDetails();
        } else if (option2 == 1) {
            // Update Actor Details
            updateActorDetails();
        }
    }
    private void updateProductionDetails() {
        String productionName = JOptionPane.showInputDialog(optionsFrame, "Enter the name of the production:");
        if (productionName == null || productionName.isEmpty()) {
            return;
        }
        Production production = findProduction(productionName);
        if (production == null) {
            JOptionPane.showMessageDialog(
                    optionsFrame,
                    "No production with this name!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if (!production.resposability.equals(user.username) &&
                !(user instanceof Admin && production.resposability.equals("Admin"))) {
            JOptionPane.showMessageDialog(
                    optionsFrame,
                    "You are not allowed to update this production!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        String[] options = {"Update Description", "Update Directors", "Update Actors", "Update Genres",
                "Update release year"};
        if (production instanceof Movie) {
            options = Arrays.copyOf(options, options.length + 1);
            options[options.length - 1] = "Update Duration";
        } else {
            options = Arrays.copyOf(options, options.length + 2);
            options[options.length - 2] = "Update Number of Seasons";
            options[options.length - 1] = "Update Seasons";
        }
        int option2 = JOptionPane.showOptionDialog(
                optionsFrame,
                "Select one of the following options:",
                "Update Details",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                "Update Description"
        );
        if (option2 == 0) {
            // Update Description
            String newDescription = JOptionPane.showInputDialog(optionsFrame, "Enter the new description:");
            if (newDescription == null || newDescription.isEmpty()) {
                return;
            }
            production.description = newDescription;
        } else if (option2 == 1) {
            // Update Directors
            String newDirectors = JOptionPane.showInputDialog(optionsFrame, "Enter the new directors:");
            if (newDirectors == null || newDirectors.isEmpty()) {
                return;
            }
            List<String> directors = Arrays.asList(newDirectors.split(","));
            production.directorNames = directors;
        } else if (option2 == 2) {
            // Update Actors
            String actorsString = JOptionPane.showInputDialog(optionsFrame, "Enter the new actors:");
            if (actorsString == null || actorsString.isEmpty()) {
                return;
            }
            List<String> actors = Arrays.asList(actorsString.split(","));
            production.actorNames = actors;
        } else if (option2 == 3) {
            String genresString = JOptionPane.showInputDialog(optionsFrame, "Enter the new genres:");
            if (genresString == null || genresString.isEmpty()) {
                return;
            }
            List<Genre> genres = new ArrayList<>();
            for (String genre : genresString.split(",")) {
                try {
                    genres.add(Genre.valueOf(genre));
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(
                            optionsFrame,
                            "Invalid genre!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }
            production.genres = genres;
        } else if (option2 == 4) {
            // Update release year
            String releaseYear = JOptionPane.showInputDialog(optionsFrame, "Enter the new release year:");
            if (releaseYear == null || releaseYear.isEmpty()) {
                return;
            }
            int intReleaseYear = 0;
            try {
                intReleaseYear = Integer.parseInt(releaseYear);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        optionsFrame,
                        "Invalid release year!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if (production instanceof Movie) {
                ((Movie) production).yearProduced = intReleaseYear;
            } else {
                ((Series) production).yearOfRelease = intReleaseYear;
            }
        } else if (option2 == 5) {
            // Update duration
            String duration = JOptionPane.showInputDialog(optionsFrame, "Enter the new duration:");
            if (duration == null || duration.isEmpty()) {
                return;
            }
            ((Movie) production).duration = duration;
        } else if (option2 == 6 && production instanceof Movie) {
            String duration = JOptionPane.showInputDialog(optionsFrame, "Enter the new duration:");
            if (duration == null || duration.isEmpty()) {
                return;
            }
            ((Movie) production).duration = duration;
        } else if (option2 == 6 && production instanceof Series) {
            String numberOfSeasons = JOptionPane.showInputDialog(optionsFrame, "Enter the new number of seasons:");
            if (numberOfSeasons == null || numberOfSeasons.isEmpty()) {
                return;
            }
            int intNumberOfSeasons = 0;
            try {
                intNumberOfSeasons = Integer.parseInt(numberOfSeasons);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        optionsFrame,
                        "Invalid number of seasons!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            ((Series) production).numberOfSeasons = intNumberOfSeasons;
        } else if (option2 == 7 && production instanceof Series) {
            String seasonToBeUpdated = JOptionPane.showInputDialog(optionsFrame, "Enter the season to be updated:");
            if (seasonToBeUpdated == null || seasonToBeUpdated.isEmpty()) {
                return;
            }
            String newEpisodes = JOptionPane.showInputDialog(optionsFrame, "Enter the new episodes:\n" +
                    "Please enter the episodes in the following format:\n" +
                    "EpisodeName1-EpisodeDuration1,EpisodeName2-EpisodeDuration2,...");
            if (newEpisodes == null || newEpisodes.isEmpty()) {
                return;
            }
            List<Episode> episodes = new ArrayList<>();
            for (String episode : newEpisodes.split(",")) {
                String[] parts = episode.split("-");
                if (parts.length != 2) {
                    JOptionPane.showMessageDialog(
                            optionsFrame,
                            "Invalid episode!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                episodes.add(new Episode(parts[0], parts[1]));
            }
            ((Series) production).updateEpisodes(seasonToBeUpdated, episodes);
        }
        JOptionPane.showMessageDialog(
                optionsFrame,
                "Details successfully updated!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
        ((Staff) user).updateProduction(production);
    }
    private void updateActorDetails() {
        String actorName = JOptionPane.showInputDialog(optionsFrame, "Enter the name of the actor:");
        if (actorName == null || actorName.isEmpty()) {
            return;
        }
        Actor actor = findActor(actorName);
        if (actor == null) {
            JOptionPane.showMessageDialog(
                    optionsFrame,
                    "No actor with this name!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if (!actor.responsability.equals(user.username) &&
                !(user instanceof Admin && actor.responsability.equals("Admin"))) {
            JOptionPane.showMessageDialog(
                    optionsFrame,
                    "You are not allowed to update this actor!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        String[] options = {"Update Biography", "Update Movies"};
        int option2 = JOptionPane.showOptionDialog(
                optionsFrame,
                "Select one of the following options:",
                "Update Details",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                "Update Biography"
        );
        if (option2 == 0) {
            // Update Biography
            String newBiography = JOptionPane.showInputDialog(optionsFrame, "Enter the new biography:");
            if (newBiography == null || newBiography.isEmpty()) {
                return;
            }
            actor.biography = newBiography;
        } else if (option2 == 1) {
            // Update Movies
            String moviesString = JOptionPane.showInputDialog(optionsFrame, "Enter the new movies:\n" +
                    "Please enter the movies in the following format:\n" +
                    "MovieName1-MovieType1,MovieName2-MovieType2,...");
            if (moviesString == null || moviesString.isEmpty()) {
                return;
            }
            List<Pair> movies = new ArrayList<>();
            for (String movie : moviesString.split(",")) {
                String[] parts = movie.split("-");
                if (parts.length != 2) {
                    JOptionPane.showMessageDialog(
                            optionsFrame,
                            "Invalid movie!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                movies.add(new Pair(parts[0], parts[1]));
            }
            actor.movies = movies;
        }
        JOptionPane.showMessageDialog(
                optionsFrame,
                "Details successfully updated!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
        ((Staff) user).updateActor(actor);
    }
    private void handleManageUsersButtonClick() {
        String[] options = {"Add User", "Delete User"};

        int option2 = JOptionPane.showOptionDialog(
                optionsFrame,
                "Select one of the following options:",
                "Manage Users",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                "Add User");

        if (option2 == 0) {
            // Add User
            addUser();
        } else if (option2 == 1) {
            // Delete User
            deleteUser();
        }
    }
    private void addUser() {
        User.InformationBuilder informationBuilder = new User.InformationBuilder();
        String name = JOptionPane.showInputDialog(optionsFrame, "Enter the name of the user:");
        if (name == null || name.isEmpty()) {
            return;
        }
        informationBuilder.setName(name);
        String country = JOptionPane.showInputDialog(optionsFrame, "Enter the country of the user:");
        if (country == null || country.isEmpty()) {
            return;
        }
        informationBuilder.setCountry(country);
        String age = JOptionPane.showInputDialog(optionsFrame, "Enter the age of the user:");
        if (age == null || age.isEmpty()) {
            return;
        }
        int intAge = 0;
        try {
            intAge = Integer.parseInt(age);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    optionsFrame,
                    "Invalid age!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        informationBuilder.setAge(intAge);
        String dateBirth = JOptionPane.showInputDialog(optionsFrame, "Enter the date of birth of the user:");
        if (dateBirth == null || dateBirth.isEmpty()) {
            return;
        }
        informationBuilder.setDateOfBirth(dateBirth);
        String email = JOptionPane.showInputDialog(optionsFrame, "Enter the email of the user:");
        if (email == null || email.isEmpty()) {
            return;
        }
        String password = IMDB.getInstance().createRandomStrongPassword();
        informationBuilder.setCredentials(new Credentials(email, password));
        String usernameOfNewUser = "";
        List<String> names = new ArrayList<>();
        names = Arrays.asList(name.split(" "));
        for (String n : names) {
            usernameOfNewUser += n;
        }
        List<User> users = IMDB.getInstance().users;
        while (true) {
            boolean ok = true;
            int random = (int) (Math.random() * 900 + 100);
            for (User u : users) {
                if (u.username.equals(usernameOfNewUser + "_" + random)) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                usernameOfNewUser += "_" + random;
                break;
            }
        }
        String type = JOptionPane.showInputDialog(optionsFrame, "Enter the type of the user:");
        if (type == null || type.isEmpty()) {
            return;
        }
        if (!type.equals("Admin") && !type.equals("Staff") && !type.equals("Regular")) {
            JOptionPane.showMessageDialog(
                    optionsFrame,
                    "Invalid type!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if (name == null || name.isEmpty() || email == null || email.isEmpty() ||
                password == null) {
            return;
        }
        User newUser = UserFactory.factory(type, null, usernameOfNewUser, 0);
        newUser.information = informationBuilder.build(newUser);
        IMDB.getInstance().users.add(newUser);
        JOptionPane.showMessageDialog(
                        optionsFrame,
                        "User successfully added!\n" +
                                "Username: " + usernameOfNewUser + "\n" +
                                "Password: " + password,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
        );
    }
    private void deleteUser() {
        Map<Integer, User> map = new LinkedHashMap<>();
        int i = 1;
        List<User> users = IMDB.getInstance().users;
        for (User u : users) {
            map.put(i, u);
            i++;
        }
        if (map.size() == 0) {
            JOptionPane.showMessageDialog(
                    optionsFrame,
                    "There are no users!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        String[] options = new String[map.size()];
        for (int j = 1; j <= map.size(); j++) {
            options[j - 1] = j + ". " + map.get(j).username;
        }
        String selectedOption = (String) JOptionPane.showInputDialog(
                optionsFrame,
                "Choose one of the following options:",
                "Delete User",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        if (selectedOption != null) {
            int userId = parseRequestId(selectedOption);
            if (userId != -1 && userId <= map.size()) {
                User userToDelete = map.get(userId);
                if (userToDelete instanceof Staff) {
                    // put his productions and actors he is responsible for in the system to the admin list
                    List<Production> productions = IMDB.getInstance().productions;
                    List<Actor> actors = IMDB.getInstance().actors;
                    for (Production p : productions) {
                        if (p.resposability.equals(userToDelete.username)) {
                            p.resposability = "Admin";
                            for (User u : users) {
                                if (u instanceof Admin) {
                                    ((Admin) u).addProductionSystem(p);
                                    break;
                                }
                            }
                        }
                    }
                    for (Actor a : actors) {
                        if (a.responsability.equals(userToDelete.username)) {
                            a.responsability = "Admin";
                            for (User u : users) {
                                if (u instanceof Admin) {
                                    ((Admin) u).addActorSystem(a);
                                    break;
                                }
                            }
                        }
                    }
                }
                IMDB.getInstance().users.remove(userToDelete);
            }
        }
    }
}