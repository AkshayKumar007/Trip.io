"use client"

import { Check } from "lucide-react"

interface PackingItemProps {
  id: string
  name: string
  category?: string
  isChecked?: boolean
  onToggle?: (id: string) => void
  showCategory?: boolean
}

export function PackingItem({
  id,
  name,
  category,
  isChecked = false,
  onToggle,
  showCategory = false,
}: PackingItemProps) {
  return (
    <div className="flex items-center gap-3 p-3 hover:bg-[#f7fafc] rounded-md transition-colors border border-red">
      <button
        onClick={() => onToggle?.(id)}
        className={`w-5 h-5 rounded border-2 flex items-center justify-center transition-colors ${
          isChecked ? "bg-[#3d99f5] border-[#3d99f5]" : "border-[#e5e8eb] hover:border-[#4a739c]"
        }`}
      >
        {isChecked && <Check size={12} className="text-white" />}
      </button>

      <div className="flex-1">
        <p className={`text-sm ${isChecked ? "line-through text-[#4a739c]" : "text-[#0d141c]"}`}>{name}</p>
        {showCategory && category && <p className="text-xs text-[#4a739c]">{category}</p>}
      </div>
    </div>
  )
}
