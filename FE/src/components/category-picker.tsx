import type React from "react"
interface CategorySectionProps {
  title: string
  children: React.ReactNode
  collapsible?: boolean
  defaultExpanded?: boolean
}

export function CategorySection({
  title,
  children,
  collapsible = false,
  defaultExpanded = true,
}: CategorySectionProps) {
  return (
    <div className="space-y-3">
      <div className="flex items-center justify-between">
        <h3 className="font-medium text-[#0d141c] text-sm">{title}</h3>
        {collapsible && (
          <button className="text-[#4a739c] text-xs hover:text-[#0d141c]">
            {defaultExpanded ? "Collapse" : "Expand"}
          </button>
        )}
      </div>

      <div className="space-y-1">{children}</div>
    </div>
  )
}
