package eu.apuseni.discbot;

import static org.tinylog.Logger.debug;
import static org.tinylog.Logger.error;
import static org.tinylog.Logger.info;
import static org.tinylog.Logger.warn;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import discord4j.core.object.entity.Attachment;
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
				if (message.getAttachments().size() != 1) {
					warn("Problem had no attachment{}", message);
					channel.createMessage(String.format("Problem must have an attachment!"));
					return;
				}
				Attachment attach = message.getAttachments().iterator().next();
				String attUrl = attach.getUrl();
				String localFilePath = downloadFromUrl(new URL(attUrl), attach.getFilename());
				int extid = attach.getFilename().lastIndexOf('.');
				String ext;
				if (extid != -1) {
					ext = attach.getFilename().substring(extid + 1);
				} else {
					warn("No file extension; defaulting to zip");
					ext = "zip";
				}
				boolean result = manager.addProblem(Integer.parseInt(cmps[2]), cmps[3] + "." + ext, localFilePath);
				if (result == true) {
					channel.createMessage(String.format("Problem %s added to contest %s", cmps[3], cmps[2])).block();
					info("Problem {} added to contest {}", cmps[3], cmps[2]);
				} else {
					channel.createMessage(String.format("Contest %s does not exist", cmps[2])).block();
					info("Contest {} does not exist", cmps[2]);
				}
			} catch (Exception ex) {
				if (ex instanceof NumberFormatException) {
					error(ex, "Malformed ID received");
					channel.createMessage(
							String.format("%s is not a valid contest ID. The ID must be an integer.", cmps[2])).block();
				} else {
					error(ex, "Failed to add problem");
					channel.createMessage(String.format("The problem cannot be added. Try again later.", cmps[2]))
							.block();
				}
			}
		} else if (cmps.length == 3) {
			channel.createMessage("The problem must have a name.").block();
		} else {
			channel.createMessage("You must specify a contest ID.").block();
		}
	}

	private static String downloadFromUrl(URL url, String localFilename) throws IOException {
		File temp = File.createTempFile(localFilename, ".tmp");
		String outputPath = temp.getAbsolutePath();
		URLConnection urlConn = url.openConnection();
		try (InputStream is = new BufferedInputStream(urlConn.getInputStream());
				FileOutputStream fos = new FileOutputStream(outputPath);) {
			byte[] buffer = new byte[4096];
			int length = 0;
			while ((length = is.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
		}
		return outputPath;
	}

}
