package com.simple.book.domain.member.entity;

import java.util.ArrayList;
import java.util.List;

import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "member",
    uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(
            name = "member_uk",
            columnNames = {"user_id", "project_id"}
        )
    }
)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id",updatable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "project_id",updatable = false)
    private Project project;
    
    @Column(name = "is_manager")
    private boolean isManager;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberTask> taskMembers = new ArrayList<>();
}
