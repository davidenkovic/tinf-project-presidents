package drivingpresidents;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.concurrent.locks.Lock;

import static drivingpresidents.Controller.running;


public class President implements Runnable
{
    private Lock leftCar;
    private Lock rightCar;
    private String name;
    private Presentable presenter;
    private boolean isAngry;
    private ImageView presidentView;
    private Image talkingImg;
    private Image angryImg;
    private Image drivingImg;
    private ImageView leftCarView;
    private ImageView rightCarView;

    public President(Lock _leftCar, Lock _rightCar, String _name, Presentable presenter, ImageView _presidentView, Image _talkingImg, Image _angryImg, Image _drivingImg, ImageView _leftCarView, ImageView _rightCarView)
    {
        this.leftCar = _leftCar;
        this.rightCar = _rightCar;
        this.name = _name;
        this.presenter = presenter;
        this.isAngry = false;
        this.presidentView = _presidentView;
        this.talkingImg = _talkingImg;
        this.angryImg = _angryImg;
        this.drivingImg = _drivingImg;
        this.leftCarView = _leftCarView;
        this.rightCarView = _rightCarView;
    }
    public void talk()
    {
        if (isAngry == false)
        {
            try
            {
                System.out.println(name + " is talking...");
                Platform.runLater(() ->
                {
                    presidentView.setImage(talkingImg);
                });
                Thread.sleep((long) (Math.random() * 15000));
            }
            catch (InterruptedException ex) {
                System.out.println(name + "  interrupted when talking");
            }
            isAngry = true;
        }

    }
    public void drive()
    {
        System.out.println(name + " is angry...");
        if (isAngry == true)
        {
            Platform.runLater(() ->
            {
                presidentView.setImage(angryImg);
            });
        }
        try
        {
            Thread.sleep((long) (Math.random() * 15000));
        }
        catch (InterruptedException ex) { }
        if (leftCar.tryLock())
        {
            try
            {
                if (rightCar.tryLock())
                {
                    try
                    {
                        System.out.println(name + " is driving...");
                        Platform.runLater(() ->
                        {
                            presidentView.setImage(drivingImg);
                            leftCarView.setVisible(false);
                            rightCarView.setVisible(false);
                        });
                        isAngry = false;
                        try
                        {
                            Thread.sleep((long) (Math.random() * 15000));
                        }
                        catch (InterruptedException ex) { }
                        Platform.runLater(() ->
                        {
                            leftCarView.setVisible(true);
                            rightCarView.setVisible(true);
                        });
                    }
                    finally
                    {
                        rightCar.unlock();
                    }
                }
            }
            finally
            {
                leftCar.unlock();
            }
        }
    }

    @Override
    public void run()
    {
        while (running)
        {
            talk();
            drive();
        }
        System.out.println(name + " stopped...");
        talk();
        Platform.runLater(() -> {
            presidentView.setImage(talkingImg);
            presenter.presidentIsStopped();
        });

    }
}