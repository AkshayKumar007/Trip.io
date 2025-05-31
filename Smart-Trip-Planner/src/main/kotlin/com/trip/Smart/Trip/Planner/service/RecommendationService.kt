package com.trip.Smart.Trip.Planner.service

import com.trip.Smart.Trip.Planner.model.dto.AccommodationDTO
import com.trip.Smart.Trip.Planner.model.dto.TravelOptionDTO

/**
 * Service interface for providing travel and accommodation recommendations.
 * Offers methods to get personalized recommendations for accommodations and travel options
 * based on various parameters like destination, dates, and number of people.
 */
interface RecommendationService {

    /**
     * Retrieves accommodation recommendations for a given destination and travel dates.
     *
     * @param destination The destination city or location
     * @param startDate The check-in date in string format (e.g., "2023-12-01")
     * @param endDate The check-out date in string format (e.g., "2023-12-07")
     * @param numPeople Number of people for the accommodation
     * @return List of recommended accommodations with details
     */
    fun getAccommodationRecommendations(
        destination: String,
        startDate: String,
        endDate: String,
        numPeople: Int
    ): List<AccommodationDTO>

    /**
     * Retrieves travel recommendations between two locations for a specific date.
     *
     * @param origin The starting location (city/airport code)
     * @param destination The destination location (city/airport code)
     * @param travelDate The date of travel in string format (e.g., "2023-12-01")
     * @param numPeople Number of people traveling
     * @return List of recommended travel options with details
     */
    fun getTravelRecommendations(
        origin: String,
        destination: String,
        travelDate: String,
        numPeople: Int
    ): List<TravelOptionDTO>
}