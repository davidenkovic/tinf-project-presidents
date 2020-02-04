package drivingpresidents;
import java.net.URL;
import java.util.ArrayList;
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
    public Image[] erdoganImg = new Image[3];
    @FXML
    public ImageView kim;
    public Image[] kimImg = new Image[3];
    @FXML
    public ImageView trump;
    public Image[] trumpImg = new Image[3];
    @FXML
    public ImageView putin;
    public Image[] putinImg = new Image[3];
    @FXML
    public ImageView obama;
    public Image[] obamaImg = new Image[3];

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
    private List<Thread> presidentThreads = new ArrayList<>();

    public static President[] presidents = new President[PRESIDENTS_COUNT];
    public static Lock[] policeCars = new ReentrantLock[PRESIDENTS_COUNT];

    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        for (President p : presidents) {
            presidentThreads.add(new Thread(p));
        }

        for (Thread p : presidentThreads) {
            p.start();
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
        erdoganImg[0] = new Image(getClass().getResourceAsStream("img/erdogan-talking.png"));
        erdoganImg[1] = new Image(getClass().getResourceAsStream("img/erdogan-angry.png"));
        erdoganImg[2] = new Image(getClass().getResourceAsStream("img/erdogan-driving.png"));

        kimImg[0] = new Image(getClass().getResourceAsStream("img/kim-talking.png"));
        kimImg[1] = new Image(getClass().getResourceAsStream("img/kim-angry.png"));
        kimImg[2] = new Image(getClass().getResourceAsStream("img/kim-driving.png"));

        obamaImg[0] = new Image(getClass().getResourceAsStream("img/obama-talking.png"));
        obamaImg[1] = new Image(getClass().getResourceAsStream("img/obama-angry.png"));
        obamaImg[2] = new Image(getClass().getResourceAsStream("img/obama-driving.png"));

        putinImg[0] = new Image(getClass().getResourceAsStream("img/putin-talking.png"));
        putinImg[1] = new Image(getClass().getResourceAsStream("img/putin-angry.png"));
        putinImg[2] = new Image(getClass().getResourceAsStream("img/putin-driving.png"));

        trumpImg[0] = new Image(getClass().getResourceAsStream("img/trump-talking.png"));
        trumpImg[1] = new Image(getClass().getResourceAsStream("img/trump-angry.png"));
        trumpImg[2] = new Image(getClass().getResourceAsStream("img/trump-driving.png"));

        for (int i = 0; i < PRESIDENTS_COUNT; i++)
        {
            policeCars[i] = new ReentrantLock();
        }

        presidents[0] = new President(policeCars[0], policeCars[1], "erdogan", this, erdogan, erdoganImg[0], erdoganImg[1], erdoganImg[2], police4, police3);
        presidents[1] = new President(policeCars[1], policeCars[2], "kim", this, kim, kimImg[0], kimImg[1], kimImg[2], police2, police3);
        presidents[2] = new President(policeCars[2], policeCars[3], "trump", this, trump, trumpImg[0], trumpImg[1], trumpImg[2], police1, police2);
        presidents[3] = new President(policeCars[3], policeCars[4], "putin", this, putin, putinImg[0], putinImg[1], putinImg[2], police1, police5);
        presidents[4] = new President(policeCars[4], policeCars[0], "obama", this, obama, obamaImg[0], obamaImg[1], obamaImg[2], police5, police4);
    }

    @Override
    public void presidentIsStopped()
    {
        runningPresidentsCount--;
        if (runningPresidentsCount == 0)
            setButtonsToStart(true);
    }

    private void setButtonsToStart(boolean isOn) {
        startButton.setDisable(!isOn);
        stopButton.setDisable(isOn);
    }

    @Override
    public void showMeTalking(President president) {

    }

    @Override
    public void showMeAngry(President president) {

    }
}