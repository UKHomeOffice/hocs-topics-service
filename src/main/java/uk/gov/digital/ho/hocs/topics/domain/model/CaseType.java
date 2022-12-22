package uk.gov.digital.ho.hocs.topics.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "case_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = { "type" })
public class CaseType implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "short_code")
    private String shortCode;

    @Column(name = "type")
    private String type;

    @Column(name = "owning_unit_uuid")
    private UUID unitUUID;

    @Column(name = "deadline_stage")
    private String deadlineStage;

    @Column(name = "bulk")
    private boolean bulk;

    @Column(name = "active")
    private boolean active;

    @Column(name = "previous_case_type")
    private String previousCaseType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deadline_stage", referencedColumnName = "type", insertable = false, updatable = false)
    private StageTypeEntity deadlineStageEntity;

    public CaseType(String displayName,
                    String shortCode,
                    String type,
                    UUID unitUUID,
                    String deadlineStage,
                    boolean bulk,
                    boolean active,
                    String previousCaseType) {
        this.uuid = UUID.randomUUID();
        this.displayName = displayName;
        this.shortCode = shortCode;
        this.type = type;
        this.unitUUID = unitUUID;
        this.deadlineStage = deadlineStage;
        this.bulk = bulk;
        this.active = active;
        this.previousCaseType = previousCaseType;
    }

    public CaseType(String displayName,
                    String shortCode,
                    String type,
                    String deadlineStage,
                    boolean bulk,
                    boolean active,
                    String previousCaseType) {
        this.uuid = UUID.randomUUID();
        this.displayName = displayName;
        this.shortCode = shortCode;
        this.type = type;
        this.unitUUID = UUID.randomUUID();
        this.deadlineStage = deadlineStage;
        this.bulk = bulk;
        this.active = active;
        this.previousCaseType = previousCaseType;
    }

}
