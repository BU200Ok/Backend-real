package com.bu200.project.entity;

import com.bu200.login.entity.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyGroup;
import org.springframework.cglib.core.Local;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Entity @Table(name = "task")
@Getter@Setter@NoArgsConstructor@AllArgsConstructor
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_code")
    private Long taskCode;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "task_detail")
    private String taskDetail;

    @Column(name = "task_type")
    private Integer taskType;

    @Column(name = "task_type_detail")
    private String taskTypeDetail;

    @CreationTimestamp
    @Column(name = "task_start")
    private Timestamp taskStart;

    @Column(name = "task_status")
    private String taskStatus;

    @Column(name = "task_end")
    private LocalDate taskEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_code")
    private Project project;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
    private List<AccountTask> accountTasks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
    private List<TaskPost> taskPosts;
}
