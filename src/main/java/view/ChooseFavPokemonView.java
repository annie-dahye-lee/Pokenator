package view;

import data_access.FileUserDataAccessObject;
import data_access.PokeApiGateway;
import interface_adapter.ViewManagerModel;
import interface_adapter.choose_fav_pokemon.ChooseFavPokemonController;
import interface_adapter.choose_fav_pokemon.ChooseFavPokemonState;
import interface_adapter.choose_fav_pokemon.ChooseFavPokemonViewModel;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * The View for when the user is choosing their favourite Pokemon to display on profile.
 */
public class ChooseFavPokemonView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "Choose Favourite Pokemon";
    private ChooseFavPokemonController chooseFavPokemonController;
    private ChooseFavPokemonState chooseFavPokemonState;

    private final ChooseFavPokemonViewModel chooseFavPokemonViewModel;
    private final ViewManagerModel viewManagerModel;
    private final GameDashboard gameDashboard;
    private final FileUserDataAccessObject DAO;
    private final PokeApiGateway pokeApiGateway;

    private final int NUM_ROWS = 5;
    private int page = 0;
    private final ArrayList<String> pokemonList;
    private final ArrayList<JPanel> listDisplay;

    private JLabel chosenLabel;

    private JButton nextPage;
    private JButton previousPage;
    private JButton cancel;
    private JButton save;

    private String currentUser;
    private String chosenPokemon;

    public ChooseFavPokemonView(ChooseFavPokemonViewModel chooseFavPokemonViewModel, ViewManagerModel viewManagerModel,
                                GameDashboard gameDashboard, FileUserDataAccessObject DAO,
                                PokeApiGateway pokeApiGateway) {

        this.chooseFavPokemonViewModel = chooseFavPokemonViewModel;
        this.viewManagerModel = viewManagerModel;
        this.gameDashboard = gameDashboard;
        this.DAO = DAO;
        this.pokeApiGateway = pokeApiGateway;
        this.chooseFavPokemonState = new ChooseFavPokemonState(DAO.get(gameDashboard.getCurrentUser()));

        gameDashboard.setCFPV(this);

        this.pokemonList = getPokemonList();
        this.chosenPokemon = "None";

        JPanel rows = new JPanel();
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));

        chosenLabel = new JLabel("Selected Pokemon: " + this.chosenPokemon);
        chosenLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        listDisplay = new ArrayList<JPanel>();
        for (int i = 0; i < pokemonList.size(); i++) {
            if (page * NUM_ROWS + i < pokemonList.size()) {
                String pokemonName = pokemonList.get(page * NUM_ROWS + i);
                JPanel row = new JPanel();
                row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
                JButton label = new JButton(pokemonName);

                label.addActionListener(
                        new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                if (evt.getSource().equals(label)) {
                                    setChosenPokemon(pokemonName);
                                    chooseFavPokemonViewModel.getState().setFav_pokemon(pokemonName);
                                    chosenLabel.setText("Selected Pokemon: " + pokemonName);
                                }
                            }
                        }
                );

                row.add(label);
                row.add(new JLabel(" "));
                JLabel pokeImage = new JLabel("      ");
                getPokeImage(pokeImage, pokemonName);
                row.add(pokeImage);
                listDisplay.add(row);
            }
        }

        rowHelper(rows);

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

        //buttons
        previousPage = new JButton("Previous");
        previousPage.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(previousPage)) {
                            if (page != 0) {
                                page -= 1;
                                rowHelper(rows);
                            }
                        }
                    }
                }
        );
        nextPage = new JButton("Next");
        nextPage.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(nextPage)) {
                            if ((1 + page) * NUM_ROWS < pokemonList.size()) {
                                page += 1;
                                rowHelper(rows);
                            }
                        }
                    }
                }
        );
        cancel = new JButton("Cancel");
        cancel.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(cancel)) {
                            reset();
                            viewManagerModel.setState("User Profile");
                            viewManagerModel.firePropertyChange();
                        }
                    }
                }
        );
        save = new JButton("Save");
        save.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(save)) {
                            final ChooseFavPokemonState currentState = chooseFavPokemonViewModel.getState();

                            if (currentState.getFav_pokemon().equals("None")) {
                                currentState.setFav_pokemon(null);
                            }

                            chooseFavPokemonController.execute(
                                    currentState.getUsername(),
                                    currentState.getPassword(),
                                    currentState.getScore(),
                                    currentState.getBio(),
                                    currentState.getFav_pokemon()
                            );
//                            if (currentState.getProfileError() != null) {
//                                errorLabel.setText(currentState.getProfileError());
//                            } else {
//                                errorLabel.setText(" ");
//                            }
                            viewManagerModel.setState("User Profile");
                            viewManagerModel.firePropertyChange();
                        }
                    }
                }
        );

        buttons.add(previousPage);
        buttons.add(nextPage);
        buttons.add(cancel);
        buttons.add(save);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(chosenLabel);
        this.add(rows);
        this.add(buttons);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //final EditProfileState state = (EditProfileState) evt.getNewValue();
        //setFields(state);
    }

    public void rowHelper(JPanel rows) {
        rows.removeAll();
        for (int i = 0; i < NUM_ROWS; i++) {
            if (page * NUM_ROWS + i < pokemonList.size()) {
                rows.add(listDisplay.get(NUM_ROWS * page + i));
            }
        }
        rows.revalidate();
        rows.repaint();
    }

    //TODO: read and write adjustments for CA

    public ArrayList<String> getPokemonList1() {
        ArrayList<String> pokemonList = new ArrayList<>();

        //just for testing out
        pokemonList.add("None");
        pokemonList.add("Pikachu");
        pokemonList.add("Charizard");
        pokemonList.add("Greninja");
        pokemonList.add("Snorlax");
        pokemonList.add("Mewtwo");
        pokemonList.add("Gengar");
        pokemonList.add("Lucario");
        pokemonList.add("Gardevoir");
        pokemonList.add("Lugia");
        pokemonList.add("Bulbasaur");
        pokemonList.add("Squirtle");
        pokemonList.add("Rowlet");
        pokemonList.add("Togekiss");
        pokemonList.add("Metagross");
        pokemonList.add("Darkrai");

        return pokemonList;
    }

    public ArrayList<String> getPokemonList() {
        ArrayList<String> pokemonList = new ArrayList<>();
        pokemonList.add("None");
        try {
            File jsonFile = new File("gen1Pokemon.json");
            JSONArray json = new JSONArray(new String(Files.readAllBytes(jsonFile.toPath())));
            for (int i = 0; i < json.length(); i++) {
                String name = json.getJSONObject(i).getString("name");
                name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                pokemonList.add(name);
            }
        } catch (Exception e) {}
        return  pokemonList;
    }

    public void getPokeImage(JLabel pokeImage, String pokemonName) {
        try {
            if (pokemonName.equals("None")) {
                pokeImage.setIcon(new ImageIcon(ImageIO.read(new File("nonepokemon.jpg")).
                        getScaledInstance(50,50,Image.SCALE_SMOOTH)));
            } else {
                Image image = ImageIO.read(new URL(pokeApiGateway.fetchPokemon(pokemonName).getSpriteUrl()));
                pokeImage.setIcon(new ImageIcon(image.getScaledInstance(
                        50, 50, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {}
    }

    public void setChosenPokemon(String chosenPokemon) {
        this.chosenPokemon = chosenPokemon;
    }

    public void setFields(String currentUser) {
        this.currentUser = currentUser;
    }

    public void reset() {
        this.page = 0;
        this.chosenPokemon = "None";
    }


    public String getViewName() {
        return viewName;
    }

    public void setChooseFavPokemonController(ChooseFavPokemonController chooseFavPokemonController) {
        this.chooseFavPokemonController = chooseFavPokemonController;
    }
}
