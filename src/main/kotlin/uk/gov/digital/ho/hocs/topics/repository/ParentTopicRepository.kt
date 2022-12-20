package uk.gov.digital.ho.hocs.topics.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.digital.ho.hocs.topics.repository.entity.ParentTopic
import java.util.UUID

@Repository
interface ParentTopicRepository : JpaRepository<ParentTopic, UUID>
