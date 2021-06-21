package eu.apuseni.discbot;

public abstract class AbstractAhaCommand implements AhaCommand {

	private final String name;
	private final String helpMessage;

	public AbstractAhaCommand(String name, String helpMessage) {
		this.name = name;
		this.helpMessage = helpMessage;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getHelp() {
		return helpMessage;
	}

}
