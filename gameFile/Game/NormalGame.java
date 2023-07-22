package gameFile.Game;

import gameFile.*;
import gameFile.Game.Game;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;

/**
 * Class representing the Nine Men's Morris game.
 */
public class NormalGame extends Game {
    /**
     * The two players, player 1 or player 2.
     */
    private Player player1, player2;

    /**
     * Boolean to check if a game is over.
     */
    private boolean gameOver = false;
    /**
     * Boolean to check if hint toggle is on
     */
    private boolean isHintOn = false;
    /**
     * Game class constructor.
     *
     * @param player1Name The string of player1's name.
     * @param player2Name The string of player2's name.
     * @param player1Token The token color of player1.
     * @param player2Token The token color of player2.
     */
    public NormalGame(String player1Name, String player2Name, Token player1Token, Token player2Token) {
        player1 = new Player(player1Name, player1Token);
        player2 = new Player(player2Name, player2Token);
        board = new Board(false);
    }

    /**
     * NormalGame class' main method to begin the game.
     */
    public void startGame(){
        //player 1 start first
        this.currentPlayer = player1;
        //game start with placing phase
        this.gamePhase = UIConstants.PLACING_PHASE;

        setListenerOnHintButton(getBoard().getHintButton());

        //play the turn
        this.playTurn();
    }

    /**
     * Set listener on hint toggle button
     */
    private void setListenerOnHintButton(ToggleButton hintButton) {
        // Handle toggle button events
        hintButton.setOnAction(event -> {
            if (hintButton.isSelected()) {
                this.isHintOn = true;
                hintButton.setText("Hint: ON");
                if(gamePhase == UIConstants.PLACING_PHASE){
                    updateHintText("Place your tokens \non the highlighted\n      positions.");
                }
                else if(gamePhase == UIConstants.MOVING_PHASE){

                    if (currentPlayer.allowedFly()== true){
                        updateHintText("You can move \nyour tokens to \nany positions now.");
                    }
                    else{
                        updateHintText("Click your tokens \nand move it to \nhighlighted positions.");
                    }
                }
                for(Position pos : this.board.getPositionList()) {
                    if (pos.isHighlighted()== true) {
                        highlight(pos);
                    }
                }

            } else {
                this.isHintOn = false;
                updateHintText(" ");
                hintButton.setText("Hint: OFF");
                for(Position pos : this.board.getPositionList()) {
                    if (pos.isHighlighted()== true) {
                        pos.getCircle().setStrokeWidth(0);
                    }
                }
            }
        });
    }

    /**
     * Method to implement turn-based system and control the game phase.
     */
    protected void playTurn() {
        //First reset all the highlight and event listener on every position
        resetHighlight();
        removeAllListeners();
        //check for win condition to make sure the game continue
        gameOver = checkWinConditions();
        //update the UI indicate which player turn is in current turn
        updateCurrentTurnUI();


        if (!gameOver) {

            //when turns == 18, means both player have placed 9 tokens each, then it becomes moving phase
            if (this.turns == 18) {
                gamePhase = UIConstants.MOVING_PHASE;
            //when current game phase is moving phase and one of the player has only 3 tokens on the board,
                // then that player can fly
            } else if (gamePhase ==UIConstants.MOVING_PHASE && (player1.getNumOfTokenLeft() == 3 || player2.getNumOfTokenLeft() == 3)) {
                if (player1.getNumOfTokenLeft() == 3) {
                    player1.enableFly();
                } else if (player2.getNumOfTokenLeft() == 3) {
                    player2.enableFly();
                }
            }
            //check if current game phase is placing phase, get empty position and set placing listener on them
            if (gamePhase == UIConstants.PLACING_PHASE) {
                updateHintUI("Place your tokens on the board.");
                if(isHintOn == true){
                    updateHintText("Place your tokens \non the highlighted\n      positions.");
                }
                highlightEmptyPositions();
                setListenerOnPositions(this.getEmptyPositions(), this.placing());
                //else set listener on player token for moving
            } else {
                updateHintUI("Move your tokens to create a mill.");
                if(isHintOn == true){
                    updateHintText("Click your tokens \nand move it to\nhighlighted positions.");
                }
                if(currentPlayer.allowedFly()){
                    if(isHintOn == true){
                        updateHintText("You can move \nyour tokens to \nany positions now.");
                    }
                    updateHintUI(currentPlayer.getName() + " can fly now ! Move your tokens to anyplace.");
                }
                setListenerOnPlayerTokens(this.clickOnToken());
            }
            this.turns++;
            //when the game is over
        } else {
                updateHintUI("Game ended!!" +
                   getOpponent().getName() + " (" + getOpponent().getToken().toString() + ") won.");
        }
    }

