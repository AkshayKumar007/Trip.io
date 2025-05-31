package com.trip.Smart.Trip.Planner.service.impl // Corrected package declaration

import com.trip.Smart.Trip.Planner.exception.ResourceNotFoundException
import com.trip.Smart.Trip.Planner.model.dto.PackingItemDTO
import com.trip.Smart.Trip.Planner.model.dto.ShopItemDTO
import com.trip.Smart.Trip.Planner.model.dto.WardrobeDTO
import com.trip.Smart.Trip.Planner.model.entity.TripEntity // Ensure TripEntity is imported if needed for context
import com.trip.Smart.Trip.Planner.model.entity.WardrobeItemEntity
import com.trip.Smart.Trip.Planner.repository.TripRepository
import com.trip.Smart.Trip.Planner.repository.WardrobeItemRepository
import com.trip.Smart.Trip.Planner.service.WardrobeService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WardrobeServiceImpl(
    private val wardrobeItemRepository: WardrobeItemRepository,
    private val tripRepository: TripRepository // Injected TripRepository
) : WardrobeService {

    private val logger = LoggerFactory.getLogger(WardrobeServiceImpl::class.java)

    @Transactional
    override fun updateWardrobe(tripId: Long, wardrobeDTO: WardrobeDTO): WardrobeDTO {
        logger.info("Updating wardrobe for trip ID: $tripId")
        val trip = tripRepository.findById(tripId)
            .orElseThrow { ResourceNotFoundException("Trip not found with ID: $tripId") }

        // Clear existing items for simplicity or implement merging logic
        val existingWardrobeItems = wardrobeItemRepository.findByTripId(tripId)
        wardrobeItemRepository.deleteAll(existingWardrobeItems)
        trip.wardrobeItems.clear()

        val newWardrobeItems = wardrobeDTO.items.map { dto ->
            WardrobeItemEntity(
                trip = trip,
                itemName = dto.itemName,
                category = dto.category,
                quantity = dto.quantity,
                isOwned = dto.isOwned,
                isTrendySuggestion = dto.isTrendySuggestion,
                notes = dto.notes
            )
        }
        trip.wardrobeItems.addAll(newWardrobeItems)
        tripRepository.save(trip) // Cascade save to wardrobe items

        logger.info("Wardrobe updated for trip ID: $tripId with ${newWardrobeItems.size} items.")
        return getWardrobeByTripId(tripId) // Return the updated state
    }

    @Transactional(readOnly = true)
    override fun getWardrobeByTripId(tripId: Long): WardrobeDTO {
        logger.debug("Fetching wardrobe for trip ID: $tripId")
        val trip = tripRepository.findById(tripId)
            .orElseThrow { ResourceNotFoundException("Trip not found with ID: $tripId") }

        val packingItemDTOs = trip.wardrobeItems.map { entity ->
            PackingItemDTO(
                id = entity.id,
                itemName = entity.itemName,
                category = entity.category,
                quantity = entity.quantity,
                isOwned = entity.isOwned,
                isTrendySuggestion = entity.isTrendySuggestion,
                notes = entity.notes
            )
        }
        return WardrobeDTO(tripId = tripId, items = packingItemDTOs)
    }

    @Transactional(readOnly = true)
    override fun getShopItems(tripId: Long): List<ShopItemDTO> {
        logger.info("Fetching shop items for trip ID: $tripId")
        val nonOwnedItems = wardrobeItemRepository.findByTripIdAndIsOwnedFalse(tripId)
        return nonOwnedItems.map { entity ->
            ShopItemDTO(
                itemName = entity.itemName,
                category = entity.category,
                simulatedAffiliateLink = "https://example.com/shop?item=${entity.itemName.replace(" ", "+")}"
            )
        }
    }

    override fun suggestTrendyItems(tripType: String, currentItems: List<PackingItemDTO>): List<PackingItemDTO> {
        logger.debug("Suggesting trendy items for trip type: $tripType")
        val suggestions = mutableListOf<PackingItemDTO>()
        when (tripType.lowercase()) {
            "beach vacation" -> {
                suggestions.add(PackingItemDTO(itemName = "Stylish Sunglasses", category = "Accessories", quantity = 1, isTrendySuggestion = true))
                suggestions.add(PackingItemDTO(itemName = "Linen Shirt", category = "Tops", quantity = 1, isTrendySuggestion = true))
            }
            "city break" -> {
                suggestions.add(PackingItemDTO(itemName = "Comfortable Walking Shoes", category = "Footwear", quantity = 1, isTrendySuggestion = true))
                suggestions.add(PackingItemDTO(itemName = "Crossbody Bag", category = "Accessories", quantity = 1, isTrendySuggestion = true))
            }
            "adventure travel" -> {
                suggestions.add(PackingItemDTO(itemName = "Quick-dry Hiking Pants", category = "Bottoms", quantity = 1, isTrendySuggestion = true))
                suggestions.add(PackingItemDTO(itemName = "Waterproof Jacket", category = "Outerwear", quantity = 1, isTrendySuggestion = true))
            }
            else -> {
                suggestions.add(PackingItemDTO(itemName = "Universal Adapter", category = "Electronics", quantity = 1, isTrendySuggestion = true))
            }
        }
        val currentItemNames = currentItems.map { it.itemName.lowercase() }
        return suggestions.filterNot { it.itemName.lowercase() in currentItemNames }
    }
}

