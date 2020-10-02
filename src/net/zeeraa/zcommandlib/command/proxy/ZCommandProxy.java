package net.zeeraa.zcommandlib.command.proxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.util.StringUtil;

import net.zeeraa.zcommandlib.command.ZCommand;
import net.zeeraa.zcommandlib.command.ZSubCommand;
import net.zeeraa.zcommandlib.command.base.ZCommandBase;

/**
 * This class is used to turn {@link ZCommand} into a normal bukkit
 * {@link Command}
 * 
 * @author Zeeraa
 */
public class ZCommandProxy extends Command {
	private ZCommand zCommand;

	/**
	 * Create a {@link ZCommandProxy} for a {@link ZCommand}
	 * 
	 * @param zCommand The {@link ZCommand} being proxied
	 */
	public ZCommandProxy(ZCommand zCommand) {
		super(zCommand.getName());

		this.zCommand = zCommand;

		this.setAliases(zCommand.getAliases());
		this.setDescription(zCommand.getDescription());
	}

	/**
	 * Get the {@link ZCommand} being proxied by this class
	 * 
	 * @return {@link ZCommand} instance
	 */
	public ZCommand getCommandBase() {
		return zCommand;
	}

	/**
	 * Get the name of the {@link ZCommand} being proxied by this class
	 * 
	 * @return name of the {@link ZCommand}
	 */
	public String getRealName() {
		return zCommand.getName();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		return recursiceCommandExecutionCheck(zCommand, sender, commandLabel, args);
	}

	private boolean recursiceCommandExecutionCheck(ZCommandBase command, CommandSender sender, String commandLabel, String[] args) {
		if (args.length > 0) {
			// Scan sub commands
			for (ZCommandBase subCommand : command.getSubCommands()) {
				boolean isMatching = false;
				if (subCommand.getName().equalsIgnoreCase(args[0])) {
					isMatching = true;
				} else {
					for (String alias : subCommand.getAliases()) {
						if (alias.equalsIgnoreCase(args[0])) {
							isMatching = true;
							break;
						}
					}
				}

				if (isMatching) {
					// remove first argument to prevent the sub command name to be the first entry
					// in arguments
					String[] newArgs = new String[args.length - 1];
					for (int i = 1; i < args.length; i++) {
						newArgs[i - 1] = args[i];
					}

					return recursiceCommandExecutionCheck(subCommand, sender, commandLabel, newArgs);
				}
			}
			// No sub command exists. Execute the current command
		}

		if (!(sender instanceof ConsoleCommandSender)) {
			if (!command.hasSenderPermission(sender)) {
				sender.sendMessage(command.getNoPermissionMessage());
				return false;
			}
		}

		if (!command.getAllowedSenders().isAllowed(sender)) {
			sender.sendMessage(command.getAllowedSenders().getErrorMessage());
			return false;
		}

		return command.execute(sender, commandLabel, args);
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		return recursiceCommandTabCheck(zCommand, sender, alias, args);
	}

	private List<String> recursiceCommandTabCheck(ZCommandBase command, CommandSender sender, String alias, String[] args) {
		if (args.length == 1) {
			if (!command.hasSenderPermission(sender)) {
				return new ArrayList<String>();
			}

			List<String> result = new ArrayList<>();

			List<String> commandResults = command.tabComplete(sender, alias, args);

			String lastWord = args[args.length - 1];

			if (command.isFilterAutocomplete()) {
				// Filter
				Collections.sort(commandResults, String.CASE_INSENSITIVE_ORDER);
				for (String string : commandResults) {
					if (StringUtil.startsWithIgnoreCase(string, lastWord)) {
						result.add(string);
					}
				}
			} else {
				// Do not filter
				result.addAll(commandResults);
			}

			// Find aliases for sub commands
			ArrayList<String> matchedSubCommands = new ArrayList<String>();
			
			for (ZSubCommand subCommand : command.getSubCommands()) {
				if (StringUtil.startsWithIgnoreCase(subCommand.getName(), lastWord)) {
					matchedSubCommands.add(subCommand.getName());
				}

				for (String subCommandAlias : subCommand.getAliases()) {
					if (StringUtil.startsWithIgnoreCase(subCommandAlias, lastWord)) {
						matchedSubCommands.add(subCommandAlias);
					}
				}
			}

			Collections.sort(matchedSubCommands, String.CASE_INSENSITIVE_ORDER);

			result.addAll(0, matchedSubCommands);
			
			return result;
		} else {
			// Recursive check for sub commands
			if (args.length >= 2) {
				for (ZSubCommand subCommand : command.getSubCommands()) {
					boolean isMatching = false;
					String newAlias = "";

					if (subCommand.getName().equalsIgnoreCase(args[0])) {
						isMatching = true;
						newAlias = subCommand.getName();
					} else {
						for (String subCommandAlias : subCommand.getAliases()) {
							if (subCommandAlias.equalsIgnoreCase(args[0])) {
								isMatching = true;
								newAlias = subCommandAlias;
								break;
							}
						}
					}

					if (isMatching) {
						// remove first argument to prevent the sub command name to be the first entry
						// in arguments
						String[] newArgs = new String[args.length - 1];
						for (int i = 1; i < args.length; i++) {
							newArgs[i - 1] = args[i];
						}

						return recursiceCommandTabCheck(subCommand, sender, newAlias, newArgs);
					}
				}
			}

			// No matching sub commands found, Check permission
			if (!command.hasSenderPermission(sender)) {
				return new ArrayList<String>();
			}

			// No matching sub commands found, Return the tab value of this command
			return command.tabComplete(sender, alias, args);
		}
	}
}