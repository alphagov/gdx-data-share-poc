package uk.gov.gdx.datashare.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.time.LocalDate

@Service
class HmrcApiService(
  private val hmrcApiWebClient: WebClient
) {

  suspend fun findNiNoByNameAndDob(surname: String, firstname: String, dob: LocalDate) =
    hmrcApiWebClient.get()
      .uri("/hmrc/surname/$surname/firstname/$firstname/dob/$dob")
      .retrieve()
      .bodyToMono<NinoRecord>()
}

data class NinoRecord(
  val id: Long,
  val ni_number: String,
)