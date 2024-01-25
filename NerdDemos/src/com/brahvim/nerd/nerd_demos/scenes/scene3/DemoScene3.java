package com.brahvim.nerd.nerd_demos.scenes.scene3;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.framework.lights.NerdAmbientLight;
import com.brahvim.nerd.framework.scene_layer_api.NerdSceneState;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModuleSettings.NerdSceneLayerCallbackOrder;
import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.scenes.NerdP3dScene;
import com.brahvim.nerd.nerd_demos.debug_layers.DebugFpsGizmoLayer;
import com.brahvim.nerd.nerd_demos.effect_layers.CinematicBarsLayer;
import com.brahvim.nerd.nerd_demos.scenes.TestGlScene;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.MouseEvent;
import processing.opengl.PGraphics3D;

public class DemoScene3 extends NerdP3dScene {

	// region Fields.
	private PImage bgImage;
	private CubeManager cubeMan;
	private SmoothCamera camera;
	private NerdAmbientLight light;
	// endregion

	protected DemoScene3(final NerdScenesModule<PGraphics3D> p_sceneMan) {
		super(p_sceneMan);
	}

	// @Override
	// protected synchronized void preload() {
	// for (int i = 1; i != 5; i++)
	// this.ASSETS.addAsset(new OggBufferDataAsset("data/Pops/Pop" + i +
	// ".ogg"));
	// }

	@Override
	protected void setup(final NerdSceneState p_state) {
		super.MANAGER.getScenesModuleSettings().drawFirstCaller
		/*   */ = NerdSceneLayerCallbackOrder.SCENE;
		super.SCENE.addLayer(CinematicBarsLayer.class);
		super.SCENE.addLayer(DebugFpsGizmoLayer.class);

		this.camera = new SmoothCamera(super.GRAPHICS);
		this.camera.fov = PApplet.radians(75);
		super.GRAPHICS.setCurrentCamera(this.camera);
		// SKETCH.frameRate(90);

		// final AlBuffer<?>[] alBuffers = new AlBuffer<?>[4];
		// for (int i = 1; i != 5; i++)
		// alBuffers[i - 1] = this.ASSETS.get("Pop" + i).getData();

		this.cubeMan = new CubeManager(this /* , alBuffers */);
		this.light = new NerdAmbientLight(
				super.GRAPHICS, new PVector(0, 0, 0),
				// new PVector(255, 255, 0), // Yellow.
				// new PVector(224, 152, 27), // The orange at the top.
				// new PVector(228, 117, 111), // The color in the middle.
				new PVector(232, 81, 194) // The pink at the bottom.
		);

		this.bgImage = this.createBackgroundImage();
		// super.GRAPHICS.background(this.bgImage);
	}

	@Override
	protected void draw() {
		// Stress test! (`125` FPS at minimum for me! Max is `144`, the refresh rate):
		this.cubeMan.emitCubes(this.cubeMan.cubesPerClick);

		super.GRAPHICS.tint(255, 100);
		// super.GRAPHICS.background(0);
		super.GRAPHICS.background(this.bgImage);
		this.camera.apply();

		// Faster in `draw()`:
		if (super.INPUT.keysPressedAreOrdered(KeyEvent.VK_CONTROL, KeyEvent.VK_R))
			super.MANAGER.restartScene();

		super.GRAPHICS.lights();
		this.light.apply();
		this.cubeMan.draw();

		super.GRAPHICS.tint(255, 150);
		super.GRAPHICS.image(super.SKETCH.getGraphics(), 1000, 1000);
	}

	private PImage createBackgroundImage() {
		final int
		/*   */ color1 = super.SKETCH.color(224, 152, 27),
				color2 = super.SKETCH.color(232, 81, 194);
		final PImage toRet = super.SKETCH.createImage(
				this.DISPLAY.displayWidth, this.DISPLAY.displayHeight, PConstants.RGB);

		toRet.loadPixels();

		for (int y = 0; y < toRet.height; y++)
			for (int x = 0; x < toRet.width; x++)
				toRet.pixels[x + y * toRet.width] = super.SKETCH.lerpColor(
						color1, color2, PApplet.map(y, 0, toRet.height, 0, 1));

		toRet.updatePixels();
		return toRet;
	}

	// region Event callbacks.
	@Override
	public void mouseClicked() {
		switch (super.INPUT.mouseButton) {
			case PConstants.CENTER -> this.camera.setRoll(0);
			case PConstants.RIGHT -> super.MANAGER.startScene(TestGlScene.class); // startScene(DemoScene4.class);
			case PConstants.LEFT -> {
				this.cubeMan.emitCubes(this.cubeMan.cubesPerClick);
				// if (this.cubeMan.numCubes() < 2)
				// // this.cubeMan.emitCubes(1);
			}
		}
	}

	@Override
	public void keyPressed() {
		if (super.INPUT.keyCode == KeyEvent.VK_F) {
			this.WINDOW.cursorVisible = !this.WINDOW.cursorVisible;
			this.camera.holdMouse = !this.camera.holdMouse;
		}
	}

	@Override
	public void mouseWheel(final MouseEvent p_mouseEvent) {
		this.camera.fov -= p_mouseEvent.getCount() * 0.1f;
		this.camera.fov = PApplet.constrain(this.camera.fov, 0, 130);
	}

	@Override
	protected void sceneChanged() {
		this.cubeMan.removeAll(); // REALLY helps the GC out!
		System.gc(); // Surprisingly, this is an effective hint to the GC.

		// super.GRAPHICS.tint(255);
		// super.GRAPHICS.background(this.bgImage);
	}
	// endregion

}
