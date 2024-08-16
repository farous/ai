package com.sivalabs.aidemo.ragdb;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "accedant")
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(
        name = "generateur_accedant_sequence",
        sequenceName = "seq_id_accedant",
        allocationSize = 1
)
@Inheritance(strategy = InheritanceType.JOINED)
public class Accedant {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generateur_accedant_sequence")
    private Long id;

    @Column(name = "uuid_keycloak", columnDefinition = "uuid")
    private UUID uuIdKeycloak;

    @Column(name = "nom")
    @Length(max = 100)
    @NotNull
    private String nom;

    @Column(name = "prenom")
    @Length(max = 100)
    @NotNull
    private String prenom;

    @Column(name = "email")
    @Length(max = 50)
    private String email;

    @Column(name = "rpps", length = 11)
    private String rpps;

    @Column(name = "nom_justificatif_identite")
    @Length(max = 100)
    private String justificatifIdentite;

    @Column(name = "cgu_validee")
    private Boolean cguValidee = false;

    @Column(name = "nb_connexions")
    @NotNull
    private Integer nbConnexions = 0;

    @Column(name = "role_courant")
    private String roleCourant;

}
