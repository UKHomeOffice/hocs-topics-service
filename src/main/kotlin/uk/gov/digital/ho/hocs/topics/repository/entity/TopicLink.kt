package uk.gov.digital.ho.hocs.topics.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "topic_link")
@SequenceGenerator(name = "topic_link_id_seq", sequenceName = "topic_link_id_seq", allocationSize = 1)
data class TopicLink(
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "topic_link_id_seq"
    )
    val id: Long? = null,
    @Column(name = "case_type")
    val caseType: String,
    @Column(name = "stage_type")
    val stageType: String,
    @Column(name = "link_value", insertable = false, updatable = false)
    val linkValue: UUID,
    @Column(name = "responsible_team_uuid")
    val responsibleTeamUUID: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_value")
    val topic: Topic? = null
) {
    protected constructor() : this(null, "", "", UUID.randomUUID(), UUID.randomUUID(), null)
}
