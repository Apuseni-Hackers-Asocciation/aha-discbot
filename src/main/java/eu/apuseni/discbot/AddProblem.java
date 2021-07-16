package eu.apuseni.discbot;

import static org.tinylog.Logger.debug;
import static org.tinylog.Logger.error;
import static org.tinylog.Logger.info;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

public class AddProblem extends AbstractAhaCommand {

	private final AhaManager manager;

	public AddProblem(AhaManager ahaMan) {
		super("add", "!aha add <contest_id> <problem_name>", "Add problem to contest");
		this.manager = ahaMan;
	}

	@Override
	public void execute(Message message) {
		String content = message.getContent();
		String[] cmps = content.split("\\s+");
		MessageChannel channel = message.getChannel().block();
		if (cmps.length >= 4) {
			try {
				debug("Got a new problem command {}", message);
				boolean result = manager.addProblem(Integer.parseInt(cmps[2]), cmps[3]);
				if (result == true) {
					channel.createMessage(String.format("Problem %s added to contest %s", cmps[3], cmps[2])).block();
					info("Problem {} added to contest {}", cmps[3], cmps[2]);
				} else {
					channel.createMessage(String.format("Contest %s already has problem '%s'", cmps[2], cmps[3]))
							.block();
					info("Contest {} already has problem '{}'", cmps[2], cmps[3]);
				}
			} catch (Exception ex) {
				if (ex instanceof NumberFormatException) {
					error(ex, "Malformed ID received");
					channel.createMessage(
							String.format("%s is not a valid contest ID. The ID must be an integer.", cmps[2])).block();
				} else {
					error(ex, "Failed to add problem");
					channel.createMessage(String.format(
							"The problem cannot be added. Make sure that the contest with ID %s exists.", cmps[2]))
							.block();
				}
			}
		} else if (cmps.length == 3) {
			channel.createMessage("The problem must have a name.").block();
		} else {
			channel.createMessage("You must specify a contest ID.").block();
		}
	}

}
