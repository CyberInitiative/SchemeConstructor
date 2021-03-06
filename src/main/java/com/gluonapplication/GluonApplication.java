package com.gluonapplication;

import com.gluonapplication.views.PrimaryView;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;

public class GluonApplication extends MobileApplication {

    public static final String PRIMARY_VIEW = HOME_VIEW;

    @Override
    public void init() {
        addViewFactory(PRIMARY_VIEW, () -> new PrimaryView().getView());
        DrawerManager.buildDrawer(this);    
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);
//        scene.getStylesheets().add(GluonApplication.class.getResource("style.css").toExternalForm());
    }

    public static void main(String args[]) {
        launch(args);
    }
}
