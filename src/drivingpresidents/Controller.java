package drivingpresidents;

import java.net.URL;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Controller implements Initializable, Presentable {
    //region Constant variable
    public static final int PRESIDENTS_COUNT = 5;
    public static boolean running = true;
    //endregion


    //region President Image[] and ImageView
    @FXML
    public ImageView erdogan;
    public Image[] erdoganImg;
    @FXML
    public ImageView kim;
    public Image[] kimImg;
    @FXML
    public ImageView trump;
    public Image[] trumpImg;
    @FXML
    public ImageView putin;
    public Image[] putinImg;
    @FXML
    public ImageView obama;
    public Image[] obamaImg;
    //endregion

    //region Police ImageViews
    @FXML
    public ImageView police1;
    @FXML
    public ImageView police2;
    @FXML
    public ImageView police3;
    @FXML
    public ImageView police4;
    @FXML
    public ImageView police5;
    //endregion

    //region Buttons
    public Button startButton;
    public Button stopButton;
    //endregion

    //region Counter
    int runningPresidentsCount = 0;
    //endregion

    //region President and Lock variable array
    public static President[] presidents = new President[PRESIDENTS_COUNT];
    //endregion

    //region HandleButtons
    @FXML
    private void handleButtonAction(ActionEvent event) {
        for (President p : presidents) {
            new Thread(p).start();
            runningPresidentsCount++;
        }
        running = true;
        setButtonsToStart(false);
    }

    @FXML
    private void handleButtonStop(ActionEvent event) {
        running = false;
    }
    //endregion

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        erdoganImg = getImages("erdogan");
        kimImg = getImages("kim");
        obamaImg = getImages("obama");
        trumpImg = getImages("trump");
        putinImg = getImages("putin");


        presidents[0] = new President(new PoliceCar(), new PoliceCar(), "erdogan", this, this::handleErdoganImages);
        presidents[1] = new President(new PoliceCar(), new PoliceCar(), "kim", this, this::handleKimImages);
        presidents[2] = new President(new PoliceCar(), new PoliceCar(), "trump", this, this::handleTrumpImages);
        presidents[3] = new President(new PoliceCar(), new PoliceCar(), "putin", this, this::handlePutinImages);
        presidents[4] = new President(new PoliceCar(), new PoliceCar(), "obama", this, this::handleObamaImages);
    }

    private Image[] getImages(String presidentName) {
        Image[] images = new Image[3];
        images[0] = getImage("img/" + presidentName + "-talking.png");
        images[1] = getImage("img/" + presidentName + "-angry.png");
        images[2] = getImage("img/" + presidentName + "-driving.png");
        return images;
    }

    private Image getImage(String resource) {
        return new Image(getClass().getResourceAsStream(resource));
    }


    private void setButtonsToStart(boolean isOn) {
        startButton.setDisable(!isOn);
        stopButton.setDisable(isOn);
    }

    //region Presentable methods
    @Override
    public void presidentIsStopped() {
        runningPresidentsCount--;
        if (runningPresidentsCount == 0)
            setButtonsToStart(true);
    }

    @Override
    public boolean presentationIsRunning() {
        return running;
    }

    private void handleErdoganImages(President.State c) {
        setPresidentImages(c, erdogan, erdoganImg, police4, police3);
        System.out.println("erdogan is " + c);
    }

    private void setPresidentImages(President.State c, ImageView presidentImageView, Image[] presidentImage, ImageView leftPolice, ImageView rightPolice) {
        if (c == President.State.TALKING) {
            Platform.runLater(() -> {
                presidentImageView.setImage(presidentImage[0]);
                leftPolice.setVisible(true);
                rightPolice.setVisible(true);
            });
        }
        if (c == President.State.ANGRY) {
            Platform.runLater(() -> presidentImageView.setImage(presidentImage[1]));
        }
        if (c == President.State.DRIVING) {
            Platform.runLater(() -> {
                presidentImageView.setImage(presidentImage[2]);
                leftPolice.setVisible(false);
                rightPolice.setVisible(false);
            });
        }
    }

    private void handleKimImages(President.State c) {
        setPresidentImages(c, kim, kimImg, police2, police3);
        System.out.println("kim is " + c);
    }

    private void handleTrumpImages(President.State c) {
        setPresidentImages(c, trump, trumpImg, police1, police2);
        System.out.println("trump is " + c);
    }

    private void handlePutinImages(President.State c) {
        setPresidentImages(c, putin, putinImg, police1, police5);
        System.out.println("putin is " + c);
    }

    private void handleObamaImages(President.State c) {
        setPresidentImages(c, obama, obamaImg, police5, police4);
        System.out.println("Obama is " + c);
    }
    //endregion
}