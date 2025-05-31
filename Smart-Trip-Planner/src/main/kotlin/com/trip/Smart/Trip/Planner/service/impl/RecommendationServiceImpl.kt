package com.trip.Smart.Trip.Planner.service.impl

import com.trip.Smart.Trip.Planner.model.dto.AccommodationDTO
import com.trip.Smart.Trip.Planner.model.dto.TravelOptionDTO
import com.trip.Smart.Trip.Planner.service.RecommendationService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RecommendationServiceImpl : RecommendationService {

    private val logger = LoggerFactory.getLogger(RecommendationServiceImpl::class.java)

    override fun getAccommodationRecommendations(
        destination: String,
        startDate: String,
        endDate: String,
        numPeople: Int
    ): List<AccommodationDTO> {
        logger.info("Fetching mock accommodation recommendations for $destination")
        // Mock data - in a real app, this would call external APIs or a database
        return listOf(
            AccommodationDTO(
                name = "The Grand Hotel $destination",
                type = "Hotel",
                priceEstimate = "$150-250/night",
                rating = 4.5,
                simulatedBookingLink = "https://example.com/book/hotel/grand-${destination.lowercase().replace(" ", "-")}"
            ),
            AccommodationDTO(
                name = "Cozy Hostel $destination",
                type = "Hostel",
                priceEstimate = "$30-50/night",
                rating = 4.0,
                simulatedBookingLink = "https://example.com/book/hostel/cozy-${destination.lowercase().replace(" ", "-")}"
            ),
            AccommodationDTO(
                name = "$destination Central Apartments",
                type = "Airbnb",
                priceEstimate = "$100-180/night for $numPeople people",
                rating = 4.8,
                simulatedBookingLink = "https://example.com/book/airbnb/${destination.lowercase().replace(" ", "-")}-central"
            )
        )
    }

    override fun getTravelRecommendations(
        origin: String,
        destination: String,
        travelDate: String,
        numPeople: Int
    ): List<TravelOptionDTO> {
        logger.info("Fetching mock travel recommendations from $origin to $destination on $travelDate")
        // Mock data
        return listOf(
            TravelOptionDTO(
                modeOfTransport = "Flight",
                providerName = "AirExample",
                durationEstimate = "3h 45m",
                priceEstimate = "$200-400 per person",
                simulatedBookingLink = "https://example.com/book/flight/$origin/$destination"
            ),
            TravelOptionDTO(
                modeOfTransport = "Train",
                providerName = "RailWays Inc.",
                durationEstimate = "6h 15m",
                priceEstimate = "$80-150 per person",
                simulatedBookingLink = "https://example.com/book/train/$origin/$destination"
            ),
            TravelOptionDTO(
                modeOfTransport = "Bus",
                providerName = "ComfortBus Lines",
                durationEstimate = "8h 30m",
                priceEstimate = "$40-70 per person",
                simulatedBookingLink = "https://example.com/book/bus/$origin/$destination"
            )
        )
    }
}

