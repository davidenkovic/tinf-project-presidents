package drivingpresidents;

import javafx.application.Platform;

import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;


public class President implements Runnable {
    public static final int TIME_IN_STATE = 5000;
    //region Private fields
    private PoliceCar leftCar;
    private PoliceCar rightCar;
    private String name;
    private Presentable presenter;
    private State state;
    //endregion

    //region Enums
    enum State {TALKING, ANGRY, DRIVING, DIRTY, CLEAN}
    //endregion

    //region Consumer
    Consumer<State> presidentStateConsumer;
    //endregion

    //region Constructor
    public President(PoliceCar _leftCar, PoliceCar _rightCar, String _name, Presentable presenter, Consumer<State> _presidentStateConsumer) {
        this.leftCar = _leftCar;
        this.leftCar.setRightPresident(this);
        this.rightCar = _rightCar;
        this.rightCar.setLeftPresident(this);
        this.name = _name;
        this.presenter = presenter;
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
            returnPoliceCars();
            presidentStateConsumer.accept(State.CLEAN);
        }
        leftCar.makeDirty();
        rightCar.makeDirty();
        presidentStateConsumer.accept(State.DIRTY);
    }

    @Override
    public void run() {
        while (presenter.presentationIsRunning() || state == State.ANGRY) {
            if (state != State.ANGRY) {
                talk();
                getAngry();
            }
            if (isAvailable(leftCar)) {
                if (isAvailable(rightCar)) {
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
        leftCar.getLeftPresident().borrowYourCarTo(this, rightCar);
        rightCar.getRightPresident().borrowYourCarTo(this, leftCar);
    }

    private boolean isAvailable(PoliceCar car) {
        return car.isAssignedTo(this);
    }

    private void returnPoliceCars() {
        System.out.println(name + " is coming back");
        returnReservedCar(leftCar);
        returnReservedCar(rightCar);
    }

    private void returnReservedCar(PoliceCar car) {
        if (car.isReserved()) {
            car.clean();
            car.assignTo(car.getPresidentWhoReserved());
        }
    }

    public void borrowYourCarTo(President president, PoliceCar whichCar) {
        if (whichCar.isDirty()) {
            whichCar.clean();
            whichCar.assignTo(president);
        } else {
            whichCar.reserveFor(president);
        }
    }
}