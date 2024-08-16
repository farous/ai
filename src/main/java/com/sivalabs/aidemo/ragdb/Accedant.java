package com.sivalabs.aidemo.ragdb;

//import fr.irsn.siseri.service.util.date.TimestampConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
//import org.apache.commons.lang3.StringUtils;
//import org.hibernate.annotations.Cascade;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

//import java.time.ZonedDateTime;
//import java.util.HashSet;
//import java.util.Set;
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

//    @Convert(converter = TimestampConverter.class)
//    @Column(name = "date_derniere_connexion")
//    private ZonedDateTime dateDerniereConnexion;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_etablissement")
//    private Etablissement etablissement;
//
//    @OneToMany(mappedBy = "accedant", orphanRemoval = true)
//    @Cascade({org.hibernate.annotations.CascadeType.ALL})
//    private Set<EtablissementAccedantRole> etablissementAccedantRoles = new HashSet<>();
//
//    @OneToMany(mappedBy = "accedant", orphanRemoval = true)
//    @Cascade({org.hibernate.annotations.CascadeType.ALL})
//    private Set<EmetteurAccedant> emetteurAccedants = new HashSet<>();
//
//    @ManyToMany
//    @JoinTable(name = "asso_accd_groupe",
//            joinColumns = @JoinColumn(name = "id_accedant"),
//            inverseJoinColumns = @JoinColumn(name = "id_groupe_travailleur"))
//    @Cascade({org.hibernate.annotations.CascadeType.ALL})
//    private Set<GroupeTravailleur> groupes = new HashSet<>();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "id_statut",
//            referencedColumnName = "id",
//            foreignKey = @ForeignKey(name = "fk_accedant_statut")
//    )
//    private Statut statut;

    @Column(name = "nb_connexions")
    @NotNull
    private Integer nbConnexions = 0;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "id_type_accedant",
//            foreignKey = @ForeignKey(name = "accedant_type_accedant_fk")
//    )
//    private TypeAccedant typeAccedant;

    @Column(name = "role_courant")
    private String roleCourant;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "id_corps_inspection",
//            foreignKey = @ForeignKey(name = "accedant_corps_inspection_fk")
//    )
//    private CorpsInspection corpsInspection;
//
//    @PreUpdate
//    public void preUpdate() {
//        if (id != null && StringUtils.isBlank(email) && uuIdKeycloak != null) {
//            Logger log = LoggerFactory.getLogger(this.getClass());
//            log.warn("Accedant.preUpdate() email vide pour l'utilisateur {}", id);
//            for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
//                if (ste.getClassName().contains("fr.irsn.siseri") && !ste.getClassName().contains("$$")) {
//                    log.warn("{}", ste);
//                }
//            }
//        }
//    }

}
