package com.trip.Smart.Trip.Planner

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping("/")
    fun hello(): String = "Hello, Smart Trip Planner is running!"
}

