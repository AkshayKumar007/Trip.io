"use client"

import { MapPin, Calendar, Users } from "lucide-react"

interface TripCardProps {
  title: string
  location?: string
  date?: string
  participants?: number
  image?: string
  status?: "upcoming" | "completed" | "planning"
  onClick?: () => void
}

export function TripCard({ title, location, date, participants, image, status = "planning", onClick }: TripCardProps) {
  const statusColors = {
    upcoming: "bg-[#3d99f5] text-white",
    completed: "bg-green-500 text-white",
    planning: "bg-[#e8edf5] text-[#4a739c]",
  }

  return (
    <div
      className="bg-white rounded-lg border border-[#e5e8eb] p-4 hover:shadow-md transition-shadow cursor-pointer"
      onClick={onClick}
    >
      {image && (
        <div className="w-full h-32 relative mb-3 rounded-md overflow-hidden">
          <img src={image || "/placeholder.svg"} alt={title} className="object-cover w-full h-full" />
        </div>
      )}

      <div className="space-y-2">
        <div className="flex items-center justify-between">
          <h3 className="font-medium text-[#0d141c] text-sm">{title}</h3>
          <span className={`px-2 py-1 rounded-full text-xs font-medium ${statusColors[status]}`}>{status}</span>
        </div>

        {location && (
          <div className="flex items-center gap-1 text-xs text-[#4a739c]">
            <MapPin size={12} />
            <span>{location}</span>
          </div>
        )}

        {date && (
          <div className="flex items-center gap-1 text-xs text-[#4a739c]">
            <Calendar size={12} />
            <span>{date}</span>
          </div>
        )}

        {participants && (
          <div className="flex items-center gap-1 text-xs text-[#4a739c]">
            <Users size={12} />
            <span>{participants} participants</span>
          </div>
        )}
      </div>
    </div>
  )
}
