package departmentservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@RequiredArgsConstructor
public class SQSPublisherService {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${aws.sqs.payments-events-queue-url}")
    private String paymentsEventsQueueUrl;

    public void publishPaymentEvent(Object event) {
        try {
            String messageBody = objectMapper.writeValueAsString(event);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(paymentsEventsQueueUrl)
                    .messageBody(messageBody)
                    .build();

            sqsClient.sendMessage(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send SQS message", e);
        }
    }
}
