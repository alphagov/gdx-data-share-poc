package uk.gov.gdx.datashare.resource

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import uk.gov.gdx.datashare.service.DataReceiverService
import java.time.LocalDateTime

@RestController
@RequestMapping("/event-data-receiver", produces = [ MediaType.APPLICATION_JSON_VALUE])
@PreAuthorize("hasAnyAuthority('SCOPE_data_receiver/notify')")
class EventListener(
  private val dataReceiverService: DataReceiverService
) {

  @PostMapping
  @Operation(
    summary = "Send events to GDS - The 'Source' of the event - this could be HMPO or DWP for example",
    description = "Scope is data_receiver/notify",
    responses = [
      ApiResponse(
        responseCode = "201",
        description = "Data Accepted"
      )
    ]
  )
  suspend fun publishEvent(
    @Schema(
      description = "Event Payload",
      required = true,
      implementation = EventToPublish::class,
    )
    @RequestBody eventPayload: EventToPublish,
  ) = dataReceiverService.sendToDataProcessor(eventPayload)
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Event Payload for GDX")
data class EventToPublish(

  @Schema(description = "Type of event", required = true, example = "DEATH_NOTIFICATION")
  val eventType: EventType,

  @Schema(description = "Date and time when the event took place, default is now", required = false, type="date-time", example = "2021-12-31T12:34:56.789012")
  val eventTime: LocalDateTime? = null,

  @Schema(description = "ID that references the event (optional)", required = false, example = "10a1bc74-3f81-44ef-af5f-662776950d80")
  val id: String? = null,

  @Schema(description = "Json payload of data, normally no additional data would be sent", required = false, example = "{'data': 'payload'}")
  val eventDetails: String? = null,
)

enum class EventType {
  DEATH_NOTIFICATION,
  BIRTH_NOTIFICATION,
  MARRIAGE_NOTIFICATION
}