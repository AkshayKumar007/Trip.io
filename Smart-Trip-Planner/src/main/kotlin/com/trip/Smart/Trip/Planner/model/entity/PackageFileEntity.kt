package com.trip.Smart.Trip.Planner.model.entity

import jakarta.persistence.*

@Entity
@Table(name = "packing_items")
data class PackingItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    var trip: TripEntity? = null,

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var category: String = "",

    @Column(nullable = false)
    var isPacked: Boolean = false,

    var quantity: Int = 1,

    var notes: String? = null
)