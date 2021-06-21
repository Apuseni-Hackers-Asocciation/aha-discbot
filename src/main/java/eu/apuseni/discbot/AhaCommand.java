package eu.apuseni.discbot;

import java.util.function.Predicate;

import discord4j.core.object.entity.Message;

public interface AhaCommand extends Predicate<String> {
	String getName();

	String getHelp();

	void execute(Message message);

}
