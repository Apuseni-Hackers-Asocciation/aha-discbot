package eu.apuseni.discbot;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

public class Version extends AbstractAhaCommand {

	public Version() {
		super("version", "!aha version)", "Displays bot version");
	}

	@Override
	public void execute(Message message) {
		MessageChannel channel = message.getChannel().block();
		channel.createMessage("Version 1.1").block();
	}

}
