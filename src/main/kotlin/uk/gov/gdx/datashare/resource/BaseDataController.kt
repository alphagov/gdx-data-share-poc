package uk.gov.gdx.datashare.resource

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.toList
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import uk.gov.gdx.datashare.repository.*
import java.util.*

@RestController
@RequestMapping("/data", produces = [MediaType.APPLICATION_JSON_VALUE])
//TODO-https://trello.com/c/VgQeakpA: Add admin control
@PreAuthorize("hasAnyAuthority('SCOPE_pubsub/maintain')")
@Validated
@Tag(name = "10. Data")
class BaseDataController(
  private val consumerRepository: ConsumerRepository,
  private val consumerSubscriptionRepository: ConsumerSubscriptionRepository,
  private val egressEventDataRepository: EgressEventDataRepository,
  private val eventDatasetRepository: EventDatasetRepository,
  private val ingressEventDataRepository: IngressEventDataRepository,
  private val ingressEventTypeRepository: IngressEventTypeRepository,
  private val publisherRepository: PublisherRepository,
  private val publisherSubscriptionRepository: PublisherSubscriptionRepository,
) {
  @GetMapping("/consumers")
  @Operation(
    summary = "Get Consumers",
    description = "Need scope of pubsub/maintain",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "Consumers"
      )
    ]
  )
  suspend fun getConsumers() = consumerRepository.findAll().toList()

  @DeleteMapping("/consumers/{id}")
  @Operation(
    summary = "Delete Consumer",
    description = "Need scope of pubsub/maintain",
    responses = [
      ApiResponse(
        responseCode = "204",
        description = "Consumer deleted"
      )
    ]
  )
  suspend fun deleteConsumer(
    @Schema(description = "Consumer ID", required = true)
    @PathVariable id: UUID,
  ) = consumerRepository.deleteById(id)
  
  @GetMapping("/consumerSubscriptions")
  @Operation(
    summary = "Get Consumer Subscriptions",
    description = "Need scope of pubsub/maintain",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "Consumer Subscriptions"
      )
    ]
  )
  suspend fun getConsumerSubscriptions() = consumerSubscriptionRepository.findAll().toList()

  @DeleteMapping("/consumerSubscriptions/{id}")
  @Operation(
    summary = "Delete Consumer Subscription",
    description = "Need scope of pubsub/maintain",
    responses = [
      ApiResponse(
        responseCode = "204",
        description = "Consumer Subscription deleted"
      )
    ]
  )
  suspend fun deleteConsumerSubscription(
    @Schema(description = "Consumer Subscription ID", required = true)
    @PathVariable id: UUID,
  ) = consumerSubscriptionRepository.deleteById(id)
  
  @GetMapping("/egressEvents")
  @Operation(
    summary = "Get Egress Events",
    description = "Need scope of pubsub/maintain",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "Egress Event"
      )
    ]
  )
  suspend fun getEgressEvents() = egressEventDataRepository.findAll().toList()
  
  @DeleteMapping("/egressEvents/{id}")
  @Operation(
    summary = "Delete Egress Event",
    description = "Need scope of pubsub/maintain",
    responses = [
      ApiResponse(
        responseCode = "204",
        description = "Event deleted"
      )
    ]
  )
  suspend fun deleteEgressEvent(
    @Schema(description = "Event ID", required = true)
  @PathVariable id: UUID,
  ) = egressEventDataRepository.deleteById(id)

  @GetMapping("/eventDataset")
  @Operation(
    summary = "Get Egress Datasets",
    description = "Need scope of pubsub/maintain",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "Egress Dataset"
      )
    ]
  )
  suspend fun getEgressDatasets() = eventDatasetRepository.findAll().toList()

  @DeleteMapping("/eventDataset/{id}")
  @Operation(
    summary = "Delete Egress Event",
    description = "Need scope of pubsub/maintain",
    responses = [
      ApiResponse(
        responseCode = "204",
        description = "Event deleted"
      )
    ]
  )
  suspend fun deleteEventDataset(
    @Schema(description = "Event Dataset ID", required = true)
    @PathVariable id: String,
  ) = eventDatasetRepository.deleteById(id)

  @GetMapping("/ingressEvent")
  @Operation(
    summary = "Get Ingress Events",
    description = "Need scope of pubsub/maintain",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "Ingress Event"
      )
    ]
  )
  suspend fun getIngressEvents() = ingressEventDataRepository.findAll().toList()

  @DeleteMapping("/ingressEvent/{id}")
  @Operation(
    summary = "Delete Ingress Event",
    description = "Need scope of pubsub/maintain",
    responses = [
      ApiResponse(
        responseCode = "204",
        description = "Event deleted"
      )
    ]
  )
  suspend fun deleteIngressEvent(
    @Schema(description = "Event ID", required = true)
    @PathVariable id: UUID,
  ) = ingressEventDataRepository.deleteById(id)

  @GetMapping("/ingressType")
  @Operation(
    summary = "Get Ingress Event Types",
    description = "Need scope of pubsub/maintain",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "Ingress Event Type"
      )
    ]
  )
  suspend fun getIngressEventTypes() = ingressEventTypeRepository.findAll().toList()

  @DeleteMapping("/ingressType/{id}")
  @Operation(
    summary = "Delete Ingress Type",
    description = "Need scope of pubsub/maintain",
    responses = [
      ApiResponse(
        responseCode = "204",
        description = "Type deleted"
      )
    ]
  )
  suspend fun deleteIngressEventType(
    @Schema(description = "Type ID", required = true)
    @PathVariable id: String,
  ) = ingressEventTypeRepository.deleteById(id)

  @GetMapping("/publishers")
  @Operation(
    summary = "Get Publishers",
    description = "Need scope of pubsub/maintain",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "Publishers"
      )
    ]
  )
  suspend fun getPublishers() = publisherRepository.findAll().toList()

  @DeleteMapping("/publishers/{id}")
  @Operation(
    summary = "Delete Publisher",
    description = "Need scope of pubsub/maintain",
    responses = [
      ApiResponse(
        responseCode = "204",
        description = "Publisher deleted"
      )
    ]
  )
  suspend fun deletePublisher(
    @Schema(description = "Publisher ID", required = true)
    @PathVariable id: UUID,
  ) = publisherRepository.deleteById(id)

  @GetMapping("/publisherSubscriptions")
  @Operation(
    summary = "Get Publisher Subscriptions",
    description = "Need scope of pubsub/maintain",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "Publisher Subscriptions"
      )
    ]
  )
  suspend fun getPublisherSubscriptions() = publisherSubscriptionRepository.findAll().toList()

  @DeleteMapping("/publisherSubscriptions/{id}")
  @Operation(
    summary = "Delete Publisher Subscription",
    description = "Need scope of pubsub/maintain",
    responses = [
      ApiResponse(
        responseCode = "204",
        description = "Publisher Subscription deleted"
      )
    ]
  )
  suspend fun deletePublisherSubscription(
    @Schema(description = "Publisher Subscription ID", required = true)
    @PathVariable id: UUID,
  ) = publisherSubscriptionRepository.deleteById(id)
}