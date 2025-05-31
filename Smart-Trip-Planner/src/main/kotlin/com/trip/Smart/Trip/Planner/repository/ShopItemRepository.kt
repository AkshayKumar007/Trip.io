package com.trip.Smart.Trip.Planner.repository

import com.trip.Smart.Trip.Planner.model.entity.ShopItemEntity
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ShopItemRepository : BaseRepository<ShopItemEntity, Long> {
    
    fun findByTripId(tripId: Long): List<ShopItemEntity>
    
    fun findByTripIdAndIsPurchased(tripId: Long, isPurchased: Boolean): List<ShopItemEntity>
    
    fun findByTripIdAndCategory(tripId: Long, category: String): List<ShopItemEntity>
    
    @Query("""
        SELECT s FROM ShopItemEntity s 
        WHERE s.tripId = :tripId 
        AND LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    fun searchByTripIdAndName(
        @Param("tripId") tripId: Long,
        @Param("query") query: String
    ): List<ShopItemEntity>
    
    @Modifying
    @Query("""
        UPDATE ShopItemEntity s 
        SET s.isPurchased = :isPurchased 
        WHERE s.id = :id AND s.tripId = :tripId
    """)
    fun updatePurchasedStatus(
        @Param("id") id: Long,
        @Param("tripId") tripId: Long,
        @Param("isPurchased") isPurchased: Boolean
    ): Int
    
    @Query("""
        SELECT s.priority, COUNT(s) as count 
        FROM ShopItemEntity s 
        WHERE s.tripId = :tripId 
        GROUP BY s.priority
    """)
    fun countItemsByPriority(@Param("tripId") tripId: Long): List<Array<Any>>
    
    @Query("""
        SELECT s.category, COUNT(s) as count 
        FROM ShopItemEntity s 
        WHERE s.tripId = :tripId 
        GROUP BY s.category
    """)
    fun countItemsByCategory(@Param("tripId") tripId: Long): List<Array<Any>>
    
    @Query("""
        SELECT COALESCE(SUM(s.estimatedPrice), 0) 
        FROM ShopItemEntity s 
        WHERE s.tripId = :tripId AND s.isPurchased = false
    """)
    fun calculateTotalEstimatedCost(@Param("tripId") tripId: Long): Double
    
    @Query("""
        SELECT DISTINCT s.category 
        FROM ShopItemEntity s 
        WHERE s.tripId = :tripId 
        ORDER BY s.category
    """)
    fun findCategoriesByTripId(@Param("tripId") tripId: Long): List<String>
    
    fun deleteByTripId(tripId: Long)
    
    @Modifying
    @Query("DELETE FROM ShopItemEntity s WHERE s.tripId = :tripId AND s.id IN :ids")
    fun deleteByIds(@Param("tripId") tripId: Long, @Param("ids") ids: Collection<Long>)
    
    @Query("""
        SELECT s FROM ShopItemEntity s 
        WHERE s.tripId = :tripId 
        AND s.isPurchased = false
        ORDER BY 
            CASE s.priority 
                WHEN 'HIGH' THEN 1 
                WHEN 'MEDIUM' THEN 2 
                WHEN 'LOW' THEN 3 
                ELSE 4 
            END,
            s.name
    """)
    fun findUnpurchasedItemsPrioritized(@Param("tripId") tripId: Long): List<ShopItemEntity>
}
