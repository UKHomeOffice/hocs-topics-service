package uk.gov.digital.ho.hocs.topics.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.topics.client.casework.CaseworkClient;
import uk.gov.digital.ho.hocs.topics.client.casework.dto.GetTopicResponse;
import uk.gov.digital.ho.hocs.topics.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.topics.domain.model.Team;
import uk.gov.digital.ho.hocs.topics.domain.repository.ParentTopicRepository;
import uk.gov.digital.ho.hocs.topics.domain.repository.TeamRepository;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class TeamService {

    private TeamRepository teamRepository;

    //TODO: used within Delete Team method - needs coordination and likely new endpoint.
    private ParentTopicRepository parentTopicRepository;

    private CaseworkClient caseworkClient;

    public TeamService(TeamRepository teamRepository,
                       ParentTopicRepository parentTopicRepository,
                       CaseworkClient caseworkClient) {
        this.teamRepository = teamRepository;
        this.parentTopicRepository = parentTopicRepository;
        this.caseworkClient = caseworkClient;
    }

    public Team getTeamByTopicAndStage(UUID caseUUID, UUID topicUUID, String stageType) {
        log.debug("Getting Team for Topic {} and Stage {}", topicUUID, stageType);
        GetTopicResponse topicResponse = caseworkClient.getTopic(caseUUID, topicUUID);
        Team team = teamRepository.findByTopicAndStage(topicResponse.getTopicUUID(), stageType);
        if (team != null) {
            log.info("Got Team for Topic {} and Stage {}", topicUUID, stageType);
            return team;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Team not found for Topic %s and Stage %s",
                    topicUUID, stageType);
        }
    }

    public Set<Team> getTeamsByTopic(UUID topicUUID) {
        log.debug("Getting Team by Topic {}", topicUUID);
        Set<Team> teams = teamRepository.findTeamsByTopicUuid(topicUUID);
        return teams;
    }

}
