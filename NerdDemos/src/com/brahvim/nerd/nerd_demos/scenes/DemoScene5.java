package com.brahvim.nerd.nerd_demos.scenes;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.framework.scene_layer_api.NerdSceneState;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;

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
		super.GRAPHICS.getCurrentCamera().setClearColor(0x006699);
		super.GRAPHICS.getCurrentCamera().getPos().x = 500;
		// super.GRAPHICS.background(0x006699,
		// PApplet.sin(super.getMillisSinceStart()));
	}

	@Override
	protected void drawImpl() {
		if (super.INPUT.keysPressedAreOrdered(KeyEvent.VK_CONTROL, KeyEvent.VK_R))
			super.MANAGER.restartScene();

		// super.GRAPHICS.background(0x006699,
		// PApplet.sin(super.getMillisSinceStart()));

		try (var a = super.GRAPHICS.new TwoDimensionalPush()) {
			super.GRAPHICS.scale(2);
			super.GRAPHICS.fill(233);
			super.GRAPHICS.text(this.TEXT, 0, 0);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		// super.GRAPHICS.circle(
		// super.INPUT.mouseX - this.WINDOW.cx,
		// super.INPUT.mouseY - this.WINDOW.cy, 20);
		super.GRAPHICS.circle(super.GRAPHICS.getMouseInWorld(), 20);
	}

}
