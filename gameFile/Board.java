package gameFile;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class representing the Nine Men's Morris board.
 */
public class Board {
    /**
     * A layout pane to arrange node/shape on it.
     */
    private StackPane stackPane;
    /**
     * The 24 positions on NMM board.
     */
    private Position[] positionList = new Position[24];
    /**
     * The 24 positions' neighbours stored as list of integers.
     */
    private int[][] neighbourPositionList;
    /**
     * All the 16 mills combinations on the board as list of integers.
     */
    private int[][] millsCombination;
    /**
     * All the text ui list,
     * uiList[0] counter of player 1 tokens outside the board
     * uiList[1] counter of player 2 tokens outside the board
     * uiList[2] hint
     * uiList[3] indicate current turn
     */
    private Text[] textUIList = new Text[5];
    /**
     * Circle UI indicating current turn.
     */
    private Circle currentTurnCircleUI;
    /**
     *  Hint button
     */
    private ToggleButton hintButton;





    /**
     * Board class constructor.
     */
    public Board(Boolean tutorial) {
        stackPane = new StackPane();
        stackPane.setMaxSize(UIConstants.BOARD_WIDTH, UIConstants.BOARD_HEIGHT);

        drawLines();
        buildBoard();
        setNeighbours();
        setMillsCombination();
        drawUI(tutorial);
        drawCircleUI(tutorial);


    }

    /**
     * Get all the 24 positions on board.
     *
     * @return list of positions.
     */
    public List<Position> getPositionList() {
        List<Position> posList = Arrays.asList(this.positionList);
        return Collections.unmodifiableList(posList);
    }

    public Position getOnePosition(int posIndex) {
        return this.positionList[posIndex];
    }

    /**
     * Get the target position's neighbour positions.
     *
     * @param position The position to find its neighbours.
     * @return
     */
    public ArrayList<Position> getNeighbours(Position position) {
        ArrayList<Position> neighbours = new ArrayList<>();
        for(int neighboursIndex : this.neighbourPositionList[position.getPosIndex()]) {
            neighbours.add(this.positionList[neighboursIndex]);
        }
        return neighbours;
    }

    /**
     * Get the stack pane layout used by board class.
     *
     * @return stack pane with lines and circles.
     */
    public StackPane getStackPane() {
        return this.stackPane;
    }

    /**
     * Get all the mills combinations on NMM.
     *
     * @return a list of lists of 3 integers.
     */
    public int[][] getMillsCombination() {
        return this.millsCombination;
    }

    /**
     * Helper function to draw lines for NMM board on stack pane.
     */
    private void drawLines() {
        for (int s = 0; s < 3; s++) {
            int scale = UIConstants.BOARD_WIDTH - s * 250;
            Rectangle square = new Rectangle(scale, scale);
            square.setStroke(Color.BLACK);
            square.setStrokeWidth(2);
            square.setFill(Color.TRANSPARENT);
            this.stackPane.getChildren().add(square);
        }
        Line[] lines = {
                new Line(0, 0, UIConstants.LINE_LENGTH, 0),
                new Line(0, 0, UIConstants.LINE_LENGTH, 0),
                new Line(0, 0, 0, UIConstants.LINE_LENGTH),
                new Line(0, 0, 0, UIConstants.LINE_LENGTH)
        };

        for (Line l : lines) {
            l.setStroke(Color.BLACK);
            l.setStrokeWidth(2);
        }

        lines[0].setTranslateX(-UIConstants.MIDLINE_OFFSET);
        lines[1].setTranslateX(UIConstants.MIDLINE_OFFSET);
        lines[2].setTranslateY(-UIConstants.MIDLINE_OFFSET);
        lines[3].setTranslateY(UIConstants.MIDLINE_OFFSET);

        this.stackPane.getChildren().addAll(lines);

    }

    /**
     * Helper function to create positions/circles and place it on set coordinates.
     */
    private void buildBoard() {
        int[] offsets = {UIConstants.OUTER_OFFSET, UIConstants.MIDDLE_OFFSET, UIConstants.INNER_OFFSET};

        int[][] translations = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};

