package delta.cion.tokyo.server.motd;

import delta.cion.tokyo.api.ServerBranding;
import delta.cion.tokyo.api.online.ServerMOTD;
import delta.cion.tokyo.server.Server;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.network.player.PlayerConnection;
import delta.cion.tokyo.api.event.DeltaEvent;
import net.minestom.server.ping.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class MOTDHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(MOTDHandler.class);
	private static DeltaEvent<ServerListPingEvent> serverListPingEvent;

	private static final Path SERVER_ICON = Path.of("server-icon.png");

	private static final Consumer<ServerListPingEvent> DEFAULT_HANDLER = event -> {
		PlayerConnection connection = event.getConnection();
		if (connection != null) LOGGER.debug("Server pinged by {}", connection.getRemoteAddress());
		if (Server.getLanStatus() && connection == null) LOGGER.debug("Server pinged by LAN");

		Collection<Player> players = MinecraftServer.getConnectionManager().getOnlinePlayers();

		String serverDescription = ServerBranding.getBrandName() + " Server";

		Status.Builder rawResponse = new ServerMOTD()
			.setMOTDVersion(ServerBranding.getBrandName() + " " + MinecraftServer.VERSION_NAME)
			.setMOTDDescription(serverDescription)
			.getRaw();

		rawResponse.playerInfo(Status.PlayerInfo.builder()
			.onlinePlayers(players.size())
			.maxPlayers(-1)
			.sample(new ArrayList<>(players))
			.build()
		);

		if (buildServerIcon() != null)
			rawResponse.favicon(buildServerIcon());

		Status response = rawResponse.build();

		event.setStatus(response);
	};

	private static byte[] buildServerIcon() {
		if (!SERVER_ICON.toFile().exists()) return null;

		try {
			byte[] data = Files.readAllBytes(SERVER_ICON);
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));

			if (image == null) return null;
			if (image.getWidth() > 64) return null;
			if (image.getWidth() != image.getHeight()) return null;

			return data;
		} catch (IOException e) {
			LOGGER.warn("Incorrect image {}", SERVER_ICON);
			return null;
		}
	}

	public static void registerCustomMOTD(Consumer<ServerListPingEvent> handler) {
		if (serverListPingEvent != null) serverListPingEvent.unregister();
		if (handler == null) {
			registerVanillaMOTD();
			return;
		}

		serverListPingEvent = new DeltaEvent<>(ServerListPingEvent.class, handler);
		serverListPingEvent.register();
	}

	public static void registerVanillaMOTD() {
		registerCustomMOTD(DEFAULT_HANDLER);
	}
}
