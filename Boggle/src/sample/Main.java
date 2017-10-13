package sample;


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.animation.AnimationTimer;


public class Main extends Application {


    private Stage window;
    private String playword = "";
    private ArrayList<String> words = Dictonary.fillDictonary();
    private ArrayList<Button> pressedbuttons = new ArrayList<Button>();
    private ArrayList<String> checkedWords = new ArrayList<>();
    private Button[][] board;
    private boolean[][] visited;
    private double validCount = 0;
    private double totalCount = 0;
    private Timer timer;
    private int totalScore = 0;
    public Label timelabel;
    long startTime;
    long restartedTime = 0;
    long pausedTime = 0;
    long estimatedTime;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        window = primaryStage;
        window.setTitle("Boggle");
        startScene();

    }

    /**
     * This is the first screen that we will see. It will simply display buttons that will
     * Allow the users to choose how large they want their borad to be.
     */
    private void startScene()
    {
        BorderPane startLayout = new BorderPane();


        HBox titlebox = new HBox();
        Label title = new Label("B O G G L E");
        title.setStyle("-fx-font-size: 72px; " +
                "-fx-font-style: italic;" +
                "-fx-font-weight: bolder;"  +
                "-fx-text-fill: lime;");

        VBox buttonbox = new VBox(2);
        Button Four_Button = new Button("4x4 Board");
        Four_Button.setStyle("-fx-text-fill: black;" +
                "    -fx-background-color: lime;" +
                "    -fx-background-radius: 20;");

        Button Five_Button = new Button("5x5 Board");
        Five_Button.setStyle("-fx-text-fill: black;;" +
                "    -fx-background-color: lime;;" +
                "    -fx-background-radius: 20;");
        Four_Button.setPrefWidth(150);
        Four_Button.setOnAction(e ->
                mainController(4));

        Five_Button.setPrefWidth(150);
        Five_Button.setOnAction(e ->
                mainController(5));

        buttonbox.getChildren().addAll(Five_Button,Four_Button);
        buttonbox.setAlignment(Pos.CENTER);
        startLayout.setCenter(buttonbox);


        titlebox.getChildren().add(title);
        titlebox.setAlignment(Pos.TOP_CENTER);
        startLayout.setTop(titlebox);


        startLayout.setStyle("-fx-background-color: black;");
        Scene startScene = new Scene(startLayout, 750, 500);
        window.setScene(startScene);
        window.show();
    }

    /**
     *
     * @param word is the word that we want to pass in to score
     * @return the length of the word minus two which is our score
     */
    public int scoreWord(String word)
    {
        return  word.length() - 2;
    }

    /**
     *
     * @param letter is the letter passed in from the button pressed
     * @function makes the letter lowercase and then adds it to the playword
     */
    public void addToWord(String letter)
    {

        playword += letter;
    }

    /**
     *
     * @param checkme is our word we want to check if it's in the dictionary
     * @return returns true if the word is in the dictionary, false if not.
     */
    private boolean checkWord(String checkme)
    {
        if(words.contains(checkme.toLowerCase()) && !(checkedWords.contains(checkme)) && scoreWord(checkme) > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     *  This method cleans the visited 2-D array that I am using to treack
     *  which buttons have been visited yet so that the game is able to
     *  know where you have already used words
     * @param n - n is the tray size
     */
    private void cleanVisited(int n)
    {
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                visited[i][j] = false;
            }
        }
    }


    /**
     *  This function does all the work for pressing the submit button.
     *  When submit is clicked, we check the choosen word, score the word,
     *  then update the GUI with the word and matching score.
     * @param infobox - Where the words will be displayed
     * @param totscore - Where the total score will be displayed
     */
    private void submitButtonPress(VBox infobox, Label totscore)
    {
        if (checkWord(playword) == true) {
            if (scoreWord(playword) > 0) {
                validCount++;
                checkedWords.add(playword);
                totalScore += scoreWord(playword);
                totscore.setText("Total Score: " + totalScore);
                Label tmp = new Label();
                tmp.setText(playword + "  ,  " + scoreWord(playword));
                tmp.setStyle("-fx-font-size: 18px");
                infobox.getChildren().add(tmp);
                playword = "";
            }

        }
        else
        {
            Label tmp = new Label();
            tmp.setStyle("-fx-text-fill: red;" + "-fx-font-size: 18px");
            tmp.setText(playword + "  ,  " + 0);
            infobox.getChildren().add(tmp);
            playword = "";
            System.out.println("Sorry that word is not valid");
        }
        for (Button b : pressedbuttons) {

            b.setStyle("-fx-font-size: 35px;" +
                    " -fx-background-color: black;" +
                    "-fx-text-fill: lime;" +
                    " -fx-border-color:  lime;");
        }
        pressedbuttons.clear();

    }

    /**
     * gameButtonPress is the logic behind every press of a board piece
     * all styling and logic is done in this method for if the button is pressed
     * @param button - the button from the gameboard
     */
    private void gameButtonPress(Button button)
    {
       /*n is rows and m is cols*/
        int n = button.getId().charAt(0)-'0';
        int m = button.getId().charAt(1)-'0';
        /*This prevents us from clicking the same button twice*/
        if(visited[n][m] == true)
        {
            button.setStyle("-fx-background-color: yellow;");
        }
        else
        {
            visited[n][m] = true;
            addToWord(button.getText());
            button.setStyle("-fx-background-color: darkblue;" + "-fx-text-fill: maroon;");
            Label tmp = new Label();
            tmp.setStyle("-fx-font-size: 15px");
            tmp.setText(playword);
        }
    }

    /**
     * createBoard will create all of our buttons for us so that we don't have
     * a super messy maincontroller class.
     * @param traySize input to board so we know how big to make it
     * @return returns an 2-D array for our board.
     */
    private Button[][] createBoard(int traySize )
    {
        /*I added double the vowels so that the board would have more words*/
        String abet = "AABCDEEFGHIIJKLMNOOPQRSTUUVWXYZ";
        Random rand = new Random();

        for (int i = 0; i < traySize; i++)
        {
            for (int j = 0; j < traySize; j++)
            {
                String btnletter = Character.toString(abet.charAt(rand.nextInt(abet.length() - 1)));
                Button button = new Button(btnletter);
                button.setPrefSize(200, 200);
                button.setFont(Font.font(20));
                button.setStyle("-fx-font-size: 35px;" +
                        " -fx-background-color: black;" +
                        " -fx-text-fill: lime;" +
                        " -fx-border-color:  lime;");
                /*Here i store where the button is in the array by assigning it to the ID*/
                button.setId(""+i+j);
                button.setOnMousePressed(e ->
                        {
                            gameButtonPress(button);
                        }
                );
                button.setOnMouseReleased(e ->
                {
                    button.setStyle("-fx-background-color: black;" + "-fx-text-fill: maroon;");
                    pressedbuttons.add(button);
                });
                board[i][j] = button;
            }
        }
        return board;
    }




    /**
     * mainController holds all of our gameplay. Will do everything as far as we need
     * for playing the game. When the timer has ended this scence will end.
     * @param traySize is input so we know how big to make the board for the game
     */
    private void mainController(int traySize)
    {


        Scene gameScene;
        BorderPane rootPane = new BorderPane();
        GridPane gameTray = new GridPane();
        timer = new Timer();
        startTime = System.nanoTime();
        timer.start();
        board = new Button[traySize][traySize];
        board = createBoard(traySize);
        visited = new boolean[traySize][traySize];
        //findAllWords(traySize);


        VBox infobox = new VBox(3);
        Label info = new Label();
        info.setText("Guessed words, (word , score)");
        info.setStyle("-fx-font-weight: 800;" + "-fx-font-size: 20px;");



        Label totscore = new Label();
        totscore.setStyle("-fx-font-weight: bolder;" + "-fx-font-size: 20px");
        totscore.setText("Total Score: " + totalScore);


        HBox submitBox = new HBox();
        Button submit = new Button("Submit Selection");
        submit.setPrefSize(500,60);
        submit.setStyle( " -fx-background-color: black;" +
                " -fx-text-fill: lime;" +
                " -fx-border-color:  lime;");
        submit.setOnAction(e ->
        {
            cleanVisited(traySize);
            totalCount++;
            submitButtonPress(infobox , totscore);
        });



        for (int i = 0; i < traySize; i++)
        {
            for (int j = 0; j < traySize; j++)
            {
                gameTray.add(board[i][j], i, j);
            }
        }


        submitBox.getChildren().add(submit);
        submitBox.setAlignment(Pos.CENTER_RIGHT);
        rootPane.setRight(submitBox);


        gameTray.setAlignment(Pos.CENTER);
        rootPane.setCenter(gameTray);

        timelabel = new Label();
        infobox.getChildren().addAll(timelabel,totscore, info);
        infobox.setAlignment(Pos.TOP_RIGHT);
        rootPane.setLeft(infobox);



        rootPane.setStyle("-fx-border-color: lime;" +
                "-fx-background-color: darkorange;");
        gameScene = new Scene(rootPane);
        window.setScene(gameScene);
        window.setFullScreen(true);



    }


    private void endController()
    {
        Scene endScene;
        BorderPane endPane = new BorderPane();

        VBox accuracy = new VBox();
        Label happyHalloween = new Label("Happy Halloween / Fall Break!!!!!!");
        happyHalloween.setStyle("-fx-font-size: 28px;");
        Label valid   = new Label("Valid words: " + String.valueOf((int)validCount));
        Label invalid = new Label("Invalid words: " + String.valueOf((int)totalCount-validCount));
        Label totscore = new Label("Finishing score was : " + totalScore);
        Label perc = new Label();
        double acc = ((validCount/totalCount)*100);
        perc.setText("Accuracy % : " + String.valueOf(acc) + "%");

        accuracy.getChildren().addAll(valid, invalid,perc, totscore, happyHalloween);
        accuracy.setAlignment(Pos.CENTER);
        endPane.setTop(accuracy);
        endPane.setStyle("-fx-background-color: darkorange;");
        endScene = new Scene(endPane, 400,350);

        window.setScene(endScene);
        window.show();

    }





    class Timer extends AnimationTimer
    {
        @Override
        public void handle(long arg0)
        {
            estimatedTime = (int)(1.8e5) - ((int) (((System.nanoTime() - startTime) / 1e6) - (restartedTime) - (pausedTime)));
            if(estimatedTime < 0) estimatedTime *= -1;
            TimeZone tz = TimeZone.getTimeZone("MTN");
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            df.setTimeZone(tz);
            String time = df.format(new Date(estimatedTime));
            timelabel.setText("Time Remaining: " + time);
            timelabel.setStyle("-fx-font-weight: bolder;" + "-fx-font-size: 17px");


            if(estimatedTime <= 10)
            {
                playword = "";
                endController();
            }
        }
    }

}
