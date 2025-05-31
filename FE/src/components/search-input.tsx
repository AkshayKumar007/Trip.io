"use client"

import { Search, X } from "lucide-react"

interface SearchInputProps {
  placeholder?: string
  value?: string
  onChange?: (value: string) => void
  onClear?: () => void
  showClearButton?: boolean
}

export function SearchInput({
  placeholder = "Search...",
  value = "",
  onChange,
  onClear,
  showClearButton = true,
}: SearchInputProps) {
  return (
    <div className="relative">
      <div className="absolute left-3 top-1/2 transform -translate-y-1/2">
        <Search size={16} className="text-[#4a739c]" />
      </div>

      <input
        type="text"
        placeholder={placeholder}
        value={value}
        onChange={(e) => onChange?.(e.target.value)}
        className="w-full pl-10 pr-10 py-2 border border-[#e5e8eb] rounded-md text-sm 
                   placeholder-[#4a739c] focus:outline-none focus:ring-2 focus:ring-[#3d99f5] 
                   focus:border-transparent"
      />

      {showClearButton && value && (
        <button
          onClick={onClear}
          className="absolute right-3 top-1/2 transform -translate-y-1/2 
                     text-[#4a739c] hover:text-[#0d141c]"
        >
          <X size={16} />
        </button>
      )}
    </div>
  )
}
