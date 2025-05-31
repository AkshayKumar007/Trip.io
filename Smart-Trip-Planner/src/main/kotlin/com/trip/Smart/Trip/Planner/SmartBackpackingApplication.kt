package com.smartbackpacking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class SmartBackpackingApplication

fun main(args: Array<String>) {
    runApplication<SmartBackpackingApplication>(*args)
}

