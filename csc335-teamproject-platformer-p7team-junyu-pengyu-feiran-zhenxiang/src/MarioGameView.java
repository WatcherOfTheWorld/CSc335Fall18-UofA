/**
 * It is a view class of mario game
 * @author Junyu Liu， Pengyu Yang, Zhengxiang Jin, Feiran Yange
 *
 */

import java.io.*;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;



public class MarioGameView extends Application implements Observer{
	private GameModel gameModel= new GameModel();
	private GameController gameController = new GameController(gameModel);
	Canvas canvasForMario = gameModel.getCanvasForMario();
	GraphicsContext gcForMario = gameModel.getGcForMario();
	private int slow = 1;
	Image background = new Image("resources/start_background.png");
	Image marioImage = new Image("resources/mario.png");
    Image pause = new Image("resources/pause.png");
	private static Image blocksImage = new Image("resources/blocks.png");
    Font font = Font.loadFont(getClass().getResourceAsStream("resources/font.ttf"),13);
    MediaPlayer BGM = new MediaPlayer(new Media("http://www.codem.xyz/resource/mp3/jicouBGM.mp3"));
	MediaPlayer BGM2 = new MediaPlayer(new Media("http://www.codem.xyz/resource/mp3/君のヒロインでいるために.mp3"));
	Media jumpSound = new Media("http://www.codem.xyz/resource/mp3/マリオジャンプ.mp3");
	DropShadow ds = new DropShadow();

