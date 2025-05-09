/*
 *
 */

package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.util.ByteUtil;

/**
 * The Class VariantWizardEntity.
 */
@Getter
@Setter
@Entity
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
@Table( name = "variant_wizard" )
public class VariantWizardEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 3148129331695808273L;

    /**
     * The id.
     */
    @Column( name = "id" )
    @Type( type = "uuid-char" )
    @Id
    private UUID id;

    /**
     * The object selection id.
     */
    @Column( name = "object_selection" )
    @Type( type = "uuid-char" )
    private UUID objectSelectionId;

    /**
     * The loadcase selection id.
     */
    @Column( name = "locase_selection" )
    @Type( type = "uuid-char" )
    private UUID loadcaseSelectionId;

    /**
     * The reference id.
     */
    @Column( name = "reference_id" )
    @Type( type = "uuid-char" )
    private UUID referenceId;

    /**
     * The copy.
     */
    @Column( name = "copy" )
    private boolean copy;

    /**
     * The object loadcase relation.
     */
    @Lob
    @Column( name = "object_loadcase_json", length = Integer.MAX_VALUE )
    private byte[] objectLoadcaseRelation;

    /**
     * The loadcase submit.
     */
    @Lob
    @Column( name = "loadcase_submit_json", length = Integer.MAX_VALUE )
    private byte[] loadcaseSubmit;

    /**
     * The assemble.
     */
    @Lob
    @Column( name = "assemble_json", length = Integer.MAX_VALUE )
    private byte[] assemble;

    /**
     * The solve.
     */
    @Lob
    @Column( name = "solve_json", length = Integer.MAX_VALUE )
    private byte[] solve;

    /**
     * The post.
     */
    @Lob
    @Column( name = "post_json", length = Integer.MAX_VALUE )
    private byte[] post;

    /**
     * The form json.
     */
    @Lob
    @Column( name = "form_json", length = Integer.MAX_VALUE )
    private byte[] formJson;

    /**
     * The created on.
     */
    @Column( name = "created_on" )
    private Date createdOn;

    /**
     * The is frozen.
     */
    @Column( name = "frozen" )
    private boolean isFrozen;

    /**
     * The user id.
     */
    @Column( name = "user_id" )
    private String userId;

    /**
     * The description.
     */
    @Column( name = "job_description" )
    private String jobDescription;

    /**
     * The solver type.
     */
    @Column( name = "solver_type" )
    private String solverType;

    // cb2 variant wizard fields STAER

    /**
     * The general project selection id.
     */
    private String generalProjectSelectionId;

    /**
     * The general item.
     */
    private String generalItem;

    /**
     * The general variant defination.
     */
    private String generalVariantDefination;

    /**
     * The general variant type.
     */
    private String generalVariantType;

    /**
     * The general derived from.
     */
    private String generalDerivedFrom;

    /**
     * The general project phase.
     */
    private String generalProjectPhase;

    /**
     * The general simulation generator settings.
     */
    private String generalSimulationGeneratorSettings;

    /**
     * The general variant overview.
     */
    private String generalVariantOverview;
    // cb2 variant fields END

    /**
     * The general discipline context.
     */
    private String generalDisciplineContext;

    /**
     * The assemble pim type.
     */
    private String assemblePimType;

    /**
     * The solve pim type.
     */
    private String solvePimType;

    /**
     * The post pim type.
     */
    private String postPimType;

    /**
     * Gets the form json.
     *
     * @return the form json
     */
    public String getFormJson() {
        return ByteUtil.convertByteToString( this.formJson );
    }

    /**
     * Sets the form json.
     *
     * @param formJson
     *         the new form json
     */
    public void setFormJson( String formJson ) {
        this.formJson = ByteUtil.convertStringToByte( formJson );
    }

    /**
     * Instantiates a new variant wizard entity.
     */
    public VariantWizardEntity() {
        super();
    }

    /**
     * Instantiates a new variant wizard entity.
     *
     * @param id
     *         the id
     * @param objectSelectionId
     *         the object selection id
     * @param loadcaseSelectionId
     *         the loadcase selection id
     * @param referenceId
     *         the reference id
     * @param copy
     *         the copy
     */
    public VariantWizardEntity( UUID id, UUID objectSelectionId, UUID loadcaseSelectionId, UUID referenceId, boolean copy ) {
        super();
        this.id = id;
        this.objectSelectionId = objectSelectionId;
        this.loadcaseSelectionId = loadcaseSelectionId;
        this.referenceId = referenceId;
        this.copy = copy;
    }

    /**
     * Gets the object loadcase relation.
     *
     * @return the object loadcase relation
     */
    public String getObjectLoadcaseRelation() {
        return ByteUtil.convertByteToString( this.objectLoadcaseRelation );
    }

    /**
     * Sets the object loadcase relation.
     *
     * @param objectLoadcaseRelation
     *         the new object loadcase relation
     */
    public void setObjectLoadcaseRelation( String objectLoadcaseRelation ) {
        this.objectLoadcaseRelation = ByteUtil.convertStringToByte( objectLoadcaseRelation );
    }

    /**
     * Gets the loadcase submit.
     *
     * @return the loadcase submit
     */
    public String getLoadcaseSubmit() {
        return ByteUtil.convertByteToString( this.loadcaseSubmit );
    }

    /**
     * Sets the loadcase submit.
     *
     * @param loadcaseSubmit
     *         the new loadcase submit
     */
    public void setLoadcaseSubmit( String loadcaseSubmit ) {
        this.loadcaseSubmit = ByteUtil.convertStringToByte( loadcaseSubmit );
    }

    /**
     * Gets the assemble.
     *
     * @return the assemble
     */
    public String getAssemble() {
        return ByteUtil.convertByteToString( this.assemble );
    }

    /**
     * Sets the assemble.
     *
     * @param assemble
     *         the new assemble
     */
    public void setAssemble( String assemble ) {
        this.assemble = ByteUtil.convertStringToByte( assemble );
    }

    /**
     * Gets the solve.
     *
     * @return the solve
     */
    public String getSolve() {
        return ByteUtil.convertByteToString( this.solve );
    }

    /**
     * Sets the solve.
     *
     * @param solve
     *         the new solve
     */
    public void setSolve( String solve ) {
        this.solve = ByteUtil.convertStringToByte( solve );
    }

    /**
     * Gets the post.
     *
     * @return the post
     */
    public String getPost() {
        return ByteUtil.convertByteToString( this.post );
    }

    /**
     * Sets the post.
     *
     * @param post
     *         the new post
     */
    public void setPost( String post ) {
        this.post = ByteUtil.convertStringToByte( post );
    }

    /**
     * Checks if is frozen.
     *
     * @return true, if is frozen
     */
    public boolean isFrozen() {
        return isFrozen;
    }

    /**
     * Sets the frozen.
     *
     * @param isFrozen
     *         the new frozen
     */
    public void setFrozen( boolean isFrozen ) {
        this.isFrozen = isFrozen;
    }

}
