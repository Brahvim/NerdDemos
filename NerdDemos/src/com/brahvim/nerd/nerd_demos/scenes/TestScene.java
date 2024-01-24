package com.brahvim.nerd.nerd_demos.scenes;

import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.scenes.NerdGenericGraphicsScene;

import processing.core.PGraphics;

public class TestScene extends NerdGenericGraphicsScene {

    protected TestScene(final NerdScenesModule<PGraphics> p_sceneMan) {
        super(p_sceneMan);
    }

    @Override
    public void draw() {
        super.GRAPHICS.clear();
        super.GRAPHICS.text(super.SKETCH.frameRate, super.GENERIC_WINDOW.cx, super.GENERIC_WINDOW.cy);
        super.GRAPHICS.text(super.INPUT.getHeldKeysDebugString(), super.GENERIC_WINDOW.q3x, super.GENERIC_WINDOW.qy);
    }

}
