package drivingpresidents;

import java.util.concurrent.locks.ReentrantLock;

public class PoliceCar extends ReentrantLock {
    private President leftPresident;
    private President rightPresident;

    public President getLeftPresident() {
        return leftPresident;
    }

    public President getRightPresident() {
        return rightPresident;
    }

    public void setLeftPresident(President leftPresident) {
        this.leftPresident = leftPresident;
    }

    public void setRightPresident(President rightPresident) {
        this.rightPresident = rightPresident;
    }
}
