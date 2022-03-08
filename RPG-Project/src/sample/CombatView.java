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
    Image background;
    Button attack, run, magic, retryYes, retryNo, attackOne, attackTwo, attackThree, attackFour;
    ProgressBar playerXPBar, playerHealthBar, playerManaBar, enemyHealthBar;
    Label Enemy, Player, HP, XP, Mana;
    CombatModel model;
    double currentHealth;
    double currentEnemyHealth;
    double currentMana;
    double currentXP;

    public CombatView() throws FileNotFoundException {

        currentHealth = 100;
        currentEnemyHealth = 100;
        currentMana = 100;
        currentXP = 100;

        // Background Picture
        FileInputStream inputStream = new FileInputStream("background.png");
        background = new Image(inputStream);
        ImageView imageView = new ImageView();
        imageView.setImage(background);
        imageView.setFitWidth(1000);
        imageView.setFitHeight(1000);

        // XP and Health Bars
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
        HBox hp = new HBox(playerHealthBar, HP);
        HBox xp = new HBox(playerXPBar, XP);
        HBox mana = new HBox(playerManaBar, Mana);

        Enemy = new Label("Enemy");
        Enemy.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 15));
        Player = new Label("Player");
        Player.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 15));
        VBox enemy = new VBox();
        VBox player = new VBox();
        enemy.getChildren().addAll(Enemy, enemyHealthBar);
        player.getChildren().addAll(Player, hp, xp, mana);

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

        retryYes = new Button("Yes");
        retryNo = new Button("No");
        attackOne = new Button("Attack One");
        attackTwo = new Button("Attack Two");
        attackThree = new Button("Attack Three");
        attackFour = new Button("Attack Four");

        VBox buttons = new VBox();
        HBox bottom = new HBox();
        buttons.getChildren().addAll(attack, magic);
        buttons.setSpacing(100);
        bottom.getChildren().addAll(buttons, run);
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(650);


        HBox top = new HBox();
        top.getChildren().addAll(enemy,player);
        top.setSpacing(550);

        VBox main = new VBox();
        main.getChildren().addAll(top, bottom);
        main.setSpacing(550);
        main.setPrefSize(1000,1000);

        this.getChildren().addAll(imageView, main);
        this.setPrefHeight(1000);
        this.setPrefWidth(1000);
    }

    public void setController(Controller controller){
        attack.setOnAction(e -> controller.handleAttack());
        run.setOnAction((e -> controller.handleRun()));
        magic.setOnAction(e -> controller.handleMagic());
    }

    public void setModel(CombatModel comModel){
        model = comModel;
    }

    @Override
    public void modelChanged() {
        currentEnemyHealth = model.enemy.characterStats.getHealth();
        if (currentEnemyHealth==0) {
            System.out.println("YOU WIN");
        }
        enemyHealthBar.setProgress(currentEnemyHealth / 100);

        currentHealth = model.player.characterStats.getHealth();
        if (currentHealth==0){
            System.out.print("You LOSE!");
        }
        playerHealthBar.setProgress(currentHealth/100);

        currentMana = model.player.characterStats.getWis();
        playerManaBar.setProgress(currentMana/100);

        if (currentMana==0){
            magic.setDisable(true);
        }

    }
}
