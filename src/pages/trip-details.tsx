import { useState } from "react";
import { CalendarPicker } from "../components/calendar-picker";
import { useHistory } from "react-router-dom";

const TRIP_TYPES = [
    { label: "Beach & Coastal", value: "beach_coastal" },
    { label: "City Break", value: "city_break" },
    { label: "Adventure & Outdoor", value: "adventure_outdoor" },
    { label: "Cultural & Historical", value: "cultural_historical" },
    { label: "Nature & Wildlife", value: "nature_wildlife" },
    { label: "Mountain & Hiking", value: "mountain_hiking" },
    { label: "Road Trip", value: "road_trip" },
    { label: "Cruise", value: "cruise" },
    { label: "Wellness & Spa", value: "wellness_spa" },
    { label: "Food & Wine", value: "food_wine" },
    { label: "Business Travel", value: "business_travel" },
    { label: "Family Vacation", value: "family_vacation" },
    { label: "Romantic Getaway", value: "romantic_getaway" },
    { label: "Solo Travel", value: "solo_travel" },
    { label: "Backpacking", value: "backpacking" },
    { label: "Luxury Travel", value: "luxury_travel" },
    { label: "Festival & Events", value: "festival_events" },
    { label: "Winter Sports", value: "winter_sports" },
    { label: "Pilgrimage & Spiritual", value: "pilgrimage_spiritual" },
    { label: "Volunteer & Service", value: "volunteer_service" },
    { label: "Educational & Learning", value: "educational_learning" },
    { label: "Photography Tour", value: "photography_tour" },
];

type TripDetails = {
  destination: string;
  tripType: string;
  fromDate: Date | null;
  toDate: Date | null;
  numberOfPeople: string | number;
};

function formatTripDetails({ destination, tripType, fromDate, toDate, numberOfPeople }: TripDetails) {
  return {
    destination,
    tripType,
    duration: {
      from: fromDate ? fromDate.toISOString().split("T")[0] : null,
      to: toDate ? toDate.toISOString().split("T")[0] : null,
    },
    numberOfPeople: Number(numberOfPeople),
  };
}

export default function TripDetailsPage() {
  const [destination, setDestination] = useState("");
  const [tripType, setTripType] = useState("");
  const [dateRange, setDateRange] = useState<[Date | null, Date | null]>([null, null]);
  const [numberOfPeople, setNumberOfPeople] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const history = useHistory();

  const handleDateRange = (from: Date, to: Date) => {
    setDateRange([from, to]);
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setSubmitting(true);
    const payload = formatTripDetails({
      destination,
      tripType,
      fromDate: dateRange[0],
      toDate: dateRange[1],
      numberOfPeople,
    });
    await fetch("/api/trip", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
    setSubmitting(false);
    // Optionally, navigate to next page or show success
  };

  const handleDummyWeather = () => {
    // Dummy data for outlook
    const dummyWeather = {
      temperatureRange: "15°C – 25°C",
      chanceOfRain: "20%",
      generalOutlook: "Sunny with occasional clouds",
      suggestions: [
        "Warm layers needed for cooler evenings",
        "Pack swimwear and sun protection for sunny days"
      ]
    };
    // Pass data via query params or state (using state here)
    history.push({
      pathname: "/outlook",
      state: dummyWeather
    });
  };

  return (
    <div className="max-w-xl mx-auto py-12 px-4">
      <h2 className="text-2xl font-bold mb-8 text-center">Plan Your Trip</h2>
      <form onSubmit={handleSubmit} className="space-y-6">
        <div>
          <label className="block mb-1 font-medium">Destination</label>
          <input
            type="text"
            className="w-full border border-[#e5e8eb] rounded-md px-3 py-2"
            placeholder="Enter destination"
            value={destination}
            onChange={(e) => setDestination(e.target.value)}
            required
          />
        </div>
        <div>
          <label className="block mb-1 font-medium">Trip Type</label>
          <select
            className="w-full border border-[#e5e8eb] rounded-md px-3 py-2"
            value={tripType}
            onChange={(e) => setTripType(e.target.value)}
            required
          >
            <option value="" disabled>Select type</option>
            {TRIP_TYPES.map((type) => (
              <option key={type.value} value={type.value}>{type.label}</option>
            ))}
          </select>
        </div>
        <div>
          <label className="block mb-1 font-medium">Trip Duration</label>
          <CalendarPicker
            mode="range"
            selectedDates={dateRange.filter((d): d is Date => d !== null)}
            onDateRangeSelect={handleDateRange}
          />
        </div>
        <div>
          <label className="block mb-1 font-medium">Number of People</label>
          <input
            type="number"
            min={1}
            className="w-full border border-[#e5e8eb] rounded-md px-3 py-2"
            placeholder="Enter number of people"
            value={numberOfPeople}
            onChange={(e) => setNumberOfPeople(e.target.value)}
            required
          />
        </div>
        <div className="flex justify-end">
          <button
            type="submit"
            className="bg-[#3d99f5] text-white px-4 py-2 rounded-md font-medium disabled:opacity-50"
            disabled={submitting}
          >
            {submitting ? "Submitting..." : "Next"}
          </button>
        </div>
      </form>
      <div className="flex justify-end mt-4">
        <button
          type="button"
          className="bg-gray-200 text-gray-800 px-4 py-2 rounded-md font-medium mr-2"
          onClick={handleDummyWeather}
        >
          Dummy Weather
        </button>
      </div>
    </div>
  );
}
