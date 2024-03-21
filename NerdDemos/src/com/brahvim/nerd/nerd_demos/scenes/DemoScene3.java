package com.brahvim.nerd.nerd_demos.scenes;

import java.awt.event.KeyEvent;

import com.brahvim.nerd.framework.cameras.NerdAbstractCamera;
import com.brahvim.nerd.framework.graphics_backends.NerdP3dGraphics;
import com.brahvim.nerd.framework.lights.NerdAmbientLight;
import com.brahvim.nerd.framework.scene_layer_api.NerdSceneState;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.nerd_demos.debug_layers.DemoDebugFpsGizmoLayer;
import com.brahvim.nerd.nerd_demos.effect_layers.DemoCinematicBarsLayer;
import com.brahvim.nerd.nerd_demos.scenes.scene3.AnimatedCubesManager;
import com.brahvim.nerd.nerd_demos.scenes.scene3.SmoothCamera;
import com.brahvim.nerd.processing_wrapper.NerdAbstractGraphics;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.MouseEvent;
import processing.opengl.PGraphics3D;

public class DemoScene3 extends AbstractDemoScene {

	// region Fields.
	private PImage bgImage;
	private AnimatedCubesManager cubeMan;
	private SmoothCamera camera;
	private NerdAmbientLight light;
	private boolean shouldAutomaticallyAddCubes = true;
	// endregion

	protected DemoScene3(final NerdScenesModule<PGraphics3D> p_sceneMan) {
		super(p_sceneMan);
	}

	// @Override
	// protected synchronized void preload() {
	// for (int i = 1; i != 5; ++i)
	// super.ASSETS.addAsset(new OggBufferDataAsset("data/Pops/Pop" + i + ".ogg"));
	// }

	@Override
	protected void setup(final NerdSceneState p_state) {
		super.addLayer(DemoCinematicBarsLayer.class);
		super.addLayer(DemoDebugFpsGizmoLayer.class);

		this.bgImage = this.createBackgroundImageUsingPGraphics();
		this.camera = super.GRAPHICS.setCurrentCamera(
				new SmoothCamera(super.GRAPHICS));

		// super.GRAPHICS.setClearImage(this.bgImage);

		this.camera.fov = PApplet.radians(75);
		this.camera.far = 100_000_000.0f;

		this.cubeMan = new AnimatedCubesManager(this);
		this.light = new NerdAmbientLight(
				// new PVector(255, 255, 0), // Yellow.
				// new PVector(224, 152, 27), // The orange at the top.
				new PVector(228, 117, 111), // The color in the middle.
				// new PVector(232, 81, 194), // The pink at the bottom.
				this.camera.POSITION);

		super.GRAPHICS.setLightSlotObject(NerdP3dGraphics.NerdLightSlot.ONE, this.light);
		super.GRAPHICS.setLightSlotObject(NerdP3dGraphics.NerdLightSlot.TWO, this.light);
		super.GRAPHICS.setLightSlotObject(NerdP3dGraphics.NerdLightSlot.THREE, this.light);
	}

	@Override
	protected void drawImpl() {
		// Stress test!:
		if (this.shouldAutomaticallyAddCubes || super.INPUT.mouseLeft)
			this.cubeMan.emitCubes(this.cubeMan.cubesPerClick);
		else
			this.cubeMan.cubesToAdd = 0;

		// Nearly `1,000` cubes at once after sufficient time!
		// (`125` FPS at minimum for me! It's `60` without the JIT kicking in, though.)
		// (Max possible is `144`, the refresh rate).

		// this.drawFaintBackground();
		this.drawBackground();
		// this.light.apply();
		this.cubeMan.draw();
		this.drawScreen();
		this.drawOverlay();
	}

