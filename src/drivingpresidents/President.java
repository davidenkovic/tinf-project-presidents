package drivingpresidents;

import javafx.application.Platform;

import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;


public class President implements Runnable {
    public static final int TIME_IN_STATE = 15000;
    //region Private fields
    private Lock leftCar;
    private Lock rightCar;
    private String name;
    private Presentable presenter;
    private boolean isAngry;
    private State state;
    //endregion

    //region Enums
    enum State {TALKING, ANGRY, DRIVING}
    //endregion

    //region Consumer
    Consumer<State> presidentStateConsumer;
    //endregion

    //region Constructor
    public President(Lock _leftCar, Lock _rightCar, String _name, Presentable presenter, Consumer<State> _presidentStateConsumer) {
        this.leftCar = _leftCar;
        this.rightCar = _rightCar;
        this.name = _name;
        this.presenter = presenter;
        this.isAngry = false;
        this.state = State.TALKING;
        this.presidentStateConsumer = _presidentStateConsumer;
    }
    //endregion

    //region Getter
    public String getName() {
        return name;
    }
    //endregion

    public void talk() {
        this.presidentStateConsumer.accept(State.TALKING);
        try {
            Thread.sleep((long) (Math.random() * TIME_IN_STATE));
        } catch (InterruptedException ex) {
            System.out.println(name + "  interrupted when talking");
        }

    }

    public void drive() {
        presidentStateConsumer.accept(State.DRIVING);
        try {
            Thread.sleep((long) (Math.random() * TIME_IN_STATE));
        } catch (InterruptedException ex) {
            System.out.println(getName() + " interrupted when being angry");
            leftCar.unlock();
            rightCar.unlock();
        }
    }

    @Override
    public void run() {
        while (presenter.presentationIsRunning() || state == State.ANGRY) {
            if (state != State.ANGRY) {
                talk();
                getAngry();
            }
            if (grabPoliceCar(leftCar)) {
                if (grabPoliceCar(rightCar)) {
                    drive();
                    returnToTalking();
                }
            }
        }
        Platform.runLater(() -> presenter.presidentIsStopped());
    }

    private void returnToTalking() {
        returnPoliceCars();
        state = State.TALKING;
        presidentStateConsumer.accept(State.TALKING);
    }

    private void getAngry() {
        state = State.ANGRY;
        presidentStateConsumer.accept(state);
    }

    private boolean grabPoliceCar(Lock car) {
        return car.tryLock();
    }

    private void returnPoliceCars() {
        System.out.println(name + " is coming back");
        leftCar.unlock();
        rightCar.unlock();
    }
}