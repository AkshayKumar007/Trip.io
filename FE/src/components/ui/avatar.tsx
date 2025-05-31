import * as React from "react"

export interface AvatarProps extends React.HTMLAttributes<HTMLDivElement> {
  src?: string
  alt?: string
  fallback?: string
  className?: string
  children?: React.ReactNode
}

export function Avatar({ src, alt, fallback, className = "", children, ...props }: AvatarProps) {
  return (
    <div className={`relative inline-flex items-center justify-center rounded-full bg-gray-200 overflow-hidden ${className}`} {...props}>
      {src ? (
        <img src={src} alt={alt} className="object-cover w-full h-full" />
      ) : fallback ? (
        <span className="text-gray-500 text-xs font-medium">{fallback}</span>
      ) : (
        children
      )}
    </div>
  )
}

export function AvatarImage(props: React.ImgHTMLAttributes<HTMLImageElement>) {
  return <img {...props} className={`object-cover w-full h-full ${props.className || ""}`} />
}

export interface AvatarFallbackProps extends React.HTMLAttributes<HTMLSpanElement> {
  children?: React.ReactNode
  className?: string
}

export function AvatarFallback({ children, className = "", ...props }: AvatarFallbackProps) {
  return (
    <span className={`flex items-center justify-center w-full h-full text-gray-500 bg-gray-200 ${className}`} {...props}>
      {children}
    </span>
  )
}
