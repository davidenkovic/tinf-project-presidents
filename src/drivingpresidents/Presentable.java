package drivingpresidents;

import javafx.scene.image.Image;

public interface Presentable {
    void showMe(Image image);
    void presidentIsStopped();
    boolean presentationIsRunning();
}
