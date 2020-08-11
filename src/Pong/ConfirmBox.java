package Pong;

import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
	
	static boolean answer;
	static Media clickSound = new Media(new File("src/Sounds/ClickSound.mp3").toURI().toString());
	static MediaPlayer clickSoundPlayer = new MediaPlayer(clickSound);
	
	public static boolean display(String title, String message){
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		
		Text text = new Text();
		text.setText(message);
		text.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 20));
		text.setFill(Color.WHITE);
		
		//Two buttons 
		Button yesButton = new Button("_Yes");
		yesButton.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 15));
		yesButton.getStyleClass().add("pongStyle");
		Button noButton = new Button("_No");
		noButton.setFont(Font.font("OCR A Extended", FontWeight.BOLD, 15));
		
		yesButton.setOnAction(event -> {
			makeClickNoise(true);
			answer = true;
			window.close();
		});
		noButton.setOnAction(event ->{
			makeClickNoise(true);
			answer = false;
			window.close();
		});
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(text, yesButton, noButton);
		layout.setAlignment(Pos.CENTER);
		layout.setStyle("-fx-background-color: black");
		
		Scene scene = new Scene(layout, 200, 200);
		
		//Make window non sizeable 
		window.setMinWidth(250);
		window.setMinHeight(250);
		window.setMaxWidth(250);
		window.setMaxHeight(250);
		window.setScene(scene);
		window.showAndWait();
		
		return answer;
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
