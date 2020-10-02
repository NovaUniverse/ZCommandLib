package net.zeeraa.zcommandlib.command.help;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.zcommandlib.command.ZSubCommand;
import net.zeeraa.zcommandlib.command.base.ZCommandBase;

/**
 * This is a command that can be added to show all sub commands and their
 * description for a command
 * 
 * @author Zeeraa
 */
public class HelpSubCommand extends ZSubCommand {
	public HelpSubCommand() {
		super("help");
		setDescription("Show help");
	}

	public HelpSubCommand(String permission, String permissionDescription, PermissionDefault permissionDefaultValue) {
		super("help");
		setDescription("Show help");
		setPermission(permission);
		setPermissionDescription(permissionDescription);
		setPermissionDefaultValue(permissionDefaultValue);

		setFilterAutocomplete(false);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		String result = "";

		result += getCommandHelpText(this.getParentCommand()) + "\n";
		for (ZSubCommand command : this.getParentCommand().getSubCommands()) {
			if (sender.hasPermission(command.getPermission()) || sender.isOp()) {
				result += getCommandHelpText(command) + "\n";
			}
		}

		sender.sendMessage(result);

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		return new ArrayList<>();
	}

	private String getCommandHelpText(ZCommandBase command) {
		if (command.getHelpString() != null) {
			return ChatColor.GOLD + command.getHelpString();
		}
		return ChatColor.GOLD + getFullCommandLable(command) + ChatColor.AQUA + " " + command.getDescription();
	}

	private String getFullCommandLable(ZCommandBase command) {
		if (command.hasParentCommand()) {
			return getFullCommandLable(command.getParentCommand()) + " " + command.getName();
		}

		return "/" + command.getName();
	}
}