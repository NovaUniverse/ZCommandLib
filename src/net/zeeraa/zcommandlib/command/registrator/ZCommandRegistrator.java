package net.zeeraa.zcommandlib.command.registrator;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import net.zeeraa.zcommandlib.command.ZCommand;
import net.zeeraa.zcommandlib.command.ZSubCommand;
import net.zeeraa.zcommandlib.command.base.ZCommandBase;
import net.zeeraa.zcommandlib.command.proxy.ZCommandProxy;
import net.zeeraa.zcommandlib.command.utils.PermissionRegistrator;

public class ZCommandRegistrator {
	/**
	 * Register a {@link ZCommand}
	 * 
	 * @param command {@link ZCommand} to be registered
	 * @param plugin  The {@link Plugin} that owns the command
	 * @return <code>true</code> on success
	 */
	public static boolean registerCommand(Plugin plugin, ZCommand command) {
		registerCommandPermissions(command);
		ZCommandProxy commandProxy = new ZCommandProxy(command);
		try {
			String version = plugin.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
			String name = "org.bukkit.craftbukkit." + version + ".CraftServer";
			Class<?> craftserver = null;
			craftserver = Class.forName(name);

			SimpleCommandMap scm = (SimpleCommandMap) craftserver.cast(plugin.getServer()).getClass().getMethod("getCommandMap").invoke(plugin.getServer());
			
			scm.register(plugin.getName(), commandProxy);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void registerCommand(Plugin plugin, Class<? extends ZCommand> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		registerCommand(plugin, clazz.getConstructor().newInstance());
	}

	private static void registerCommandPermissions(ZCommandBase command) {
		if (command.hasPermission()) {
			PermissionRegistrator.registerPermission(command.getPermission(), command.getPermissionDescription(), command.getPermissionDefaultValue());
		}

		for (ZSubCommand subCommand : command.getSubCommands()) {
			registerCommandPermissions(subCommand);
		}
	}
}