package BinPacking.Data.UI.SceneModels;

import javafx.scene.Group;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Xsignati on 09.05.2017.
 * Composite for all models displayed in GUI.
 */
public class SceneModelComposite implements SceneModel {
    List<SceneModel> modelsList = new LinkedList<>();
    public void add(SceneModel model){
        modelsList.add(model);
    }
    @Override
    public void addModel(Group binSceneModels){
        modelsList.forEach(m -> m.addModel(binSceneModels));
    }
    @Override
    public void addModel(Group binSceneModels, int id){modelsList.forEach(m -> m.addModel(binSceneModels, id));}
    @Override
    public void scale(double scale) {
        modelsList.forEach(m -> m.scale(scale));
    }

}
