"use client"

import { useState } from "react"
import { ChevronLeft, ChevronRight } from "lucide-react"

interface CalendarPickerProps {
  selectedDates?: Date[]
  onDateSelect?: (date: Date) => void
  onDateRangeSelect?: (startDate: Date, endDate: Date) => void
  mode?: "single" | "range"
}

export function CalendarPicker({
  selectedDates = [],
  onDateSelect,
  onDateRangeSelect,
  mode = "single",
}: CalendarPickerProps) {
  const [currentMonth, setCurrentMonth] = useState(new Date())
  const [rangeStart, setRangeStart] = useState<Date | null>(null)

  const daysInMonth = new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1, 0).getDate()
  const firstDayOfMonth = new Date(currentMonth.getFullYear(), currentMonth.getMonth(), 1).getDay()

  const monthNames = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ]

  const isDateSelected = (date: Date) => {
    return selectedDates.some((selectedDate) => selectedDate.toDateString() === date.toDateString())
  }

  const handleDateClick = (day: number) => {
    const clickedDate = new Date(currentMonth.getFullYear(), currentMonth.getMonth(), day)

    if (mode === "single") {
      onDateSelect?.(clickedDate)
    } else if (mode === "range") {
      if (!rangeStart) {
        setRangeStart(clickedDate)
      } else {
        onDateRangeSelect?.(rangeStart, clickedDate)
        setRangeStart(null)
      }
    }
  }

  const navigateMonth = (direction: "prev" | "next") => {
    setCurrentMonth((prev) => {
      const newMonth = new Date(prev)
      if (direction === "prev") {
        newMonth.setMonth(prev.getMonth() - 1)
      } else {
        newMonth.setMonth(prev.getMonth() + 1)
      }
      return newMonth
    })
  }

  return (
    <div className="bg-white rounded-lg border border-[#e5e8eb] p-4">
      <div className="flex items-center justify-between mb-4">
        <button onClick={() => navigateMonth("prev")} className="p-1 rounded-full hover:bg-[#f7fafc]">
          <ChevronLeft size={16} className="text-[#4a739c]" />
        </button>

        <h3 className="font-medium text-[#0d141c]">
          {monthNames[currentMonth.getMonth()]} {currentMonth.getFullYear()}
        </h3>

        <button onClick={() => navigateMonth("next")} className="p-1 rounded-full hover:bg-[#f7fafc]">
          <ChevronRight size={16} className="text-[#4a739c]" />
        </button>
      </div>

      <div className="grid grid-cols-7 gap-1 mb-2">
        {["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"].map((day) => (
          <div key={day} className="text-center text-xs text-[#4a739c] p-2 font-medium">
            {day}
          </div>
        ))}
      </div>

      <div className="grid grid-cols-7 gap-1">
        {Array.from({ length: firstDayOfMonth }, (_, i) => (
          <div key={`empty-${i}`} className="p-2" />
        ))}

        {Array.from({ length: daysInMonth }, (_, i) => {
          const day = i + 1
          const date = new Date(currentMonth.getFullYear(), currentMonth.getMonth(), day)
          const isSelected = isDateSelected(date)

          return (
            <button
              key={day}
              onClick={() => handleDateClick(day)}
              className={`p-2 text-sm rounded-full hover:bg-[#f7fafc] transition-colors ${
                isSelected ? "bg-[#3d99f5] text-white hover:bg-[#3d99f5]" : "text-[#0d141c]"
              }`}
            >
              {day}
            </button>
          )
        })}
      </div>
    </div>
  )
}
