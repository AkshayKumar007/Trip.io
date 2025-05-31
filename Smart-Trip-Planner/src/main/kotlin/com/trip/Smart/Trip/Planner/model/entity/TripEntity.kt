package com.trip.Smart.Trip.Planner.model.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "trips")
data class TripEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var tripType: String? = "",

    @Column(nullable = false)
    var destination: String? = "",

    var startDate: LocalDate? = null,
    var endDate: LocalDate? = null,
    var numDays: Int? = null,
    var numPeople: Int? = null,
    var notes: String? = null,

    @OneToMany(mappedBy = "trip", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var wardrobeItems: MutableList<WardrobeItemEntity> = mutableListOf(),

    @OneToMany(mappedBy = "trip", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var costItems: MutableList<CostItemEntity> = mutableListOf(),

    @CreationTimestamp
    @Column(updatable = false)
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
)

