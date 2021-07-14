package eu.apuseni.discbot;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

public class NewContest extends AbstractAhaCommand {

	private final AhaManager manager;

	public NewContest(AhaManager ahaMan) {
		super("new", "!aha new <contest_name>", "Creates new contest");
		this.manager = ahaMan;
	}

	@Override
	public void execute(Message message) {
		String content = message.getContent();
		String[] cmps = content.split("\\s+");
		MessageChannel channel = message.getChannel().block();
		if (cmps.length >= 3) {
			long id = manager.createContest(cmps[2], message.getAuthor().get().getUsername());
			channel.createMessage(String.format("Contest %s saved with ID %d", cmps[2], id)).block();
		} else {
			channel.createMessage("The contest must have a name.").block();
		}
	}

}
