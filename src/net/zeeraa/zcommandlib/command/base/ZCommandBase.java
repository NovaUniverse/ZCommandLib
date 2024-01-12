package net.zeeraa.zcommandlib.command.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

import net.zeeraa.zcommandlib.command.ZCommand;
import net.zeeraa.zcommandlib.command.ZSubCommand;
import net.zeeraa.zcommandlib.command.help.HelpSubCommand;
import net.zeeraa.zcommandlib.command.messages.IMessageSender;
import net.zeeraa.zcommandlib.command.messages.implementation.DefaultChatMessageSender;
import net.zeeraa.zcommandlib.command.utils.AllowedSenders;

/**
 * This class is the shared code between {@link ZCommand} and
 * {@link ZSubCommand}
 * 
 * @author Zeeraa
 */
public abstract class ZCommandBase {
	private ZCommandBase parentCommand;
	private List<ZSubCommand> subCommands;

	private AllowedSenders allowedSenders;

	private String permission;
	private String permissionDescription;
	private PermissionDefault permissionDefaultValue;
	private boolean requireOp;

	private String description;
	private String helpString;

	private String name;
	private List<String> aliases;

	private boolean filterAutocomplete;

	private boolean emptyTabMode;

	private String noPermissionMessage;

	private IMessageSender noPermissionMessageSender;
	private IMessageSender disallowedSenderMessageSender;

	public ZCommandBase(String name) {
		this.name = name;
		this.description = "";
		this.permission = null;
		this.permissionDescription = "";
		this.permissionDefaultValue = PermissionDefault.FALSE;
		this.requireOp = false;
		this.subCommands = new ArrayList<ZSubCommand>();
		this.allowedSenders = AllowedSenders.ALL;
		this.parentCommand = null;
		this.helpString = null;

		this.aliases = new ArrayList<String>();

		this.emptyTabMode = false;

		this.filterAutocomplete = false;

		this.noPermissionMessage = ChatColor.RED + "You dont have permission to use this command";

		this.noPermissionMessageSender = new DefaultChatMessageSender();
		this.disallowedSenderMessageSender = new DefaultChatMessageSender();
	}

	/**
	 * Get all sub commands
	 * 
	 * @return {@link List} with all sub commands for this command
	 */
	public List<ZSubCommand> getSubCommands() {
		return subCommands;
	}

	/**
	 * Get aliases for this command
	 * 
	 * @return {@link List} with aliases
	 */
	public List<String> getAliases() {
		return aliases;
	}

	/**
	 * FSet the aliases for this command.
	 * {@link ZCommandBase#generateAliasList(String...)} can be used to create the
	 * list in one line
	 * 
	 * @param aliases {@link List} with aliases
	 */
	protected void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	/**
	 * Get command description
	 * 
	 * @return Description for this command
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the string that shows in the help command
	 * <p>
	 * If not set the command will be shown instead
	 * 
	 * @param helpString The help string
	 */
	public void setHelpString(String helpString) {
		this.helpString = helpString;
	}

	/**
	 * Get the string that shows in the help command
	 * <p>
	 * if this is null the command will be shown instead
	 * 
	 * @return Help string
	 */
	public String getHelpString() {
		return helpString;
	}

