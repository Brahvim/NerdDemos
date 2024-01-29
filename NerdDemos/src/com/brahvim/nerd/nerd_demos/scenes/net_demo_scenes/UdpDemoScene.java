package com.brahvim.nerd.nerd_demos.scenes.net_demo_scenes;

import java.nio.charset.StandardCharsets;

import com.brahvim.nerd.framework.scene_layer_api.NerdSceneState;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.scenes.NerdJava2dScene;
import com.brahvim.nerd.io.net.NerdUdpSocket;

import processing.awt.PGraphicsJava2D;

public class UdpDemoScene extends NerdJava2dScene {

	// region Fields and constructor!
	public static final String MESSAGE = String.format("""
			====================================================================
			Welcome to the `UdpDemoScene`!
			This demo showcases the ease of using Nerd's UDP API!
			Pressing `G` should start the demo and exit this program.
			Please read the logs in the developer console upon its completion.
			Try modifying `%s::netTest()` to experiment with it and learn more!
			====================================================================
			""", UdpDemoScene.class.getSimpleName());
	// endregion

	protected UdpDemoScene(final NerdScenesModule<PGraphicsJava2D> p_sceneMan) {
		super(p_sceneMan);
	}

	@Override
	protected void setup(final NerdSceneState p_state) {
		super.GRAPHICS.textSize(25);
		super.GENERIC_WINDOW.fullscreen = true;
		// super.GRAPHICS.getCurrentCamera().getPos().z = 500;
	}

	@Override
	protected void draw() {
		super.GRAPHICS.background(0);
		super.GRAPHICS.text(UdpDemoScene.MESSAGE, 0, 0);
	}

	@Override
	public void keyPressed() {
		if (super.INPUT.key == 'g' || super.INPUT.key == 'G')
			this.netTest();
	}

	private void netTest() {
		final NerdUdpSocket socket1 = UdpDemoScene.createUdpSocketLoggingTheName("Socket-1"),
				socket2 = UdpDemoScene.createUdpSocketLoggingTheName("Socket-2");

		socket1.send(
				"Hello from `Socket-1`!",
				"127.0.0.1", // **Can also be an empty string, apparently!**
				socket2.getLocalPort());

		try {
			socket1.close();
			socket2.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		this.SKETCH.exit();
	}

	@SuppressWarnings("all")
	private static NerdUdpSocket createUdpSocketLoggingTheName(final String p_name) {
		return new NerdUdpSocket() { // NOSONAR This thing is closed later on!

			@Override
			protected void onStart() {
				System.out.println("Started `" + p_name + "`.");
			}

			@Override
			protected void onClose() {
				System.out.println("Closed `" + p_name + "`.");
			}

		}.addReceivingCallback((d, a, p) -> // "dap" and not "dip" - 'a' is for "address" here!
		System.out.printf("Received message \"%s\" from IP `%s`, on port `%d`.%n",
				new String(d, StandardCharsets.UTF_8), a, p));
	}

}
