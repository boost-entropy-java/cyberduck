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
import ch.cyberduck.core.deepbox.io.swagger.client.model.UserContext;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import org.joda.time.DateTime;
/**
 * TimeUserContext
 */



public class TimeUserContext {
  @JsonProperty("time")
  private DateTime time = null;

  @JsonProperty("user")
  private UserContext user = null;

  public TimeUserContext time(DateTime time) {
    this.time = time;
    return this;
  }

   /**
   * Get time
   * @return time
  **/
  @Schema(description = "")
  public DateTime getTime() {
    return time;
  }

  public void setTime(DateTime time) {
    this.time = time;
  }

  public TimeUserContext user(UserContext user) {
    this.user = user;
    return this;
  }

   /**
   * Get user
   * @return user
  **/
  @Schema(description = "")
  public UserContext getUser() {
    return user;
  }

  public void setUser(UserContext user) {
    this.user = user;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimeUserContext timeUserContext = (TimeUserContext) o;
    return Objects.equals(this.time, timeUserContext.time) &&
        Objects.equals(this.user, timeUserContext.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(time, user);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimeUserContext {\n");
    
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
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
