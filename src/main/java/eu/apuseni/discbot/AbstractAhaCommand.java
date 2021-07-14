package eu.apuseni.discbot;

public abstract class AbstractAhaCommand implements AhaCommand {

	private final String name;
	private final String description;
	private final String syntax;

	public AbstractAhaCommand(String name, String commandText, String description) {
		this.name = name;
		this.syntax = commandText;
		this.description = description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSyntax() {
		return syntax;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public final boolean test(String content) {
		String[] cmps = content.split("\\s+");
		if (cmps.length < 2) {
			return false;
		}
		return cmps[0].equalsIgnoreCase("!aha") && (cmps[1].equalsIgnoreCase(this.getName()) || alternateFilter(cmps))
				&& extraFilter(cmps);
	}

	protected boolean extraFilter(String[] cmps) {
		return true;
	}

	protected boolean alternateFilter(String[] cmps) {
		return false;
	}

	@Override
	public String toString() {
		return String.format("%s: %s. Syntax: %s", getName(), getDescription(), getSyntax());
	}
}