        int index = 0;
        for (int l = 0; l < 3; l++) {
            for (int i = 0; i < 9; i++) {
                if (i != 4) {
                    int y = i % 3;
                    int x = Math.floorDiv(i, 3);
                    Position pos = new Position(index);
                    pos.getCircle().setTranslateX(translations[i][0] * offsets[l]);
                    pos.getCircle().setTranslateY(translations[i][1] * offsets[l]);
                    this.positionList[index] = pos;
                    index++;

                    this.stackPane.getChildren().add(pos.getCircle());
                }
            }
        }
    }

    /**
     * create and store each position's neighbours' integer index.
     */
    private void setNeighbours() {
        int[] p0 = {1,3};
        int[] p1 = {0,2,9};
        int[] p2 = {1,4};
        int[] p3 = {0,5,11};
        int[] p4 = {2,7,12};
        int[] p5 = {3,6};
        int[] p6 = {5,7,14};
        int[] p7 = {4,6};
        int[] p8 = {9,11};
        int[] p9 = {1,8,10,17};
        int[] p10 = {9,12};
        int[] p11 = {3,8,13,19};
        int[] p12 = {4,10,15,20};
        int[] p13 = {11,14};
        int[] p14 = {6,13,15,22};
        int[] p15 = {12,14};
        int[] p16 = {17,19};
        int[] p17 = {9,16,18};
        int[] p18 = {17,20};
        int[] p19 = {11,16,21};
        int[] p20 = {12,18,23};
        int[] p21 = {19,22};
        int[] p22 = {14,21,23};
        int[] p23 = {20,22};

        this.neighbourPositionList = new int[][]{p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22, p23};


    }

    /**
     * getter of the hint toggle button on
     */
    public ToggleButton getHintButton() {
        return hintButton;
    }

    /**
     * create and store all possible mills combinations.
     */
    private void setMillsCombination() {
        this.millsCombination = new int[][]{
                {0,1,2}, {0,3,5}, {1,9,17}, {2,4,7},
                {3,11,19}, {4,12,20}, {5,6,7}, {6,14,22},
                {8,9,10}, {8,11,13}, {10,12,15}, {13,14,15},
                {16,17,18}, {16,19,21}, {18,20,23}, {21,22,23}
        };
    }
    /**
     * getter of textUIList
     */

    public Text[] getTextUIList() {
        return textUIList;
    }

    /**
     * getter of currentTurnUI
     */
    public Circle getCurrentTurnCircleUI() {
        return currentTurnCircleUI;
    }

    /**
     * create and store all text ui element
     */
    private void drawUI(boolean tutorial) {
        // Create Text node
        //default tokens outside the board of both players are 9
        Text whiteTokensCounter = new Text("Tokens left: 9");
        Text blackTokensCounter = new Text("Tokens left: 9");
        Text hint = new Text(" ");
        Text currentTurn = new Text(" ");
        Text hintText = new Text(" ");

        // Set the font and font size
        whiteTokensCounter.setFont(Font.font("Arial", 20));
        blackTokensCounter.setFont(Font.font("Arial", 20));
        hint.setFont(Font.font("Arial", 20));
        currentTurn.setFont(Font.font("Arial", 20));
        hintText.setFont(Font.font("Arial", 15));

        // Set the position of the text
        whiteTokensCounter.setTranslateX(-600); // X-coordinate
        whiteTokensCounter.setTranslateY(0);  // Y-coordinate
        blackTokensCounter.setTranslateX(600); // X-coordinate
        blackTokensCounter.setTranslateY(0);  // Y-coordinate
        hint.setTranslateX(0); // X-coordinate
        hint.setTranslateY(-450);  // Y-coordinate
        currentTurn.setTranslateX(0); // X-coordinate
        currentTurn.setTranslateY(450);  // Y-coordinate
        hintText.setTranslateX(00); // X-coordinate
        hintText.setTranslateY(-40);  // Y-coordinate

        if (tutorial == false){
            ToggleButton hintButton = new ToggleButton("Hint: OFF");
            hintButton.setMinSize(135, 30);
            hintButton.setTranslateY(60);
            hintButton.setTranslateX(0);
            this.stackPane.getChildren().add(hintButton);
            this.hintButton = hintButton;
        }


        textUIList[0]= whiteTokensCounter;
        textUIList[1]= blackTokensCounter;
        textUIList[2]= hint;
        textUIList[3]= currentTurn;
        textUIList[4]= hintText;

        this.stackPane.getChildren().add(whiteTokensCounter);
        this.stackPane.getChildren().add(blackTokensCounter);
        this.stackPane.getChildren().add(hint);
        this.stackPane.getChildren().add(currentTurn);
        this.stackPane.getChildren().add(hintText);

    }

    /**
     * create  circle ui elements
     */
    private void drawCircleUI(boolean tutorial){

        Circle currentTurnCircle = new Circle(20);
        currentTurnCircle.setTranslateX(-100);
        currentTurnCircle.setTranslateY(450);
        currentTurnCircle.setFill(Color.RED);

        Circle player1TokenCircle  = new Circle(20);
        player1TokenCircle .setTranslateX(-600);
        player1TokenCircle .setTranslateY(-50);
        player1TokenCircle .setFill(Color.RED);

        Circle player2TokenCircle  = new Circle(20);
        player2TokenCircle.setTranslateX(600);
        player2TokenCircle.setTranslateY(-50);
        player2TokenCircle.setFill(Color.BLUE);

        this.currentTurnCircleUI = currentTurnCircle;

        if (tutorial == false) {
            this.stackPane.getChildren().add(currentTurnCircle);
        }


        this.stackPane.getChildren().add(player1TokenCircle);
        this.stackPane.getChildren().add(player2TokenCircle);
    }
}