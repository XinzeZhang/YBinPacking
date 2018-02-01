package BinPacking.Data.UI.Scene;

import BinPacking.Data.UI.SceneModels.SceneModel;
import javafx.beans.NamedArg;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
/**
 * Custom BinScene displaying boxes and bins.
 * Created by Xsignati on 08.04.2017.
 */
public class BinScene extends SubScene {
    private final CameraModel camera;
    private final Group binSceneElements;
    private Scale scale;
    private final double rotationSpeed = 0.2;
    private final double scrollSpeed = 50;
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;
    private double scrollDelta;
    private double scrollPosZ;

    /**
     * Custom BinScene
     * @param root SubScene class root
     * @param width SubScene class width
     * @param height SubScene class height
     * @param depthBuffer SubScene class dephtBuffer
     */
    public BinScene(@NamedArg("root") Parent root, @NamedArg("width") double width, @NamedArg("height") double height, @NamedArg("depthBuffer") boolean depthBuffer) {
        super(root, width, height, true, SceneAntialiasing.BALANCED);
        camera = (CameraModel) getRoot().getChildrenUnmodifiable().get(0);
        binSceneElements = (Group) getRoot().getChildrenUnmodifiable().get(1);

        //Add SubScene event handlers
        setCamera(camera.getCamera());
        setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });

        setOnMouseDragged(me -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            if (me.isPrimaryButtonDown()) {
                camera.getRz().setAngle(camera.getRz().getAngle() - mouseDeltaX * rotationSpeed);
                camera.getRx().setAngle(camera.getRx().getAngle() + mouseDeltaY * rotationSpeed);
            }
        });

        setOnScroll((se) -> {
            scrollDelta = se.getDeltaY() > 0 ?  scrollSpeed : -scrollSpeed;
            scrollPosZ = camera.getCamera().getTranslateZ();
            camera.getCamera().setTranslateZ(scrollPosZ + scrollDelta);
        });
    }

    public void add(SceneModel modelComposite, int selectedBin) {
        modelComposite.addModel(binSceneElements, selectedBin);
    }

    public void init(double binLength, double binWidth, double binHeight){
        camera.setDistance(getWidth() * 4);
        camera.reset();
        scale = new Scale(getWidth() / binLength, getHeight() / binWidth, getWidth() / binHeight);
    }

    public void rescale(SceneModel sceneModel){
        sceneModel.scale(scale.get());
    }

    public void clear(){
        binSceneElements.getChildren().clear();
    }

    /**
     * Created by Xsignati on 09.04.2017.
     * Computes an appropriate scaling factor. Every box and bin is scaled
     * before drawing to match to the SubScene camera view and avoid too large objects to displaying.
     */
    private class Scale{
        private final double scale; //Scale factor
        private Scale(double s1, double s2, double s3){
            scale = getMin(s1, s2, s3);
        }
        private double getMin(double r1, double r2, double r3) {
            return Math.min(r1, Math.min(r2, r3));
        }
        private double get() {
            return scale;
        }
    }

    public Scale getScale() {
        return scale;
    }
}

