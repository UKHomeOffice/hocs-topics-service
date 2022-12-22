package uk.gov.digital.ho.hocs.topics.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.topics.domain.model.TeamLink;

import java.util.Set;

@Repository
public interface TeamLinkRepository extends CrudRepository<TeamLink, String> {

    Set<TeamLink> findAllByLinkValueAndLinkType(String linkValue, String linkType);

    TeamLink findByLinkValueAndLinkTypeAndCaseTypeAndStageType(String linkValue,
                                                               String linkType,
                                                               String caseType,
                                                               String stageType);

}
