package com.brahvim.nerd.nerd_demos.scenes;

import com.brahvim.nerd.framework.scene_layer_api.NerdSceneState;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;

import processing.opengl.PGraphics3D;

public class DemoScene1 extends AbstractDemoScene {

    protected DemoScene1(final NerdScenesModule<PGraphics3D> p_sceneMan) {
        super(p_sceneMan);
    }

    @Override
    protected void setup(final NerdSceneState p_state) {
        super.GRAPHICS.getCurrentCamera();
    }

    @Override
    protected void drawImpl() {
        // super.GRAPHICS.clear();
        super.GRAPHICS.text(super.SKETCH.frameRate);
        super.GRAPHICS.text(super.INPUT.getHeldKeysDebugString(), super.GENERIC_WINDOW.q3x, super.GENERIC_WINDOW.qy);
    }

}
