package view;

import data_access.FileUserDataAccessObject;
import data_access.PokeApiGateway;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.EditProfileState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * The View for when the user is choosing their favourite Pokemon to display on profile.
 */
public class ChooseFavPokemonView extends JPanel implements ActionListener, PropertyChangeListener {

    private final ViewManagerModel viewManagerModel;
    private final GameDashboard gameDashboard;
    private final FileUserDataAccessObject DAO;
    private final PokeApiGateway pokeApiGateway;

    private final int NUM_ROWS = 5;
    private int page = 0;
    private final ArrayList<String> pokemonList;

    private JButton nextPage;
    private JButton previousPage;
    private JButton cancel;
    private JButton save;

    private String currentUser;
    private String chosenPokemon;

    ChooseFavPokemonView(ViewManagerModel viewManagerModel, GameDashboard gameDashboard,
                         FileUserDataAccessObject DAO, PokeApiGateway pokeApiGateway) {

        this.viewManagerModel = viewManagerModel;
        this.gameDashboard = gameDashboard;
        this.DAO = DAO;
        this.pokeApiGateway = pokeApiGateway;

        gameDashboard.setCFPV(this);

        this.pokemonList = getPokemonList();
        this.chosenPokemon = "None";

        JPanel rows = new JPanel();
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));

        for (int i = 0; i < NUM_ROWS; i++) {
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
                                }
                            }
                        }
                );

                row.add(label);
                JLabel pokeImage = new JLabel("");
                getPokeImage(pokeImage, pokemonName);
                row.add(pokeImage);
                rows.add(row);
            }
        }

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

        //buttons
        previousPage = new JButton("Previous");
        previousPage.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(previousPage)) {
                            if (page != 0) { page -= 1; }
                        }
                    }
                }
        );
        nextPage = new JButton("Next");
        nextPage.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(nextPage)) {
                            if ((1 + page) * NUM_ROWS <  pokemonList.size()) { page += 1; }
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
                            viewManagerModel.setState("Edit Profile");
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
                            //TODO: execute use case
                            viewManagerModel.setState("Edit Profile");
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
        //TODO: wtv this is
    }

    public ArrayList<String> getPokemonList() {
        ArrayList<String> pokemonList = new ArrayList<>();

        pokemonList.add("None");
        pokemonList.add("pikachu");
        pokemonList.add("charizard");
        pokemonList.add("greninja");
        pokemonList.add("snorlax");
        pokemonList.add("mewtwo");
        pokemonList.add("gengar");
        pokemonList.add("lucario");
        pokemonList.add("gardevoir");
        pokemonList.add("lugia");
        pokemonList.add("bulbasaur");
        pokemonList.add("squirtle");
        pokemonList.add("rowlet");
        pokemonList.add("togekiss");
        pokemonList.add("metagross");
        pokemonList.add("darkrai");

        return pokemonList;
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
}
