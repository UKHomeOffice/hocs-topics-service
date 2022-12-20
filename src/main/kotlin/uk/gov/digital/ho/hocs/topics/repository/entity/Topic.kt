package uk.gov.digital.ho.hocs.topics.repository.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "topic")
@SequenceGenerator(name = "topic_id_seq", sequenceName = "topic_id_seq", allocationSize = 1)
data class Topic(
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "topic_id_seq"
    )
    val id: Long? = null,
    val uuid: UUID,
    @Column(name = "display_name")
    val displayName: String,
    @Column(name = "parent_topic_uuid")
    val parentTopicUUID: UUID,
    val active: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_topic_uuid")
    val parentTopic: ParentTopic? = null,

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val topicLink: List<TopicLink> = emptyList()
) {
    protected constructor() : this(null, UUID.randomUUID(), "", UUID.randomUUID(), false, null, emptyList())
}
