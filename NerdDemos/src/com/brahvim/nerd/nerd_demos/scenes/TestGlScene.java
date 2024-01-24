package com.brahvim.nerd.nerd_demos.scenes;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.framework.scene_layer_api.NerdSceneState;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.scenes.NerdP3dScene;
import com.brahvim.nerd.nerd_demos.SmoothCamera;

import processing.core.PApplet;
import processing.event.MouseEvent;
import processing.opengl.PGraphics3D;

public class TestGlScene extends NerdP3dScene {

    private SmoothCamera camera;

    protected TestGlScene(NerdScenesModule<PGraphics3D> p_sceneMan) {
        super(p_sceneMan);
    }

    @Override
    protected void setup(final NerdSceneState p_state) {
        this.camera = GRAPHICS.setCurrentCamera(new SmoothCamera(GRAPHICS));
        this.camera.setPos(null);
    }

    @Override
    protected void draw() {
        if (INPUT.keysPressedAreOrdered(KeyEvent.VK_CONTROL, KeyEvent.VK_R))
            MANAGER.restartScene();

        GRAPHICS.clear();
        GRAPHICS.translateToCenter();
        // GRAPHICS.translateX(PApplet.sin(SKETCH.millis() * 0.01f) * 25);
        // GRAPHICS.rotateX(PApplet.sin(SKETCH.millis() * 0.001f));
        // GRAPHICS.rotateZ(PApplet.cos(SKETCH.millis() * 0.001f));
        GRAPHICS.box(50);
    }

    @Override
    public void keyPressed() {
        if (INPUT.keyCode == KeyEvent.VK_F) {
            GENERIC_WINDOW.cursorVisible = !GENERIC_WINDOW.cursorVisible;
            this.camera.holdMouse = !this.camera.holdMouse;
        }
    }

    @Override
    public void mouseWheel(final MouseEvent p_mouseEvent) {
        this.camera.fov -= p_mouseEvent.getCount() * 0.1f;
        this.camera.fov = PApplet.constrain(this.camera.fov, 0, 130);
    }

}
