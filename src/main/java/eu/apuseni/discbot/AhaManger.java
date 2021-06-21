package eu.apuseni.discbot;

import java.util.List;
import java.util.Optional;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.GetObjectTaggingResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest.Builder;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.Tag;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class AhaManger {
	private static final String PROBLEM_VALIDATION_QUEUE = "https://sqs.eu-central-1.amazonaws.com/204858712721/ProblemValidation";
	private static final String CONTESTS = "contests/";
	private static final RequestBody EMPTY_OBJECT = RequestBody.fromBytes(new byte[0]);
	private final S3Client s3;
	private final String rootBucketName;
	private final SqsClient sqs;

	public AhaManger(S3Client s3, String rootBucketName, SqsClient sqs) {
		this.s3 = s3;
		this.rootBucketName = rootBucketName;
		this.sqs = sqs;
	}

	private long nextSerquence() {
		GetObjectTaggingRequest seqNumber = GetObjectTaggingRequest.builder().bucket(rootBucketName).key(CONTESTS)
				.build();
		List<Tag> tagSet = s3.getObjectTagging(seqNumber).tagSet();
		Optional<Tag> seqTag = tagSet.stream().filter(tag -> tag.key().equals("sequence")).findFirst();
		long nextSq = 1;
		if (seqTag.isPresent()) {
			nextSq = Long.parseLong(seqTag.get().value());
		}
		PutObjectRequest request = PutObjectRequest.builder().bucket(rootBucketName).key(CONTESTS)
				.tagging("sequence=" + (nextSq + 1)).build();
		s3.putObject(request, EMPTY_OBJECT);
		return nextSq;
	}

	public long createContest(String contestName, String author) {
		long contestNumber = nextSerquence();
		String tagging = String.format("contestName=%s&author=%s", contestName, author);
		Builder putReq = PutObjectRequest.builder().bucket(rootBucketName).key(CONTESTS + contestNumber + "/")
				.tagging(tagging);
		PutObjectResponse response = s3.putObject(putReq.build(), EMPTY_OBJECT);
		System.out.println(response);
		return contestNumber;
	}

	public boolean contestExists(long contestNumber) {
		try {
			GetObjectTaggingRequest seqNumber = GetObjectTaggingRequest.builder().bucket(rootBucketName)
					.key(CONTESTS + contestNumber + "/").build();
			GetObjectTaggingResponse response = s3.getObjectTagging(seqNumber);
			System.out.println(response);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void addProblem(long contestNumber, String problemId) {
		if (contestExists(contestNumber)) {
			String key = CONTESTS + contestNumber + "/" + problemId;
			String tagging = String.format("validated=false");
			Builder putReq = PutObjectRequest.builder().bucket(rootBucketName).key(key).tagging(tagging);
			PutObjectResponse response = s3.putObject(putReq.build(), EMPTY_OBJECT);
			SendMessageRequest anounce = SendMessageRequest.builder().queueUrl(PROBLEM_VALIDATION_QUEUE)
					.messageBody(key).build();
			sqs.sendMessage(anounce);
		}
	}

	public boolean checkProblem(long contestNumber, String problemId) {
		try {
			String key = CONTESTS + contestNumber + "/" + problemId;
			GetObjectTaggingRequest seqNumber = GetObjectTaggingRequest.builder().bucket(rootBucketName).key(key)
					.build();
			GetObjectTaggingResponse response = s3.getObjectTagging(seqNumber);
			List<Tag> tagSet = s3.getObjectTagging(seqNumber).tagSet();
			Optional<Tag> valTag = tagSet.stream().filter(tag -> tag.key().equals("validated")).findFirst();
			if (valTag.isPresent()) {
				return Boolean.valueOf(valTag.get().value());
			}
		} catch (Exception e) {

		}
		return false;
	}

	public static void main(String[] args) {
		Region region = Region.EU_CENTRAL_1;
		String rootBucketName = "apuseni.eu-dev";
		S3Client s3 = S3Client.builder().region(region).build();
		SqsClient sqs = SqsClient.builder().region(region).build();
//		SqsReader reader = new SqsReader(sqs, S3Storage.PROBLEM_VALIDATION_QUEUE);
//		new Thread(reader).start();
		AhaManger s3st = new AhaManger(s3, rootBucketName, sqs);
//		// System.out.println(s3st.createContest("One more", "Romi"));
//		if (s3st.contestExists(5)) {
//			s3st.addProblem(5, "b");
//			s3st.addProblem(5, "c");
//			s3st.addProblem(5, "d");
//		}
		System.out.println(s3st.checkProblem(5, "b"));
		System.out.println(s3st.checkProblem(5, "c"));
		System.out.println(s3st.checkProblem(5, "d"));

//		PutObjectRequest req = new PutObjectRequest();
//		s3.putObject(null, null);

//		s3.listObjects(ListObjectsRequest.builder().bucket(rootBucketName).build()).contents()
//				.forEach(System.out::println);
	}

}
