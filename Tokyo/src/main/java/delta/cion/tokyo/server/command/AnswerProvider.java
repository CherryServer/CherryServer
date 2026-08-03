package delta.cion.tokyo.server.command;

import delta.cion.tokyo.api.ServerBranding;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class AnswerProvider {

	private static final MiniMessage miniMessage = MiniMessage.miniMessage();

	private static final String serverName = ServerBranding.getBrandName();
	private static final String serverVersion = ServerBranding.getServerVersion();

	private static final String baseString = "%s[%s%s%s] ";
	private static final String coloredString =
		baseString.formatted(
			"<dark_gray>",
			"<#ffe4c4>",
			serverName,
			"<dark_gray>"
		);

	public AnswerProvider() {}

	private static final Component baseComponent = miniMessage.deserialize(coloredString);

	public static String getAnswerString() {
		return baseString.formatted("", "", serverName, "");
	}

	public static Component getAnswerComponent() {
		return baseComponent;
	}

	public static Component getAnswerComponent(boolean withDebugInfo) {
		Component version = Component.text(serverVersion);

		if (withDebugInfo) return baseComponent.hoverEvent(version);
		return baseComponent;
	}

	public String buildStringMessage(String... content) {
		String base = getAnswerString();

		if (content == null || content.length == 0) return base;
		return base + String.join(" ", content);
	}


	public Component buildMessage(String... content) {
		return buildMessage(false, content);
	}

	public Component buildMessage(boolean withDebugInfo, String... content) {
		Component base = getAnswerComponent(withDebugInfo);
		if (content == null || content.length == 0) return base;

		String joined = String.join(" ", content);
		Component c = miniMessage.deserialize("<white>"+joined);

		return base.append(Component.space()).append(c);
	}

}
