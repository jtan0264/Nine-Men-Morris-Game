package gameFile;

import javafx.scene.shape.Circle;

/**
 * Class representing the position on NMM board.
 */
public class Position {
    /**
     * Unique integer to indicate position index.
     */
    private final int posIndex;
    /**
     * Store which token currently occupying this position.
     */
    private Token occupiedBy;
    /**
     * Shape used for UI.
     */
    private Circle circle;
    /**
     * boolean indicating the pos is highlighted
     */
    private boolean isHighlighted;



    /**
     * Position class constructor.
     *
     * @param positionIndex The integer representing its position index in the list from board object.
     */
    public Position(int positionIndex) {
        this.posIndex = positionIndex;
        this.occupiedBy = Token.EMPTY;
        this.circle = new Circle(UIConstants.CIRCLE_RADIUS_POSITION);
    }

    /**
     * Get the position's index.
     *
     * @return position index
     */
    public int getPosIndex() {
        return this.posIndex;
    }

    /**
     * Getter of the boolean isHighlighted
     *
     * @return boolean isHighlighted
     */
    public boolean isHighlighted() {
        return isHighlighted;
    }
    /**
     * Setter of the boolean isHighlighted
     *
     * @param highlighted  boolean isHighlighted
     */
    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }
    /**
     * Set which token occupied the position.
     *
     * @param token The Token to be set on the position.
     */
    public void setOccupied(Token token) {
        this.occupiedBy = token;
    }

    /**
     * Get which token is currently occupying the position.
     *
     * @return The token that occupied the position.
     */
    public Token getToken() {
        return this.occupiedBy;
    }

    /**
     * Get the circle object of the position.
     *
     * @return The circle object.
     */
    public Circle getCircle() {
        return this.circle;
    }

    /**
     * Check if the position is occupied by black token or white token.
     *
     * @return true if no token is occupying the position, otherwise false.
     */
    public boolean isEmpty() {
        return this.occupiedBy == Token.EMPTY;
    }
}
