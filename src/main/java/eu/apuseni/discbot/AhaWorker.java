package eu.apuseni.discbot;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

public class AhaWorker implements Runnable {
	private final SqsClient sqsClient;
	private final String url;

	public AhaWorker(SqsClient sqsClient, String url) {
		this.sqsClient = sqsClient;
		this.url = url;
	}

	@Override
	public void run() {
		ReceiveMessageRequest request = ReceiveMessageRequest.builder().queueUrl(url).maxNumberOfMessages(1)
				.waitTimeSeconds(20).build();
		while (true) {
			System.out.println("Getting messages...");
			sqsClient.receiveMessage(request).messages().forEach(msg -> {
				System.err.println(msg);
				DeleteMessageRequest delReq = DeleteMessageRequest.builder().queueUrl(url)
						.receiptHandle(msg.receiptHandle()).build();
				sqsClient.deleteMessage(delReq);
			});
		}
	}
}
