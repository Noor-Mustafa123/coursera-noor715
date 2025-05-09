package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class SuSObjectDataSourceEntity extends DataSourceEntity {

    /**
     * The Selection entity.
     */
    @OneToOne
    private SelectionEntity selectionEntity;

    private String updateInterval;

    private String traversalDepth;

    private String cacheUpdatedAt;

}
