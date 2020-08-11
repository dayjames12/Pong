package Pong;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Court { 
	
	static Stage window;
	static Scene scene;
	static double ballX, ballY;
	static double ballxVelocity;
	static double ballyVelocity;
	static Pane court = new Pane();
	static Rectangle centerDivide = new Rectangle(12, 5000);
	static Circle centerCircle = new Circle(100);
	static Rectangle ball = new Rectangle(20, 20);
	static Rectangle playerPaddle1 = new Rectangle(20, 100);
	static Rectangle playerPaddle2 = new Rectangle(20, 100);
	static Rectangle aiPaddle = new Rectangle(20, 100);
	static Timeline ballAnimation;
	static Timeline aiPaddleAnimation;
	static Media paddleSound = new Media(new File("src/Sounds/PaddleNoise.mp3").toURI().toString());
	static Media bounceSound = new Media(new File("src/Sounds/BounceNoise.mp3").toURI().toString());
	static Media winSound = new Media(new File("src/Sounds/WinSound.mp3").toURI().toString());
	static Media loseSound = new Media(new File("src/Sounds/LoseSound.wav").toURI().toString());
	static Media clickSound = new Media(new File("src/Sounds/ClickSound.mp3").toURI().toString());
	static MediaPlayer paddleSoundPlayer = new MediaPlayer(paddleSound);
	static MediaPlayer bounceSoundPlayer = new MediaPlayer(bounceSound);
	static MediaPlayer winSoundPlayer = new MediaPlayer(winSound);
	static MediaPlayer loseSoundPlayer = new MediaPlayer(loseSound);
	static MediaPlayer clickSoundPlayer = new MediaPlayer(clickSound);
 	static ImageView trump = new ImageView(new Image("Images/trumpHead.png"));
 	static ImageView cage = new ImageView(new Image("Images/NicolasCage.png"));
	
	static void game(Integer gameType){
		window = new Stage(); 
		scene = new Scene(court, 1000, 500);
		court.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		
		ballAnimation = new Timeline(new KeyFrame(Duration.millis(50), e -> moveBall(gameType)));
		ballAnimation.setCycleCount(Timeline.INDEFINITE);
		aiPaddleAnimation = new Timeline(new KeyFrame(Duration.millis(50), e -> moveAiPaddle()));
		aiPaddleAnimation.setCycleCount(Timeline.INDEFINITE);

		centerDivide.setFill(Color.WHITE);
		centerDivide.xProperty().bind(court.widthProperty().divide(2).subtract(centerDivide.getWidth()/2));

		centerCircle.setStrokeWidth(12);
		centerCircle.setStroke(Color.WHITE);
		centerCircle.centerXProperty().bind(court.widthProperty().divide(2.0));
		centerCircle.centerYProperty().bind(court.heightProperty().divide(2.0));
	
		playerPaddle1.setFill(Color.WHITE);
		playerPaddle1.xProperty().bind(court.widthProperty().subtract(court.widthProperty().subtract(20)));
		playerPaddle1.setY(((court.getHeight()/2) - 50));
		
		playerPaddle2.setFill(Color.WHITE);
		playerPaddle2.xProperty().bind(court.widthProperty().subtract(40));
		playerPaddle2.setY(((court.getHeight()/2) - 50));
		
		aiPaddle.setFill(Color.WHITE);
		aiPaddle.xProperty().bind(court.widthProperty().subtract(40));
		aiPaddle.setY(((court.getHeight()/2) - 50));
		
		ball.setFill(Color.WHITE);
		ball.setX(court.getWidth()/2 - 10);
		ball.setY(court.getHeight()/2 - 10);
		ballX = ball.xProperty().doubleValue();
		ballY = ball.yProperty().doubleValue();
		ballxVelocity = -2 + (Math.random() * 2);
		ballyVelocity = -2 + (Math.random() * 2);
		
		trump.setFitWidth(100);
		trump.setFitHeight(100);
		trump.xProperty().bind(ball.xProperty().subtract(trump.getFitWidth()/3));
		trump.yProperty().bind(ball.yProperty().subtract(trump.getFitHeight()/3));
		cage.setFitWidth(100);
		cage.setFitHeight(100);
		cage.xProperty().bind(ball.xProperty().subtract(trump.getFitWidth()/3));
		cage.yProperty().bind(ball.yProperty().subtract(trump.getFitHeight()/3));
	
		
		gameType(gameType);
		
		scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){
			public void handle(KeyEvent event){
				if (event.getCode() == KeyCode.W)
				{
					playerPaddle1.setY(playerPaddle1.yProperty().doubleValue() - 20);
					if (playerPaddle1.getY() < 0)
					{
						playerPaddle1.setY(0);
					}
				}
				else if (event.getCode() == KeyCode.S)
				{
					playerPaddle1.setY(playerPaddle1.yProperty().doubleValue() + 20);
					if (playerPaddle1.getY() > court.getHeight() - 100)
					{
						playerPaddle1.setY(court.getHeight() - 100);
					}
				}
				else if (event.getCode() == KeyCode.UP)
				{
					playerPaddle2.setY(playerPaddle2.yProperty().doubleValue() - 30);
					if (playerPaddle2.getY() < 0 )
					{
						playerPaddle2.setY(0);
					}
				}
				else if (event.getCode() == KeyCode.DOWN)
				{
					playerPaddle2.setY(playerPaddle2.yProperty().doubleValue() + 30);
					if (playerPaddle2.getY() > court.getHeight() - 100)
					{
						playerPaddle2.setY(court.getHeight() - 100);
					}
				}
				else if (event.getCode() == KeyCode.SPACE)
				{
					play();
					clickSoundPlayer.play();
					//need start sound 
				}
				else if (event.getCode() == KeyCode.CLOSE_BRACKET)
				{
					makeClickNoise(true);
					increaseSpeed();
				}
				else if (event.getCode() == KeyCode.OPEN_BRACKET)
				{
					makeClickNoise(true);
					decreaseSpeed();
				}
			}
		});
		
		window.setOnCloseRequest(event -> {
			makeClickNoise(true);
			event.consume(); //stops close event (top right corner exiting) and allows closeGame() to take over function
			closeGame(window);
			});
		window.setMinWidth(800);
	    window.setMinHeight(400);
