package eu.apuseni.discbot;

public abstract class AbstractAhaCommand implements AhaCommand {

	private final String name;
	private final String helpMessage;
	private final String commandText;

	public AbstractAhaCommand(String name, String helpMessage, String commandText) {
		this.name = name;
		this.helpMessage = helpMessage;
		this.commandText = commandText;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getHelp() {
		return helpMessage;
	}

	@Override
	public String getText() {
		return commandText;
	}
}
