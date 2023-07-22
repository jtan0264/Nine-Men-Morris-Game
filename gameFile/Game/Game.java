package gameFile.Game;

import gameFile.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public abstract class Game {
    /**
     * current player which tells us who is playing right now
     */
    protected Player currentPlayer;
    /**
     * The board object created for the game.
     */
    protected Board board;
    /**
     * An integer representing the game phase.
     */
    protected int gamePhase;

    /**
     * Store the amount of turns taken in-game.
     */
    protected int turns = 0;


    /**
     * A getter to return an instance of itself.
     *
     * @return Board object.
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Update the hint UI
     * @param hint string that needed to be shown in the UI
     */
    protected void updateHintUI(String hint) {
        getBoard().getTextUIList()[2].setText(hint);
    }

    /**
     * Implementation of remove token logic
     *
     * @param removePos The position of the token to be removed.
     */
    protected void removeToken(Position removePos) {
        //update the position UI and update position to be occupied by empty
        removePos.getCircle().setFill(Color.BLACK);
        removePos.getCircle().setRadius(UIConstants.CIRCLE_RADIUS_POSITION);
        removePos.setOccupied(Token.EMPTY);
    }

    /**
     * Set listener on given list of positions and handle the listened event based on given event handler.
     *
     * @param posList The list of positions that will listen to an event.
     * @param eventHandler The handler method to be set on position.
     */
    protected void setListenerOnPositions(ArrayList<Position> posList, EventHandler<MouseEvent> eventHandler) {
        for(Position pos : posList) {
            Circle circlePos = pos.getCircle();
            circlePos.setOnMouseClicked(eventHandler);
        }
    }

    /**
     * Remove all the highlights applied on positions/tokens.
     */
    protected void resetHighlight() {
        for(Position pos : this.board.getPositionList()) {
            pos.getCircle().setStrokeWidth(0);
            pos.setHighlighted(false);
        }
    }




    /**
     * Remove all the listener set on positions.
     */
    protected void removeAllListeners() {
        List<Position> posList = this.board.getPositionList();
        for(Position p : posList) {
            p.getCircle().setOnMouseClicked(null);
        }
    }

    /**
     * Search the position based on mouse event occurred.
     *
     * @param event The event that triggered from mouse click.
     * @return The circle's position where the mouse clicked on it.
     */
    protected Position findPositionFromCircle(MouseEvent event) {
        // This is an explicit casting but since we know that players will only be interacting
        // with positions(circle) on board, all the event source will only be from circle object.
        Position clickPos = null;
        Circle circle = (Circle) event.getSource();
        if(gamePhase ==UIConstants.MOVING_PHASE){
            circle.setStrokeWidth(UIConstants.CIRCLE_STROKE_WIDTH);
            circle.setStroke(Color.BLACK);
        }
        for(Position pos : this.board.getPositionList()) {
            if (pos.getCircle() == circle) {
                clickPos = pos;
                break;
            }
        }
        return clickPos;
    }



    public abstract void startGame();
    protected abstract void playTurn();

    protected abstract void setListenerOnPlayerTokens(EventHandler<MouseEvent> eventHandler);

    protected abstract EventHandler<MouseEvent> placing();

    protected abstract EventHandler<MouseEvent> removing();

    protected abstract EventHandler<MouseEvent> moving(Position fromPos, Position toPos);

    protected abstract EventHandler<MouseEvent> clickOnToken();

    protected abstract void placeToken(Position pos);

    protected abstract void highlight(Position position);

    protected abstract void updatePlayerTokenOutsideUI();





}
