import { Cloud, Sun, CloudRain, Thermometer } from "lucide-react"

interface WeatherCardProps {
  temperature: string
  condition: "sunny" | "cloudy" | "rainy"
  humidity?: string
  location: string
  date?: string
}

export function WeatherCard({ temperature, condition, humidity, location, date }: WeatherCardProps) {
  const weatherIcons = {
    sunny: <Sun size={24} className="text-yellow-500" />,
    cloudy: <Cloud size={24} className="text-gray-500" />,
    rainy: <CloudRain size={24} className="text-blue-500" />,
  }

  return (
    <div className="bg-[#f7fafc] rounded-lg p-4 border border-[#e5e8eb]">
      <div className="flex items-center justify-between mb-3">
        <div>
          <h3 className="font-medium text-[#0d141c] text-sm">Weather Summary</h3>
          <p className="text-xs text-[#4a739c]">{location}</p>
          {date && <p className="text-xs text-[#4a739c]">{date}</p>}
        </div>
        {weatherIcons[condition]}
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="flex items-center gap-2">
          <Thermometer size={16} className="text-[#4a739c]" />
          <div>
            <p className="text-lg font-bold text-[#0d141c]">{temperature}</p>
            <p className="text-xs text-[#4a739c]">Temperature</p>
          </div>
        </div>

        {humidity && (
          <div>
            <p className="text-lg font-bold text-[#0d141c]">{humidity}</p>
            <p className="text-xs text-[#4a739c]">Humidity</p>
          </div>
        )}
      </div>
    </div>
  )
}
