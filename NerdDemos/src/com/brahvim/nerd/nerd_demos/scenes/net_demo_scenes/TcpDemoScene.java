package com.brahvim.nerd.nerd_demos.scenes.net_demo_scenes;

import java.util.function.Consumer;

import com.brahvim.nerd.framework.scene_layer_api.NerdSceneState;
import com.brahvim.nerd.framework.scene_layer_api.NerdScenesModule;
import com.brahvim.nerd.framework.scene_layer_api.renderer_specific_impls.scenes.NerdGenericGraphicsScene;
import com.brahvim.nerd.io.net.tcp.abstracts.NerdAbstractTcpServer;
import com.brahvim.nerd.io.net.tcp.abstracts.NerdAbstractTcpServer.NerdClientSentTcpPacket;
import com.brahvim.nerd.io.net.tcp.implementations.no_ssl.NerdTcpNoSslClient;
import com.brahvim.nerd.io.net.tcp.implementations.no_ssl.NerdTcpNoSslServer;
import com.brahvim.nerd.utils.NerdByteSerialUtils;

import processing.core.PGraphics;

public class TcpDemoScene extends NerdGenericGraphicsScene {

	// I'm not using JSON here, which is *the* standard for communication nowadays.
	// Thanks to Processing, however, you can (using the `processing.data` package)!

	// region Fields and constructor!
	public static final String MESSAGE = String.format("""
			====================================================================
			Welcome to the `RestaurantApi` demo!
			This demo showcases the ease of using Nerd's TCP API!
			Pressing `G` should start the demo and exit this program.
			Please read the logs in the developer console upon its completion.
			Try modifying `%s::netTest()` to experiment with it and learn more!
			====================================================================
			""", TcpDemoScene.class.getSimpleName());

	protected TcpDemoScene(final NerdScenesModule<PGraphics> p_sceneMan) {
		super(p_sceneMan);
	}
	// endregion

	// region Message `enum`s.
	private enum ClientQuery {
		ORDER_FOOD(),
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

	@Override
	protected void setup(final NerdSceneState p_state) {
		this.GRAPHICS.textSize(25);
		this.GENERIC_WINDOW.fullscreen = true;
		// GRAPHICS.getCurrentCamera().getPos().z = 500;
	}

	@Override
	protected void draw() {
		this.GRAPHICS.background(0);
		this.GRAPHICS.text(TcpDemoScene.MESSAGE, 0, 0);
	}

	@Override
	public void keyPressed() {
		if (this.INPUT.key == 'g' || this.INPUT.key == 'G')
			this.netTest();
	}

	private void netTest() {
		// Start a TCP server on the given port and check for clients to join!:
		final NerdAbstractTcpServer server = new NerdTcpNoSslServer(8080);

		server.setClientInvitationCallback(
				// Yes, you can directly pass this lambda into the constructor!
				// `c` holds the client object that wishes to join.
				c -> {
					// ...Let clients be refused by chance:
					final boolean clientAccepted = this.SKETCH.random(1) < 0.5f;

					if (clientAccepted) {
						// This client got accepted - hooray! Tell it!:
						c.send(NerdByteSerialUtils.toBytes(ServerResponse.ALLOWED));
						System.out.println("Ayy! A new client joined! Info: "
								+ c.getSocket().toString());
					} else // Tell us that it got refused otherwise.
						System.out.println("The server refused a connection.");

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
								System.out.println("Asked the server for food!");
							}
							case SERVED_FOOD -> { // If we're served food, we happily take it and go!:
								p.getReceivingClient().disconnect();
								System.out.println("Received the food, disconnecting now!");
							}
						}

						if (response != null)
							p.getReceivingClient().send(NerdByteSerialUtils.toBytes(response));
					});

		this.SKETCH.delay(50);
		System.out.println("Great day! The restaurant served well.");

		try {
			server.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		this.SKETCH.exit();
	}

}
