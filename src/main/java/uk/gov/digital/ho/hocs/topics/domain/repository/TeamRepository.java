package uk.gov.digital.ho.hocs.topics.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.topics.controller.dto.data.SimpleMapItem;
import uk.gov.digital.ho.hocs.topics.domain.model.Team;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

    @Query(value = "SELECT t.* FROM team t JOIN team_link tl on tl.responsible_team_uuid = t.uuid WHERE tl.link_value = cast(?1 as text) and tl.link_type = 'TOPIC' and tl.stage_type = ?2",
           nativeQuery = true)
    Team findByTopicAndStage(UUID topicUUID, String stageType);

    @Query(value = "SELECT DISTINCT tl.link_value as value, t.display_name as label FROM team t JOIN team_link tl on tl.responsible_team_uuid = t.uuid WHERE tl.link_type = 'TOPIC' and tl.stage_type = ?1",
           nativeQuery = true)
    List<SimpleMapItem> findTopicToTeamMappingByStageType(String stageType);

    @Query(value = "SELECT t.* FROM team t INNER JOIN team_link tl ON tl.responsible_team_uuid=t.uuid INNER JOIN topic ON cast(topic.uuid as text)=tl.link_value WHERE tl.link_type='TOPIC' AND topic.uuid=?1 AND topic.active=TRUE AND t.active=TRUE",
           nativeQuery = true)
    Set<Team> findTeamsByTopicUuid(UUID topicUUID);

}
