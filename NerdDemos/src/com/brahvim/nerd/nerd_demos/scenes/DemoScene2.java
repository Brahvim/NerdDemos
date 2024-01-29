package com.brahvim.nerd.nerd_demos.scenes;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.framework.scene_layer_api.NerdSceneState;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.nerd_demos.debug_layers.DebugFpsGizmoLayer;
import com.brahvim.nerd.nerd_demos.scenes.scene3.SmoothCamera;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.MouseEvent;
import processing.opengl.PGraphics3D;

public class DemoScene2 extends AbstractDemoScene {

    private SmoothCamera camera;

    protected DemoScene2(final NerdScenesModule<PGraphics3D> p_sceneMan) {
        super(p_sceneMan);
    }

    @Override
    protected void setup(final NerdSceneState p_state) {
        this.camera = super.GRAPHICS.setCurrentCamera(new SmoothCamera(super.GRAPHICS));
        super.addLayer(DebugFpsGizmoLayer.class);
        this.camera.getPos().x = -450;
    }

    @Override
    protected void draw() {
        super.draw();

        if (this.INPUT.areKeysPressedAreOrdered(KeyEvent.VK_CONTROL, KeyEvent.VK_R))
            this.MANAGER.restartScene();

        super.GRAPHICS.clear();
        this.camera.apply();

        super.GRAPHICS.pushMatrix();
        super.GRAPHICS.translateToCenter();
        // super.GRAPHICS.translate(super.GRAPHICS.getMouseInWorld());
        // super.GRAPHICS.translateZ(PApplet.sin(this.SKETCH.millis() * 0.01f) * 250);
        super.GRAPHICS.rotateX(PApplet.sin(this.SKETCH.millis() * 0.001f));
        super.GRAPHICS.rotateZ(PApplet.cos(this.SKETCH.millis() * 0.001f));
        super.GRAPHICS.box(50);
        super.GRAPHICS.popMatrix();
    }

    @Override
    public void keyPressed() {
        if (this.INPUT.keyCode == KeyEvent.VK_F) {
            super.WINDOW.cursorVisible = !super.WINDOW.cursorVisible;
            this.camera.holdMouse = !this.camera.holdMouse;
        }
    }

    @Override
    public void mouseClicked() {
        switch (super.INPUT.mouseButton) {
            case PConstants.CENTER -> this.camera.setRoll(0);
        }
    }

    @Override
    public void mouseWheel(final MouseEvent p_mouseEvent) {
        this.camera.fov -= p_mouseEvent.getCount() * 0.1f;
        this.camera.fov = PApplet.constrain(this.camera.fov, 0, 130);
    }

}