	// region Extra drawing functions.
	private void drawScreen() {
		try (var a = super.GRAPHICS.new MatrixPush()) {
			super.GRAPHICS.tint(255, PApplet.map(
					this.camera.POSITION.magSq(), this.camera.near, this.camera.far, 200, 0));
			super.GRAPHICS.rotateY(PConstants.HALF_PI + PConstants.PI);
			super.GRAPHICS.image(super.GRAPHICS);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void drawOverlay() {
		try (var a = super.GRAPHICS.new TwoDimensionalPush()) {
			super.GRAPHICS.tint(255, 150);
			super.GRAPHICS.translateFromCenter();
			super.GRAPHICS.image(super.GRAPHICS);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void drawBackground() {
		super.GRAPHICS.background(this.bgImage);
	}

	@SuppressWarnings("unused")
	private void drawFaintBackground() {
		this.addTint(super.GRAPHICS);
		super.GRAPHICS.background(this.bgImage);
	}

	@SuppressWarnings("unused")
	private PImage createBackgroundImage() {
		final int
		/*   */ color1 = super.SKETCH.color(224, 152, 27),
				color2 = super.SKETCH.color(232, 81, 194);

		// TODO: Figure out why this breaks LOL.
		final NerdP3dGraphics toRet = new NerdP3dGraphics(
				super.SKETCH, super.GRAPHICS.width, super.GRAPHICS.height); // .getUnderlyingBuffer();

		try (var a = toRet.new PixelOperation()) {
			for (int y = 0; y < toRet.height; y++)
				for (int x = 0; x < toRet.width; x++)
					/////////////////////////////////
					a.PIXELS[x + y * toRet.width]
					/*   */ = super.SKETCH.lerpColor(
							color1, color2, // ...Linearly-interpolate b/w these colors,
							PApplet.map(y, 0, toRet.height, 0, 1)); // ...by this amount! :D!
		} catch (final Exception e) {
			e.printStackTrace();
		}

		toRet.endDraw();

		return toRet.getUnderlyingBuffer();
	}

	private PImage createBackgroundImageUsingPGraphics() {
		final int
		/*   */ color1 = super.SKETCH.color(224, 152, 27),
				color2 = super.SKETCH.color(232, 81, 194);

		final PGraphics toRet = super.SKETCH.createGraphics();

		toRet.loadPixels();
		for (int y = 0; y < toRet.height; y++)
			for (int x = 0; x < toRet.width; x++)
				/////////////////////////////////
				toRet.pixels[x + y * toRet.width]
				/*   */ = super.SKETCH.lerpColor(
						color1, color2, // ...Linearly-interpolate b/w these colors,
						PApplet.map(y, 0, toRet.height, 0, 1)); // ...by this amount! :D!
		toRet.updatePixels();

		return toRet;
	}
	// endregion

	protected void addTint(final NerdAbstractGraphics<?> p_graphics) {
		p_graphics.tint(255, 50);
	}

	// region Event callbacks.
	@Override
	protected void exit() {
		final int numCubes = this.cubeMan.numCubes();
		super.STATE.set("CubeCount", numCubes);
		System.out.printf("Number of cubes: `%d`.%n", numCubes);
	}

	@Override
	public void keyPressed() {
		if (super.INPUT.keyCode == KeyEvent.VK_F) {
			this.camera.holdMouse = !this.camera.holdMouse;
			super.WINDOW.cursorVisible = !super.WINDOW.cursorVisible;
		}

		// Pressing `x` changes the state of cube emission.
		if (super.INPUT.keyCode == KeyEvent.VK_X)
			this.shouldAutomaticallyAddCubes = !this.shouldAutomaticallyAddCubes;
	}

	@Override
	public void mouseClicked() {
		switch (super.INPUT.mouseButton) {
			case PConstants.CENTER -> {
				this.camera.setRoll(0);
				this.camera.fov = NerdAbstractCamera.DEFAULT_FOV;
			}
			// case PConstants.LEFT -> this.cubeMan.emitCubes(this.cubeMan.cubesPerClick);
		}
	}

	@Override
	public void mouseWheel(final MouseEvent p_mouseEvent) {
		this.camera.fov -= p_mouseEvent.getCount() * 0.1f;
		this.camera.fov = PApplet.constrain(this.camera.fov, 0, 130);
	}
	// endregion

}
