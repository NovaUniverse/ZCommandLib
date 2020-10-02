package net.zeeraa.zcommandlib.command;

import net.zeeraa.zcommandlib.command.base.ZCommandBase;
/**
 * Represents a command thats managed by the ZCommandLib system
 * <p>
 * See {@link ZCommandBase} for function documentation
 * 
 * @author Zeeraa
 */
public abstract class ZCommand extends ZCommandBase {
	public ZCommand(String name) {
		super(name);
	}
}