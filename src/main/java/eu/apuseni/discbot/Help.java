package eu.apuseni.discbot;

import java.util.HashMap;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

public class Help extends AbstractAhaCommand {
	private final HashMap<String, AhaCommand> man = new HashMap<>();

	public Help() {
		super("help", "!aha help <command>", "Display details and syntax of command");
		register(this);
	}

	public void register(AbstractAhaCommand com) {
		man.put(com.getName(), com);
	}

	@Override
	public void execute(Message message) {
		String content = message.getContent();
		String[] cmps = content.split("\\s+");
		MessageChannel channel = message.getChannel().block();
		if (cmps.length < 3) {
			channel.createMessage("You must specify a command!").block();
		}
		if (man.containsKey(cmps[2])) {
			AhaCommand ahaCommand = man.get(cmps[2]);
			channel.createMessage(ahaCommand.toString()).block();
		} else {
			channel.createMessage(String.format("Command %s not found", cmps[2])).block();
		}
	}

}
