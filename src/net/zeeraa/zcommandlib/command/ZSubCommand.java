package net.zeeraa.zcommandlib.command;

import net.zeeraa.zcommandlib.command.base.ZCommandBase;
/**
 * Represents a sub command that can be added to a {@link ZSubCommand}
 * <p>
 * See {@link ZCommandBase} for function documentation
 * 
 * @author Zeeraa
 */
public abstract class ZSubCommand extends ZCommandBase {
	public ZSubCommand(String name) {
		super(name);
	}
}