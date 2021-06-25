package eu.apuseni.discbot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

public class Help extends AbstractAhaCommand {
	private final HashMap<String, AhaCommand> man = new HashMap<>();

	public Help() {
		super("help", "!aha help <command>", "Display details and syntax of command");
		register(this);
	}

	public void register(AhaCommand com) {
		man.put(com.getName(), com);
	}

	@Override
	public void execute(Message message) {
		String content = message.getContent();
		String[] cmps = content.split("\\s+");
		MessageChannel channel = message.getChannel().block();
		if (cmps.length < 3) {
			channel.createMessage("You must specify a command!").block();
		} else {
			AhaCommand ahaCommand = man.get(cmps[2]);
			if (ahaCommand != null) {
				channel.createMessage(ahaCommand.toString()).block();
			} else if (cmps[2].equals("all")) {
				handleAll(channel);
			} else {
				channel.createMessage(String.format("Command %s not found", cmps[2])).block();
			}
		}
	}

	private void handleAll(MessageChannel channel) {
		List<String> commandNames = new ArrayList<>(man.keySet());
		Collections.sort(commandNames);
		StringBuilder ans = new StringBuilder();
		commandNames.forEach(cmd -> {
			ans.append(man.get(cmd)).append("\n");
		});
		channel.createMessage(ans.toString().trim()).block();
	}

}
