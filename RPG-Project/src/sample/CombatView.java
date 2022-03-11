package sample;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class CombatView extends StackPane implements CombatSubscriber{
    protected Image background;
    protected Button attack, run, magic, retryYes, retryNo, attackOne, attackTwo, attackThree, attackFour, next;
    protected ProgressBar playerXPBar, playerHealthBar, playerManaBar, enemyHealthBar;
    protected Label Enemy, Player, HP, XP, Mana, Retry, Dialogue;
    protected CombatModel model;
    protected HBox bottomMain,hp ,xp, mana, top;
    protected VBox main, retryBottom, buttonsMain, retry, enemy, player,diaNext, dialogueMain;
    protected ImageView imageView;
    protected FileInputStream inputStream;

    /**
     * CombatView controller
     * @throws FileNotFoundException for background image if image is not found
     */
    public CombatView() throws FileNotFoundException {

        // Background Picture
        inputStream = new FileInputStream("background.png");
        background = new Image(inputStream);
        imageView = new ImageView();
        imageView.setImage(background);
        imageView.setFitWidth(1000);
        imageView.setFitHeight(1000);

        // XP, Mana, and Health Bars
        playerXPBar = new ProgressBar(1);
        playerXPBar.setStyle("-fx-accent: GREEN");
        playerHealthBar = new ProgressBar(1);
        playerHealthBar.setStyle("-fx-accent: RED");
        playerManaBar = new ProgressBar(1);
        enemyHealthBar = new ProgressBar(1);
        enemyHealthBar.setStyle("-fx-accent: RED");

        // Bars Labels
        HP = new Label("HP");
        HP.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        XP = new Label("XP");
        XP.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        Mana = new Label("Mana");
        Mana.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        hp = new HBox(playerHealthBar, HP);
        xp = new HBox(playerXPBar, XP);
        mana = new HBox(playerManaBar, Mana);

        Enemy = new Label("Enemy");
        Enemy.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 15));
        Player = new Label("Player");
        Player.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 15));
        enemy = new VBox();
        player = new VBox();
        enemy.getChildren().addAll(Enemy, enemyHealthBar);
        player.getChildren().addAll(Player, hp, xp, mana);

        // Dialogue Label
        Dialogue = new Label();
        Dialogue.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 25));

        // All Buttons
        Font font = Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 15);

        attack = new Button("Attack");
        attack.setFont(font);
        attack.setStyle("-fx-background-color: WHITE");

        run = new Button("Run");
        run.setFont(font);
        run.setStyle("-fx-background-color: WHITE");

        magic = new Button("Magic");
        magic.setDisable(false);
        magic.setFont(font);
        magic.setStyle("-fx-background-color: WHITE");

        Retry = new Label("Would you like to retry?");
        Retry.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));
        retryYes = new Button("Yes");
        retryYes.setFont(font);
        retryYes.setStyle("-fx-background-color: WHITE");
        retryNo = new Button("No");
        retryNo.setFont(font);
        retryNo.setStyle("-fx-background-color: WHITE");


        // TODO: Implement buttons for a player to have more than one
        //      attack to choose from
        attackOne = new Button("Attack One");
        attackTwo = new Button("Attack Two");
        attackThree = new Button("Attack Three");
        attackFour = new Button("Attack Four");


        // Coordinate all top boxes together to fit screen properly
        top = new HBox();
        top.getChildren().addAll(enemy,player);
        top.setSpacing(550);

        // Will be used for smoother dialogue in the place of other buttons
        next = new Button("Head Back");
        next.setFont(font);
        next.setStyle("-fx-background-color: WHITE");
        diaNext = new VBox(Dialogue, next);
        diaNext.setAlignment(Pos.BOTTOM_CENTER);
        diaNext.setSpacing(50);


        // Retry bottom of screen for CombatView
        retryBottom = new VBox(Retry, retryYes, retryNo);
        retryBottom.setSpacing(25);
        retryBottom.setAlignment(Pos.BOTTOM_CENTER);
        retry = new VBox();
        retry.getChildren().addAll(top, retryBottom);

        // Main bottom of screen for CombatView
        buttonsMain = new VBox();
        bottomMain = new HBox();
        buttonsMain.getChildren().addAll(attack, magic);
        buttonsMain.setSpacing(100);
        bottomMain.getChildren().addAll(buttonsMain, run);
        bottomMain.setAlignment(Pos.CENTER);
        bottomMain.setSpacing(650);
        dialogueMain = new VBox(Dialogue, bottomMain);
        dialogueMain.setAlignment(Pos.CENTER);
        main = new VBox();
        main.getChildren().addAll(top, dialogueMain);
        main.setSpacing(525);
        main.setPrefSize(1000,1000);


        this.getChildren().addAll(imageView, main);
        this.setPrefHeight(1000);
        this.setPrefWidth(1000);
    }

    /**
     * Notifies controller that a button has been pressed
     * @param controller The views controller to handle user interaction
     */
    protected void setController(Controller controller){
        attack.setOnAction(e -> controller.handleAttack());
        run.setOnAction((e -> controller.handleRun()));
        magic.setOnAction(e -> controller.handleMagic());
        retryYes.setOnAction(e -> {controller.handleCombatRest();this.reset();});
        retryNo.setOnAction(e -> {controller.handleNoReset(this.getScene());this.reset();});
        next.setOnAction(e -> {controller.handleWin(this.getScene());this.reset();});
    }

    private void reset() {
        this.getChildren().retainAll();
        this.getChildren().addAll(imageView, main);
    }

    /**
     * The views model to receive updates from (and to subscribe too)
     * @param comModel the model to set as the views model
     */
    protected void setModel(CombatModel comModel){
        model = comModel;
    }

    private void retryButtons(){
        this.getChildren().remove(1);
        this.getChildren().add(1, retry);
    }

    private void end(){
        Dialogue.setText("Player Wins!");
        this.getChildren().remove(1);
        this.getChildren().addAll(Dialogue, diaNext);
    }


    /**
     * Update view according to what has changed in the model
     */
    @Override
    public void modelChanged() {

        Dialogue.setText(model.getCurrentDialogue());

        // Get current health, xp, and mana for the progress bars
        // Divided by 100 to get a float between 0-1 for progress bar (Max for all is 100)
        playerManaBar.setProgress((float)model.player.characterStats.getMana()/100);
        playerHealthBar.setProgress((float)model.player.characterStats.getHealth()/100);
        enemyHealthBar.setProgress((float)model.enemy.characterStats.getHealth()/100);

        // If player loses, option to replay
        if(model.player.characterStats.getHealth() <= 0){
            playerHealthBar.setProgress(0); // So it doesn't look like player has negative health
            this.retryButtons();
        }
        // If player wins, go back to traversal scene
        else if (model.enemy.characterStats.getHealth() <= 0){
            enemyHealthBar.setProgress(0);
            this.end();
        }
        // If run is successful, go back to traversal scene
        if (model.runAway){
            this.retryNo.fire();
        }

        // If mana bar is empty then player can no longer use magic button
        if (model.player.characterStats.getMana() <= 0){
            magic.setDisable(true);
        }
    }
}
