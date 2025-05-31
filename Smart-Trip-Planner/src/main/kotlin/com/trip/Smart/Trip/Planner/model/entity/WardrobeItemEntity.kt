package com.trip.Smart.Trip.Planner.model.entity

import jakarta.persistence.*

@Entity
@Table(name = "wardrobe_items")
data class WardrobeItemEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    var trip: TripEntity? = null,

    @Column(nullable = false)
    var itemName: String = "",

    var category: String? = null,

    @Column(columnDefinition = "integer default 1")
    var quantity: Int = 1,

    @Column(columnDefinition = "boolean default false")
    var isOwned: Boolean = false,

    @Column(columnDefinition = "boolean default false")
    var isTrendySuggestion: Boolean = false,

    var notes: String? = null
)

