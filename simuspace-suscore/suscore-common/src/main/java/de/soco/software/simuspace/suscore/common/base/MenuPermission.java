/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.base;

/**
 * The Class MenuPermission represents the all permissions for resource.
 *
 * @author Faisal.Hameed
 * @since 1.0
 */
public class MenuPermission {

    /**
     * The Constant CREATE.
     */
    public static final String CREATE = "create";

    /**
     * The Constant READ.
     */
    public static final String READ = "read";

    /**
     * The Constant UPDATE.
     */
    public static final String UPDATE = "update";

    /**
     * The Constant DELETE.
     */
    public static final String DELETE = "delete";

    /**
     * The Constant EXECUTE.
     */
    public static final String EXECUTE = "execute";

    /**
     * The Constant MANAGE.
     */
    public static final String MANAGE = "manage";

    /**
     * The Constant KILL.
     */
    public static final String KILL = "kill";

    /**
     * The Constant SHARE.
     */
    public static final String SHARE = "share";

    /**
     * The Constant NO_PERMISSION.
     */
    public static final int NO_PERMISSION = 4;

    /**
     * The Constant USER_PERMISSION.
     */
    public static final int USER_PERMISSION = 6;

    /**
     * The Constant PROFILE_PERMISSION.
     */
    public static final int PROFILE_PERMISSION = 7;

    /**
     * The resourc key.
     */
    private String resourcKey;

    /**
     * The permission type.
     */
    private int permissionType = USER_PERMISSION;

    /**
     * The create.
     */
    private int create;

    /**
     * The read.
     */
    private int read;

    /**
     * The update.
     */
    private int update;

    /**
     * The delete.
     */
    private int delete;

    /**
     * The execute.
     */
    private int execute;

    /**
     * The manage.
     */
    private int manage;

    /**
     * The kill.
     */
    private int kill;

    /**
     * The share.
     */
    private int share;

    /**
     * Instantiates a new menu permission.
     */
    public MenuPermission() {
    }

    /**
     * Instantiates a new menu permission.
     *
     * @param resourceKey
     *         the resource key
     * @param create
     *         the create
     * @param read
     *         the read
     * @param update
     *         the update
     * @param delete
     *         the delete
     * @param execute
     *         the execute
     * @param manage
     *         the manage
     * @param kill
     *         the kill
     * @param share
     *         the share
     */
    public MenuPermission( String resourceKey, int create, int read, int update, int delete, int execute, int manage, int kill,
            int share ) {
        super();
        this.resourcKey = resourceKey;
        this.create = create;
        this.read = read;
        this.update = update;
        this.delete = delete;
        this.execute = execute;
        this.manage = manage;
        this.kill = kill;
        this.share = share;
    }

    /**
     * Gets the resourc key.
     *
     * @return the resourc key
     */
    public String getResourcKey() {
        return resourcKey;
    }

    /**
     * Sets the resourc key.
     *
     * @param resourcKey
     *         the new resourc key
     */
    public void setResourcKey( String resourcKey ) {
        this.resourcKey = resourcKey;
    }

    /**
     * Gets the permission type.
     *
     * @return the permission type
     */
    public int getPermissionType() {
        return permissionType;
    }

    /**
     * Sets the permission type.
     *
     * @param permissionType
     *         the new permission type
     */
    public void setPermissionType( int permissionType ) {
        this.permissionType = permissionType;
    }

    /**
     * Gets the creates the.
     *
     * @return the creates the
     */

    @Override
    public String toString() {
        return "MenuPermission [resourcKey=" + resourcKey + ", permissionType=" + permissionType + ", create=" + create + ", read=" + read
                + ", update=" + update + ", delete=" + delete + ", execute=" + execute + ", manage=" + manage + ", kill=" + kill
                + ", share=" + share + "]";
    }

    public boolean getCreate() {
        return create >= permissionType;
    }

    public void setCreate( int create ) {
        this.create = create;
    }

    public boolean getRead() {
        return read >= permissionType;
    }

    public void setRead( int read ) {
        this.read = read;
    }

    public boolean getUpdate() {
        return update >= permissionType;
    }

    public void setUpdate( int update ) {
        this.update = update;
    }

    public boolean getDelete() {
        return delete >= permissionType;
    }

    public void setDelete( int delete ) {
        this.delete = delete;
    }

    public boolean getExecute() {
        return execute >= permissionType;
    }

    public void setExecute( int execute ) {
        this.execute = execute;
    }

    public boolean getManage() {
        return manage >= permissionType;
    }

    public void setManage( int manage ) {
        this.manage = manage;
    }

    public boolean getKill() {
        return kill >= permissionType;
    }

    public void setKill( int kill ) {
        this.kill = kill;
    }

    public boolean getShare() {
        return share >= permissionType;
    }

    public void setShare( int share ) {
        this.share = share;
    }

    /**
     * Checks if is any allowed.
     *
     * @return true, if is any allowed
     */
    public boolean isAnyAllowed() {
        return getCreate() || getRead() || getUpdate() || getDelete() || getExecute() || getManage() || getKill() || getShare();
    }

}
