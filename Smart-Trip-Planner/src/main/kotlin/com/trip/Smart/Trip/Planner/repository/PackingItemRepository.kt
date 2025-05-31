package com.trip.Smart.Trip.Planner.repository

import com.trip.Smart.Trip.Planner.model.entity.PackingItemEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PackingItemRepository : BaseRepository<PackingItemEntity, Long> {
    
    fun findByTripId(tripId: Long): List<PackingItemEntity>
    
    fun findByTripIdAndCategory(tripId: Long, category: String): List<PackingItemEntity>
    
    fun findByTripIdAndIsPacked(tripId: Long, isPacked: Boolean): List<PackingItemEntity>
    
    @Query("""
        SELECT p FROM PackingItemEntity p 
        WHERE p.tripId = :tripId 
        AND LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    fun searchByTripIdAndName(
        @Param("tripId") tripId: Long,
        @Param("query") query: String
    ): List<PackingItemEntity>
    
    @Modifying
    @Query("""
        UPDATE PackingItemEntity p 
        SET p.isPacked = :isPacked 
        WHERE p.id = :id AND p.tripId = :tripId
    """)
    fun updatePackedStatus(
        @Param("id") id: Long,
        @Param("tripId") tripId: Long,
        @Param("isPacked") isPacked: Boolean
    ): Int
    
    @Query("""
        SELECT p.category, COUNT(p) as count 
        FROM PackingItemEntity p 
        WHERE p.tripId = :tripId 
        GROUP BY p.category
    """)
    fun countItemsByCategory(@Param("tripId") tripId: Long): List<Array<Any>>
    
    @Query("""
        SELECT p.category, COUNT(p) as count 
        FROM PackingItemEntity p 
        WHERE p.tripId = :tripId 
        AND p.isPacked = true
        GROUP BY p.category
    """)
    fun countPackedItemsByCategory(@Param("tripId") tripId: Long): List<Array<Any>>
    
    fun deleteByTripId(tripId: Long)
    
    @Modifying
    @Query("DELETE FROM PackingItemEntity p WHERE p.tripId = :tripId AND p.id IN :ids")
    fun deleteByIds(@Param("tripId") tripId: Long, @Param("ids") ids: Collection<Long>)
    
    @Query("""
        SELECT DISTINCT p.category 
        FROM PackingItemEntity p 
        WHERE p.tripId = :tripId 
        ORDER BY p.category
    """)
    fun findCategoriesByTripId(@Param("tripId") tripId: Long): List<String>
}
