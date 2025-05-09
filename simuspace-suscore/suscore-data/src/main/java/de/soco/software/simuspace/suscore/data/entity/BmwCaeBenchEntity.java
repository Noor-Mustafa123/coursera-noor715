package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import de.soco.software.simuspace.suscore.common.util.ByteUtil;

/**
 * The Class BmwCaeBenchEntity.
 *
 * @author noman arshad
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table( name = "bmw_bench" )
@JsonIgnoreProperties( ignoreUnknown = true )
public class BmwCaeBenchEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -5499972600130604881L;

    /**
     * The primary key.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The name.
     */
    @Column( name = "object_name", length = 512 )
    private String name;

    /**
     * The oid.
     */
    private String oid;

    /**
     * The bmw cae data type.
     */
    private String bmwCaeDataType;

    /**
     * The user id.
     */
    private String userId;

    /**
     * The assemble type.
     */
    private String assembleType;

    /**
     * The car project.
     */
    private String carProject;

    /**
     * The owner.
     */
    private String owner;

    /**
     * The created at.
     */
    private String createdAt;

    /**
     * The Module label.
     */
    private String modulelabel;

    /**
     * The variant label.
     */
    private String variantLabel;

    /**
     * The modelState label.
     */
    private String modelStatelabel;

    /**
     * The modelDef label.
     */
    private String modelDefLabel;

    /**
     * The description.
     */
    @Column( name = "description" )
    @Lob
    private byte[] description;

    /**
     * The release level label.
     */
    private String releaseLevelLabel;

    /**
     * The reference.
     */
    private String reference;

    /**
     * The input decks.
     */
    @Column( length = 512 )
    private String inputDecks;

    /**
     * The phase.
     */
    private String phase;

    /**
     * The project.
     */
    private String project;

    /**
     * The variant overview.
     */
    private String variantOverview;

    /**
     * The label.
     */
    @Column( length = 512 )
    private String label;

    /**
     * The base name.
     */
    private String baseName;

    /**
     * The format.
     */
    private String format;

    /**
     * The project phase.
     */
    private String projectPhase;

    /**
     * The type.
     */
    private String type;

    /**
     * The simulation def.
     */
    private String simulationDef;

    /**
     * The item.
     */
    private String item;

    /**
     * The variant.
     */
    private String variant;

    /**
     * The keyresults.
     */
    private String keyresults;

    /**
     * The overall status.
     */
    private String overallStatus;

    /**
     * The sim process status.
     */
    private String simProcessStatus;

    /**
     * The sim post process status.
     */
    private String simPostProcessStatus;

    /**
     * The key result count.
     */
    private String keyResultCount;

    /**
     * The created by.
     */
    private String createdBy;

    /**
     * The discipline context.
     */
    private String disciplineContext;

    /**
     * The alert.
     */
    private String alert;

    /**
     * The auto delete.
     */
    private String autoDelete;

    /**
     * The model state.
     */
    private String modelState;

    /**
     * The derived from.
     */
    private String derivedFrom;

    /**
     * The seeds.
     */
    private String seeds;

    /**
     * The simulation type.
     */
    private String simulationType;

    /**
     * The short name.
     */
    private String shortName;

    /**
     * The input deck.
     */
    private String inputDeck;

    /**
     * isDeleted.
     */
    @Column( name = "is_delete" )
    private boolean isDelete;

    /**
     * The bmw cae bench node entity.
     */
    @ManyToOne( fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    @JoinColumn( name = "bmw_node_id" )
    private BmwCaeBenchNodeEntity bmwCaeBenchNodeEntity;

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return ByteUtil.convertByteToString( description );
    }

    /**
     * Sets the description.
     *
     * @param description
     *         the new description
     */
    public void setDescription( String description ) {
        this.description = ByteUtil.convertStringToByte( description );
    }

    /**
     * Checks if is delete.
     *
     * @return true, if is delete
     */
    public boolean isDelete() {
        return isDelete;
    }

    /**
     * Sets the delete.
     *
     * @param isDelete
     *         the new delete
     */
    public void setDelete( boolean isDelete ) {
        this.isDelete = isDelete;
    }

}
