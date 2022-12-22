package uk.gov.digital.ho.hocs.topics.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateParentTopicResponse {

    @JsonProperty("topicUUID")
    String parentTopicUUID;

}
