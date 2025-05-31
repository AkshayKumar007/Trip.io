package com.trip.Smart.Trip.Planner.controller

import com.trip.Smart.Trip.Planner.exception.ResourceNotFoundException
import com.trip.Smart.Trip.Planner.model.dto.PackingItemDTO
import com.trip.Smart.Trip.Planner.model.dto.ShopItemDTO
import com.trip.Smart.Trip.Planner.model.dto.WardrobeDTO
import com.trip.Smart.Trip.Planner.service.WardrobeService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/trips/{tripId}/wardrobe")
class WardrobeController(
    private val wardrobeService: WardrobeService
) {
    private val logger = LoggerFactory.getLogger(WardrobeController::class.java)

    @PutMapping
    fun updateWardrobe(
        @PathVariable tripId: Long,
        @Valid @RequestBody wardrobeDTO: WardrobeDTO
    ): ResponseEntity<WardrobeDTO> {
        logger.info("Updating wardrobe for trip ID: $tripId")
        return try {
            val updatedWardrobe = wardrobeService.updateWardrobe(tripId, wardrobeDTO)
            ResponseEntity.ok(updatedWardrobe)
        } catch (ex: ResourceNotFoundException) {
            logger.error("Trip not found with ID: $tripId", ex)
            ResponseEntity.notFound().build()
        } catch (ex: Exception) {
            logger.error("Error updating wardrobe for trip ID: $tripId", ex)
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping
    fun getWardrobe(@PathVariable tripId: Long): ResponseEntity<WardrobeDTO> {
        logger.info("Fetching wardrobe for trip ID: $tripId")
        return try {
            val wardrobe = wardrobeService.getWardrobeByTripId(tripId)
            ResponseEntity.ok(wardrobe)
        } catch (ex: ResourceNotFoundException) {
            logger.error("Wardrobe not found for trip ID: $tripId", ex)
            ResponseEntity.notFound().build()
        } catch (ex: Exception) {
            logger.error("Error fetching wardrobe for trip ID: $tripId", ex)
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/shop-items")
    fun getShopItems(@PathVariable tripId: Long): ResponseEntity<List<ShopItemDTO>> {
        logger.info("Fetching shop items for trip ID: $tripId")
        return try {
            val shopItems = wardrobeService.getShopItems(tripId)
            ResponseEntity.ok(shopItems)
        } catch (ex: ResourceNotFoundException) {
            logger.error("Trip not found with ID: $tripId", ex)
            ResponseEntity.notFound().build()
        } catch (ex: Exception) {
            logger.error("Error fetching shop items for trip ID: $tripId", ex)
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/suggestions")
    fun getWardrobeSuggestions(
        @PathVariable tripId: Long,
        @RequestParam tripType: String
    ): ResponseEntity<List<PackingItemDTO>> {
        logger.info("Fetching wardrobe suggestions for trip ID: $tripId, type: $tripType")
        return try {
            // First get current wardrobe to avoid suggesting items already in the list
            val currentWardrobe = wardrobeService.getWardrobeByTripId(tripId)
            val currentItems = currentWardrobe.items
            
            val suggestions = wardrobeService.suggestTrendyItems(tripType, currentItems)
            ResponseEntity.ok(suggestions)
        } catch (ex: ResourceNotFoundException) {
            logger.error("Wardrobe not found for trip ID: $tripId", ex)
            ResponseEntity.notFound().build()
        } catch (ex: Exception) {
            logger.error("Error fetching wardrobe suggestions for trip ID: $tripId", ex)
            ResponseEntity.internalServerError().build()
        }
    }
}

