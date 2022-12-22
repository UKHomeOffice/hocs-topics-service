package uk.gov.digital.ho.hocs.topics.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.topics.controller.dto.AddTeamToTopicDto;
import uk.gov.digital.ho.hocs.topics.controller.dto.TopicTeamDto;
import uk.gov.digital.ho.hocs.topics.controller.dto.data.SimpleMapItem;
import uk.gov.digital.ho.hocs.topics.domain.model.TopicTeam;
import uk.gov.digital.ho.hocs.topics.service.TopicTeamService;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
public class TopicTeamResource {

    private final TopicTeamService topicTeamService;

    @Autowired
    public TopicTeamResource(TopicTeamService topicTeamService) {
        this.topicTeamService = topicTeamService;
    }

    @GetMapping(value = "/topics/{caseType}/teams", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<TopicTeamDto>> getTopicsByCaseTypeWithTeams(@PathVariable String caseType) {
        log.info("requesting all topics by case type {} with teams", caseType);
        Set<TopicTeam> topicTeams = topicTeamService.getTopicsByCaseTypeWithTeams(caseType);
        return ResponseEntity.ok(topicTeams.stream().map(t -> TopicTeamDto.from(t)).collect(Collectors.toSet()));
    }

    @PostMapping(value = "/topic/{topicUUID}/team/{teamUUID}")
    public ResponseEntity addTeamToTopic(@PathVariable UUID topicUUID,
                                         @PathVariable UUID teamUUID,
                                         @RequestBody AddTeamToTopicDto request) {
        log.info("Adding team () to topic {}", teamUUID, topicUUID);
        topicTeamService.addTeamToTopic(topicUUID, teamUUID, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/team/topic/stage/{stageType}")
    public ResponseEntity<List<SimpleMapItem>> getTopicToTeamMappingByStageType(@PathVariable String stageType) {
        return ResponseEntity.ok(topicTeamService.getTopicToTeamMappingByStageType(stageType));
    }

}
