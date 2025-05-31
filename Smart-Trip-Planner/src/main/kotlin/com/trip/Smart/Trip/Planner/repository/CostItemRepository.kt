package com.trip.Smart.Trip.Planner.repository

import com.trip.Smart.Trip.Planner.model.entity.CostItemEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface CostItemRepository : BaseRepository<CostItemEntity, Long> {
    
    fun findByTripId(tripId: Long): List<CostItemEntity>
    
    fun findByTripId(tripId: Long, pageable: Pageable): Page<CostItemEntity>
    
    fun findByTripIdAndCategory(tripId: Long, category: String): List<CostItemEntity>
    
    fun findByTripIdAndIsPaid(tripId: Long, isPaid: Boolean): List<CostItemEntity>
    
    @Query("""
        SELECT c FROM CostItemEntity c 
        WHERE c.tripId = :tripId 
        AND LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    fun searchByTripIdAndName(
        @Param("tripId") tripId: Long,
        @Param("query") query: String
    ): List<CostItemEntity>
    
    @Query("""
        SELECT c FROM CostItemEntity c 
        WHERE c.tripId = :tripId 
        AND c.paymentDate BETWEEN :startDate AND :endDate
    """)
    fun findByTripIdAndPaymentDateBetween(
        @Param("tripId") tripId: Long,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): List<CostItemEntity>
    
    @Modifying
    @Query("""
        UPDATE CostItemEntity c 
        SET c.isPaid = :isPaid, 
            c.paymentDate = CASE WHEN :isPaid = true THEN CURRENT_DATE ELSE NULL END 
        WHERE c.id = :id AND c.tripId = :tripId
    """)
    fun updatePaymentStatus(
        @Param("id") id: Long,
        @Param("tripId") tripId: Long,
        @Param("isPaid") isPaid: Boolean
    ): Int
    
    @Query("""
        SELECT c.category, SUM(c.amount) as total 
        FROM CostItemEntity c 
        WHERE c.tripId = :tripId 
        GROUP BY c.category
    """)
    fun sumAmountsByCategory(@Param("tripId") tripId: Long): List<Array<Any>>
    
    @Query("""
        SELECT COALESCE(SUM(c.amount), 0) 
        FROM CostItemEntity c 
        WHERE c.tripId = :tripId AND c.isPaid = true
    """)
    fun calculateTotalPaidAmount(@Param("tripId") tripId: Long): Double
    
    @Query("""
        SELECT COALESCE(SUM(c.amount), 0) 
        FROM CostItemEntity c 
        WHERE c.tripId = :tripId AND c.isPaid = false
    """)
    fun calculateTotalUnpaidAmount(@Param("tripId") tripId: Long): Double
    
    @Query("""
        SELECT DISTINCT c.category 
        FROM CostItemEntity c 
        WHERE c.tripId = :tripId 
        ORDER BY c.category
    """)
    fun findCategoriesByTripId(@Param("tripId") tripId: Long): List<String>
    
    @Query("""
        SELECT c.currency, COUNT(c) as count 
        FROM CostItemEntity c 
        WHERE c.tripId = :tripId 
        GROUP BY c.currency
    """)
    fun countByCurrency(@Param("tripId") tripId: Long): List<Array<Any>>
    
    @Query("""
        SELECT c.currency, SUM(c.amount) as total 
        FROM CostItemEntity c 
        WHERE c.tripId = :tripId 
        GROUP BY c.currency
    """)
    fun sumAmountsByCurrency(@Param("tripId") tripId: Long): List<Array<Any>>
    
    fun deleteByTripId(tripId: Long)
    
    @Modifying
    @Query("DELETE FROM CostItemEntity c WHERE c.tripId = :tripId AND c.id IN :ids")
    fun deleteByIds(@Param("tripId") tripId: Long, @Param("ids") ids: Collection<Long>)
    
    @Query("""
        SELECT c FROM CostItemEntity c 
        WHERE c.tripId = :tripId 
        AND c.isPaid = false
        ORDER BY c.paymentDate NULLS LAST, c.amount DESC
    """)
    fun findUnpaidItemsSorted(@Param("tripId") tripId: Long): List<CostItemEntity>
}
