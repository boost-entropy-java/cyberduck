/*
 * DeepBox
 * DeepBox API Documentation
 *
 * OpenAPI spec version: 1.0
 * Contact: info@deepcloud.swiss
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package ch.cyberduck.core.deepbox.io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import ch.cyberduck.core.deepbox.io.swagger.client.model.NodePolicy;
import ch.cyberduck.core.deepbox.io.swagger.client.model.NodeWatch;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * PathSegment
 */



public class PathSegment {
  @JsonProperty("nodeId")
  private String nodeId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("displayName")
  private String displayName = null;

  @JsonProperty("mimeType")
  private String mimeType = null;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    FOLDER("folder"),
    FILE("file");

    private String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String input) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(input)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("type")
  private TypeEnum type = null;

  @JsonProperty("policy")
  private NodePolicy policy = null;

  @JsonProperty("hasAccesses")
  private Boolean hasAccesses = null;

  @JsonProperty("watch")
  private NodeWatch watch = null;

  public PathSegment nodeId(String nodeId) {
    this.nodeId = nodeId;
    return this;
  }

   /**
   * Get nodeId
   * @return nodeId
  **/
  @Schema(description = "")
  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public PathSegment name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @Schema(description = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PathSegment displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

   /**
   * Get displayName
   * @return displayName
  **/
  @Schema(description = "")
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public PathSegment mimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

   /**
   * Get mimeType
   * @return mimeType
  **/
  @Schema(description = "")
  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public PathSegment type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @Schema(description = "")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public PathSegment policy(NodePolicy policy) {
    this.policy = policy;
    return this;
  }

   /**
   * Get policy
   * @return policy
  **/
  @Schema(description = "")
  public NodePolicy getPolicy() {
    return policy;
  }

  public void setPolicy(NodePolicy policy) {
    this.policy = policy;
  }

  public PathSegment hasAccesses(Boolean hasAccesses) {
    this.hasAccesses = hasAccesses;
    return this;
  }

   /**
   * Get hasAccesses
   * @return hasAccesses
  **/
  @Schema(description = "")
  public Boolean isHasAccesses() {
    return hasAccesses;
  }

  public void setHasAccesses(Boolean hasAccesses) {
    this.hasAccesses = hasAccesses;
  }

  public PathSegment watch(NodeWatch watch) {
    this.watch = watch;
    return this;
  }

   /**
   * Get watch
   * @return watch
  **/
  @Schema(description = "")
  public NodeWatch getWatch() {
    return watch;
  }

  public void setWatch(NodeWatch watch) {
    this.watch = watch;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PathSegment pathSegment = (PathSegment) o;
    return Objects.equals(this.nodeId, pathSegment.nodeId) &&
        Objects.equals(this.name, pathSegment.name) &&
        Objects.equals(this.displayName, pathSegment.displayName) &&
        Objects.equals(this.mimeType, pathSegment.mimeType) &&
        Objects.equals(this.type, pathSegment.type) &&
        Objects.equals(this.policy, pathSegment.policy) &&
        Objects.equals(this.hasAccesses, pathSegment.hasAccesses) &&
        Objects.equals(this.watch, pathSegment.watch);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeId, name, displayName, mimeType, type, policy, hasAccesses, watch);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PathSegment {\n");
    
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    policy: ").append(toIndentedString(policy)).append("\n");
    sb.append("    hasAccesses: ").append(toIndentedString(hasAccesses)).append("\n");
    sb.append("    watch: ").append(toIndentedString(watch)).append("\n");
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