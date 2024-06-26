/*
 * Files.com API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 0.0.1
 * Contact: support@files.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package ch.cyberduck.core.brick.io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import org.joda.time.DateTime;
/**
 * List External Events
 */
@Schema(description = "List External Events")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-07-25T22:25:43.390877+02:00[Europe/Paris]")
public class ExternalEventEntity {
  @JsonProperty("id")
  private Integer id = null;

  /**
   * Type of event being recorded.
   */
  public enum EventTypeEnum {
    LDAP_SYNC("ldap_sync"),
    REMOTE_SERVER_SYNC("remote_server_sync"),
    LOCKOUT("lockout"),
    LDAP_LOGIN("ldap_login"),
    SAML_LOGIN("saml_login"),
    CLIENT_LOG("client_log");

    private String value;

    EventTypeEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static EventTypeEnum fromValue(String text) {
      for (EventTypeEnum b : EventTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("event_type")
  private EventTypeEnum eventType = null;

  /**
   * Status of event.
   */
  public enum StatusEnum {
    SUCCESS("success"),
    ERROR("error"),
    PARTIAL_FAILURE("partial_failure");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("status")
  private StatusEnum status = null;

  @JsonProperty("body")
  private String body = null;

  @JsonProperty("created_at")
  private DateTime createdAt = null;

  @JsonProperty("body_url")
  private String bodyUrl = null;

  @JsonProperty("folder_behavior_id")
  private Integer folderBehaviorId = null;

  @JsonProperty("successful_files")
  private Integer successfulFiles = null;

  @JsonProperty("errored_files")
  private Integer erroredFiles = null;

  @JsonProperty("bytes_synced")
  private Integer bytesSynced = null;

  @JsonProperty("remote_server_type")
  private String remoteServerType = null;

  public ExternalEventEntity id(Integer id) {
    this.id = id;
    return this;
  }

   /**
   * Event ID
   * @return id
  **/
  @Schema(example = "1", description = "Event ID")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public ExternalEventEntity eventType(EventTypeEnum eventType) {
    this.eventType = eventType;
    return this;
  }

   /**
   * Type of event being recorded.
   * @return eventType
  **/
  @Schema(description = "Type of event being recorded.")
  public EventTypeEnum getEventType() {
    return eventType;
  }

  public void setEventType(EventTypeEnum eventType) {
    this.eventType = eventType;
  }

  public ExternalEventEntity status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Status of event.
   * @return status
  **/
  @Schema(description = "Status of event.")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public ExternalEventEntity body(String body) {
    this.body = body;
    return this;
  }

   /**
   * Event body
   * @return body
  **/
  @Schema(description = "Event body")
  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public ExternalEventEntity createdAt(DateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

   /**
   * External event create date/time
   * @return createdAt
  **/
  @Schema(description = "External event create date/time")
  public DateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(DateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ExternalEventEntity bodyUrl(String bodyUrl) {
    this.bodyUrl = bodyUrl;
    return this;
  }

   /**
   * Link to log file.
   * @return bodyUrl
  **/
  @Schema(description = "Link to log file.")
  public String getBodyUrl() {
    return bodyUrl;
  }

  public void setBodyUrl(String bodyUrl) {
    this.bodyUrl = bodyUrl;
  }

  public ExternalEventEntity folderBehaviorId(Integer folderBehaviorId) {
    this.folderBehaviorId = folderBehaviorId;
    return this;
  }

   /**
   * Folder Behavior ID
   * @return folderBehaviorId
  **/
  @Schema(example = "1", description = "Folder Behavior ID")
  public Integer getFolderBehaviorId() {
    return folderBehaviorId;
  }

  public void setFolderBehaviorId(Integer folderBehaviorId) {
    this.folderBehaviorId = folderBehaviorId;
  }

  public ExternalEventEntity successfulFiles(Integer successfulFiles) {
    this.successfulFiles = successfulFiles;
    return this;
  }

   /**
   * For sync events, the number of files handled successfully.
   * @return successfulFiles
  **/
  @Schema(example = "1", description = "For sync events, the number of files handled successfully.")
  public Integer getSuccessfulFiles() {
    return successfulFiles;
  }

  public void setSuccessfulFiles(Integer successfulFiles) {
    this.successfulFiles = successfulFiles;
  }

  public ExternalEventEntity erroredFiles(Integer erroredFiles) {
    this.erroredFiles = erroredFiles;
    return this;
  }

   /**
   * For sync events, the number of files that encountered errors.
   * @return erroredFiles
  **/
  @Schema(example = "1", description = "For sync events, the number of files that encountered errors.")
  public Integer getErroredFiles() {
    return erroredFiles;
  }

  public void setErroredFiles(Integer erroredFiles) {
    this.erroredFiles = erroredFiles;
  }

  public ExternalEventEntity bytesSynced(Integer bytesSynced) {
    this.bytesSynced = bytesSynced;
    return this;
  }

   /**
   * For sync events, the total number of bytes synced.
   * @return bytesSynced
  **/
  @Schema(example = "1", description = "For sync events, the total number of bytes synced.")
  public Integer getBytesSynced() {
    return bytesSynced;
  }

  public void setBytesSynced(Integer bytesSynced) {
    this.bytesSynced = bytesSynced;
  }

  public ExternalEventEntity remoteServerType(String remoteServerType) {
    this.remoteServerType = remoteServerType;
    return this;
  }

   /**
   * Associated Remote Server type, if any
   * @return remoteServerType
  **/
  @Schema(description = "Associated Remote Server type, if any")
  public String getRemoteServerType() {
    return remoteServerType;
  }

  public void setRemoteServerType(String remoteServerType) {
    this.remoteServerType = remoteServerType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExternalEventEntity externalEventEntity = (ExternalEventEntity) o;
    return Objects.equals(this.id, externalEventEntity.id) &&
        Objects.equals(this.eventType, externalEventEntity.eventType) &&
        Objects.equals(this.status, externalEventEntity.status) &&
        Objects.equals(this.body, externalEventEntity.body) &&
        Objects.equals(this.createdAt, externalEventEntity.createdAt) &&
        Objects.equals(this.bodyUrl, externalEventEntity.bodyUrl) &&
        Objects.equals(this.folderBehaviorId, externalEventEntity.folderBehaviorId) &&
        Objects.equals(this.successfulFiles, externalEventEntity.successfulFiles) &&
        Objects.equals(this.erroredFiles, externalEventEntity.erroredFiles) &&
        Objects.equals(this.bytesSynced, externalEventEntity.bytesSynced) &&
        Objects.equals(this.remoteServerType, externalEventEntity.remoteServerType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, eventType, status, body, createdAt, bodyUrl, folderBehaviorId, successfulFiles, erroredFiles, bytesSynced, remoteServerType);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExternalEventEntity {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    bodyUrl: ").append(toIndentedString(bodyUrl)).append("\n");
    sb.append("    folderBehaviorId: ").append(toIndentedString(folderBehaviorId)).append("\n");
    sb.append("    successfulFiles: ").append(toIndentedString(successfulFiles)).append("\n");
    sb.append("    erroredFiles: ").append(toIndentedString(erroredFiles)).append("\n");
    sb.append("    bytesSynced: ").append(toIndentedString(bytesSynced)).append("\n");
    sb.append("    remoteServerType: ").append(toIndentedString(remoteServerType)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
