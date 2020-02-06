package drivingpresidents;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;


public class President implements Runnable
{
    //region Private fields
    private Lock leftCar;
    private Lock rightCar;
    private String name;
    private Presentable presenter;
    private boolean isAngry;
    //endregion

    //region Enums
    enum PresidentState { TALKING, ANGRY, DRIVING }
    //endregion

    //region Consumer
    Consumer<PresidentState> presidentStateConsumer;
    //endregion

    //region Constructor
    public President(Lock _leftCar, Lock _rightCar, String _name, Presentable presenter, ImageView _presidentView, Image[] images, ImageView _leftCarView, ImageView _rightCarView,Consumer<PresidentState> _presidentStateConsumer )
    {
        this.leftCar = _leftCar;
        this.rightCar = _rightCar;
        this.name = _name;
        this.presenter = presenter;
        this.isAngry = false;
        this.presidentStateConsumer = _presidentStateConsumer;
    }
    //endregion

    //region Getter
    public String getName() { return name; }
    //endregion

    public void talk()
    {
        if (!isAngry) {
            try {
                this.presidentStateConsumer.accept(PresidentState.TALKING);
                Thread.sleep((long) (Math.random() * 15000));
            }
            catch (InterruptedException ex) { System.out.println(name + "  interrupted when talking"); }
            isAngry = true;
        }
    }

    public void drive()
    {
        if (isAngry) { this.presidentStateConsumer.accept(PresidentState.ANGRY); }
        try { Thread.sleep((long) (Math.random() * 15000)); }
        catch (InterruptedException ex) { System.out.println(getName() + " interrupted when being angry"); }
        if (leftCar.tryLock()) {
            try {
                if (rightCar.tryLock()) {
                    try {
                        this.presidentStateConsumer.accept(PresidentState.DRIVING);
                        isAngry = false;
                        try { Thread.sleep((long) (Math.random() * 15000)); }
                        catch (InterruptedException ex) { System.out.println(getName() + " interrupted when driving"); }
                    }
                    finally { rightCar.unlock(); }
                }
            }
            finally { leftCar.unlock(); }
        }
    }

    @Override
    public void run()
    {
        while (presenter.presentationIsRunning()) {
            talk();
            drive();
        }
        talk();
        Platform.runLater(() -> presenter.presidentIsStopped());
        this.presidentStateConsumer.accept(PresidentState.TALKING);

    }
}