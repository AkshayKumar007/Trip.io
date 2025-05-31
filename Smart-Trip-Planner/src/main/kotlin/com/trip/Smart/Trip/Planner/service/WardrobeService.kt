package com.trip.Smart.Trip.Planner.service

import com.trip.Smart.Trip.Planner.model.dto.PackingItemDTO
import com.trip.Smart.Trip.Planner.model.dto.ShopItemDTO
import com.trip.Smart.Trip.Planner.model.dto.WardrobeDTO

interface WardrobeService {
    fun updateWardrobe(tripId: Long, wardrobeDTO: WardrobeDTO): WardrobeDTO
    fun getWardrobeByTripId(tripId: Long): WardrobeDTO
    fun getShopItems(tripId: Long): List<ShopItemDTO>
    fun suggestTrendyItems(tripType: String, currentItems: List<PackingItemDTO>): List<PackingItemDTO> // Helper
}

