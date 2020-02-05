package drivingpresidents;
import java.net.URL;
import java.security.KeyPair;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Controller implements Initializable, Presentable
{
    public static final int PRESIDENTS_COUNT = 5;
    public static boolean running = true;

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
    public Button startButton;
    public Button stopButton;

    int runningPresidentsCount = 0;

    public static President[] presidents = new President[PRESIDENTS_COUNT];
    public static Lock[] policeCars = new ReentrantLock[PRESIDENTS_COUNT];

    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        for (President p : presidents) {
            new Thread(p).start();
            runningPresidentsCount++;
        }
        running = true;
        setButtonsToStart(false);
    }

    @FXML
    private void handleButtonStop(ActionEvent event)
    {
        running = false;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        erdoganImg = getImages("erdogan");
        kimImg = getImages("kim");
        obamaImg = getImages("obama");
        trumpImg = getImages("trump");
        putinImg = getImages("putin");

        for (int i = 0; i < PRESIDENTS_COUNT; i++) {
            policeCars[i] = new ReentrantLock();
        }

        presidents[0] = new President(policeCars[0], policeCars[1], "erdogan", this, erdogan, erdoganImg, police4, police3, c->{
            HashMap<President.PresidentState,ImageView> presidentHashMapErdogan = new HashMap<>();

            for(Map.Entry<President.PresidentState,ImageView> p : presidentHashMapErdogan.entrySet()){
                if (c == President.PresidentState.TALKING){
                    Platform.runLater(() -> {
                        erdogan.setImage(erdoganImg[0]);
                        police4.setVisible(true);
                        police3.setVisible(true);
                    });
                }
                if (c == President.PresidentState.ANGRY){
                    Platform.runLater(() -> { erdogan.setImage(erdoganImg[1]); });
                }
                if (c == President.PresidentState.DRIVING){
                    Platform.runLater(() -> {
                        erdogan.setImage(erdoganImg[2]);
                        police4.setVisible(false);
                        police3.setVisible(false);
                    });
                }
            }
            System.out.println("erdogan is " + c);
        });
        presidents[1] = new President(policeCars[1], policeCars[2], "kim", this, kim, kimImg, police2, police3, c->{
            HashMap<President.PresidentState,ImageView> presidentHashMapKim = new HashMap<>();
            for(Map.Entry<President.PresidentState,ImageView> p : presidentHashMapKim.entrySet()){
                if (c == President.PresidentState.TALKING){
                    Platform.runLater(() -> {
                        kim.setImage(erdoganImg[0]);
                        police2.setVisible(true);
                        police3.setVisible(true);
                    });
                }
                if (c == President.PresidentState.ANGRY){
                    Platform.runLater(() -> { kim.setImage(erdoganImg[1]); });
                }
                if (c == President.PresidentState.DRIVING){
                    Platform.runLater(() -> {
                        kim.setImage(erdoganImg[2]);
                        police2.setVisible(false);
                        police3.setVisible(false);
                    });
                }
            }
            System.out.println("kim is "+ c);
        });
        presidents[2] = new President(policeCars[2], policeCars[3], "trump", this, trump, trumpImg, police1, police2, c->{
            HashMap<President.PresidentState,ImageView> presidentHashMapTrump = new HashMap<>();
            for(Map.Entry<President.PresidentState,ImageView> p : presidentHashMapTrump.entrySet()){
                if (c == President.PresidentState.TALKING){
                    Platform.runLater(() -> {
                        trump.setImage(erdoganImg[0]);
                        police1.setVisible(true);
                        police2.setVisible(true);
                    });
                }
                if (c == President.PresidentState.ANGRY){ Platform.runLater(() -> { trump.setImage(erdoganImg[1]); });
                }
                if (c == President.PresidentState.DRIVING){
                    Platform.runLater(() -> {
                        trump.setImage(erdoganImg[2]);
                        police1.setVisible(false);
                        police2.setVisible(false);
                    });
                }
            }
            System.out.println("trump is "+ c);
        });
        presidents[3] = new President(policeCars[3], policeCars[4], "putin", this, putin, putinImg, police1, police5, c->{
            HashMap<President.PresidentState,ImageView> presidentHashMapPutin = new HashMap<>();
            for(Map.Entry<President.PresidentState,ImageView> p : presidentHashMapPutin.entrySet()){
                if (c == President.PresidentState.TALKING){
                    Platform.runLater(() -> {
                        putin.setImage(erdoganImg[0]);
                        police1.setVisible(true);
                        police5.setVisible(true);
                    });
                }
                if (c == President.PresidentState.ANGRY){ Platform.runLater(() -> { putin.setImage(erdoganImg[1]); });
                }
                if (c == President.PresidentState.DRIVING){ Platform.runLater(() -> {
                        putin.setImage(erdoganImg[2]);
                        police1.setVisible(false);
                        police5.setVisible(false);
                    });
                }
            }
            System.out.println("putin is "+ c);
        });
        presidents[4] = new President(policeCars[4], policeCars[0], "obama", this, obama, obamaImg, police5, police4, c->{
            HashMap<President.PresidentState,ImageView> presidentHashMapObama = new HashMap<>();

            for(Map.Entry<President.PresidentState,ImageView> p : presidentHashMapObama.entrySet()){
                if (c == President.PresidentState.TALKING){
                    Platform.runLater(() -> {
                        obama.setImage(erdoganImg[0]);
                        police5.setVisible(false);
                        police4.setVisible(false);
                    });
                }
                if (c == President.PresidentState.ANGRY){
                    Platform.runLater(() ->{ obama.setImage(erdoganImg[1]); });
                }
                if (c == President.PresidentState.DRIVING){
                    Platform.runLater(() -> {
                        obama.setImage(erdoganImg[2]);
                        police5.setVisible(false);
                        police4.setVisible(false);
                    });
                }
            }
            System.out.println("obama is "+ c);
        });
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

    @Override
    public void showMe(Image image) {

    }

    @Override
    public void presidentIsStopped()
    {
        runningPresidentsCount--;
        if (runningPresidentsCount == 0)
            setButtonsToStart(true);
    }

    @Override
    public boolean presentationIsRunning() {
        return running;
    }
}