package com.trip.Smart.Trip.Planner.model.entity

import jakarta.persistence.*

@Entity
@Table(name = "shop_items")
data class ShopItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "trip_id", nullable = false)
    var tripId: Long = 0,

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var category: String = "",

    @Column(nullable = false)
    var isPurchased: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var priority: Priority = Priority.MEDIUM,

    var quantity: Int = 1,

    @Column(name = "estimated_price")
    var estimatedPrice: Double = 0.0,

    var notes: String? = null
) {
    enum class Priority {
        HIGH, MEDIUM, LOW
    }
}