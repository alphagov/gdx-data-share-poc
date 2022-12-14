package uk.gov.gdx.datashare.integration

import com.amazonaws.services.sqs.model.PurgeQueueRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import uk.gov.gdx.datashare.integration.LocalStackContainer.setLocalStackProperties
import uk.gov.gdx.datashare.queue.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SqsIntegrationTestBase : IntegrationTestBase() {

  @Autowired
  private lateinit var awsQueueService: AwsQueueService

  @SpyBean
  protected lateinit var sqsPropertiesSpy: SqsProperties

  @Autowired
  protected lateinit var objectMapper: ObjectMapper

  private val eventTopic by lazy {
    awsQueueService.findByTopicId("event") ?: throw MissingQueueException("Topic event not found")
  }
  protected val eventTopicSnsClient by lazy { eventTopic.snsClient }
  protected val eventTopicArn by lazy { eventTopic.arn }

  protected val auditQueue by lazy { awsQueueService.findByQueueId("audit") as AwsQueue }
  protected val dataProcessorQueue by lazy { awsQueueService.findByQueueId("dataprocessor") as AwsQueue }
  protected val adaptorQueue by lazy { awsQueueService.findByQueueId("adaptor") as AwsQueue }
  protected val odgQueue by lazy { awsQueueService.findByQueueId("odg") as AwsQueue }

  fun SqsProperties.eventTopicConfig() =
    topics["event"] ?: throw MissingTopicException("event has not been loaded from configuration properties")

  @BeforeEach
  fun cleanQueue() {
    auditQueue.sqsClient.purgeQueue(PurgeQueueRequest(auditQueue.queueUrl))
    dataProcessorQueue.sqsClient.purgeQueue(PurgeQueueRequest(dataProcessorQueue.queueUrl))
    adaptorQueue.sqsClient.purgeQueue(PurgeQueueRequest(adaptorQueue.queueUrl))
    odgQueue.sqsClient.purgeQueue(PurgeQueueRequest(odgQueue.queueUrl))
  }

  companion object {
    private val localStackContainer = LocalStackContainer.instance

    @Suppress("unused")
    @JvmStatic
    @DynamicPropertySource
    fun testcontainers(registry: DynamicPropertyRegistry) {
      localStackContainer?.also { setLocalStackProperties(it, registry) }
    }
  }

  protected fun jsonString(any: Any) = objectMapper.writeValueAsString(any) as String

  fun getNumberOfMessagesCurrentlyOnAdaptorQueue(): Int? {
    val queueAttributes =
      adaptorQueue.sqsClient.getQueueAttributes(adaptorQueue.queueUrl, listOf("ApproximateNumberOfMessages"))
    return queueAttributes.attributes["ApproximateNumberOfMessages"]?.toInt()
  }
}
