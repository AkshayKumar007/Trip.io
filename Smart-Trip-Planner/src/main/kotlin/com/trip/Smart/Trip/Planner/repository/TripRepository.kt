package com.trip.Smart.Trip.Planner.repository

import com.trip.Smart.Trip.Planner.model.entity.TripEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface TripRepository : BaseRepository<TripEntity, Long> {
    
    fun findByUserId(userId: String, pageable: Pageable): Page<TripEntity>
    
    fun findByDestinationContainingIgnoreCase(destination: String, pageable: Pageable): Page<TripEntity>
    
    fun findByStartDateBetween(startDate: LocalDate, endDate: LocalDate, pageable: Pageable): Page<TripEntity>
    
    @Query("""
        SELECT t FROM TripEntity t 
        WHERE (:destination IS NULL OR LOWER(t.destination) LIKE LOWER(CONCAT('%', :destination, '%')))
        AND (:startDate IS NULL OR t.startDate >= :startDate)
        AND (:endDate IS NULL OR t.endDate <= :endDate)
    """)
    fun searchTrips(
        @Param("destination") destination: String?,
        @Param("startDate") startDate: LocalDate?,
        @Param("endDate") endDate: LocalDate?,
        pageable: Pageable
    ): Page<TripEntity>
    
    fun existsByUserIdAndId(userId: String, tripId: Long): Boolean
    
    @Query("SELECT COUNT(t) > 0 FROM TripEntity t WHERE t.id = :tripId AND t.userId = :userId")
    fun existsByIdAndUserId(@Param("tripId") tripId: Long, @Param("userId") userId: String): Boolean
    
    @Query("""
        SELECT t FROM TripEntity t 
        WHERE t.userId = :userId 
        AND (t.startDate <= CURRENT_DATE AND t.endDate >= CURRENT_DATE)
    """)
    fun findCurrentTrips(@Param("userId") userId: String): List<TripEntity>
    
    @Query("""
        SELECT t FROM TripEntity t 
        WHERE t.userId = :userId 
        AND t.startDate > CURRENT_DATE
        ORDER BY t.startDate ASC
        LIMIT 1
    """)
    fun findUpcomingTrip(@Param("userId") userId: String): TripEntity?
}