	int curr = 1;

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * start method
	 * @param primaryStage - stage
	 */
	public void start(Stage primaryStage) {
		// set effect
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0.4f, 0.4f, 0.4f));

	    BGM.setAutoPlay(true);
		Group root = new Group();
		root.getChildren().add(canvasForMario);
		Scene scene = new Scene(root);
		StartMenu(scene, gcForMario);
		primaryStage.getIcons().add(new Image("resources/icon.png"));
		primaryStage.setTitle("Mario Game");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * init all the content based on the level data 
	 */
	private void initContent() {
		gcForMario.drawImage(gameController.getBackground().getImage(), // the image to be drawn or null.
				gameController.getBackground().getOffset_x(), // the source rectangle's X coordinate position.
				gameController.getBackground().getOffset_y(), // the source rectangle's Y coordinate position.
				gameController.getBackground().getWidth(), // the source rectangle's width.
				gameController.getBackground().getHeight(), // the source rectangle's height.
				gameController.getBackground().getX(), // the destination rectangle's X coordinate position.
				gameController.getBackground().getY(), // the destination rectangle's Y coordinate position.
				gameController.getBackground().getWidth(), // the destination rectangle's width.
				gameController.getBackground().getHeight()); // the destination rectangle's height.


		gcForMario.drawImage(gameController.getMario().getImage(), // the image to be drawn or null.
				gameController.getMario().getOffset_x(), // the source rectangle's X coordinate position.
				gameController.getMario().getOffset_y(), // the source rectangle's Y coordinate position.
				gameController.getMario().getWidth(), // the source rectangle's width.
				gameController.getMario().getHeight(), // the source rectangle's height.
				gameController.getMario().getX(), // the destination rectangle's X coordinate position.
				gameController.getMario().getY(), // the destination rectangle's Y coordinate position.
				gameController.getMario().getWidth(), // the destination rectangle's width.
				gameController.getMario().getHeight()); // the destination rectangle's height.

		// initial information
		//
		Font infofont = Font.loadFont(getClass().getResourceAsStream("resources/font.ttf"),20);
		// initial name Mario
		gameController.getInformations().add(new Information("Mario", Color.WHITE, 100, 50, infofont));
		// initial score
		gameController.getInformations().add(new Information(Integer.valueOf(gameController.getMario().getSCORE()).toString(),
				Color.WHITE, 100, 70, infofont, 1));
		// initial coins
		gameController.getInformations().add(new Information("*"+Integer.valueOf(gameController.getMario().getCOINS()).toString(),
				Color.WHITE, 250, 70, infofont, 2));
		// initial mario's life
		gameController.getInformations().add(new Information("*"+Integer.valueOf(gameController.getMario().getLife()).toString(),
				Color.WHITE, 650, 70, infofont, 5));




		for (int i = 0; i < LevelData.getMap(gameModel.getLevel()).length; i++) {
			String line = LevelData.getMap(gameModel.getLevel())[i];
			for (int j = 0; j < line.length(); j++) {
				switch (line.charAt(j)) {
					case '0':
						break;
					case '1':
						Brick brick = new Brick(gameController.getBlocks(), 40, 40, j*40, i*40);
						gameController.getBricks().add(brick);
						int index = gameController.getBricks().size() -1;
						gcForMario.drawImage(gameController.getBricks().get(index).getImage(), // the image to be drawn or null.
								gameController.getBricks().get(index).getOffset_x(), // the source rectangle's X coordinate position.
								gameController.getBricks().get(index).getOffset_y(), // the source rectangle's Y coordinate position.
								gameController.getBricks().get(index).getWidth(), // the source rectangle's width.
								gameController.getBricks().get(index).getHeight(), // the source rectangle's height.
								gameController.getBricks().get(index).getX(), // the destination rectangle's X coordinate position.
								gameController.getBricks().get(index).getY(), // the destination rectangle's Y coordinate position.
								gameController.getBricks().get(index).getWidth(), // the destination rectangle's width.
								gameController.getBricks().get(index).getHeight()); // the destination rectangle's height.
						break;
					case '2':
						Wall wall = new Wall(gameModel.getBlocks(),40,40,j*40,i*40);
						gameController.getBricks().add(wall);
						gcForMario.drawImage(wall.getImage(),
								wall.getOffset_x(), wall.getOffset_y(),
								wall.getWidth(), wall.getHeight(),
								wall.getX(),wall.getY(), wall.getWidth(), wall.getHeight()
						);
						break;

                    case '3':
                        QuestionBrick questionBrick = new QuestionBrick(gameModel.getBlocks(),40,40,j*40,i*40);
//                        Coin coin = new Coin( 3, 3, 946, 40, 40,40,j*40,i*40);
//                        System.out.println("file:coin's location:"+j*40+" ,"+i*40);
                        gameController.getBricks().add(questionBrick);
//
                        gcForMario.drawImage(questionBrick.getImage(),
								questionBrick.getOffset_x(), questionBrick.getOffset_y(),
								questionBrick.getWidth(), questionBrick.getHeight(),
								questionBrick.getX(),questionBrick.getY(), questionBrick.getWidth(), questionBrick.getHeight()
                        );
                        break;
                    case 'g':
                    	Monster goomba = new Goomba(0,40,j*40, i*40);
                    	gameController.getMonsters().add(goomba);
                        //System.out.println("file:goomba's location:"+ j*40+" ,"+i*40);

                    	int index2 = gameController.getMonsters().size() -1;
                    	gcForMario.drawImage(gameController.getMonsters().get(index2).getImage(), // the image to be drawn or null.
    							gameController.getMonsters().get(index2).getOffset_x(), // the source rectangle's X coordinate position.
    							gameController.getMonsters().get(index2).getOffset_y(), // the source rectangle's Y coordinate position.
    							gameController.getMonsters().get(index2).getWidth(), // the source rectangle's width.
    							gameController.getMonsters().get(index2).getHeight(), // the source rectangle's height.
    							gameController.getMonsters().get(index2).getX(), // the destination rectangle's X coordinate position.
    							gameController.getMonsters().get(index2).getY(), // the destination rectangle's Y coordinate position.
    							gameController.getMonsters().get(index2).getWidth(), // the destination rectangle's width.
    							gameController.getMonsters().get(index2).getHeight());
                    	break;
                    	
                    case 'k':
                    	
                    	Monster koopa = new Koopa(240,0,j*40, i*36);
                    	gameController.getMonsters().add(koopa);
                    	int index3 = gameController.getMonsters().size() -1;
                    	gcForMario.drawImage(gameController.getMonsters().get(index3).getImage(), 
    							gameController.getMonsters().get(index3).getOffset_x(),
    							gameController.getMonsters().get(index3).getOffset_y(), // the source rectangle's Y coordinate position.
    							gameController.getMonsters().get(index3).getWidth(), // the source rectangle's width.
    							gameController.getMonsters().get(index3).getHeight(), // the source rectangle's height.
    							gameController.getMonsters().get(index3).getX(), // the destination rectangle's X coordinate position.
    							gameController.getMonsters().get(index3).getY(), // the destination rectangle's Y coordinate position.
    							gameController.getMonsters().get(index3).getWidth(), // the destination rectangle's width.
    							gameController.getMonsters().get(index3).getHeight());
                    	break;
                

					case '5':
						Coin coin = new Coin(gameModel.getBlocks(), j * 40, i * 40);
						gameModel.getCoins().add(coin);
						gcForMario.drawImage(coin.getImage(), coin.getOffset_x(), coin.getOffset_y(), coin.getWidth(),
								coin.getHeight(), coin.getX(), coin.getY(), coin.getWidth(), coin.getHeight());
						break;
				
                    case 'h':
                    	Monster piranhaPlant = new PiranhaPlant(480,0,j*40, i*36);
                    	gameController.getMonsters().add(piranhaPlant);
                    	int index4 = gameController.getMonsters().size() -1;
                    	gcForMario.drawImage(gameController.getMonsters().get(index4).getImage(), 
    							gameController.getMonsters().get(index4).getOffset_x(),
    							gameController.getMonsters().get(index4).getOffset_y(), // the source rectangle's Y coordinate position.
    							gameController.getMonsters().get(index4).getWidth(), // the source rectangle's width.
    							gameController.getMonsters().get(index4).getHeight(), // the source rectangle's height.
    							gameController.getMonsters().get(index4).getX(), // the destination rectangle's X coordinate position.
    							gameController.getMonsters().get(index4).getY(), // the destination rectangle's Y coordinate position.
    							gameController.getMonsters().get(index4).getWidth(), // the destination rectangle's width.
    							gameController.getMonsters().get(index4).getHeight());
                    	break;  
                    default:
    
    			}
    		}
    	}
    }

	@Override
	public void update(Observable o, Object arg1) {
		GameModel model = (GameModel)o;
		gcForMario.clearRect(0, 0, 1000, 480);
		reDrawExceptionMario();
		reDrawMario();
		if(model.won()){
			//switch stage when curr stage has been cleared
		    if(this.gameModel.getLevel() != 2 &&this.gameModel.getLevel() != -1) {
                int currLevel = gameModel.getLevel()+1;
                System.out.println("游戏结束");
                model.deleteObserver(this);
                int level = model.getMarioLevel();
                this.gameModel = new GameModel();
                this.gameModel.setLevel(currLevel);
                this.gameModel.setMarioLevel(level);
                this.gameModel.addObserver(this);
                this.gameModel.getMario().setSize(model.getMario().getSize());
                gameController.restoreModel(this.gameModel, gcForMario, canvasForMario);
                initContent();
                gameModel.start();
            }
        }else if(model.getMario().getY()<0){
			// switch to hidden stage
			int currLevel = -1;
			model.deleteObserver(this);
			BGM.stop();
			BGM2.play();
			int level = model.getMarioLevel();
			this.gameModel.setBackground(new Image("resources/background_h.png"));

			this.gameModel = new GameModel();
			this.gameModel.setLevel(currLevel);
			this.gameModel.setMarioLevel(level);
			this.gameModel.addObserver(this);
			this.gameModel.getMario().setSize(model.getMario().getSize());
			gameController.restoreModel(this.gameModel, gcForMario, canvasForMario);
			initContent();
			gameModel.start();
		}else if(model.getLevel() == -1){
			if(!gameModel.touchFlag()) {
				gameController.setStart(true);
				gameController.getMario().setSpeed(slow);
				gameController.getMario().setRight(true);
			}
		}else if(model.getMario().getLife() < 0 && !model.touchFlag()){
			gcForMario.setFill(Color.BLACK);
			gcForMario.fillRect(0,0,1000,480);
			gcForMario.setFill(Color.WHITE);
			gcForMario.fillText("GAME OVER", 400, 100);
		}
	}

	/**
	 * redraw the mario
	 */
	private void reDrawMario() {
		gcForMario.drawImage(gameController.getMario().getImage(), // the image to be drawn or null.
				gameController.getMario().getOffset_x(), // the source rectangle's X coordinate position.
				gameController.getMario().getOffset_y(), // the source rectangle's Y coordinate position.
				gameController.getMario().getWidth(), // the source rectangle's width.
				gameController.getMario().getHeight(), // the source rectangle's height.
				gameController.getMario().getX(), // the destination rectangle's X coordinate position.
				gameController.getMario().getY(), // the destination rectangle's Y coordinate position.
				gameController.getMario().getWidth(), // the destination rectangle's width.
				gameController.getMario().getHeight()); // the destination rectangle's height.
	}

	/**
	 * redraw the stuff except mario
	 */
	private void reDrawExceptionMario() {
		gcForMario.drawImage(gameController.getBackground().getImage(), // the image to be drawn or null.
				gameController.getBackground().getOffset_x(), // the source rectangle's X coordinate position.
				gameController.getBackground().getOffset_y(), // the source rectangle's Y coordinate position.
				gameController.getBackground().getWidth(), // the source rectangle's width.
				gameController.getBackground().getHeight(), // the source rectangle's height.
				gameController.getBackground().getX(), // the destination rectangle's X coordinate position.
				gameController.getBackground().getY(), // the destination rectangle's Y coordinate position.
				gameController.getBackground().getWidth(), // the destination rectangle's width.
				gameController.getBackground().getHeight()); // the destination rectangle's height.

		// show the coin image and the coin is cannot be collected
		gcForMario.drawImage(blocksImage, 948, 41, 38, 37, 225, 45, 25, 25);

		// show the mario image and the mario cannot be move or touched
		gcForMario.drawImage(marioImage,198, 80, 40,38, 625, 45, 25, 25);

		for (int i = 0; i < gameController.getBricks().size(); i++) {
		    if (gameController.getBricks().get(i) == null){
		        continue;
            }
			gcForMario.drawImage(gameController.getBricks().get(i).getImage(), // the image to be drawn or null.
					gameController.getBricks().get(i).getOffset_x(), // the source rectangle's X coordinate position.
					gameController.getBricks().get(i).getOffset_y(), // the source rectangle's Y coordinate position.
					gameController.getBricks().get(i).getWidth(), // the source rectangle's width.
					gameController.getBricks().get(i).getHeight(), // the source rectangle's height.
					gameController.getBricks().get(i).getX(), // the destination rectangle's X coordinate position.
					gameController.getBricks().get(i).getY(), // the destination rectangle's Y coordinate position.
					gameController.getBricks().get(i).getWidth(), // the destination rectangle's width.
					gameController.getBricks().get(i).getHeight()); // the destination rectangle's height.
		}

		for (Coin coin : gameController.getCoins()) {
			if (coin == null) continue;
			gcForMario.drawImage(coin.getImage(),
					coin.getOffset_x(), coin.getOffset_y(),
					coin.getWidth(), coin.getHeight(),
					coin.getX(), coin.getY(), coin.getWidth(), coin.getHeight()
			);
		}

        for (Mushroom mushroom : gameController.getMushrooms()) {
            if (mushroom != null) {
                gcForMario.drawImage(mushroom.getImage(),
                        mushroom.getOffset_x(), mushroom.getOffset_y(),
                        mushroom.getWidth(), mushroom.getHeight(),
                        mushroom.getX(), mushroom.getY(), mushroom.getWidth(), mushroom.getHeight()
                );
            }

        }

        for (Bullet bullet : gameController.getBullets()){
            if (bullet != null){
                gcForMario.drawImage(bullet.getImage(),
                        bullet.getOffset_x(), bullet.getOffset_y(),
                        bullet.getWidth(), bullet.getHeight(),
                        bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight()
                );
            }
        }

        
        for(Monster monster: gameController.getMonsters()) {
        	if (monster != null) {
				gcForMario.drawImage(monster.getImage(), monster.getOffset_x(), monster.getOffset_y(),
						monster.getWidth(), monster.getHeight(),
						monster.getX(), monster.getY(), monster.getWidth(), monster.getHeight());

        	}
        }

        //draw fireworks
		for (Firework firework: gameController.getFireworks()) {
			gcForMario.drawImage( firework.getImage(), firework.getOffsetX(), firework.getOffsetY(),
					firework.getWidth(), firework.getHeight(),
					firework.getX(), firework.getY(), 40, 40);
		}

		// draw black circles
		for (BlackCircle blackCircle: gameController.getBlackCircles()) {
			gcForMario.setLineWidth(21);
			gcForMario.setFill(Color.BLACK);
			if (!blackCircle.isFill()) {
				gcForMario.strokeOval(blackCircle.getX(), blackCircle.getY(),
						blackCircle.getWidth(), blackCircle.getHeight());
			} else {
				gcForMario.fillOval(blackCircle.getX(), blackCircle.getY(),
						blackCircle.getWidth(), blackCircle.getHeight());
			}
		}
		// draw information
		// set effect

		gcForMario.setEffect(ds);
		for (Information information: gameController.getInformations()) {
			if (information.isNeedUpdate() == 3) {
				if (information.getCurrentCount() > information.getCount()) {
					information = null;
					continue;
				} else {
					information.setCurrentCount(information.getCurrentCount()+1);
					information.setY(information.getY()-1);
				}
			} else if (information.isNeedUpdate() == 1) {
				information.setText(Integer.valueOf(gameController.getMario().getSCORE()).toString());
			} else if (information.isNeedUpdate() == 5) {
				information.setText("*"+Integer.valueOf(gameController.getMario().getLife()).toString());
			}
			gcForMario.setFill(information.getColor());
			gcForMario.setFont(information.getFont());
			gcForMario.fillText(information.getText(), information.getX(), information.getY());
		}
		gcForMario.setEffect(null);
	}
        	
        	
        

	/**
	 * initial the game
	 * @param scene Scene
	 */
	public void initGame(Scene scene){
		gameModel.start();
		gameModel.addObserver(this);
		setControl(scene);

		initContent();
	}

	public void setControl(Scene scene){
		scene.setOnKeyPressed(event -> {
			if (!gameModel.isOUT_OF_CONTROL()) {
				if (event.getCode().toString().equals("D")) {
					gameController.setStart(true);
					gameController.getMario().setSpeed(2);
					gameController.getMario().setRight(true);

				} else if (event.getCode().toString().equals("A")) {
					gameController.setStart(true);
					gameController.getMario().setSpeed(-2);
					gameController.getMario().setLeft(true);
				} else if (event.getCode().toString().equals("W")) {
					if(!gameController.getMario().isJump()){
						(new MediaPlayer(jumpSound)).play();
					}
					gameController.setStart(true);
					gameController.getMario().setJump(true);
				} else if (event.getCode().toString().equals("K")) {
					if (gameController.getMario().getLevel() == 3) {
						if (gameController.getMario().getDirection() == 1) {
							Bullet bullet = new Bullet(gameController.getWxzImage(), 40, 40,
									Bullet.getRoffset_x(), Bullet.getRoffset_y(), gameController.getMario().getLeftTopC_x() + 4, gameController.getMario().getRightTopC_y() - 8);
							gameController.getBullets().add(bullet);
							bullet.setSpeed(6);
						} else {
							Bullet bullet = new Bullet(gameController.getWxzConvertImage(), 40, 40,
									Bullet.getLoffset_x(), Bullet.getLoffset_y(), gameController.getMario().getLeftTopC_x() + 4, gameController.getMario().getLeftTopC_y() - 8);
							gameController.getBullets().add(bullet);
							bullet.setSpeed(-6);
						}
					}
				}else if(event.getCode() == KeyCode.X){
					gameModel.skip();
					if(!gameModel.touchFlag()) {
						slow = 2;
					}
				}
			}
			// when ESC has been hit, open or close the pause menu
			if(event.getCode() == KeyCode.ESCAPE){
				if(!gameModel.getPaused()) {
				    BGM.pause();
				    // set the model to paused, draw the pause menu image on gc
					gameModel.pause();
					gcForMario.drawImage(pause,0,0);
					gcForMario.setFill(Color.WHITE);
					gcForMario.fillText("Paused", 850,40);
					curr = 1;
					pauseMenu();
				}else {
					if(gameModel.getLevel()!= -1) {
						BGM.play();
					}
					gameModel.resume();
				}
			// update the highlighted pauseMenu item when the game has been paused
			}else if(event.getCode()==KeyCode.UP){
				if(curr > 1){
					if(gameModel.getPaused()){
						curr --;
						pauseMenu();
					}
				}
			}else if(event.getCode() == KeyCode.DOWN) {
				if (curr < 3) {
					if(gameModel.getPaused()){
						curr++;
						pauseMenu();
					}
				}
			// execute the option when enter has been hit and game has been paused
			}else if(event.getCode() == KeyCode.ENTER){
				if(gameModel.getPaused()){
					if(curr == 1){
						if(gameModel.getLevel() == -1 ){
							return;
						}
						try {
						    // save data for the first option
							FileOutputStream file = new FileOutputStream("save_game.dat");
							ObjectOutputStream out = new ObjectOutputStream(file);
							out.writeObject(gameModel);
							out.close();
							file.close();
							System.out.println("Game Saved");
						}catch (IOException e){
							e.printStackTrace();
						}
					}else if(curr == 2){
						loadGame();
					}else{
					    // exit for the last option
						System.exit(0);
					}
					BGM.play();
					gameModel.resume();
				}
			}

		});
		scene.setOnKeyReleased(event -> {
			if (gameController.getMario().getMarioAnimation() != null) {
				if (event.getCode().toString().equals("D")) {
					gameController.getMario().setRight(false);
					gameController.getMario().setSpeed(0);
				}

				if (event.getCode().toString().equals("A")) {
					gameController.getMario().setLeft(false);
					gameController.getMario().setSpeed(0);
				}
				if(event.getCode() == KeyCode.X){
					gameModel.unSkip();
					slow = 1;
				}
			}
		});
	}

    /**
     * load previous game data from disk
     */
	private void loadGame(){
		try{
			// try to load save_game.data
			FileInputStream file = new FileInputStream("save_game.dat");
			ObjectInputStream in = new ObjectInputStream(file);
			gameModel = (GameModel) in.readObject();
			in.close();
			file.close();
			gameController.restoreModel(gameModel, gcForMario, canvasForMario);
			gameModel.start();
			gameModel.addObserver(this);
			gameModel.pause();
		// handel the situation when saved file does not exist
		}catch (IOException ioe ){
			Alert information = new Alert(Alert.AlertType.ERROR,"Previous game data does not exist\n"+ioe.getMessage());
			information.setTitle("Load Failed");
			information.setHeaderText("Unable to load game");
			information.showAndWait();
        // handel the situation when save file does not contains game data
		}catch (ClassNotFoundException nfe){
			Alert information = new Alert(Alert.AlertType.ERROR,"Previous game data has been corrupted\n"+nfe.getMessage());
			information.setTitle("Load Failed");
			information.setHeaderText("Unable to load game");
			information.showAndWait();
		}
	}

    /**
     * draw the pause menu
     * @param null
     * @return void 
     */
	private void pauseMenu(){
		double[] x = {0,0,240};
		double[] y = {240, 480, 480};
		gcForMario.setFill(Color.BLACK);
		gcForMario.fillPolygon(x,y ,3);
		gcForMario.fillRect(20, 280,100,40);
		gcForMario.fillRect(80, 340,100,40);
		gcForMario.fillRect(140, 400, 100,40);
		gcForMario.setFill(Color.GRAY);
		gcForMario.fillText("Save",30,310);
		gcForMario.fillText("Load", 90, 370);
		gcForMario.fillText("Exit", 150, 430);
		gcForMario.setFill(Color.WHITE);
		// draw the highlighted option
		if(curr == 1){
			gcForMario.fillText("Save",30,310);
		}else if(curr == 2){
			gcForMario.fillText("Load",90,370);
		}else{
			gcForMario.fillText("Exit", 150, 430);
		}
	}

    /**
     * init the game from beginning
     * @param scene Scene
     * @param gc GraphicsContest
     */
	private void MainGame(Scene scene,GraphicsContext gc){
		gc.clearRect(0,0,856,550);
		initGame(scene);
	}

    /**
     * Load previous game data from disk at main menu
     * @param scene
     */
	private void LoadGame(Scene scene){
	    // check if previous game data exist
		try {
			(new ObjectInputStream(new FileInputStream("save_game.dat"))).readObject();
		}catch (IOException e){
			Alert information = new Alert(Alert.AlertType.ERROR,"Previous game data does not exist\n"+e.getMessage());
			information.setTitle("Load Failed");
			information.setHeaderText("Unable to load game");
			information.showAndWait();
			return;
		}catch (ClassNotFoundException nfe){
			Alert information = new Alert(Alert.AlertType.ERROR,"Previous game data has been corrupted\n"+nfe.getMessage());
			information.setTitle("Load Failed");
			information.setHeaderText("Unable to load game");
			information.showAndWait();
			return;
		}
		// load when every thing is normal
		initGame(scene);
		gameModel.pause();
		loadGame();
		gameModel.resume();
		setControl(scene);
	}


	private void aboutThisGame(Scene scene, GraphicsContext gc){
		System.exit(0);
		gc.clearRect(0,0,856,550);
		gc.fillText("WORKING ON IT", 300,250);
		// TODO: do something
        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                StartMenu(scene,gc);
            }
        });
	}

    /**
     * update highlighted item on main menu
     * @param gc GraphicsContext
     */
	public void reDrawStart(GraphicsContext gc){
		gc.clearRect(0,0,1000,480);
		gc.drawImage(background, 0,0);

		// load main GUI
		Image title = new Image("resources/title.png");
		gc.drawImage(title,275,40);
		gc.setFill(Color.WHITE);
		gc.setFont(font);
		gc.fillText("\uD83C\uDD2F8102 NINJIGOKU",520,275);
		//gc.drawImage(i, 640, 360);

		Font newfont = Font.loadFont(getClass().getResourceAsStream("resources/font.ttf"),20);
		gc.setFont(newfont);
		gc.fillText("New Game",410, 330);
		gc.fillText("Load Game",408, 360);
		gc.fillText("Exit",410, 390);
		gc.setFill(Color.BLACK);
		// update highlighted item
		if(curr == 1){
			gc.fillText("New Game",410, 330);
		}else if(curr == 2){
			gc.fillText("Load Game",408, 360);
		}else{
			gc.fillText("Exit" ,410, 390);
		}
	}

    /**
     * draw main menu on the window, keep truck curr highlighted item.
     * @param scene Scene
     * @param gc GraphicsContext
     */
	private void StartMenu(Scene scene, GraphicsContext gc){
		reDrawStart(gc);
		scene.setOnKeyPressed(event -> {
			if(event.getCode()==KeyCode.UP|| event.getCode() == KeyCode.W){
				if(curr > 1){
					curr --;
					reDrawStart(gc);
				}
			}else if(event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S){
				if(curr < 3){
					curr ++;
					reDrawStart(gc);
				}
				
				// set up entry
			}else if(event.getCode() == KeyCode.ENTER){
				if(curr == 1){
					MainGame(scene, gc);
				}else if(curr == 2){
					LoadGame(scene);
				}else{
					aboutThisGame(scene, gc);
				}
			}
		});
	}
}

