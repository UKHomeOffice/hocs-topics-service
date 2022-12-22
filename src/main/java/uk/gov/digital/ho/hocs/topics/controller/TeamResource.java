package uk.gov.digital.ho.hocs.topics.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.topics.controller.dto.TeamDto;
import uk.gov.digital.ho.hocs.topics.domain.model.Team;
import uk.gov.digital.ho.hocs.topics.service.TeamService;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class TeamResource {

    private TeamService teamService;

    public TeamResource(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping(value = "/teams/topic/{topicUUID}")
    public ResponseEntity<Set<TeamDto>> getTeamsByTopic(@PathVariable UUID topicUUID) {
        Set<Team> teams = teamService.getTeamsByTopic(topicUUID);
        return ResponseEntity.ok(teams.stream().map(TeamDto::fromWithoutPermissions).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/team/case/{caseUUID}/topic/{topicUUID}/stage/{stageType}")
    public ResponseEntity<TeamDto> getActiveTeams(@PathVariable UUID caseUUID,
                                                  @PathVariable UUID topicUUID,
                                                  @PathVariable String stageType) {
        Team team = teamService.getTeamByTopicAndStage(caseUUID, topicUUID, stageType);
        return ResponseEntity.ok(TeamDto.from(team));
    }


}
