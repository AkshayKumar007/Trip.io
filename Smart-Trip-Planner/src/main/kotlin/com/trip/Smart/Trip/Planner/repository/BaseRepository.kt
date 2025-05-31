package com.trip.Smart.Trip.Planner.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable

@NoRepositoryBean
interface BaseRepository<T, ID : Serializable> : JpaRepository<T, ID> {
    fun findByIdOrThrow(id: ID): T
    fun findByIdOrNull(id: ID): T?
    fun existsByIdOrThrow(id: ID): Boolean
}

@NoRepositoryBean
class BaseRepositoryImpl<T, ID : Serializable>(
    private val jpaRepository: JpaRepository<T, ID>
) : BaseRepository<T, ID>, JpaRepository<T, ID> by jpaRepository {

    override fun findByIdOrThrow(id: ID): T {
        return findById(id).orElseThrow {
            IllegalArgumentException("Entity with id $id not found")
        }
    }

    override fun findByIdOrNull(id: ID): T? {
        return findById(id).orElse(null)
    }

    override fun existsByIdOrThrow(id: ID): Boolean {
        return if (existsById(id)) {
            true
        } else {
            throw IllegalArgumentException("Entity with id $id not found")
        }
    }
}
