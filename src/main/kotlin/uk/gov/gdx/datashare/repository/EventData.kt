package uk.gov.gdx.datashare.repository

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import java.time.LocalDateTime
import java.util.*

data class EventData(
  @Id
  val eventId: String,
  val eventTypeId: String,
  val datasetId: String,
  val subscriptionId: UUID,
  val dataId: String,
  val dataPayload: String?,
  val dataExpiryTime: LocalDateTime,
  val whenCreated: LocalDateTime? = null,

  @Transient
  @Value("false")
  val new: Boolean = true

) : Persistable<String> {

  override fun getId(): String = eventId

  override fun isNew(): Boolean = new
}
