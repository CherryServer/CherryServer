package delta.cion.tokyo.server.command;

import delta.cion.tokyo.api.ServerBranding;
import delta.cion.tokyo.api.command.DeltaCommand;
import delta.cion.tokyo.api.locales.Localize;
import delta.cion.tokyo.api.permission.PermissionManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;

public class VersionCommand extends DeltaCommand {

	public VersionCommand() {
		super(new Command("version"));
		getCommand().addSyntax(this::execute);
	}

	private void execute(CommandSender sender, CommandContext context) {
		boolean isPlayer = sender instanceof Player;

		if (isPlayer && !PermissionManager.hasPermission(sender, "server.version")) {
			sender.sendMessage(Localize.getTranslate("no-permission", getCommand().getName())); return; }

		boolean isDebug = isPlayer && PermissionManager.hasPermission(sender, "server.debug");

		AnswerProvider answerProvider = new AnswerProvider();
		String version = ServerBranding.getServerVersion();

		String s = "Server started on: %s".formatted(version);

		if (isPlayer) sender.sendMessage(answerProvider.buildMessage(isDebug, s));
		else sender.sendMessage(answerProvider.buildStringMessage(s));
	}
}
