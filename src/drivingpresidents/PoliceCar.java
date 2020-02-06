package drivingpresidents;

import java.util.concurrent.locks.ReentrantLock;

public class PoliceCar extends ReentrantLock {
    private President leftPresident;
    private President rightPresident;

    private President assignedPresident;
    private President presidentWhoReserved = null;
    private boolean isDirty = true;

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

    public void assignTo(President president) {
        presidentWhoReserved = null;
        assignedPresident = president;
    }

    public boolean isAssignedTo(President president) {
        return assignedPresident == president;
    }

    public void reserveFor(President president) {
        presidentWhoReserved = president;
    }

    public boolean isReserved() {
        return presidentWhoReserved != null;
    }

    public President getPresidentWhoReserved() {
        return presidentWhoReserved;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void makeDirty() {
        isDirty = true;
    }

    public void clean() {
        isDirty = false;
    }
}
