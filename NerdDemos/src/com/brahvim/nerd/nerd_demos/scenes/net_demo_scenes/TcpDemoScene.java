package com.brahvim.nerd.nerd_demos.scenes.net_demo_scenes;

import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.scene_layer_api.NerdSceneState;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.scenes.NerdJava2dScene;
import com.brahvim.nerd.io.net.tcp.abstracts.NerdAbstractTcpServer;
import com.brahvim.nerd.io.net.tcp.abstracts.NerdAbstractTcpServer.NerdClientSentTcpPacket;
import com.brahvim.nerd.io.net.tcp.implementations.no_ssl.NerdTcpNoSslClient;
import com.brahvim.nerd.io.net.tcp.implementations.no_ssl.NerdTcpNoSslServer;
import com.brahvim.nerd.utils.NerdByteSerialUtils;

import processing.awt.PGraphicsJava2D;

public class TcpDemoScene extends NerdJava2dScene {

	// I'm not using JSON here, which is *the* standard for communication nowadays.
	// Thanks to Processing, however, you can (using the `processing.data` package)!

	// region Fields and constructor!
	public static final String MESSAGE = String.format("""
			====================================================================
			Welcome to the `RestaurantApi` demo!
			This demo showcases the ease of using Nerd's TCP API!
			Pressing `G` should start the demo and exit this program.
			You may read logs in the developer console upon its completion.
			Try modifying `%s::netTest()` to experiment with it and learn more!
			====================================================================
			""", TcpDemoScene.class.getSimpleName());

	private float textHeight, logsBeginY;
	private final List<String> NET_TEST_LOGS = new Vector<>(5);

	protected TcpDemoScene(final NerdScenesModule<PGraphicsJava2D> p_sceneMan) {
		super(p_sceneMan);
	}
	// endregion

	// region Demo classes!
	// region Message `enum`s.
	private enum ClientQuery {
		ORDER_FOOD();
	}

	private enum ServerResponse {
		ALLOWED(),
		// REFUSED(),
		SERVED_FOOD(),
	}
	// endregion

	private class RestaurantApi implements Consumer<NerdClientSentTcpPacket> {

		@Override
		public void accept(final NerdClientSentTcpPacket p_packet) {
			// Get the client's message:
			final ClientQuery message = NerdByteSerialUtils.fromBytes(p_packet.getData());
			final NerdAbstractTcpServer.NerdTcpServerClient client = p_packet.getSender();
			ServerResponse response = null; // In here, we store our response!

			switch (message) {
				// If food is ordered, we serve.
				case ORDER_FOOD -> response = ServerResponse.SERVED_FOOD;
			}

			// Send the response over!
			client.send(NerdByteSerialUtils.toBytes(response));
		}

	}
	// endregion

	@Override
	protected void setup(final NerdSceneState p_state) {
		super.GRAPHICS.textSize(25);
		super.GENERIC_WINDOW.fullscreen = true;
		this.textHeight = super.GRAPHICS.textHeight() * 1.5f;
		this.logsBeginY = super.GRAPHICS.textHeight() * 8;
		// ^^^ Why times `8`? Because that's the *number of lines*, plus `1`!
	}

	@Override
	protected void draw() {
		super.GRAPHICS.background(0);
		super.GRAPHICS.translate(super.GENERIC_WINDOW.cx, super.GENERIC_WINDOW.qy);
		super.GRAPHICS.text(TcpDemoScene.MESSAGE, 0, 0);

		final int numLines = this.NET_TEST_LOGS.size();
		for (int i = 0; i < numLines; i++)
			super.GRAPHICS.text(this.NET_TEST_LOGS.get(i),
					0, this.logsBeginY + (i * this.textHeight));
	}

	@Override
	public void keyPressed() {
		if (this.INPUT.key == 'g' || this.INPUT.key == 'G')
			new Thread(this::netTest).start();
	}

	private void netTest() {
		if (this.NET_TEST_LOGS.isEmpty())
			this.SKETCH.exit();

		System.out.println("""
				========================
				Nerd TCP Networking Test""");

		// Start a TCP server on the given port and check for clients to join!:
		try (final NerdAbstractTcpServer server = new NerdTcpNoSslServer(8080)) {
			server.setClientInvitationCallback(
					// Yes, you can directly pass this lambda into the constructor!
					// `c` holds the client object that wishes to join.
					c -> {
						// ...Let clients be refused by chance:
						final boolean clientAccepted = this.SKETCH.random(1) < 0.5f;

						if (clientAccepted) {
							// This client got accepted - hooray! Tell it!:
							c.send(NerdByteSerialUtils.toBytes(ServerResponse.ALLOWED));
							this.netTestLog("Ayy! A new client joined! Info: "
									+ c.getSocket().toString());
						} else // Tell us that it got refused otherwise.
							this.netTestLog("The server refused a connection.");

						// Returning `null` or calling `c.disconnect()` should disconnect.
						// If we accept this client, we return a listener to listen to its messages!:
						return clientAccepted;
					});

			server.addMessageReceivedCallback(new RestaurantApi());

			// Now, we start 5 clients to connect to the server!

			for (int i = 0; i < 5; i++)
				new NerdTcpNoSslClient(
						// Info on the server to connect to:
						"127.0.0.1", 8080,
						// Following, is a callback to receive messages from the server.
						// `p` holds the packet of data received!:
						p -> {
							// Get the server's message,
							final ServerResponse message = (ServerResponse) NerdByteSerialUtils.fromBytes(p.getData());
							ClientQuery response = null; // We'll store our response here.

							switch (message) {
								case ALLOWED -> { // If the server happily allows us, we order!:
									response = ClientQuery.ORDER_FOOD;
									this.netTestLog("Asked the server for food!");
								}
								case SERVED_FOOD -> { // If we're served food, we happily take it and go!:
									p.getReceivingClient().disconnect();
									this.netTestLog("Received the food, disconnecting now!");
								}
							}

							if (response != null)
								p.getReceivingClient().send(NerdByteSerialUtils.toBytes(response));
						});

			this.SKETCH.delay(50);
			this.netTestLog("Great day! The restaurant served well.");
			this.netTestLog("You may now exit.");
		} catch (final Exception e) {
			e.printStackTrace();
		}

		this.SKETCH.exit();
	}

	private synchronized void netTestLog(final String p_logString) {
		this.NET_TEST_LOGS.add(p_logString);
		System.out.println(p_logString);
	}

}
