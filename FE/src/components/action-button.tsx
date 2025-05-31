"use client"

import type React from "react"

import type { LucideIcon } from "lucide-react"

interface ActionButtonProps {
  children: React.ReactNode
  variant?: "primary" | "secondary" | "outline" | "ghost"
  size?: "sm" | "md" | "lg"
  icon?: LucideIcon
  iconPosition?: "left" | "right"
  onClick?: () => void
  disabled?: boolean
  fullWidth?: boolean
}

export function ActionButton({
  children,
  variant = "primary",
  size = "md",
  icon: Icon,
  iconPosition = "left",
  onClick,
  disabled = false,
  fullWidth = false,
}: ActionButtonProps) {
  const variants = {
    primary: "bg-[#3d99f5] text-white hover:bg-opacity-90",
    secondary: "bg-[#4a739c] text-white hover:bg-opacity-90",
    outline: "border border-[#e5e8eb] text-[#0d141c] hover:bg-[#f7fafc]",
    ghost: "text-[#4a739c] hover:bg-[#f7fafc]",
  }

  const sizes = {
    sm: "px-3 py-1.5 text-xs",
    md: "px-4 py-2 text-sm",
    lg: "px-6 py-3 text-base",
  }

  return (
    <button
      onClick={onClick}
      disabled={disabled}
      className={`
        ${variants[variant]} 
        ${sizes[size]} 
        ${fullWidth ? "w-full" : ""} 
        font-medium rounded-md transition-all duration-200 
        disabled:opacity-50 disabled:cursor-not-allowed
        flex items-center justify-center gap-2
      `}
    >
      {Icon && iconPosition === "left" && <Icon size={16} />}
      {children}
      {Icon && iconPosition === "right" && <Icon size={16} />}
    </button>
  )
}
