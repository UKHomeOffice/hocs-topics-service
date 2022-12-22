package uk.gov.digital.ho.hocs.topics.client.audit;

import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.digital.ho.hocs.topics.application.RequestData;
import uk.gov.digital.ho.hocs.topics.application.aws.util.SnsStringMessageAttributeValue;
import uk.gov.digital.ho.hocs.topics.client.audit.dto.CreateAuditRequest;
import uk.gov.digital.ho.hocs.topics.client.audit.dto.EventType;
import uk.gov.digital.ho.hocs.topics.domain.model.ParentTopic;
import uk.gov.digital.ho.hocs.topics.domain.model.TeamLink;
import uk.gov.digital.ho.hocs.topics.domain.model.Topic;

import javax.json.Json;
import java.time.LocalDateTime;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.topics.application.LogEvent.AUDIT_EVENT_CREATED;
import static uk.gov.digital.ho.hocs.topics.application.LogEvent.AUDIT_FAILED;
import static uk.gov.digital.ho.hocs.topics.application.LogEvent.EVENT;

@Slf4j
@Component
public class AuditClient {

    private final String auditQueue;

    private final String raisingService;

    private final String namespace;

    private final AmazonSNSAsync auditSearchSnsClient;

    private final ObjectMapper objectMapper;

    private final RequestData requestData;

    private static final String EVENT_TYPE_HEADER = "event_type";

    private static final String TOPIC = "topicUUID";

    private static final String ACTIVE = "active";

    private static final String STAGE_TYPE = "stageType";

    private static final String TEAM_UUID = "teamUUID";

    private static final String CASE_TYPE = "caseType";

    private static final String DISPLAY_NAME = "displayName";

    @Autowired
    public AuditClient(AmazonSNSAsync auditSearchSnsClient,
                       @Value("${aws.sns.audit-search.arn}") String auditQueue,
                       @Value("${auditing.deployment.name}") String raisingService,
                       @Value("${auditing.deployment.namespace}") String namespace,
                       ObjectMapper objectMapper,
                       RequestData requestData) {
        this.auditSearchSnsClient = auditSearchSnsClient;
        this.auditQueue = auditQueue;
        this.raisingService = raisingService;
        this.namespace = namespace;
        this.objectMapper = objectMapper;
        this.requestData = requestData;
    }

    public void createTopicAudit(Topic topic) {
        String auditPayload = Json.createObjectBuilder().add(TOPIC, topic.getUuid().toString()).add(DISPLAY_NAME,
            topic.getDisplayName()).add("parentTopicUUID", topic.getParentTopic().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.CREATE_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void createParentTopicAudit(ParentTopic parentTopic) {
        String auditPayload = Json.createObjectBuilder().add(TOPIC, parentTopic.getUuid().toString()).add(DISPLAY_NAME,
            parentTopic.getDisplayName()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.CREATE_PARENT_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void deleteTopicAudit(Topic topic) {
        String auditPayload = Json.createObjectBuilder().add(TOPIC, topic.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REMOVE_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void deleteParentTopicAudit(ParentTopic parentTopic) {
        String auditPayload = Json.createObjectBuilder().add(TOPIC,
            parentTopic.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REMOVE_PARENT_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void reactivateTopicAudit(Topic topic) {
        String auditPayload = Json.createObjectBuilder().add(TOPIC, topic.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REACTIVATE_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void reactivateParentTopicAudit(ParentTopic parentTopic) {
        String auditPayload = Json.createObjectBuilder().add(TOPIC,
            parentTopic.getUuid().toString()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.REACTIVATE_PARENT_TOPIC.toString());
        sendAuditMessage(request);
    }

    public void addTeamToTopicAudit(TeamLink teamLink) {
        String auditPayload = Json.createObjectBuilder().add(TOPIC, teamLink.getLinkValue()).add(TEAM_UUID,
            teamLink.getResponsibleTeamUUID().toString()).add(CASE_TYPE, teamLink.getCaseType()).add(STAGE_TYPE,
            teamLink.getStageType()).build().toString();
        CreateAuditRequest request = generateAuditRequest(auditPayload, EventType.ADD_TEAM_TO_TOPIC.toString());
        sendAuditMessage(request);
    }

    private void sendAuditMessage(CreateAuditRequest request) {

        try {
            var publishRequest = new PublishRequest(auditQueue,
                objectMapper.writeValueAsString(request)).withMessageAttributes(getQueueHeaders(request.getType()));

            auditSearchSnsClient.publish(publishRequest);
            log.info("Create audit for event {}, correlationID: {}, UserID: {}", request.getType(),
                requestData.correlationId(), requestData.userId(), value(EVENT, AUDIT_EVENT_CREATED));
        } catch (Exception e) {
            log.error("Failed to create audit event {} for reason {}", request.getType(), e,
                value(EVENT, AUDIT_FAILED));
        }
    }

    private CreateAuditRequest generateAuditRequest(String auditPayload, String eventType) {
        return new CreateAuditRequest(requestData.correlationId(), raisingService, auditPayload, namespace,
            LocalDateTime.now(), eventType, requestData.userId());
    }

    private Map<String, MessageAttributeValue> getQueueHeaders(String eventType) {
        return Map.of(EVENT_TYPE_HEADER, new SnsStringMessageAttributeValue(eventType),
            RequestData.CORRELATION_ID_HEADER, new SnsStringMessageAttributeValue(requestData.correlationId()),
            RequestData.USER_ID_HEADER, new SnsStringMessageAttributeValue(requestData.userId()),
            RequestData.USERNAME_HEADER, new SnsStringMessageAttributeValue(requestData.username()),
            RequestData.GROUP_HEADER, new SnsStringMessageAttributeValue(requestData.groups()));
    }

}
