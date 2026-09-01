package delta.cion.tokyo.api.online;

import net.kyori.adventure.text.Component;
import net.minestom.server.ping.Status;

public class ServerMOTD {

	private final Status.Builder RESPONSE;

	public ServerMOTD() {
		this.RESPONSE = Status.builder();
	}

	public ServerMOTD setMOTDFavicon(byte[] b) {
		this.RESPONSE.favicon(b);
		return this;
	}

	public ServerMOTD setMOTDOnline(int online, int max) {
		this.RESPONSE.playerInfo(online, max);
		return this;
	}

	public ServerMOTD setMOTDPlayerInfo(Status.PlayerInfo playerInfo) {
		this.RESPONSE.playerInfo(playerInfo);
		return this;
	}

	public ServerMOTD setMOTDVersion(String s) {
		this.RESPONSE.versionInfo(new Status.VersionInfo(s, Status.VersionInfo.DEFAULT.protocolVersion()));
		return this;
	}

	public ServerMOTD setMOTDVersion(String s, int protocolVersion) {
		this.RESPONSE.versionInfo(new Status.VersionInfo(s, protocolVersion));
		return this;
	}

	@SuppressWarnings("deprecation")
	public ServerMOTD setMOTDDescription(String s) {
		this.RESPONSE.description(Component.text(s));
		return this;
	}

	public ServerMOTD setMOTDDescription(Component c) {
		this.RESPONSE.description(c);
		return this;
	}

	public Status get() {
		return this.RESPONSE.build();
	}

	public Status.Builder getRaw() {
		return this.RESPONSE;
	}

}