//	    window.minWidthProperty().bind(scene.heightProperty().multiply(2));
//	    window.minHeightProperty().bind(scene.widthProperty().divide(2));
		window.setTitle("PONG");
		window.setScene(scene);
		window.show();
	}
	public static void closeGame(Stage window) {
		boolean answer = ConfirmBox.display("Confirm", "Are you sure?");
		if (answer)
			window.close();
	}
	
	public static void play(){
		ballAnimation.setRate(6);
		ballAnimation.play();
		aiPaddleAnimation.setRate(6);
		aiPaddleAnimation.play();
	}
	
	public static void pause(){
		ballAnimation.pause();
		aiPaddleAnimation.pause();
	}
	
	public static void increaseSpeed(){
		ballAnimation.setRate(ballAnimation.getRate() + 1);
	}
	
	public static void decreaseSpeed(){
		ballAnimation.setRate(ballAnimation.getRate() > 0 ? ballAnimation.getRate() - 1 : 0);
	}

	public static void moveBall(Integer gameType){
		ballX += ballxVelocity;
		ballY += ballyVelocity;
		ball.setX(ballX);
		ball.setY(ballY);
		// Ball bounces off ceiling and roof
		if (ballY < 0)
			{
				ballyVelocity = -ballyVelocity;
				makeBounceNoise(true);
			}
		else if (ballY > court.getHeight() - 20)
			{
				ballyVelocity = -ballyVelocity;
				makeBounceNoise(true);
			}
		checkPlayerPaddleCollision(gameType);
		checkWin();
	}

	public static void checkPlayerPaddleCollision(Integer gameType){
		if (ballX <= 40) //Bounce ball for Paddle one 
		{
			if (ballY >= playerPaddle1.getY() + 30 && ballY <= playerPaddle1.getY() + 70) //Center
				{
					ballxVelocity = -ballxVelocity; 
					ballyVelocity = ballyVelocity/ballxVelocity;
					makePaddleNoise(true);
				}
			else if (ballY > playerPaddle1.getY() - 20 && ballY < playerPaddle1.getY() + 30) //Top
				{
					ballxVelocity = -ballxVelocity;
					if ( ballyVelocity >= 0)
						{
							ballyVelocity = ballyVelocity + 1 * -2;			
						}	
					makePaddleNoise(true);			
				}
			else if (ballY > playerPaddle1.getY() + 70 && ballY <= playerPaddle1.getY() + 100) //Bottom
				{
					ballxVelocity = -ballxVelocity;
					if ( ballyVelocity < 0)
						{
							ballyVelocity = ballyVelocity + 1 * -2;	
						}
					makePaddleNoise(true);			
				}
		}
		if (gameType == 2)
		{
			if (ballX >= court.getWidth() - 60) //Bounce ball for Paddle two
			{
				if (ballY >= playerPaddle2.getY() + 30 && ballY <= playerPaddle2.getY() + 70) //Center
				{
					ballxVelocity = -ballxVelocity; //change slope to 0? or try to perpinduclur???
					ballyVelocity = ballyVelocity/ballxVelocity;
					makePaddleNoise(true);			
				}
			else if (ballY > playerPaddle2.getY() - 20 && ballY < playerPaddle2.getY() + 30) //Top
				{
					ballxVelocity = -ballxVelocity;
					ballxVelocity = -ballxVelocity;
					if ( ballyVelocity >= 0)
						{
							ballyVelocity = ballyVelocity + 1 * -2;			
						}		
					makePaddleNoise(true);			
				} 
			else if (ballY > playerPaddle2.getY() + 70 && ballY <= playerPaddle2.getY() + 100) //Bottom
				{
					ballxVelocity = -ballxVelocity;
					if ( ballyVelocity < 0)
						{
							ballyVelocity = ballyVelocity + 1 * -2;	
						}
					makePaddleNoise(true);			
				}
			}
		}
		if (gameType == 1|| gameType == 3 || gameType == 4)
			{
				if (ballX >= court.getWidth() - 60)
					if (ballY >= aiPaddle.getY() + 30 && ballY <= aiPaddle.getY() + 70) //Center
					{
						ballxVelocity = -ballxVelocity; //change slope to 0? or try to perpinduclur???
						ballyVelocity = ballyVelocity/ballxVelocity;
						makePaddleNoise(true);		
					}
				else if (ballY > aiPaddle.getY() - 20 && ballY < aiPaddle.getY() + 30) //Top
					{
						ballxVelocity = -ballxVelocity;
						if ( ballyVelocity >= 0)
							{
								ballyVelocity = -ballyVelocity;			
							}		
						makePaddleNoise(true);	
					} 
				else if (ballY > aiPaddle.getY() + 70 && ballY <= aiPaddle.getY() + 100) //Bottom
					{
						ballxVelocity = -ballxVelocity;
						if ( ballyVelocity < 0)
							{
								ballyVelocity = -ballyVelocity;	
							}
						makePaddleNoise(true);		
					}
			}
	}
	
	 
	public static void moveAiPaddle(){ 
		aiPaddle.yProperty().set(ballY - 60); // Follows the ball
		if (aiPaddle.getY() < 0) //Keeps ai paddle in window
			{
				aiPaddle.setY(0);
			}
		else if (aiPaddle.getY() > court.getHeight() - 100) //Keeps paddle in window
			{
				aiPaddle.setY(court.getHeight() - 100);
			}
	}

	public static void checkWin(){
		if (ballX < 0)
		{
			makeLoseNoise(true);
			pause();
			//restart();
		}
		else if (ballX > court.getWidth() - 20)
		{
			makeWinNoise(true);
			pause();
			//restart(); 
		}
	}
	
	public static void restart(){
		ball.setX(court.getWidth()/2 - 10);
		ball.setY(court.getHeight()/2 - 10);
		ballX = ball.xProperty().doubleValue();
		ballY = ball.yProperty().doubleValue();
	}
	
	public static void gameType(Integer gameType){
		//Makes game one player or two player
		if (gameType == 1) //One player
			{
				court.getChildren().addAll(centerCircle, centerDivide, playerPaddle1, aiPaddle, ball);
			}
		else if (gameType == 2)//Two player
			{	
				court.getChildren().addAll(centerCircle, centerDivide, playerPaddle1, playerPaddle2, ball);
				//court.getChildren().add(trump);
			}
		else if (gameType == 3)
			{
				court.getChildren().addAll(centerCircle, centerDivide, playerPaddle1, aiPaddle, ball, trump);
			}
		else if (gameType == 4)
			{
			court.getChildren().addAll(centerCircle, centerDivide, playerPaddle1, aiPaddle, ball, cage);
			}
	}
	
	public static void makePaddleNoise(Boolean trigger){
		if (trigger)
			{
				if (paddleSoundPlayer.getOnEndOfMedia() == paddleSoundPlayer.getOnEndOfMedia())
					{
						paddleSoundPlayer.stop();
						paddleSoundPlayer.play();
					}
			}
	}
	
	public static void makeBounceNoise(Boolean trigger){
		if (trigger)
			{
				if (bounceSoundPlayer.getOnEndOfMedia() == bounceSoundPlayer.getOnEndOfMedia())
					{
						bounceSoundPlayer.stop();
						bounceSoundPlayer.play();
					}
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
	
	public static void makeWinNoise(Boolean trigger){
		if (trigger)
			{
				if (winSoundPlayer.getOnEndOfMedia() == winSoundPlayer.getOnEndOfMedia())
					{
						winSoundPlayer.stop();
						winSoundPlayer.play();
					}
			}
	}
	
	public static void makeLoseNoise(Boolean trigger){
		if (trigger)
			{
				if (loseSoundPlayer.getOnEndOfMedia() == loseSoundPlayer.getOnEndOfMedia())
					{
						loseSoundPlayer.stop();
						loseSoundPlayer.play();
					}
			}
	}
}



























