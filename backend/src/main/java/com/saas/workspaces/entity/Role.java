package com.saas.workspaces.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    @Column(length = 100)
    private String description;

    @Column(name = "can_create", nullable = false)
    private Boolean canCreate = false;

    @Column(name = "can_edit", nullable = false)
    private Boolean canEdit = false;

    @Column(name = "can_delete", nullable = false)
    private Boolean canDelete = false;
}
