package eu.apuseni.discbot;

import java.util.HashMap;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

public class Help extends AbstractAhaCommand {
	private HashMap<String, String> man = new HashMap<String, String>();

	public Help() {
		super("CmdHelp", "Display details and syntax of command; Usage: !aha help <command>", "help");
		man.put(this.getText(), this.getHelp());
	}

	public void register(AbstractAhaCommand com) {
		man.put(com.getText(), com.getHelp());
	}

	@Override
	public void execute(Message message) {
		String content = message.getContent();
		String[] cmps = content.split("\\s+");
		MessageChannel channel = message.getChannel().block();
		if (man.containsKey(cmps[2])) {
			channel.createMessage(String.format("%s: %s", cmps[2], man.get(cmps[2]))).block();
		} else {
			channel.createMessage(String.format("Command %s not found", cmps[2])).block();
		}
	}

	@Override
	public boolean test(String content) {
		String[] cmps = content.split("\\s+");
		if (cmps.length < 3) {
			return false;
		}
		return cmps[0].equalsIgnoreCase("!aha") && cmps[1].equalsIgnoreCase(this.getText());
	}

}
