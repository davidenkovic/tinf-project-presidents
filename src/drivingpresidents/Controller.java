package drivingpresidents;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

        for (int i = 0; i < PRESIDENTS_COUNT; i++)
        {
            policeCars[i] = new ReentrantLock();
        }

        presidents[0] = new President(policeCars[0], policeCars[1], "erdogan", this, erdogan, erdoganImg, police4, police3);
        presidents[1] = new President(policeCars[1], policeCars[2], "kim", this, kim, kimImg, police2, police3);
        presidents[2] = new President(policeCars[2], policeCars[3], "trump", this, trump, trumpImg, police1, police2);
        presidents[3] = new President(policeCars[3], policeCars[4], "putin", this, putin, putinImg, police1, police5);
        presidents[4] = new President(policeCars[4], policeCars[0], "obama", this, obama, obamaImg, police5, police4);
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