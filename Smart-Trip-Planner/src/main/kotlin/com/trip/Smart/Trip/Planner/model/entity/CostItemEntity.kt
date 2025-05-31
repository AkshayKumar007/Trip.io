package com.trip.Smart.Trip.Planner.model.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "cost_items")
data class CostItemEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    var trip: TripEntity? = null,

    @Column(nullable = false)
    var categoryName: String = "",

    var itemDescription: String? = null,

    @Column(precision = 10, scale = 2)
    var estimatedCost: BigDecimal? = null,

    @Column(precision = 10, scale = 2)
    var actualCost: BigDecimal? = null,

    var notes: String? = null
)

