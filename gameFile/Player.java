package gameFile;

import javafx.scene.paint.Color;

/**
 * Class representing player.
 */
public class Player {
    /**
     * The string of player's name.
     */
    private String name;
    /**
     * The token color player choose to use (Black/White).
     */
    private Token token;
    /**
     * Indicate the number of tokens left by the player.
     */
    private int numOfTokenLeft;
    /**
     * Indicate the number of tokens outside(haven't placed).
     */
    private int numOfTokenLeftOutside;
    /**
     * Indicate if the player can use flying move.
     */
    private boolean canFly;
    /**
     * display color of the token of the player
     */
    private Color displayColor;

    /**
     * Player class constructor.
     *
     * @param playerName The name of player.
     * @param token The token color.
     */
    public Player(String playerName, Token token) {
        this.name = playerName;
        this.token = token;
        this.numOfTokenLeft = 9;
        this.numOfTokenLeftOutside = 9;
        this.canFly = false;
        this.displayColor = (this.token == Token.BLUE)? Color.BLUE : Color.RED;
    }

    /**
     * Get the player's name.
     *
     * @return The player's name in string.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the token color used by player.
     *
     * @return The token color.
     */
    public Token getToken() {
        return this.token;
    }

    /**
     * Get the color to be displayed on board.
     * We are not using black and white yet because we are not done with UI design.
     *
     * @return The color to display on board.
     */
    public Color getDisplayColor() {
        return this.displayColor;
    }

    /**
     * Get the number of token left to play.
     *
     * @return an integer representing number of token left in play.
     */
    public int getNumOfTokenLeft() {
        return this.numOfTokenLeft;
    }
    /**
     * Get the number of token haven't placed.
     *
     * @return an integer representing number of token haven't placed.
     */
    public int getNumOfTokenLeftOutside() {
        return numOfTokenLeftOutside;
    }

    /**
     * Enable the player to use flying move.
     */
    public void enableFly() {
        this.canFly = true;
    }

    /**
     * Check if the player is allowed to use flying move.
     *
     * @return true if player can fly, otherwise false.
     */
    public boolean allowedFly() {
        return this.canFly;
    }

    /**
     * Method to reduce the token left to play if it gets removed by opponent.
     */
    public void reduceToken() {
        this.numOfTokenLeft--;
    }

    /**
     * Method to reduce the token haven't placed if it is placed by player.
     */
    public void reduceTokenOutside() {
        this.numOfTokenLeftOutside--;
    }

}
