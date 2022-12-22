package uk.gov.digital.ho.hocs.topics.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.topics.domain.model.Topic;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TopicDto {

    @JsonProperty("label")
    private String displayName;

    @JsonProperty("value")
    private UUID uuid;

    @JsonProperty("active")
    private boolean active;

    public static TopicDto from(Topic topic) {
        return new TopicDto(topic.getDisplayName(), topic.getUuid(), topic.isActive());
    }

}
