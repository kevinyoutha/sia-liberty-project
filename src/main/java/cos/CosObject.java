package cos;
/**
 * Represents an Object stored in Cloudant.
 */
public class CosObject {

  private  String title;
  private  String cosObjectName;
  private  String cosBucketName;
  private  String contentType;
  private  int dataStreamLength;
  private  String status;

  public CosObject() {
    this.title = "";
    this.cosObjectName = "";
    this.cosBucketName = "";
    this.contentType = "";
    this.dataStreamLength = 0;
    this.status = "";
  }

  public CosObject(String title, String fileName, String cosBucketName, String contentType, int dataStreamLength) {
    this.title = title;
    this.cosObjectName = fileName;
    this.cosBucketName = cosBucketName;
    this.contentType = contentType;
    this.dataStreamLength = dataStreamLength;
  }

  /**
   * Gets the title.
   *
   * @return The title.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Gets the title of the object.
   *
   * @return The title of the object.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets the CosObjectName of the object.
   *
   * @return The cosObjectName of the object.
   */
  public String getCosObjectName() {
    return cosObjectName;
  }

  /**
   * Sets the cosObjectName
   *
   * @param cosObjectName
   *            The object cosObjectName to set.
   */
  public void setCosObjectName(String cosObjectName) {
    this.cosObjectName = cosObjectName;
  }

  /**
   * Gets the cosBucketName of the object.
   *
   * @return The cosBucketName of the object.
   */
  public String getCosBucketName() {
    return cosBucketName;
  }

  /**
   * Sets the cosBucketName
   *
   * @param cosBucketName
   *            The object cosObjectName to set.
   */
  public void setCosBucketName(String cosBucketName) {
    this.cosBucketName = cosBucketName;
  }

  /**
   * Gets the ContentType of the object.
   *
   * @return The ContentType of the object.
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * Sets the ContentType
   *
   * @param contentType
   *            The object ContentType to set.
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Gets the dataStreamLength of the object.
   *
   * @return The dataStreamLength of the object.
   */
  public int getDataStreamLength() {
    return dataStreamLength;
  }

  /**
   * Sets the DataStreamLength
   *
   * @param dataStreamLength
   *            The object DataStreamLength to set.
   */
  public void setDataStreamLength(int dataStreamLength) {
    this.dataStreamLength = dataStreamLength;
  }

  /**
   * Gets the status of the object.
   *
   * @return The status of the object.
   */
  public String getStatus() {
    return status;
  }


  /**
   * Sets the status
   *
   * @param status
   *            The object status to set.
   */
  public void setStatus(String status) {
    this.status = status;
  }
}
