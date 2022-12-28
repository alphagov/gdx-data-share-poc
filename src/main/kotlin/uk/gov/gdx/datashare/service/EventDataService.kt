package uk.gov.gdx.datashare.service

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.webjars.NotFoundException
import uk.gov.gdx.datashare.config.AuthenticationFacade
import uk.gov.gdx.datashare.repository.EgressEventDataRepository
import uk.gov.gdx.datashare.repository.EgressEventTypeRepository
import uk.gov.gdx.datashare.repository.IngressEventDataRepository
import java.time.LocalDateTime
import java.util.*

@Service
class EventDataService(
  private val authenticationFacade: AuthenticationFacade,
  private val egressEventDataRepository: EgressEventDataRepository,
  private val egressEventTypeRepository: EgressEventTypeRepository,
  private val ingressEventDataRepository: IngressEventDataRepository,
  private val deathNotificationService: DeathNotificationService
) {
  @Transactional
  suspend fun deleteEvent(id: UUID) {
    val pollingClientId = authenticationFacade.getUsername()
    val egressEvent = egressEventDataRepository.findByPollerClientIdAndId(pollingClientId, id)
      ?: throw NotFoundException("Egress event $id not found for polling client $pollingClientId")

    egressEventDataRepository.deleteById(egressEvent.id)

    val remainingEvents = egressEventDataRepository.findAllByIngressEventId(egressEvent.ingressEventId).toList()
    if (remainingEvents.isEmpty()) {
      ingressEventDataRepository.deleteById(egressEvent.ingressEventId)
    }
  }

  @Transactional
  suspend fun getEvents(
    eventTypes: List<String>?,
    optionalStartTime: LocalDateTime?,
    optionalEndTime: LocalDateTime?
  ): List<EventNotification> {
    val startTime = optionalStartTime ?: LocalDateTime.of(2000, 1, 1, 12, 0)
    val endTime = optionalEndTime ?: LocalDateTime.now()
    val clientId = authenticationFacade.getUsername()

    val egressEventTypeIds = eventTypes?.let {
      egressEventTypeRepository.findAllByIngressEventTypesAndClient(clientId, eventTypes)
        .map { it.eventTypeId }
        .toList()
    }

    val egressEvents = egressEventTypeIds?.let {
      egressEventDataRepository.findAllByEventTypes(egressEventTypeIds, startTime, endTime)
    } ?: egressEventDataRepository.findAllByPollerClientId(clientId, startTime, endTime)

    return egressEvents.map {
      EventNotification(
        eventId = it.id,
        eventType = "asd",
        sourceId = it.dataId,
        eventData = it.dataPayload?.let { dataPayload ->
          deathNotificationService.mapDeathNotification(dataPayload)
        },
      )
    }.toList()
  }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Subscribed event notification")
data class EventNotification(
  @Schema(description = "Event ID (UUID)", required = true, example = "d8a6f3ba-e915-4e79-8479-f5f5830f4622")
  val eventId: UUID,
  @Schema(
    description = "Event's Type",
    required = true,
    example = "DEATH_NOTIFICATION",
    allowableValues = ["DEATH_NOTIFICATION", "LIFE_EVENT"]
  )
  val eventType: String,
  @Schema(description = "ID from the source of the notification", required = true, example = "999999901")
  val sourceId: String,
  @Schema(description = "Event Data", required = false)
  val eventData: Any?
)