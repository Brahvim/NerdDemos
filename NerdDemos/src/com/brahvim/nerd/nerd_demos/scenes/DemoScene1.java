package com.brahvim.nerd.nerd_demos.scenes;

import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.scenes.NerdP3dScene;

import processing.core.PConstants;
import processing.opengl.PGraphics3D;

public class DemoScene1 extends NerdP3dScene {

    protected DemoScene1(final NerdScenesModule<PGraphics3D> p_sceneMan) {
        super(p_sceneMan);
    }

    @Override
    public void draw() {
        super.GRAPHICS.clear();
        super.GRAPHICS.text(super.SKETCH.frameRate, super.GENERIC_WINDOW.cx, super.GENERIC_WINDOW.cy);
        super.GRAPHICS.text(super.INPUT.getHeldKeysDebugString(), super.GENERIC_WINDOW.q3x, super.GENERIC_WINDOW.qy);
    }

    @Override
    public void mousePressed() {
        switch (super.INPUT.mouseButton) {
            case PConstants.RIGHT -> super.MANAGER.startScene(DemoScene2.class);
        }
    }

}
