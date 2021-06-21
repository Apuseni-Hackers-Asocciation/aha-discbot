package eu.apuseni.discbot;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

public class Version extends AbstractAhaCommand {

	public Version() {
		super("Version", "Check both version");
	}

	@Override
	public void execute(Message message) {
		MessageChannel channel = message.getChannel().block();
		channel.createMessage("Version 1").block();
	}

	@Override
	public boolean test(String content) {
		String[] cmps = content.split("\\s+");
		if (cmps.length < 2) {
			return false;
		}
		return cmps[0].equalsIgnoreCase("!aha") && cmps[1].startsWith("vers");
	}

}
