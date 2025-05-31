"use client"

import { Star, MapPin, DollarSign } from "lucide-react"

interface RecommendationCardProps {
  title: string
  location: string
  rating?: number
  price?: string
  image: string
  type: "accommodation" | "activity" | "restaurant"
  description?: string
  onClick?: () => void
}

export function RecommendationCard({
  title,
  location,
  rating,
  price,
  image,
  type,
  description,
  onClick,
}: RecommendationCardProps) {
  const typeColors = {
    accommodation: "bg-blue-100 text-blue-800",
    activity: "bg-green-100 text-green-800",
    restaurant: "bg-orange-100 text-orange-800",
  }

  return (
    <div
      className="bg-white rounded-lg border border-[#e5e8eb] overflow-hidden hover:shadow-md transition-shadow cursor-pointer"
      onClick={onClick}
    >
      <div className="relative h-32 w-full">
        <img src={image || "/placeholder.svg"} alt={title} className="object-cover w-full h-full" />
        <div className="absolute top-2 left-2">
          <span className={`px-2 py-1 rounded-full text-xs font-medium ${typeColors[type]}`}>{type}</span>
        </div>
      </div>

      <div className="p-3 space-y-2">
        <h3 className="font-medium text-[#0d141c] text-sm">{title}</h3>

        <div className="flex items-center gap-1 text-xs text-[#4a739c]">
          <MapPin size={12} />
          <span>{location}</span>
        </div>

        <div className="flex items-center justify-between">
          {rating && (
            <div className="flex items-center gap-1">
              <Star size={12} className="text-yellow-500 fill-current" />
              <span className="text-xs text-[#0d141c]">{rating}</span>
            </div>
          )}

          {price && (
            <div className="flex items-center gap-1 text-xs text-[#4a739c]">
              <DollarSign size={12} />
              <span>{price}</span>
            </div>
          )}
        </div>

        {description && <p className="text-xs text-[#4a739c] line-clamp-2">{description}</p>}
      </div>
    </div>
  )
}