	/**
	 * Set the description for this command
	 * 
	 * @param description command description
	 */
	protected void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the permission that this command requires. if null and
	 * {@link ZCommandBase#isRequireOp()} is false the command can be used by
	 * anyone.
	 * 
	 * @return permission for this command
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * Set the permission for this command. if null and
	 * {@link ZCommandBase#isRequireOp()} is false the command can be used by
	 * anyone.
	 * 
	 * @param permission permission for this command
	 */
	protected void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * Check if this command has a permission set
	 * 
	 * @return <code>true</code> is permission is not null
	 */
	public boolean hasPermission() {
		return permission != null;
	}

	/**
	 * Get the permission description
	 * 
	 * @return permission description
	 */
	public String getPermissionDescription() {
		return permissionDescription;
	}

	/**
	 * Set the permission description
	 * 
	 * @param permissionDescription permission description
	 */
	protected void setPermissionDescription(String permissionDescription) {
		this.permissionDescription = permissionDescription;
	}

	/**
	 * Get the default value for the permission
	 * 
	 * @return {@link PermissionDefault}
	 */
	public PermissionDefault getPermissionDefaultValue() {
		return permissionDefaultValue;
	}

	/**
	 * Set the default permission value for the permisison
	 * 
	 * @param permissionDefaultValue {@link PermissionDefault}
	 */
	protected void setPermissionDefaultValue(PermissionDefault permissionDefaultValue) {
		this.permissionDefaultValue = permissionDefaultValue;
	}

	/**
	 * Set if the command requires of. if a permission is set and this is
	 * <code>true</code> the player can have either op or the permission to be able
	 * to use this command
	 * 
	 * @param requireOp <code>true</code> if the command requires op
	 */
	protected void setRequireOp(boolean requireOp) {
		this.requireOp = requireOp;
	}

	/**
	 * Check if the command requires of. if a permission is set and this is
	 * <code>true</code> the player can have either op or the permission to be able
	 * to use this command
	 * 
	 * @return <code>true</code> if the command requires op
	 */
	public boolean isRequireOp() {
		return requireOp;
	}

	/**
	 * true to not use any tab auto complete
	 * <p>
	 * This wont disable tab complete for sub commands
	 * <p>
	 * This will only work if the command does not include the
	 * {@link ZCommandBase#tabComplete(CommandSender, String, String[])} function
	 * 
	 * @return <code>true</code> if auto complete is disabled
	 */
	public boolean isEmptyTabMode() {
		return emptyTabMode;
	}

	/**
	 * Set to <code>true</code> to not use any tab auto complete
	 * <p>
	 * This wont disable tab complete for sub commands
	 * <p>
	 * This will only work if the command does not include the
	 * {@link ZCommandBase#tabComplete(CommandSender, String, String[])} function
	 * 
	 * @param emptyTabMode <code>true</code> to disable auto complete
	 */
	public void setEmptyTabMode(boolean emptyTabMode) {
		this.emptyTabMode = emptyTabMode;
	}

	/**
	 * Add the help sub command to this command
	 */
	protected void addHelpSubCommand() {
		this.addHelpSubCommand(null);
	}

	/**
	 * Add the help sub command to this command
	 * 
	 * @param permission permission for the help sub command. Default is the same as
	 *                   the parent
	 */
	protected void addHelpSubCommand(String permission) {
		this.addHelpSubCommand(permission, PermissionDefault.FALSE);
	}

	/**
	 * Add the help sub command to this command
	 * 
	 * @param permission             permission for the help sub command. Default is
	 *                               the same as the parent
	 * @param permissionDefaultValue the {@link PermissionDefault} to use
	 */
	protected void addHelpSubCommand(String permission, PermissionDefault permissionDefaultValue) {
		this.addHelpSubCommand(permission, "Show help", permissionDefaultValue);
	}

	/**
	 * Add the help sub command to this command
	 * 
	 * @param permission            permission for the help sub command. Default is
	 *                              the same as the parent
	 * @param permissionDescription the permission description
	 */
	protected void addHelpSubCommand(String permission, String permissionDescription) {
		this.addHelpSubCommand(permission, permissionDescription, PermissionDefault.FALSE);
	}

	/**
	 * Add the help sub command to this command
	 * 
	 * @param permission             permission for the help sub command. Default is
	 *                               the same as the parent
	 * @param permissionDescription  the permission description
	 * @param permissionDefaultValue the {@link PermissionDefault} to use
	 */
	protected void addHelpSubCommand(String permission, String permissionDescription, PermissionDefault permissionDefaultValue) {
		HelpSubCommand helpSubCommand = new HelpSubCommand(permission, permissionDescription, permissionDefaultValue);

		helpSubCommand.setRequireOp(this.isRequireOp());

		if (permission == null) {
			helpSubCommand.setPermission(this.getPermission());

			helpSubCommand.setPermissionDefaultValue(this.getPermissionDefaultValue());
			helpSubCommand.setPermissionDescription(this.getPermissionDescription());
		}

		addSubCommand(helpSubCommand);
	}

	/**
	 * Check if auto complete list filtering is enabled. This is disabled by
	 * default.
	 * <p>
	 * If enabled the tab auto complete will ignore values that does not match the
	 * current text the user is typing
	 * 
	 * @return <code>true</code> if enabled
	 */
	public boolean isFilterAutocomplete() {
		return filterAutocomplete;
	}

	/**
	 * Enable or disable filtering the auto complete list. This is disabled by
	 * default.
	 * <p>
	 * If enabled the tab auto complete will ignore values that does not match the
	 * current text the user is typing
	 * 
	 * @param filterAutocomplete <code>true</code> to filter the auto complete list
	 */
	public void setFilterAutocomplete(boolean filterAutocomplete) {
		this.filterAutocomplete = filterAutocomplete;
	}

	/**
	 * Executes the command, returning its success
	 *
	 * @param sender       Source object which is executing this command
	 * @param commandLabel The alias of the command used
	 * @param args         All arguments passed to the command, split via ' '
	 * @return true if the command was successful, otherwise false
	 */
	public abstract boolean execute(CommandSender sender, String commandLabel, String[] args);

	/**
	 * Executed on tab completion for this command, returning a list of options the
	 * player can tab through.
	 * <p>
	 * {@link ZSubCommand#setFilterAutocomplete(boolean)} can be set to true to
	 * filter the results using
	 * {@link StringUtil#startsWithIgnoreCase(String, String)}
	 *
	 * @param sender Source object which is executing this command
	 * @param alias  the alias being used
	 * @param args   All arguments passed to the command, split via ' '
	 * @return a list of tab-completions for the specified arguments. This will
	 *         never be null. List may be immutable.
	 * @throws IllegalArgumentException if sender, alias, or args is null
	 */
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		if (emptyTabMode) {
			return new ArrayList<String>();
		}

		if (args.length == 0) {
			return ImmutableList.of();
		}

		String lastWord = args[args.length - 1];

		Player senderPlayer = sender instanceof Player ? (Player) sender : null;

		ArrayList<String> matchedPlayers = new ArrayList<String>();
		for (Player player : sender.getServer().getOnlinePlayers()) {
			String name = player.getName();
			if ((senderPlayer == null || senderPlayer.canSee(player)) && StringUtil.startsWithIgnoreCase(name, lastWord)) {
				matchedPlayers.add(name);
			}
		}

		Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER);
		return matchedPlayers;
	}

