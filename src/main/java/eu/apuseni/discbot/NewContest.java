package eu.apuseni.discbot;

import static org.tinylog.Logger.debug;
import static org.tinylog.Logger.error;
import static org.tinylog.Logger.info;

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
			try {
				debug("Got a new contest command {}", message);
				String username = message.getAuthor().get().getUsername();
				long id = manager.createContest(cmps[2], username);
				channel.createMessage(String.format("Contest %s saved with ID %d", cmps[2], id)).block();
				info("Contest with id {} created succesfully by user {}", id, username);
			} catch (Exception ex) {
				error(ex, "Failed to create contest");
				channel.createMessage(String.format("Contest cannot be created. Please try later.")).block();
			}
		} else {
			channel.createMessage("The contest must have a name.").block();
		}
	}

}
