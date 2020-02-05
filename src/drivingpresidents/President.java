package drivingpresidents;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;


public class President implements Runnable
{
    enum PresidentState { TALKING, ANGRY, DRIVING }

    private Lock leftCar;
    private Lock rightCar;

    private String name;
    private Presentable presenter;
    private boolean isAngry;

    private Image[] images;

    private ImageView presidentView;
    private ImageView leftCarView;
    private ImageView rightCarView;

    Consumer<PresidentState> presidentStateConsumer;

    public President(Lock _leftCar, Lock _rightCar, String _name, Presentable presenter, ImageView _presidentView, Image[] images, ImageView _leftCarView, ImageView _rightCarView,Consumer<PresidentState> _presidentStateConsumer )
    {
        this.leftCar = _leftCar;
        this.rightCar = _rightCar;

        this.name = _name;
        this.presenter = presenter;
        this.isAngry = false;

        this.images = images;
        
        this.presidentView = _presidentView;
        this.leftCarView = _leftCarView;
        this.rightCarView = _rightCarView;

        this.presidentStateConsumer = _presidentStateConsumer;
    }

    public void talk()
    {
        if (!isAngry)
        {
            try
            {
                //System.out.println(name + " is talking...");
                Platform.runLater(() ->
                {
                    presenter.showMe(getImage(PresidentState.TALKING));
                    presidentView.setImage(getImage(PresidentState.TALKING));
                });
                this.presidentStateConsumer.accept(PresidentState.TALKING);
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
        //System.out.println(name + " is angry...");
        if (isAngry)
        {
            Platform.runLater(() -> presidentView.setImage(getImage(PresidentState.ANGRY)));
            this.presidentStateConsumer.accept(PresidentState.ANGRY);
        }
        try
        {
            Thread.sleep((long) (Math.random() * 15000));
        }
        catch (InterruptedException ex) {
            System.out.println(getName() + " interrupted when being angry");
        }
        if (leftCar.tryLock())
        {
            try
            {
                if (rightCar.tryLock())
                {
                    try
                    {
                        //System.out.println(name + " is driving...");
                        Platform.runLater(() ->
                        {
                            presidentView.setImage(getImage(PresidentState.DRIVING));
                            leftCarView.setVisible(false);
                            rightCarView.setVisible(false);
                        });
                        this.presidentStateConsumer.accept(PresidentState.DRIVING);
                        isAngry = false;
                        try
                        {
                            Thread.sleep((long) (Math.random() * 15000));
                        }
                        catch (InterruptedException ex) {
                            System.out.println(getName() + " interrupted when driving");
                        }
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
        while (presenter.presentationIsRunning())
        {
            talk();
            drive();
        }
        //System.out.println(name + " stopped...");
        talk();
        Platform.runLater(() -> {
            presidentView.setImage(getImage(PresidentState.TALKING));
            presenter.presidentIsStopped();
        });
        this.presidentStateConsumer.accept(PresidentState.TALKING);

    }

    public String getName() {
        return name;
    }

    private Image getImage(PresidentState state) {
        return images[state.ordinal()];
    }
}