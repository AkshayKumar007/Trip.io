import { Bell, Search, Menu } from "lucide-react"
import { Avatar, AvatarFallback, AvatarImage } from "./avatar"

interface HeaderProps {
  title: string
  showSearch?: boolean
  showMenu?: boolean
  showNotification?: boolean
  userAvatar?: string
  userName?: string
}

export function Header({
  title,
  showSearch = false,
  showMenu = false,
  showNotification = true,
  userAvatar,
  userName = "U",
}: HeaderProps) {
  return (
    <header className="w-full flex items-center justify-between px-8 py-4 border-b border-[#e5e8eb] bg-white shadow-md fixed top-0 left-0 right-0 z-50" style={{minHeight: 64}}>
      <div className="flex items-center gap-4 min-w-0">
        {showMenu && (
          <button className="p-2 rounded-full hover:bg-[#f7fafc] focus:outline-none">
            <Menu size={22} className="text-[#0d141c]" />
          </button>
        )}
        <span className="text-xl font-bold text-[#0d141c] truncate">{title}</span>
      </div>
      <div className="flex items-center gap-3">
        {showSearch && (
          <button className="p-2 rounded-full hover:bg-[#f7fafc] focus:outline-none">
            <Search size={20} className="text-[#0d141c]" />
          </button>
        )}
        {showNotification && (
          <button className="p-2 rounded-full hover:bg-[#f7fafc] focus:outline-none">
            <Bell size={20} className="text-[#0d141c]" />
          </button>
        )}
        <Avatar className="h-10 w-10 ml-3 border border-[#e5e8eb] shadow-sm">
          <AvatarImage src={userAvatar || "/placeholder.svg"} alt={userName} />
          <AvatarFallback className="text-sm bg-[#f7fafc] text-[#4a739c]">{userName.charAt(0)}</AvatarFallback>
        </Avatar>
      </div>
    </header>
  )
}
