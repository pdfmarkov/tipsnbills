package com.tagall.tipsnbills.module;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name="subsidiary")
public class Subsidiary {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    private Organization organization;

    @OneToMany(mappedBy = "subsidiary", cascade = CascadeType.ALL)
    private Set<Employee> employees = new HashSet<>();

}
