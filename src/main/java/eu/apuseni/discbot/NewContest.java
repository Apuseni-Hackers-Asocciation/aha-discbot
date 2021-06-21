package eu.apuseni.discbot;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

public class NewContest extends AbstractAhaCommand {

	private int id = 1;

	public NewContest() {
		super("NewContest", "Creates new contest; Usage: !aha new <contest name>", "new");
	}

	@Override
	public void execute(Message message) {
		String content = message.getContent();
		String[] cmps = content.split("\\s+");
		int id = save(cmps[2]);
		MessageChannel channel = message.getChannel().block();
		channel.createMessage(String.format("concrsul %s a fost salvat cu ID %d", cmps[2], id)).block();
	}

	@Override
	public boolean test(String content) {
		String[] cmps = content.split("\\s+");
		if (cmps.length < 3) {
			return false;
		}

		return cmps[0].equalsIgnoreCase("!aha") && cmps[1].equalsIgnoreCase(this.getText());
	}

	private int save(String contestName) {
		return id++;
	}

}
