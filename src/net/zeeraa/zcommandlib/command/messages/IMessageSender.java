package net.zeeraa.zcommandlib.command.messages;

import org.bukkit.command.CommandSender;

import net.zeeraa.zcommandlib.command.base.ZCommandBase;

public interface IMessageSender {
	void sendMessage(ZCommandBase command, CommandSender sender, String message);
}