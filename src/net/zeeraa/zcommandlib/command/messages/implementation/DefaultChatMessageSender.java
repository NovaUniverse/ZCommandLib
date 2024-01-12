package net.zeeraa.zcommandlib.command.messages.implementation;

import org.bukkit.command.CommandSender;

import net.zeeraa.zcommandlib.command.base.ZCommandBase;
import net.zeeraa.zcommandlib.command.messages.IMessageSender;

public class DefaultChatMessageSender implements IMessageSender {
	@Override
	public void sendMessage(ZCommandBase command, CommandSender sender, String message) {
		sender.sendMessage(message);
	}
}