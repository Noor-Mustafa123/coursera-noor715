package de.soco.software.simuspace.wizards.model.QADyna;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchSubModelDTO;
import de.soco.software.simuspace.suscore.data.model.SsfeSelectionDTO;

/**
 * The type Qa dyna parameter form.
 */
@Getter
@Setter
@JsonIgnoreProperties( ignoreUnknown = true )
public class QADynaParameterForm {

    /**
     * The Ncpu.
     */
    private List< String > ncpu;

    /**
     * The Ez type.
     */
    private String ezType;

    /**
     * The Ez list.
     */
    private String ezList;

    /**
     * The Set 1 dyna options.
     */
    private String set1_DynaOptions;

    /**
     * The Set 1 upload dyna.
     */
    private String set1_uploadDynaId;

    /**
     * The Set 1 upload dyna file.
     */
    private SsfeSelectionDTO set1_uploadDyna;

    /**
     * The Set 1 version.
     */
    private String set1_version;

    /**
     * The Set 1 mcfile.
     */
    private String set1_server_mcfileId;

    /**
     * The Set 1 cb 2 mcfile id.
     */
    private String set1_cb2_mcfileId;

    /**
     * The Set 1 mcfile option.
     */
    private String set1_mcfileOption;

    /**
     * The Set 1 mcfile file.
     */
    private SsfeSelectionDTO set1_mcfile;

    /**
     * The Set 1 mcfile cb 2.
     */
    private BmwCaeBenchSubModelDTO set1_mcfileCB2;

    /**
     * The Set 1 matdb.
     */
    private String set1_server_matdbId;

    /**
     * The Set 1 cb 2 matdb id.
     */
    private String set1_cb2_matdbId;

    /**
     * The Set 1 matdb option.
     */
    private String set1_matdbOption;

    /**
     * The Set 1 matdb file.
     */
    private SsfeSelectionDTO set1_matdb;

    /**
     * The Set 1 matdb cb 2.
     */
    private BmwCaeBenchSubModelDTO set1_matdbCB2;

    /**
     * The Set 1 matdbqs.
     */
    private String set1_server_matdbqsId;

    /**
     * The Set 1 cb 2 matdbqs id.
     */
    private String set1_cb2_matdbqsId;

    /**
     * The Set 1 matdbqs option.
     */
    private String set1_matdbqsOption;

    /**
     * The Set 1 matdbqs file.
     */
    private SsfeSelectionDTO set1_matdbqs;

    /**
     * The Set 1 matdbqs cb 2.
     */
    private BmwCaeBenchSubModelDTO set1_matdbqsCB2;

    /**
     * The Set 2 dyna options.
     */
    private String set2_DynaOptions;

    /**
     * The Set 2 upload dyna.
     */
    private String set2_uploadDynaId;

    /**
     * The Set 2 upload dyna file.
     */
    private SsfeSelectionDTO set2_uploadDyna;

    /**
     * The Set 2 version.
     */
    private String set2_version;

    /**
     * The Set 2 mcfile.
     */
    private String set2_server_mcfileId;

    /**
     * The Set 2 cb 2 mcfile id.
     */
    private String set2_cb2_mcfileId;

    /**
     * The Set 2 mcfile option.
     */
    private String set2_mcfileOption;

    /**
     * The Set 2 mcfile file.
     */
    private SsfeSelectionDTO set2_mcfile;

    /**
     * The Set 2 mcfile cb 2.
     */
    private BmwCaeBenchSubModelDTO set2_mcfileCB2;

    /**
     * The Set 2 matdb.
     */
    private String set2_server_matdbId;

    /**
     * The Set 2 cb 2 matdb id.
     */
    private String set2_cb2_matdbId;

    /**
     * The Set 2 matdb option.
     */
    private String set2_matdbOption;

    /**
     * The Set 2 matdb file.
     */
    private SsfeSelectionDTO set2_matdb;

    /**
     * The Set 2 matdb cb 2.
     */
    private BmwCaeBenchSubModelDTO set2_matdbCB2;

    /**
     * The Set 2 matdbqs.
     */
    private String set2_server_matdbqsId;

    /**
     * The Set 2 cb 2 matdbqs id.
     */
    private String set2_cb2_matdbqsId;

    /**
     * The Set 2 matdbqs option.
     */
    private String set2_matdbqsOption;

    /**
     * The Set 2 matdbqs file.
     */
    private SsfeSelectionDTO set2_matdbqs;

    /**
     * The Set 2 matdbqs cb 2.
     */
    private BmwCaeBenchSubModelDTO set2_matdbqsCB2;

}
