package com.brahvim.nerd.nerd_demos.scenes;

import com.brahvim.nerd.framework.cameras.NerdBasicCamera;
import com.brahvim.nerd.framework.cameras.NerdBasicCameraBuilder;
import com.brahvim.nerd.framework.scene_layer_api.NerdSceneState;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdP3dGraphics;

import processing.core.PConstants;
import processing.core.PImage;
import processing.event.MouseEvent;
import processing.opengl.PGraphics3D;

public class DemoScene4 extends AbstractDemoScene {

	// region Fields!
	private PImage tileImage;
	private NerdBasicCamera camera;
	private NerdP3dGraphics tileGraphic;

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
		if (this.getTimesLoaded() == 1
				&& super.MANAGER.SETTINGS.FIRST_SCENE_CLASS == DemoScene4.class) {
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

		this.tileImage = super.WINDOW.getIconImage();
		this.tileGraphic = new NerdP3dGraphics(super.SKETCH //
				, super.SKETCH.createGraphics(this.tileImage.width, this.tileImage.height)
		/*   */ );

		super.GRAPHICS.noStroke();
		super.GRAPHICS.textureWrap(PConstants.REPEAT);
		super.GRAPHICS.getCurrentCamera().POSITION.z = 500;
	}

	@Override
	protected void drawImpl() {
		super.GRAPHICS.clear();

		this.magScrollVel += this.magScrollAcc *= DemoScene4.MAG_SCROLL_DECAY_ACC;
		this.magScroll += this.magScrollVel *= DemoScene4.MAG_SCROLL_DECAY_VEL;
		this.camera.POSITION.z += this.magScrollVel;

		// ...and unleash the nerds!:
		try (var a = super.GRAPHICS.new TwoDimensionalPush()) {
			final float tileRotationTime = this.tileRotationTime();
			this.drawNerdTileShapeTo(this.tileImage, this.tileGraphic, tileRotationTime);
			this.drawInfiniteNerdTiles(tileRotationTime);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private float tileRotationTime() {
		return super.getMillisSinceStart() * 0.1f;
	}

	private NerdP3dGraphics drawNerdTileShapeTo(
			final PImage p_tileImage, final NerdP3dGraphics p_tileBuffer, final float p_time) {
		p_tileBuffer.beginDraw();
		p_tileBuffer.beginShape();
		p_tileBuffer.imageMode(PConstants.CENTER);
		p_tileBuffer.translate(p_tileBuffer.cx, p_tileBuffer.cy);
		p_tileBuffer.rotateZ(p_time * 0.01f);
		p_tileBuffer.image(
				p_tileImage, 0, 0,
				p_tileImage.width * this.magScroll,
				p_tileImage.height * this.magScroll);
		p_tileBuffer.endShape();
		p_tileBuffer.endDraw();
		return p_tileBuffer;
	}

	/**
	 * For infinite tiles with scrolling according to the time parameter.
	 *
	 * @param p_time is a parameter whose derivative defines how fast the tiles
	 *               appear to scroll. If it is set to {@code 0}, no scrolling
	 *               occurs.
	 * @see
	 *      {@linkplain DemoScene4#drawInfiniteNerdTiles()
	 *      DemoScene4::drawInfiniteNerdTiles()} for when no scrolling is preferred,
	 *      since it may be faster (though the JIT is still reliable - this
	 *      non-parameterized overload exists for educational purposes.)
	 */
	private void drawInfiniteNerdTiles(final float p_time) {
		super.GRAPHICS.beginShape();
		super.GRAPHICS.texture(this.tileGraphic);

		super.GRAPHICS.vertex(0, 0, p_time, p_time);

		super.GRAPHICS.vertex(
				this.WINDOW.width,
				0,
				p_time + this.WINDOW.width,
				p_time);

		super.GRAPHICS.vertex(
				this.WINDOW.width,
				this.WINDOW.height,
				p_time + this.WINDOW.width,
				p_time + this.WINDOW.height);

		super.GRAPHICS.vertex(
				0,
				this.WINDOW.height,
				p_time,
				p_time + this.WINDOW.height);
		super.GRAPHICS.endShape();
	}

	/** For just infinite tiles (no scrolling!). */
	// @SuppressWarnings("unused")
	// private void drawInfiniteNerdTiles() {
	// try (var a = super.GRAPHICS.new ClosedShape()) {
	// super.GRAPHICS.vertex(0, 0, 0, 0);
	//
	// super.GRAPHICS.vertex(
	// this.WINDOW.width, 0,
	// this.WINDOW.width, 0);
	//
	// super.GRAPHICS.vertex(
	// this.WINDOW.width, this.WINDOW.height,
	// this.WINDOW.width, this.WINDOW.height);
	//
	// super.GRAPHICS.vertex(
	// 0, this.WINDOW.height,
	// 0, this.WINDOW.height);
	// } catch (final Exception e) {
	// e.printStackTrace();
	// }
	// }

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
