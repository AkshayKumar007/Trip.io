package com.trip.Smart.Trip.Planner.service.impl

import com.trip.Smart.Trip.Planner.exception.ResourceNotFoundException
import com.trip.Smart.Trip.Planner.model.dto.TripDTO
import com.trip.Smart.Trip.Planner.model.dto.TripSetupDTO
import com.trip.Smart.Trip.Planner.model.entity.TripEntity
import com.trip.Smart.Trip.Planner.repository.TripRepository
import com.trip.Smart.Trip.Planner.service.TripService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Service
class TripServiceImpl(
    private val tripRepository: TripRepository
) : TripService {

    private val logger = LoggerFactory.getLogger(TripServiceImpl::class.java)
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    @Transactional
    override fun createTrip(tripSetupDTO: TripSetupDTO): TripDTO {
        logger.info("Creating new trip for destination: ${tripSetupDTO.destination}")
        
        try {
            val tripEntity = TripEntity(
                tripType = tripSetupDTO.tripType ?: "",
                destination = tripSetupDTO.destination ?: "",
                startDate = tripSetupDTO.startDate ?: throw IllegalArgumentException("Start date is required"),
                endDate = tripSetupDTO.endDate ?: throw IllegalArgumentException("End date is required"),
                numDays = tripSetupDTO.numDays ?: calculateNumDays(tripSetupDTO.startDate, tripSetupDTO.endDate)
                    ?: throw IllegalArgumentException("Could not calculate number of days"),
                numPeople = tripSetupDTO.numPeople ?: 1,
                notes = tripSetupDTO.notes
            )

            val savedTrip = tripRepository.save(tripEntity)
            logger.info("Trip created with ID: ${savedTrip.id}")
            return mapToTripDTO(savedTrip)
        } catch (ex: Exception) {
            logger.error("Error creating trip: ${ex.message}", ex)
            throw RuntimeException("Failed to create trip: ${ex.message}", ex)
        }
    }

    @Transactional(readOnly = true)
    override fun getTripById(tripId: Long): TripDTO {
        logger.info("Fetching trip with ID: $tripId")
        val tripEntity = tripRepository.findById(tripId)
            .orElseThrow { ResourceNotFoundException("Trip not found with ID: $tripId") }
        return mapToTripDTO(tripEntity)
    }

    @Transactional
    override fun updateTrip(tripId: Long, tripSetupDTO: TripSetupDTO): TripDTO {
        logger.info("Updating trip with ID: $tripId")
        val existingTrip = tripRepository.findById(tripId)
            .orElseThrow { ResourceNotFoundException("Trip not found with ID: $tripId") }

        val updatedTrip = existingTrip.copy(
            tripType = tripSetupDTO.tripType ?: existingTrip.tripType,
            destination = tripSetupDTO.destination ?: existingTrip.destination,
            startDate = tripSetupDTO.startDate ?: existingTrip.startDate,
            endDate = tripSetupDTO.endDate ?: existingTrip.endDate,
            numDays = tripSetupDTO.numDays ?: existingTrip.numDays ?: 
                calculateNumDays(tripSetupDTO.startDate ?: existingTrip.startDate, 
                               tripSetupDTO.endDate ?: existingTrip.endDate),
            numPeople = tripSetupDTO.numPeople ?: existingTrip.numPeople,
            notes = tripSetupDTO.notes ?: existingTrip.notes,
            updatedAt = LocalDateTime.now()
        )

        val savedTrip = tripRepository.save(updatedTrip)
        logger.info("Trip with ID: $tripId updated successfully")
        return mapToTripDTO(savedTrip)
    }

    @Transactional
    override fun deleteTrip(tripId: Long) {
        logger.info("Deleting trip with ID: $tripId")
        if (!tripRepository.existsById(tripId)) {
            throw ResourceNotFoundException("Trip not found with ID: $tripId")
        }
        tripRepository.deleteById(tripId)
        logger.info("Trip with ID: $tripId deleted successfully")
    }

    @Transactional(readOnly = true)
    override fun getAllTrips(
        destination: String?,
        startDate: String?,
        endDate: String?,
        pageable: Pageable
    ): Page<TripDTO> {
        logger.info("Fetching all trips with filters - destination: $destination, startDate: $startDate, endDate: $endDate")
        
        val parsedStartDate = startDate?.let { LocalDate.parse(it, dateFormatter) }
        val parsedEndDate = endDate?.let { LocalDate.parse(it, dateFormatter) }
        
        return tripRepository.searchTrips(
            destination = destination,
            startDate = parsedStartDate,
            endDate = parsedEndDate,
            pageable = pageable
        ).map { mapToTripDTO(it) }
    }

    @Transactional(readOnly = true)
    override fun searchTrips(
        query: String?,
        startDate: String?,
        endDate: String?,
        pageable: Pageable
    ): Page<TripDTO> {
        logger.info("Searching trips with query: $query, startDate: $startDate, endDate: $endDate")
        
        val parsedStartDate = startDate?.let { LocalDate.parse(it, dateFormatter) }
        val parsedEndDate = endDate?.let { LocalDate.parse(it, dateFormatter) }
        
        return if (query.isNullOrBlank()) {
            tripRepository.searchTrips(
                destination = null,
                startDate = parsedStartDate,
                endDate = parsedEndDate,
                pageable = pageable
            ).map { mapToTripDTO(it) }
        } else {
            tripRepository.findByDestinationContainingIgnoreCase(query, pageable)
                .map { mapToTripDTO(it) }
        }
    }

    @Transactional(readOnly = true)
    override fun getUpcomingTrips(userId: String, limit: Int): List<TripDTO> {
        logger.info("Fetching upcoming trip for user: $userId")
        return try {
            val upcomingTrip = tripRepository.findUpcomingTrip(userId)
            if (upcomingTrip != null) {
                listOf(mapToTripDTO(upcomingTrip))
            } else {
                emptyList()
            }
        } catch (ex: Exception) {
            logger.error("Error fetching upcoming trip for user $userId: ${ex.message}", ex)
            emptyList()
        }
    }

    @Transactional(readOnly = true)
    override fun getCurrentTrips(userId: String): List<TripDTO> {
        logger.info("Fetching current trips for user: $userId")
        return try {
            tripRepository.findCurrentTrips(userId).map { mapToTripDTO(it) }
        } catch (ex: Exception) {
            logger.error("Error fetching current trips for user $userId: ${ex.message}", ex)
            emptyList()
        }
    }

    @Transactional(readOnly = true)
    override fun isTripBelongsToUser(tripId: Long, userId: String): Boolean {
        logger.info("Checking if trip $tripId belongs to user $userId")
        return try {
            tripRepository.existsByIdAndUserId(tripId, userId)
        } catch (ex: Exception) {
            logger.error("Error checking trip ownership: ${ex.message}", ex)
            false
        }
    }

    private fun mapToTripDTO(tripEntity: TripEntity): TripDTO {
        return TripDTO(
            id = tripEntity.id!!,
            // Since TripEntity doesn't have a userId field, we're using a default value
            // In a real application, you might want to add userId to TripEntity
            // or handle this differently based on your requirements
            userId = "",
            tripType = tripEntity.tripType ?: "",
            destination = tripEntity.destination ?: "",
            startDate = tripEntity.startDate,
            endDate = tripEntity.endDate,
            numDays = tripEntity.numDays ?: 0,
            numPeople = tripEntity.numPeople ?: 1,
            notes = tripEntity.notes,
            budget = null, // Not in TripEntity
            currency = null, // Not in TripEntity
            createdAt = tripEntity.createdAt?.format(dateTimeFormatter) ?: "",
            updatedAt = tripEntity.updatedAt?.format(dateTimeFormatter) ?: ""
        )
    }


    private fun calculateNumDays(startDate: LocalDate?, endDate: LocalDate?): Int? {
        return if (startDate != null && endDate != null && !endDate.isBefore(startDate)) {
            ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1
        } else {
            null
        }
    }
}
