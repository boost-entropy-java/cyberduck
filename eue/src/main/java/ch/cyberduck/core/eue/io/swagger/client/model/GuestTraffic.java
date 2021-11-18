/*
 * ReSTFS
 * ReSTFS Open API 3.0 Spec
 *
 * OpenAPI spec version: 1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package ch.cyberduck.core.eue.io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * GuestTraffic
 */


public class GuestTraffic {
  @JsonProperty("max")
  private Long max = null;

  @JsonProperty("current")
  private Long current = null;

  public GuestTraffic max(Long max) {
    this.max = max;
    return this;
  }

   /**
   * Get max
   * @return max
  **/
  @Schema(description = "")
  public Long getMax() {
    return max;
  }

  public void setMax(Long max) {
    this.max = max;
  }

  public GuestTraffic current(Long current) {
    this.current = current;
    return this;
  }

   /**
   * Get current
   * @return current
  **/
  @Schema(description = "")
  public Long getCurrent() {
    return current;
  }

  public void setCurrent(Long current) {
    this.current = current;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuestTraffic guestTraffic = (GuestTraffic) o;
    return Objects.equals(this.max, guestTraffic.max) &&
        Objects.equals(this.current, guestTraffic.current);
  }

  @Override
  public int hashCode() {
    return Objects.hash(max, current);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuestTraffic {\n");
    
    sb.append("    max: ").append(toIndentedString(max)).append("\n");
    sb.append("    current: ").append(toIndentedString(current)).append("\n");
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