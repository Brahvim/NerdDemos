package com.brahvim.nerd.nerd_demos.scenes.scene3;

import com.brahvim.nerd.framework.scene_layer_api.NerdSceneState;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.scenes.NerdGenericGraphicsScene;

import processing.core.PGraphics;

public class TestScene extends NerdGenericGraphicsScene {

    // private float sineWave;

    protected TestScene(final NerdScenesModule<PGraphics> p_sceneMan) {
        super(p_sceneMan);
    }

    @Override
    protected void setup(final NerdSceneState p_state) {
        super.SKETCH.frameRate(144);
        super.WINDOW.setSize(640, 480);
    }

    @Override
    protected void draw() {
        super.GRAPHICS.clear();

        System.out.println(super.SKETCH.frameRate);
        // super.GRAPHICS.text(super.SKETCH.frameRate,
        // super.WINDOW.cx,
        // super.WINDOW.cy);
    }

}
