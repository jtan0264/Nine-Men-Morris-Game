package gameFile.Game;

import gameFile.*;
import gameFile.Game.Game;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class TutorialGame extends Game {
    /**
     * The two players to represent the player and botplayer.
     */
    private Player humanPlayer, botPlayer;

    /**
     *  Positions that the bot will place tokens
     */
    private int[] bot_placement = {8, 10, 9, 13,3,5};
    /**
     *  Positions that the player will be allowed to place tokens
     */
    private int[] human_placement = {16, 17, 18,10,11,21};

    public TutorialGame(String playerName, Token playerToken) {
        this.humanPlayer = new Player(playerName, playerToken);
        this.botPlayer = new Player("Bot Morris", playerToken == Token.RED? Token.BLUE : Token.RED);
        this.board = new Board(true);

    }

    public Board getBoard() {
        return this.board;
    }


    /**
     * TutorialGame class' main method to begin the game.
     */
    public void startGame(){
        //game start with placing phase
        this.gamePhase = UIConstants.PLACING_PHASE;
        //play the turn
        this.playTurn();

        if (this.turns == 0) {
            updateHintUI("                                     Welcome to the tutorial Mode.\nIn this mode you will be playing a simulated step by step 9 man morris game.\n                        Click on the position highlighted in red to begin.");
        }

    }

    /**
     * Method to implement tutorial system and control the game phase.
     */
    protected void playTurn() {
        this.currentPlayer = humanPlayer;
        resetHighlight();
        removeAllListeners();
        if (this.human_placement.length > turns){
            Position target_pos = this.board.getOnePosition(this.human_placement[turns]);
            highlight(target_pos);
            ArrayList<Position> posList = new ArrayList<>();
            posList.add(target_pos);
            setListenerOnPositions(posList, this.placing());
        }



        if(this.turns == 3){
            updateHintUI("Hah, that'll show him not to mess with you,but look now your opponent is trying to form a mill, place a token to stop him.\nFun Fact: After you form a mill, when its your opponents turn to remove a token he can't remove any token that is apart of a mill");
        }



        if (this.turns == 6) {
            gamePhase = UIConstants.MOVING_PHASE;
            updateHintUI("Now,We've skipped ahead and placed the rest of the tokens to save everyones time, your welcome.\nSelect the token you think you can move to create a mill and choose where you want to move it,\ncome on this is an easy one\nYou can only move a token to its neighbouring empty positions so that means it can't go further than one distance");
            placeToken(this.board.getOnePosition(0));
            placeToken(this.board.getOnePosition(2));
            placeToken(this.board.getOnePosition(7));
            placeToken(this.board.getOnePosition(1));
            placeToken(this.board.getOnePosition(4));
            placeToken(this.board.getOnePosition(6));
            getBoard().getTextUIList()[0].setText("Tokens left: "+ Integer.toString(0));
            getBoard().getTextUIList()[1].setText("Tokens left: "+ Integer.toString(0));
            setListenerOnPlayerTokens(this.clickOnToken());
            //when current game phase is moving phase and one of the player has only 3 tokens on the board,
            // then that player can fly
        } else if (gamePhase ==UIConstants.MOVING_PHASE && this.turns == 7) {
            humanPlayer.enableFly();
            botPlayer.enableFly();
        }


        if (this.turns == 7){
            updateHintUI("Now we've come to a point where both you and your opponent have 3 tokens left overall.\nWhen a player has only 3 tokens left they can now fly, meaning they can move to any empty position on the board\nrather then moving the normal one distance when in the normal moving phase.\nSELECT AND MOVE MY CHILD!!!!!");
            removeToken(this.board.getOnePosition(19));
            removeToken(this.board.getOnePosition(21));
            removeToken(this.board.getOnePosition(18));

            removeToken(this.board.getOnePosition(0));
            removeToken(this.board.getOnePosition(2));
            removeToken(this.board.getOnePosition(7));

            removeToken(this.board.getOnePosition(1));
            removeToken(this.board.getOnePosition(4));
            removeToken(this.board.getOnePosition(6));

            botPlayTurn();
        }

        if (this.turns == 8){
            setListenerOnPlayerTokens(this.clickOnToken());
        }

        if (this.turns == 9){
            updateHintUI("                                                                CONGRATULATIONS\nYou won 9-man morris by reducing your opponents tokens to only two.\nFUN FACT: Another way to win is by making sure your opponent doesn't have anymore valid moves.");

        }

    }
    /**
     * Set listener on current player's tokens so that player can select it.
     * A player's token is a position's circle set to be larger and with the player's display color.
     *
     * @param eventHandler
     */
    protected void setListenerOnPlayerTokens(EventHandler<MouseEvent> eventHandler) {
        ArrayList<Position> currPlayerToken = new ArrayList<>();

        if (turns == 6){
            currPlayerToken.add(this.getBoard().getOnePosition(11));
        }
        if (turns == 8){
            currPlayerToken.add(this.getBoard().getOnePosition(10));
        }
        setListenerOnPositions(currPlayerToken, eventHandler);
    }

    /**
     * bot plays its turn and, it Switches to player's turn.
     */
    public void botPlayTurn() {
        if (this.human_placement.length > turns) {
            this.currentPlayer = botPlayer;
            this.placeToken(this.board.getOnePosition(bot_placement[turns]));
        }
        if (this.turns == 7){
            removeToken(this.board.getOnePosition(9));
        }
        this.turns++;
        System.out.println(this.turns);
        updatePlayerTokenOutsideUI();
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
                if (turns == 2) {
                    ArrayList<Position> posList = new ArrayList<>();
                    Position position = board.getOnePosition(10);
                    highlight(position);
                    posList.add(position);
                    setListenerOnPositions(posList, removing());
                }
                else{
                    botPlayTurn();
                }
            }
        };
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
                    highlight(board.getOnePosition(18));
                    updateHintUI("Like i said normally you can fly to whichever empty position you want,\nbut we want to win, so we're gonna push you towards the right direction.");
                    board.getOnePosition(18).getCircle().setOnMouseClicked(moving(clickedToken, board.getOnePosition(18)));



                    // else highlight and set moving listener on all empty neighbours of the player's tokens
                } else {
                    for(Position pos : getBoard().getNeighbours(clickedToken)) {
                        if (pos.isEmpty()) {
                            System.out.println("clicked");
                            highlight(pos);
                            pos.getCircle().setOnMouseClicked(moving(clickedToken, pos));
                        }
                    }

                }
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
        if ((this.board.getOnePosition(1) == pos) || (this.board.getOnePosition(4) == pos) ||(this.board.getOnePosition(6) == pos)) {
            pos.setOccupied(botPlayer.getToken());
            //change the ui of the position
            pos.getCircle().setRadius(UIConstants.CIRCLE_RADIUS_TOKEN);
            pos.getCircle().setStroke(null);
            pos.getCircle().setFill(botPlayer.getDisplayColor());
        }else{
            pos.setOccupied(currentPlayer.getToken());
            //change the ui of the position
            pos.getCircle().setRadius(UIConstants.CIRCLE_RADIUS_TOKEN);
            pos.getCircle().setStroke(null);
            pos.getCircle().setFill(currentPlayer.getDisplayColor());
        }

        //reduce the tokens haven't placed of the player and update the UI counter

        if (this.turns == 0) {
            updateHintUI("Good job,Now keep placing the tokens in the red highlighted positions.\nThe main objective you want to achieve in 9-man-morris is create mills which are three\ntokens placed three in a row while connected by the lines.");
        }
        else if (this.turns == 1){
            updateHintUI("Waow, now your one placement away from creating a mill, place it now to remove one of your opponents tokens.");
        }
        else if(this.turns == 2){
            updateHintUI("SUPER DUPER JOB YOU CREATED A MILL,now you can remove of your opponents tokens,\nwhich will be highlighted in your token colour,Normally you can remove whichever one of your opponents tokens\nBut for the sake of this tutorial we will only allow you to remove the one we want you to remove.");
        }
        if(this.turns == 3){
            updateHintUI("Ha i can't believe he thought we wouldn't see that,lets keep going!!!\nKeep yourself warm by placing two more tokens before we skip ahead to the next scenario.\nIn the next scenario we will learn about moving");
        }


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

                botPlayTurn();
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
                System.out.println(turns);
                if (turns == 6){
                    removeAllListeners();
                    ArrayList<Position> posList = new ArrayList<>();
                    Position position = board.getOnePosition(3);
                    highlight(position);
                    posList.add(position);
                    setListenerOnPositions(posList, removing());
                    updateHintUI("Perfecto, get rid of your opponents token, no MERCY.\nAfter this we are gonna skip ahead again to learn about how to win and the flying mechanic in this tutorial.\n");
                }
                else if(turns == 8){
                    removeAllListeners();
                    ArrayList<Position> posList = new ArrayList<>();
                    Position position = board.getOnePosition(5);
                    highlight(position);
                    posList.add(position);
                    setListenerOnPositions(posList, removing());
                    updateHintUI("Oh my glob your about to win!!!!\n FINISH HIM");
                }
                else{
                    botPlayTurn();
                }


            }
        };
    }

    /**
     * Update the counter of player's token haven't placed
     */
    protected void updatePlayerTokenOutsideUI() {
        if (turns <= 6){
            getBoard().getTextUIList()[0].setText("Tokens left: "+ Integer.toString(9 - turns));
            getBoard().getTextUIList()[1].setText("Tokens left: "+ Integer.toString(9 - turns));
        }

    }

    /**
     * A simple helper method to apply highlights on given positions.
     *
     * @param position
     */
    protected void highlight(Position position) {
        position.getCircle().setStroke(currentPlayer.getDisplayColor());
        position.getCircle().setStrokeWidth(UIConstants.CIRCLE_STROKE_WIDTH);
        position.setHighlighted(true);
    }
}
