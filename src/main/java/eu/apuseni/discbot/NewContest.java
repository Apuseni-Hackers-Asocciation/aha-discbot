package eu.apuseni.discbot;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

public class NewContest extends AbstractAhaCommand {

	private int id = 1;

	public NewContest() {
		super("new", "!aha new <contest_name>", "Creates new contest");
	}

	@Override
	public void execute(Message message) {
		String content = message.getContent();
		String[] cmps = content.split("\\s+");
		int id = save(cmps[2]);
		MessageChannel channel = message.getChannel().block();
		channel.createMessage(String.format("Concursul %s a fost salvat cu Id %d", cmps[2], id)).block();
	}

	private int save(String contestName) {
		return id++;
	}

}
