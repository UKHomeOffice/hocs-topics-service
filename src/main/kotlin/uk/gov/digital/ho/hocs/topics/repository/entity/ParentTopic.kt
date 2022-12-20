package uk.gov.digital.ho.hocs.topics.repository.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "parent_topic")
@SequenceGenerator(name = "parent_topic_id_seq", sequenceName = "parent_topic_id_seq", allocationSize = 1)
data class ParentTopic(
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "parent_topic_id_seq"
    )
    val id: Long? = null,
    val uuid: UUID,
    @Column(name = "display_name")
    val displayName: String,
    val active: Boolean,
    @OneToMany(mappedBy = "parentTopic", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val childTopics: List<Topic> = emptyList()
) {
    protected constructor() : this(null, UUID.randomUUID(), "", false)
}
