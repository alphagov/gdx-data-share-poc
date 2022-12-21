package uk.gov.gdx.datashare.service

import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class HmrcApiService() {

  fun generateNiNoFromNameAndDob(surname: String, firstname: String, dob: LocalDate): NinoRecord {
    val uniqueIdentifier = (firstname + surname + dob.toString()).hashCode().toLong()
    val id = UUID(uniqueIdentifier, 0)
    val niNumber = surname.substring(0, 1) + firstname.substring(0, 1) + dob.format(DateTimeFormatter.BASIC_ISO_DATE)
      .substring(2) + surname.substring(surname.length - 1)
    return NinoRecord(id = id, ni_number = niNumber)
  }
}

data class NinoRecord(
  val id: UUID,
  val ni_number: String,
)
