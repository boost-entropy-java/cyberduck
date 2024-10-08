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
import ch.cyberduck.core.deepbox.io.swagger.client.model.CompanyEntry;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * BoxEntry
 */



public class BoxEntry {
  @JsonProperty("company")
  private CompanyEntry company = null;

  @JsonProperty("deepBoxNodeId")
  private String deepBoxNodeId = null;

  @JsonProperty("deepBoxName")
  private String deepBoxName = null;

  @JsonProperty("boxNodeId")
  private String boxNodeId = null;

  @JsonProperty("boxName")
  private String boxName = null;

  @JsonProperty("boxType")
  private String boxType = null;

  @JsonProperty("queueCount")
  private Integer queueCount = null;

  @JsonProperty("favorite")
  private Boolean favorite = null;

  public BoxEntry company(CompanyEntry company) {
    this.company = company;
    return this;
  }

   /**
   * Get company
   * @return company
  **/
  @Schema(description = "")
  public CompanyEntry getCompany() {
    return company;
  }

  public void setCompany(CompanyEntry company) {
    this.company = company;
  }

  public BoxEntry deepBoxNodeId(String deepBoxNodeId) {
    this.deepBoxNodeId = deepBoxNodeId;
    return this;
  }

   /**
   * Get deepBoxNodeId
   * @return deepBoxNodeId
  **/
  @Schema(description = "")
  public String getDeepBoxNodeId() {
    return deepBoxNodeId;
  }

  public void setDeepBoxNodeId(String deepBoxNodeId) {
    this.deepBoxNodeId = deepBoxNodeId;
  }

  public BoxEntry deepBoxName(String deepBoxName) {
    this.deepBoxName = deepBoxName;
    return this;
  }

   /**
   * Get deepBoxName
   * @return deepBoxName
  **/
  @Schema(description = "")
  public String getDeepBoxName() {
    return deepBoxName;
  }

  public void setDeepBoxName(String deepBoxName) {
    this.deepBoxName = deepBoxName;
  }

  public BoxEntry boxNodeId(String boxNodeId) {
    this.boxNodeId = boxNodeId;
    return this;
  }

   /**
   * Get boxNodeId
   * @return boxNodeId
  **/
  @Schema(description = "")
  public String getBoxNodeId() {
    return boxNodeId;
  }

  public void setBoxNodeId(String boxNodeId) {
    this.boxNodeId = boxNodeId;
  }

  public BoxEntry boxName(String boxName) {
    this.boxName = boxName;
    return this;
  }

   /**
   * Get boxName
   * @return boxName
  **/
  @Schema(description = "")
  public String getBoxName() {
    return boxName;
  }

  public void setBoxName(String boxName) {
    this.boxName = boxName;
  }

  public BoxEntry boxType(String boxType) {
    this.boxType = boxType;
    return this;
  }

   /**
   * Get boxType
   * @return boxType
  **/
  @Schema(description = "")
  public String getBoxType() {
    return boxType;
  }

  public void setBoxType(String boxType) {
    this.boxType = boxType;
  }

  public BoxEntry queueCount(Integer queueCount) {
    this.queueCount = queueCount;
    return this;
  }

   /**
   * Get queueCount
   * @return queueCount
  **/
  @Schema(description = "")
  public Integer getQueueCount() {
    return queueCount;
  }

  public void setQueueCount(Integer queueCount) {
    this.queueCount = queueCount;
  }

  public BoxEntry favorite(Boolean favorite) {
    this.favorite = favorite;
    return this;
  }

   /**
   * Get favorite
   * @return favorite
  **/
  @Schema(description = "")
  public Boolean isFavorite() {
    return favorite;
  }

  public void setFavorite(Boolean favorite) {
    this.favorite = favorite;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoxEntry boxEntry = (BoxEntry) o;
    return Objects.equals(this.company, boxEntry.company) &&
        Objects.equals(this.deepBoxNodeId, boxEntry.deepBoxNodeId) &&
        Objects.equals(this.deepBoxName, boxEntry.deepBoxName) &&
        Objects.equals(this.boxNodeId, boxEntry.boxNodeId) &&
        Objects.equals(this.boxName, boxEntry.boxName) &&
        Objects.equals(this.boxType, boxEntry.boxType) &&
        Objects.equals(this.queueCount, boxEntry.queueCount) &&
        Objects.equals(this.favorite, boxEntry.favorite);
  }

  @Override
  public int hashCode() {
    return Objects.hash(company, deepBoxNodeId, deepBoxName, boxNodeId, boxName, boxType, queueCount, favorite);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoxEntry {\n");
    
    sb.append("    company: ").append(toIndentedString(company)).append("\n");
    sb.append("    deepBoxNodeId: ").append(toIndentedString(deepBoxNodeId)).append("\n");
    sb.append("    deepBoxName: ").append(toIndentedString(deepBoxName)).append("\n");
    sb.append("    boxNodeId: ").append(toIndentedString(boxNodeId)).append("\n");
    sb.append("    boxName: ").append(toIndentedString(boxName)).append("\n");
    sb.append("    boxType: ").append(toIndentedString(boxType)).append("\n");
    sb.append("    queueCount: ").append(toIndentedString(queueCount)).append("\n");
    sb.append("    favorite: ").append(toIndentedString(favorite)).append("\n");
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
