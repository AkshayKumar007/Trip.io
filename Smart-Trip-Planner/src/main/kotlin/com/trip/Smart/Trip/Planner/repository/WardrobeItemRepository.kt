package com.trip.Smart.Trip.Planner.repository

import com.trip.Smart.Trip.Planner.model.entity.WardrobeItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WardrobeItemRepository : JpaRepository<WardrobeItemEntity, Long> {
    fun findByTripId(tripId: Long): List<WardrobeItemEntity>
    fun findByTripIdAndIsOwnedFalse(tripId: Long): List<WardrobeItemEntity>
}

