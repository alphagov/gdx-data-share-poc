package uk.gov.gdx.datashare.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IngressEventTypeRepository : CoroutineCrudRepository<IngressEventType, String>
