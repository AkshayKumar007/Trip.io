package com.trip.Smart.Trip.Planner.service

import com.trip.Smart.Trip.Planner.model.dto.TripDTO
import com.trip.Smart.Trip.Planner.model.dto.TripSetupDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TripService {

    /**
     * Create a new trip
     */
    fun createTrip(tripSetupDTO: TripSetupDTO): TripDTO

    /**
     * Get trip by ID
     */
    fun getTripById(tripId: Long): TripDTO

    /**
     * Update an existing trip
     */
    fun updateTrip(tripId: Long, tripSetupDTO: TripSetupDTO): TripDTO

    /**
     * Delete a trip by ID
     */
    fun deleteTrip(tripId: Long)

    /**
     * Get all trips with pagination and filtering
     */
    fun getAllTrips(
        destination: String?,
        startDate: String?,
        endDate: String?,
        pageable: Pageable
    ): Page<TripDTO>

    /**
     * Search trips with various criteria
     */
    fun searchTrips(
        query: String?,
        startDate: String?,
        endDate: String?,
        pageable: Pageable
    ): Page<TripDTO>

    /**
     * Get upcoming trips for a user
     */
    fun getUpcomingTrips(userId: String, limit: Int = 5): List<TripDTO>

    /**
     * Get current trips (trips that are happening now)
     */
    fun getCurrentTrips(userId: String): List<TripDTO>

    /**
     * Check if a trip belongs to a user
     */
    fun isTripBelongsToUser(tripId: Long, userId: String): Boolean
}
