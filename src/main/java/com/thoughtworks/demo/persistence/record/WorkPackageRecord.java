package com.thoughtworks.demo.persistence.record;

import com.thoughtworks.demo.domain.aggregates.IWorkPackageAggregate;
import lombok.*;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "work_package", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@Data
@Builder
@EntityListeners({AuditingEntityListener.class})
public class WorkPackageRecord implements IWorkPackageAggregate {

    @Tolerate
    WorkPackageRecord() {
    }

    @GenericGenerator(name = "work_package_id_generator", strategy = "com.thoughtworks.demo.util.IDGenerator")
    @GeneratedValue(generator = "work_package_id_generator")
    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private AircraftRecord aircraft;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "aircraft_id", insertable = false, updatable = false)
    private Long aircraftId;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "workpackage_task",
            joinColumns = @JoinColumn(name = "workpackage_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    @Builder.Default
    private Set<TaskRecord> tasks = new HashSet<>();
}
