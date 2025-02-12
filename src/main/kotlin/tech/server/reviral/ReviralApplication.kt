package tech.server.reviral

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class ReviralApplication

fun main(args: Array<String>) {
	runApplication<ReviralApplication>(*args)
}
