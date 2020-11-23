package cloudant;

/**
 * Represents a Document stored in Cloudant.
 */

public class Document {
    private String _id;
    private String _rev;
    private String title;
    private String cosBucketName;
    private String cosObjectName;
    private String status;

    public Document() {
        this.title = "";
        this.cosBucketName = "";
        this.cosObjectName = "";
        this.status = "";
    }

    public Document(String title, String cosBucketName, String cosObjectName, String status) {
        this.title = title;
        this.cosBucketName = cosBucketName;
        this.cosObjectName = cosObjectName;
        this.status = status;

    }

    /**
     * Gets the ID.
     * 
     * @return The ID.
     */
    public String get_id() {
        return _id;
    }

    /**
     * Sets the ID
     * 
     * @param _id
     *            The ID to set.
     */
    public void set_id(String _id) {
        this._id = _id;
    }

    /**
     * Gets the revision of the document.
     * 
     * @return The revision of the document.
     */
    public String get_rev() {
        return _rev;
    }

    /**
     * Sets the revision.
     * 
     * @param _rev
     *            The revision to set.
     */
    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    /**
     * Gets the documentName of the document.
     * 
     * @return The title of the document.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the name
     * 
     * @param title
     *            The documentTitle to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * Gets the documentBucketName of the document.
     *
     * @return The title of the document.
     */
    public String getCosBucketName() {
        return cosBucketName;
    }

    /**
     * Sets the cosBucketName
     *
     * @param cosBucketName
     *            The documentCosBucketName to set.
     */
    public void setCosBucketName(String cosBucketName) {
        this.cosBucketName = cosBucketName;
    }

    /**
     * Gets the documentCosObjectName of the document.
     *
     * @return The cosObjectName of the document.
     */
    public String getCosObjectName() {
        return cosObjectName;
    }

    /**
     * Sets the cosObjectName
     *
     * @param cosObjectName
     *            The document cosObjectName to set.
     */
    public void setCosObjectName(String cosObjectName) {
        this.cosObjectName = cosObjectName;
    }

    /**
     * Gets the documentStatus of the document.
     *
     * @return The status of the document.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status
     *
     * @param status
     *            The documentStatus to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

}

