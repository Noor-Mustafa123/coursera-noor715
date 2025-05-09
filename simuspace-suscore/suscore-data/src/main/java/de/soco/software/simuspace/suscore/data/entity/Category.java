package de.soco.software.simuspace.suscore.data.entity;

import java.util.UUID;

/**
 * @author fahad
 */
public class Category {

    private UUID id;

    private String categoryName;

    public UUID getId() {
        return id;
    }

    public void setId( UUID id ) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName( String categoryName ) {
        this.categoryName = categoryName;
    }

}
