package com.brahvim.nerd.nerd_demos.scenes;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.framework.scene_layer_api.NerdSceneState;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.opengl.PGraphics3D;

public class DemoScene5 extends AbstractDemoScene {

	private final String TEXT = String.format(
			"Welcome to `%s`!", super.SCENE_NAME);

	protected DemoScene5(final NerdScenesModule<PGraphics3D> p_sceneMan) {
		super(p_sceneMan);
	}

	@Override
	protected void setup(final NerdSceneState p_state) {
		System.out.println(this.TEXT);
		super.GRAPHICS.setCurrentCamera(null);
		// super.GRAPHICS.getCurrentCamera().getPos().x = 500;
		super.GRAPHICS.background(0x006699, PApplet.sin(super.getMillisSinceStart()));
	}

	@Override
	protected void draw() {
		if (super.INPUT.areKeysPressedAreOrdered(KeyEvent.VK_CONTROL, KeyEvent.VK_R))
			super.MANAGER.restartScene();

		super.GRAPHICS.background(0x006699, PApplet.sin(super.getMillisSinceStart()));

		super.GRAPHICS.pushMatrix();
		super.GRAPHICS.scale(2);

		super.GRAPHICS.fill(233);
		super.GRAPHICS.text(this.TEXT, 0, 0);

		super.GRAPHICS.popMatrix();

		// super.GRAPHICS.circle(
		// super.INPUT.mouseX - this.WINDOW.cx,
		// super.INPUT.mouseY - this.WINDOW.cy, 20);
		super.GRAPHICS.circle(super.GRAPHICS.getMouseInWorld(), 20);
	}

	@Override
	public void mousePressed() {
		switch (super.INPUT.mouseButton) {
			case PConstants.RIGHT -> super.MANAGER.startScene(DemoScene1.class);
		}
	}

}