	/**
	 * Add a {@link ZSubCommand}
	 * 
	 * @param subCommand {@link ZSubCommand} to be added
	 */
	protected void addSubCommand(ZSubCommand subCommand) {
		subCommands.add(subCommand);
		subCommand.setParentCommand(this);
	}

	/**
	 * Check if this command has a parent command. This should always be
	 * <code>false</code> for {@link ZCommand} and should always be
	 * <code>true</code> for {@link ZSubCommand}
	 * 
	 * @return <code>true</code> if a parent command exists
	 */
	public boolean hasParentCommand() {
		return parentCommand != null;
	}

	/**
	 * Set the parent command. After the parent command has been set all other calls
	 * to this function will be ignored
	 * 
	 * @param parentCommand {@link ZCommandBase} instance of the parent command
	 */
	protected void setParentCommand(ZCommandBase parentCommand) {
		if (this.parentCommand != null) {
			return;
		}

		this.parentCommand = parentCommand;
	}

	/**
	 * Get the parent command for this command. This only works on
	 * {@link ZSubCommand}
	 * 
	 * @return the {@link ZCommandBase} for the parent command or null if the
	 *         command has no parents
	 */
	public ZCommandBase getParentCommand() {
		return parentCommand;
	}

	/**
	 * Get the name of this command
	 * 
	 * @return command name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Create a {@link List} containing all aliases from strings
	 * 
	 * @param aliases The aliases for the command
	 * @return {@link List} with aliases
	 */
	public static List<String> generateAliasList(String... aliases) {
		List<String> result = new ArrayList<>();

		for (String alias : aliases) {
			result.add(alias);
		}

		return result;
	}

	/**
	 * Get the message that is shown to {@link CommandSender} that does not have
	 * permission
	 * 
	 * @return a {@link String} that will be sent to {@link CommandSender} without
	 *         permission
	 */
	public String getNoPermissionMessage() {
		return noPermissionMessage;
	}

	/**
	 * Set the message that is shown to {@link CommandSender} that does not have
	 * permission
	 * 
	 * @param noPermissionMessage a {@link String} that will be sent to
	 *                            {@link CommandSender} without permission
	 */
	protected void setNoPermissionMessage(String noPermissionMessage) {
		this.noPermissionMessage = noPermissionMessage;
	}

	/**
	 * Get the {@link IMessageSender} used when showing disallowed sender messages
	 * 
	 * @return {@link IMessageSender} responsible for disallowed sender message
	 */
	public IMessageSender getDisallowedSenderMessageSender() {
		return disallowedSenderMessageSender;
	}

	/**
	 * Get the {@link IMessageSender} used when showing no permission messages
	 * 
	 * @return {@link IMessageSender} responsible for no permission message
	 */
	public IMessageSender getNoPermissionMessageSender() {
		return noPermissionMessageSender;
	}

	/**
	 * Set the {@link IMessageSender} used for sending disallowed sender messages
	 * 
	 * @param disallowedSenderMessageSender The new {@link IMessageSender} to use
	 */
	protected void setDisallowedSenderMessageSender(IMessageSender disallowedSenderMessageSender) {
		this.disallowedSenderMessageSender = disallowedSenderMessageSender;
	}

	/**
	 * Set the {@link IMessageSender} used for sending no permission messages
	 * 
	 * @param disallowedSenderMessageSender The new {@link IMessageSender} to use
	 */
	protected void setNoPermissionMessageSender(IMessageSender noPermissionMessageSender) {
		this.noPermissionMessageSender = noPermissionMessageSender;
	}

	/**
	 * Check if a {@link CommandSender} has permission to use this command
	 * 
	 * @param sender the {@link CommandSender} to check
	 * @return <code>true</code> if allowed
	 */
	public boolean hasSenderPermission(CommandSender sender) {
		if (!hasPermission() && !isRequireOp()) {
			return true;
		}

		if (hasPermission()) {
			if (sender.hasPermission(this.getPermission())) {
				return true;
			}
		}

		if (this.isRequireOp()) {
			if (sender.isOp()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Get the allowed senders for this command
	 * 
	 * @return {@link AllowedSenders}
	 */
	public AllowedSenders getAllowedSenders() {
		return allowedSenders;
	}

	/**
	 * Set the allowed senders for this command
	 * 
	 * @param allowedSenders {@link AllowedSenders}
	 */
	protected void setAllowedSenders(AllowedSenders allowedSenders) {
		this.allowedSenders = allowedSenders;
	}
}