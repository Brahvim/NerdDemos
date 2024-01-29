package com.brahvim.nerd.nerd_demos.scenes;

import com.brahvim.nerd.framework.cameras.NerdBasicCamera;
import com.brahvim.nerd.framework.cameras.NerdBasicCameraBuilder;
import com.brahvim.nerd.framework.scene_layer_api.NerdSceneState;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.MouseEvent;
import processing.opengl.PGraphics3D;

public class DemoScene4 extends AbstractDemoScene {

	// region Fields!
	private PImage nerd;
	private float ncx, ncy;
	private PGraphics nerdGraphics;
	private NerdBasicCamera camera;

	private static final float MAG_SCROLL_ACC_MOD = 0.001f,
			MAG_SCROLL_DECAY_ACC = 0.8f,
			MAG_SCROLL_DECAY_VEL = 0.99f;
	private float magScrollAcc, magScrollVel, magScroll = 1;
	// endregion

	protected DemoScene4(final NerdScenesModule<PGraphics3D> p_sceneMan) {
		super(p_sceneMan);
	}

	// @Override
	// protected synchronized void preload() {
	// ASSETS.addAsset(new OggBufferDataAsset("data/RUBBER DUCK.ogg"));
	// System.out.println("`DemoScene4` asset preload completed!");
	// }

	@Override
	protected void setup(final NerdSceneState p_state) {
		// System.out.printf(
		// "`DemoScene4::setup()` here, I was called `%d` times!%n",
		// this.SCENE.getTimesLoaded());

		this.camera = new NerdBasicCameraBuilder(super.GRAPHICS).build();

		// region OpenAL Test.
		// ..so the effects and filters wrk perfectly, but I just didn't want them in
		// this example. Feel free to uncomment!~

		// AlAuxiliaryEffectSlot slot = new AlAuxiliaryEffectSlot(App.AL);
		// AlEcho effect = new AlEcho(App.AL);
		// slot.setEffect(effect);
		// effect.setEchoDelay(0.01f);
		// effect.setEchoDamping(0.8f);
		// effect.setEchoFeedback(0.001f);

		// AlLowpassFilter filter = new AlLowpassFilter(App.AL);
		// filter.setLowpassGain(1);
		// filter.setLowpassGainHf(0.1f);

		// final AlSource rubberDuck = new AlSource(App.openAl, ASSETS.get("RUBBER
		// DUCK").getData());
		// this.rubberDuck.attachDirectFilter(filter);
		// rubberDuck.setGain(0.1f);
		// this.rubberDuck.setEffectSlot(slot);
		// endregion

		// Loaded this scene for the first time? Do this!:
		if (this.SCENE.getTimesLoaded() == 1
				&& super.MANAGER.getScenesModuleSettings().FIRST_SCENE_CLASS == DemoScene4.class)

		{
			this.WINDOW.fullscreen = false;
			this.WINDOW.setSize(1600, 900);
			this.WINDOW.centerWindow();
		} else { // Do not play `this.rubberDuck` if this is the first start!
					// App.openAl.setListenerVelocity(0, 0, 0);
					// App.openAl.setListenerPosition(0, 0, 500);
					// App.openAl.setListenerOrientation(0, 1, 0);

			// rubberDuck.setPosition(
			// 5 * (INPUT.mouseX - WINDOW.cx), 0,
			// 5 * (INPUT.mouseY - WINDOW.cy));

			// if (!rubberDuck.isPlaying())
			// rubberDuck.play();
		}

		this.nerd = super.WINDOW.getIconImage();
		this.nerdGraphics = super.SKETCH.createGraphics(this.nerd.width, this.nerd.height);

		super.GRAPHICS.noStroke();
		super.GRAPHICS.getCurrentCamera().getPos().z = 500;
		super.GRAPHICS.textureWrap(PConstants.REPEAT);

		this.ncx = this.nerd.width * 0.5f;
		this.ncy = this.nerd.height * 0.5f;
	}

	@Override
	protected void draw() {
		super.draw();

		super.GRAPHICS.clear();
		// super.GRAPHICS.translate(-WINDOW.cx, -WINDOW.cy);

		this.magScrollVel += this.magScrollAcc *= DemoScene4.MAG_SCROLL_DECAY_ACC;
		this.magScroll += this.magScrollVel *= DemoScene4.MAG_SCROLL_DECAY_VEL;
		this.camera.getPos().z += this.magScrollVel;

		super.GRAPHICS.begin2d();

		// region Draw the nerds!!!
		super.GRAPHICS.beginShape();

		this.nerdGraphics.beginDraw();
		this.nerdGraphics.imageMode(PConstants.CENTER);
		this.nerdGraphics.translate(this.ncx, this.ncy);
		this.nerdGraphics.rotateZ(this.nerdRotTime() * 0.01f);
		this.nerdGraphics.image(this.nerd, 0, 0,
				this.nerd.width * this.magScroll,
				this.nerd.height * this.magScroll);
		this.nerdGraphics.endDraw();

		super.GRAPHICS.texture(this.nerdGraphics);

		// For just infinite tiles (no scrolling!):

		// super.GRAPHICS.vertex(0, 0, 0, 0);
		// super.GRAPHICS.vertex(WINDOW.width, 0, WINDOW.width, 0);
		// super.GRAPHICS.vertex(WINDOW.width, WINDOW.height, WINDOW.width,
		// WINDOW.height);
		// super.GRAPHICS.vertex(0, WINDOW.height, 0, WINDOW.height);

		super.GRAPHICS.vertex(0, 0, this.nerdRotTime(), this.nerdRotTime());
		super.GRAPHICS.vertex(this.WINDOW.width, 0, this.nerdRotTime() + this.WINDOW.width,
				this.nerdRotTime());
		super.GRAPHICS.vertex(this.WINDOW.width, this.WINDOW.height,
				this.nerdRotTime() + this.WINDOW.width, this.nerdRotTime() + this.WINDOW.height);
		super.GRAPHICS.vertex(0, this.WINDOW.height, this.nerdRotTime(), this.nerdRotTime() +
				this.WINDOW.height);

		super.GRAPHICS.endShape();
		// endregion

		super.GRAPHICS.translate(super.GRAPHICS.getMouseInWorld());
		super.GRAPHICS.circle(0, 0, 200);

		super.GRAPHICS.end2d();
	}

	private float nerdRotTime() {
		return this.SCENE.getMillisSinceStart() * 0.1f;
	}

	// region Events.
	@Override
	public void mouseClicked() {
		switch (super.INPUT.mouseButton) {
			case PConstants.LEFT -> this.MANAGER.restartScene();
			// case PConstants.RIGHT -> this.MANAGER.startScene(new NerdSceneClassLoader(
			// "file://" + NerdSketch.DATA_DIR_PATH + "/DemoScene5.class",
			// "com.brahvim.nerd.nerd_demos.scenes.DemoScene5").getLoadedClass());
		}
	}

	@Override
	public void mouseWheel(final MouseEvent p_mouseEvent) {
		this.magScrollAcc += p_mouseEvent.getCount() * DemoScene4.MAG_SCROLL_ACC_MOD;
	}
	// endregion

}
