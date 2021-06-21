package eu.apuseni.discbot;

import java.util.function.Predicate;

public abstract class AbstractAhaCommand implements AhaCommand {

	private final String name;
	private final String helpMessage;
	private final Predicate<String> filter;

	public AbstractAhaCommand(String name, String helpMessage, Predicate<String> filter) {
		this.name = name;
		this.helpMessage = helpMessage;
		this.filter = filter;
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
	public boolean test(String textMessage) {
		return filter.test(textMessage);
	}
}
