package uk.gov.gdx.datashare.repository

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import java.time.LocalDateTime
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EventPublisher(
  @Id
  @JvmField
  @Schema(description = "Publisher ID", required = true, example = "1")
  val id: UUID,
  @Schema(description = "Publisher Name", required = true, example = "HMPO")
  val publisherName: String,
  val whenCreated: LocalDateTime? = null,

  @Transient
  @Value("false")
  @JsonIgnore
  val new: Boolean = true

) : Persistable<UUID> {
  override fun getId(): UUID = id

  override fun isNew(): Boolean = new
}
