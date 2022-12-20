package uk.gov.digital.ho.hocs.topics.repository.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
@Table(name = "parent_topic")
data class ParentTopic(
    @Id
    @GeneratedValue
    @UuidGenerator
    val uuid: UUID,
    @Column(name = "display_name")
    val displayName: String,
    val active: Boolean,
    @OneToMany(mappedBy = "parentTopic", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val childTopics: List<Topic> = emptyList()
) {
    protected constructor() : this(UUID.randomUUID(), "", false, emptyList())
}
