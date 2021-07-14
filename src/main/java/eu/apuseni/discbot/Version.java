package eu.apuseni.discbot;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

public class Version extends AbstractAhaCommand {

	public Version() {
		super("version", "!aha ver(sion)", "Displays bot version");
	}

	@Override
	public void execute(Message message) {
		MessageChannel channel = message.getChannel().block();
		channel.createMessage("Version 1.1").block();
	}

	@Override
	protected boolean alternateFilter(String[] cmps) {
		return cmps[1].regionMatches(true, 0, "version", 0, 3);
	}
}