    /**
     * Check if oen of the players achieved win conditions.
     *
     * @return true if a player wins, otherwise false.
     */
    private boolean checkWinConditions() {
        boolean won = false;
        //check for win condition if one player has less than 3 tokens or can make any legal move
        if (player1.getNumOfTokenLeft() < 3 ||
                player2.getNumOfTokenLeft() < 3 ||
                (gamePhase != UIConstants.PLACING_PHASE && noMoveAvailable())) {
            won = true;
        }
        return won;
    }

    /**
     * Method used in checkWinConditions, check if all the current player's tokens are blocked by opponent's tokens.
     *
     * @return true if no sliding/moving can be performed, otherwise false.
     */
    private boolean noMoveAvailable() {
        //loop through all tokens to get the tokens of the player
        //and check all the tokens' neighbour if all of them are occupied by other tokens, then no valid move
        for(Position pos : this.board.getPositionList()) {
            if (pos.getToken() == currentPlayer.getToken()) {
                for(Position neighbourPos : this.board.getNeighbours(pos)) {
                    if (neighbourPos.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * Switch between player's play turn.
     */
    private void switchTurn() {
        //change current player of the turn to another player
        currentPlayer = this.getOpponent();
        this.playTurn();
    }

    /**
     * Event handler for placing tokens during placing phase.
     */
    protected EventHandler<MouseEvent> placing() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Position pos = findPositionFromCircle(mouseEvent);
                placeToken(pos);
            }
        };
    }

    /**
     * Implementation of place token logic.
     *
     * @param pos The position of the token to be placed on board.
     */
    protected void placeToken(Position pos) {
        //set the following position to be occupied by current player
        pos.setOccupied(currentPlayer.getToken());
        //change the ui of the position
        pos.getCircle().setRadius(UIConstants.CIRCLE_RADIUS_TOKEN);
        pos.getCircle().setStroke(null);
        pos.getCircle().setFill(currentPlayer.getDisplayColor());
        //reduce the tokens haven't placed of the player and update the UI counter
        if(gamePhase == UIConstants.PLACING_PHASE){
            currentPlayer.reduceTokenOutside();
            updatePlayerTokenOutsideUI();
        }
        //check for if mill formed  after placing tokens
        checkForMills(pos);
    }

    /**
     * Event handler for selecting tokens to move it.
     */
    protected EventHandler<MouseEvent> clickOnToken() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                resetHighlight();
                Position clickedToken = findPositionFromCircle(mouseEvent);

                //if the current player can fly, then highlight
                // and set moving listener on all empty positions of the board
                if ( currentPlayer.allowedFly()) {
                    for(Position pos : getEmptyPositions()) {
                        highlight(pos);
                        pos.getCircle().setOnMouseClicked(moving(clickedToken, pos));
                    }


                // else highlight and set moving listener on all empty neighbours of the player's tokens
                } else {
                    for(Position pos : getBoard().getNeighbours(clickedToken)) {
                        if (pos.isEmpty()) {
                            System.out.println("clicked");
                            System.out.println(isHintOn);
                            highlight(pos);
                            pos.getCircle().setOnMouseClicked(moving(clickedToken, pos));
                        }
                    }

                }
            }
        };
    }

    /**
     * Event handler for sliding/moving the token.
     *
     * @param fromPos The position the token moved from.
     * @param toPos The position the token moved to.
     */
    protected EventHandler<MouseEvent> moving(Position fromPos, Position toPos) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                removeToken(fromPos);
                placeToken(toPos);
            }
        };
    }

    /**
     * Event handler for selecting an opponent' token to remove from board.
     */
    protected EventHandler<MouseEvent> removing() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Position target = findPositionFromCircle(mouseEvent);
                removeToken(target);
                getOpponent().reduceToken();
                switchTurn();
            }
        };
    }

    /**
     * Set listener on current player's tokens so that player can select it.
     * A player's token is a position's circle set to be larger and with the player's display color.
     *
     * @param eventHandler
     */
    protected void setListenerOnPlayerTokens(EventHandler<MouseEvent> eventHandler) {
        ArrayList<Position> currPlayerToken = new ArrayList<>();
        for(Position pos : this.board.getPositionList()) {
            if (pos.getToken() == currentPlayer.getToken()) {
                currPlayerToken.add(pos);
            }
        }
        setListenerOnPositions(currPlayerToken, eventHandler);
    }

    /**
     * Set listener on opponent's tokens so that player can select it to remove the token.
     *
     * @param opponent The current player's opponent.
     */
    private void setRemovingListenerOnOpponentTokens(Player opponent) {
        // two array lists to implement token removal when all tokens is in mills.
        ArrayList<Position> opponentTokens = new ArrayList<>();
        ArrayList<Position> notInMillTokens = new ArrayList<>();
        for (Position pos : this.board.getPositionList()) {
            if (pos.getToken() == opponent.getToken()) {
                opponentTokens.add(pos);
            }
        }

        boolean allTokenInMill = true;
        for (Position pos : opponentTokens) {
            if (!isMill(opponent, pos)) {
                allTokenInMill = false;
                notInMillTokens.add(pos);
            }
        }

        // if all tokens is in mill, set listener and event handler on all of the tokens.
        // else only allow tokens that is not in a mill to be removed.
        if (opponent.getNumOfTokenLeft() == 3 || allTokenInMill) {
            for(Position pos : opponentTokens) {
                highlight(pos);
            }
            this.setListenerOnPositions(opponentTokens, this.removing());
        } else {
            for(Position pos : notInMillTokens) {
                highlight(pos);
            }
            this.setListenerOnPositions(notInMillTokens, this.removing());
        }
    }


    /**
     * A simple helper method to apply highlights on given positions.
     *
     * @param position
     */
    protected void highlight(Position position) {
        if(isHintOn == true){
            position.getCircle().setStroke(currentPlayer.getDisplayColor());
            position.getCircle().setStrokeWidth(UIConstants.CIRCLE_STROKE_WIDTH);
        }
        position.setHighlighted(true);
    }

    /**
     * Highlight all the empty positions with current player colour
     */
    private void highlightEmptyPositions() {
        for(Position pos : this.getEmptyPositions()) {
            highlight(pos);
        }
    }


    /**
     * Retrieve and return all the empty positions on board.
     *
     * @return a list of positions which occupiedBy Token.EMPTY.
     */
    private ArrayList<Position> getEmptyPositions() {
        ArrayList<Position> ret = new ArrayList<>();
        for(Position pos : this.board.getPositionList()) {
            if (pos.isEmpty()) {
                ret.add(pos);
            }
        }
        return ret;
    }

    /**
     * Check if a token is part of a Mill.
     *
     * @param player The player's token to check for.
     * @param position The position of the token.
     * @return true if the token is in a mill, otherwise false.
     */
    private boolean isMill(Player player, Position position) {
        int posIndex = position.getPosIndex();
        for(int[] i : this.board.getMillsCombination()) {
            for(int j : i) {
                if (posIndex == j) {
                    if (this.board.getPositionList().get(i[0]).getToken() == player.getToken() &&
                            this.board.getPositionList().get(i[1]).getToken() == player.getToken() &&
                            this.board.getPositionList().get(i[2]).getToken() == player.getToken()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check if after the current player's action results in a mill. If it did, allow for removing, else continue to next turn.
     *
     * @param position The token's position after it is placed on that position.
     */
    private void checkForMills(Position position) {
        if (isMill(currentPlayer, position)) {
            updateHintUI("Mill formed! Remove one token from your opponent.");
            if(isHintOn == true){
                updateHintText("Remove your opponent \n     highlighted tokens.");
            }
            this.removeAllListeners();
            this.resetHighlight();
            this.setRemovingListenerOnOpponentTokens(this.getOpponent());
        } else {
            this.switchTurn();
        }
    }

    /**
     * Simple method to get the current player's opponent.
     *
     * @return The opposing player.
     */
    private Player getOpponent() {
        return this.currentPlayer == player1? player2 : player1;
    }

    /**
     * Update the counter of player's token haven't placed
     */
    protected void updatePlayerTokenOutsideUI() {
        if(currentPlayer == player1){
            int numOfTokenPlayer1 = player1.getNumOfTokenLeftOutside();
            getBoard().getTextUIList()[0].setText("Tokens left: "+ Integer.toString(numOfTokenPlayer1));
        }
        else if (currentPlayer == player2){
            int numOfTokenPlayer2 = player2.getNumOfTokenLeftOutside();
            getBoard().getTextUIList()[1].setText("Tokens left: "+ Integer.toString(numOfTokenPlayer2));

        }

    }
    /**
     * Update the hint UI
     * @param hint string that needed to be shown in the UI
     */
    private void updateHintText(String hint) {

        getBoard().getTextUIList()[4].setText(hint);
    }

    /**
     * Update the current turn UI
     */
    private void updateCurrentTurnUI() {
        getBoard().getTextUIList()[3].setText(currentPlayer.getName()+ "'s turn");
        getBoard().getCurrentTurnCircleUI().setFill(currentPlayer.getDisplayColor());
    }

}
