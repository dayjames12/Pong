package Pong;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class PongMain extends Application{
	Stage window;
	Scene menuScene;
	public static Label sPlayer;
	public static Label tPlayer;
	public static Label trumpLabel;
	public static Label cageLabel;
	public static ComboBox<Label> startBox;
	static Media clickSound = new Media(new File("src/Sounds/ClickSound.mp3").toURI().toString());
	static MediaPlayer clickSoundPlayer = new MediaPlayer(clickSound);
	
	public static void main(String[] args) {
		Application.launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		Pane menu = new Pane();
		
		// Pong Gif Back Ground
		ImageView gif = new ImageView(new Image("Images/PongGif.gif"));
		menu.getChildren().add(gif);
		gif.fitWidthProperty().bind(window.widthProperty());
		gif.fitHeightProperty().bind(window.heightProperty());	
		
		// Pong Title 
		Label title = new Label("PONG");
		title.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 100));
		title.setTextFill(Color.WHITE);
		title.translateXProperty().bind(menu.widthProperty().divide(2).subtract(title.widthProperty().divide(2)));
		title.translateYProperty().bind(menu.heightProperty().divide(2).subtract(title.heightProperty().divide(2).add(100)));
		menu.getChildren().add(title);
		
		startBox = new ComboBox<>();
		menu.getChildren().add(startBox);
		startBox.layoutXProperty().bind(menu.widthProperty().divide(2).subtract(startBox.widthProperty().divide(2)));
		startBox.layoutYProperty().bind(menu.heightProperty().divide(2).subtract(startBox.heightProperty().divide(2).subtract(100)));
		startBox.setStyle("-fx-border-color: black");
		Label cbTitle = new Label("START GAME");
		startBox.setValue(cbTitle);
		cbTitle.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 20));
		cbTitle.setTextFill(Color.BLACK);
		sPlayer = new Label("ONE PLAYER");
		sPlayer.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 20));
		sPlayer.setTextFill(Color.BLACK);
		tPlayer = new Label("TWO PLAYER");
		tPlayer.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 20));
		tPlayer.setTextFill(Color.BLACK);
		trumpLabel = new Label("ONE PLAYER - TRUMP MODE");
		trumpLabel.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 20));
		trumpLabel.setTextFill(Color.BLACK);
		cageLabel = new Label("ONE PLAYER - CAGE MODE");
		cageLabel.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 20));
		cageLabel.setTextFill(Color.BLACK);
		startBox.getItems().addAll(sPlayer, tPlayer, trumpLabel, cageLabel);
		startBox.setOnAction(event ->{	
			makeClickNoise(true);
			getChoice();
		window.close();
		});
		
		// Exit Button 
		Button exitButton = new Button("_EXIT");
		menu.getChildren().add(exitButton);
		exitButton.layoutXProperty().bind(menu.widthProperty().divide(2).subtract(exitButton.widthProperty().divide(2)));
		exitButton.layoutYProperty().bind(menu.heightProperty().divide(2).subtract(exitButton.heightProperty().divide(2).subtract(150)));
		//exitButton.setStyle("-fx-background-color: black");
		exitButton.setTextFill(Color.WHITE);
		exitButton.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		exitButton.setStyle("-fx-border-color: white");
		exitButton.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 20));
		exitButton.setOnAction(event -> {
			makeClickNoise(true);
			closeGame();
		}); 
		
		menuScene = new Scene(menu, 1000, 500);
		window.minWidthProperty().bind(menu.heightProperty().multiply(2));
	    window.minHeightProperty().bind(menu.widthProperty().divide(2));
	    
		//Window Title
		window.setTitle("PONG");
		
		//When you close the game from the top right corner button
		window.setOnCloseRequest(event -> {
			makeClickNoise(true);
			event.consume(); //stops close event and allows closeGame() to take over function
			closeGame();
			});
		
		window.setScene(menuScene);
		window.show();
	}
	
	public void closeGame() {
		boolean answer = ConfirmBox.display("Confirm", "Are you sure?");
		if (answer)
			window.close();
	}
	
	public void getChoice(){
		if (startBox.getValue() == sPlayer)
			{
				Court.game(1);
			}
		else if (startBox.getValue() == tPlayer)
			{
				Court.game(2);
			}
		else if (startBox.getValue() == trumpLabel)
			{
				Court.game(3);
			}
		else if (startBox.getValue() == cageLabel)
			{
				Court.game(4);
			}
	}
	
	public static void makeClickNoise(Boolean trigger){
		if (trigger)
			{
				if (clickSoundPlayer.getOnEndOfMedia() == clickSoundPlayer.getOnEndOfMedia())
					{
						clickSoundPlayer.stop();
						clickSoundPlayer.play();
					}
			}
	}
	
}