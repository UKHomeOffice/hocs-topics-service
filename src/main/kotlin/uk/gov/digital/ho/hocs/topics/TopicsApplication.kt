package uk.gov.digital.ho.hocs.topics

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TopicsApplication

fun main(args: Array<String>) {
    runApplication<TopicsApplication>(*args)
}
