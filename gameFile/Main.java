package gameFile;
import gameFile.Game.NormalGame;
import gameFile.Game.TutorialGame;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application{
    private Stage stage;
    private Scene scene;
    private Scene mainMenuScene;
    private Scene NormalGameScene;
    private Scene tutorialGameScene;
    private NormalGame newGame;

    private TutorialGame newTutorial;

    private Button quitToMainMenuButton;

    @Override
    public void start(Stage primaryStage) {
        try{
            stage = primaryStage;
            stage.setTitle("Nine-Man Morris Game");
            stage.setResizable(true);

            NormalGameScene = createNormalGameScene();
            tutorialGameScene = createTutorialGameScene();
            mainMenuScene = createMainMenuScene();
            stage.setScene(mainMenuScene);
            stage.show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }



    private Scene createNormalGameScene() {

        BorderPane root = new BorderPane();
        scene = new Scene(root, UIConstants.APP_WIDTH, UIConstants.APP_HEIGHT, Color.AZURE);
        newGame = new NormalGame("Alex", "Jia", Token.RED, Token.BLUE);
        Board newBoard = newGame.getBoard();
        BorderPane.setAlignment(root, Pos.TOP_LEFT);
        root.setCenter(newBoard.getStackPane());

        createMainMenuButton(root);

        createRestartButton(root,false);

        newGame.startGame();

        return scene;
    }

    private Scene createTutorialGameScene() {
        BorderPane root = new BorderPane();
        scene = new Scene(root, UIConstants.APP_WIDTH, UIConstants.APP_HEIGHT, Color.AZURE);
        newTutorial = new TutorialGame("Alex", Token.RED);
        Board newBoard = newTutorial.getBoard();
        BorderPane.setAlignment(root, Pos.TOP_LEFT);
        root.setCenter(newBoard.getStackPane());

        createMainMenuButton(root);

        createRestartButton(root,true);

        newTutorial.startGame();



        return scene;
    }

    public void restart(BorderPane root, boolean isTutorial){
        if (isTutorial){
            newTutorial = new TutorialGame("Alex", Token.RED);
            Board newBoard = newTutorial.getBoard();
            BorderPane.setAlignment(root, Pos.TOP_LEFT);
            root.setCenter(newBoard.getStackPane());
            createMainMenuButton(root);
            createRestartButton(root, isTutorial);
            newTutorial.startGame();
        }
        else{
            newGame = new NormalGame("Alex", "Jia", Token.RED, Token.BLUE);
            Board newBoard = newGame.getBoard();
            BorderPane.setAlignment(root, Pos.TOP_LEFT);
            root.setCenter(newBoard.getStackPane());
            createMainMenuButton(root);
            createRestartButton(root,isTutorial);
            newGame.startGame();
        }

    }

    private void createMainMenuButton(BorderPane root){
        this.quitToMainMenuButton = new Button("To Main Menu");
        quitToMainMenuButton.setMinSize(135, 30);
        quitToMainMenuButton.setTranslateY(25);
        quitToMainMenuButton.setTranslateX(25);
        root.setTop(quitToMainMenuButton);
        quitToMainMenuButton.setOnAction(event ->
                switchScenes(mainMenuScene));
    }

    private void createRestartButton(BorderPane root,boolean isTutorial){
        Button reset = new Button("Restart");
        reset.setMinSize(135, 30);
        reset.setTranslateY(-25);
        reset.setTranslateX(25);
        root.setBottom(reset);

        reset.setOnAction(event ->
                restart(root,isTutorial));
    }

    private Scene createMainMenuScene() {

        BorderPane root = new BorderPane();
        scene = new Scene(root, UIConstants.APP_WIDTH, UIConstants.APP_HEIGHT, Color.AZURE);

        // Create Text node
        //default tokens outside the board of both players are 9
        Text tittle = new Text("        Nine-Men Morris \n MA_Friday2pm_Team22");
        // Set the font and font size
        tittle.setFont(Font.font("Arial", 70));
        // Set the position of the text
        tittle.setTranslateX(300); // X-coordinate
        tittle.setTranslateY(100);  // Y-coordinate
        root.setTop(tittle);

        Button startGameButton = new Button("Start Game");
        startGameButton.setMinSize(500, 500);
        startGameButton.setFont(new Font(50));
        startGameButton.setTranslateY(200);
        startGameButton.setTranslateX(200);
        root.setLeft(startGameButton);

        Button tutorialButton = new Button("Tutorial");
        tutorialButton.setMinSize(500, 500);
        tutorialButton.setFont(new Font(50));
        tutorialButton.setTranslateY(200);
        tutorialButton.setTranslateX(-150);
        root.setRight(tutorialButton);

        startGameButton.setOnAction(event ->
                switchScenes(NormalGameScene));

        tutorialButton.setOnAction(event ->
                switchScenes(tutorialGameScene));

        return scene;
    }

    // Switch Scenes in single Stage
    public void switchScenes(Scene scene) {
        NormalGameScene = createNormalGameScene();
        tutorialGameScene = createTutorialGameScene();
        stage.setScene(scene);
    }


    public static void main(String[]  args){
        launch(args);
    }
}


