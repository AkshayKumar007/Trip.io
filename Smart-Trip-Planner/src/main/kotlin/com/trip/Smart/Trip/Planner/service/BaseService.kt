package com.trip.Smart.Trip.Planner.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BaseService<T, ID, DTO, CreateDTO, UpdateDTO> {
    fun create(dto: CreateDTO): DTO
    fun findById(id: ID): DTO
    fun findAll(pageable: Pageable): Page<DTO>
    fun update(id: ID, dto: UpdateDTO): DTO
    fun delete(id: ID)
    fun toDto(entity: T): DTO
    fun toEntity(dto: CreateDTO): T
    fun updateEntityFromDto(entity: T, dto: UpdateDTO): T
}
