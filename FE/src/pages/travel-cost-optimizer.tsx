import React, { useState, useEffect } from "react";
import { travelCostOptions } from "./travel-cost-options.mock";

interface EstimateResponse {
  totalCost: number;
  suggestions: string[];
}

const TravelCostOptimizer: React.FC = () => {
  const [form, setForm] = useState({
    flights: "",
    trains: "",
    hotels: "",
    rentals: "",
    backpack: "",
    boots: "",
    food: "",
    activities: "",
    visas: "",
  });
  const [options, setOptions] = useState<typeof travelCostOptions | null>(null);
  const [result, setResult] = useState<EstimateResponse | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    // Simulate API call to fetch options
    setTimeout(() => {
      setOptions(travelCostOptions);
    }, 300);
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setResult(null);
    // Replace with your actual API endpoint
    const res = await fetch("/api/travel-cost-estimate", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(form),
    });
    const data = await res.json();
    setResult(data);
    setLoading(false);
  };

  return (
    <div className="min-h-screen bg-white px-8 py-8">
      <header className="flex items-center justify-between border-b pb-4 mb-8">
        <span className="font-bold text-lg">Smart Back Packing</span>
        <nav className="flex gap-8 text-sm">
          <a href="/trips" className="hover:underline">My Trips</a>
          <a href="/explore" className="hover:underline">Explore</a>
          <a href="/packing-list" className="hover:underline">Packing Lists</a>
          <a href="/travel-cost-optimizer" className="hover:underline font-semibold">Cost Estimator</a>
          <a href="/shop-essentials" className="hover:underline">Gear Shop</a>
        </nav>
        <div className="flex items-center gap-4">
          <span className="rounded-full bg-gray-200 w-8 h-8 flex items-center justify-center">
            <svg width="20" height="20" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10" /></svg>
          </span>
          <img src="https://randomuser.me/api/portraits/women/44.jpg" alt="avatar" className="w-8 h-8 rounded-full" />
        </div>
      </header>
      <main className="max-w-2xl mx-auto">
        <h1 className="text-2xl font-bold mb-2">Trip Cost Estimator</h1>
        <p className="text-sm text-gray-500 mb-8">Estimate the total cost of your trip by adding expenses for transportation, accommodation, gear, and other categories.</p>
        {!options ? (
          <div>Loading options...</div>
        ) : (
        <form className="flex flex-col gap-6" onSubmit={handleSubmit}>
          <div>
            <div className="font-semibold mb-2">Transportation</div>
            <select name="flights" value={form.flights} onChange={handleChange} className="block w-full mb-2 border rounded px-3 py-1">
              <option value="">Flights</option>
              {options.flights.map(opt => <option key={opt} value={opt}>{opt}</option>)}
            </select>
            <select name="trains" value={form.trains} onChange={handleChange} className="block w-full border rounded px-3 py-1">
              <option value="">Trains</option>
              {options.trains.map(opt => <option key={opt} value={opt}>{opt}</option>)}
            </select>
          </div>
          <div>
            <div className="font-semibold mb-2">Accommodation</div>
            <select name="hotels" value={form.hotels} onChange={handleChange} className="block w-full mb-2 border rounded px-3 py-1">
              <option value="">Hotels</option>
              {options.hotels.map(opt => <option key={opt} value={opt}>{opt}</option>)}
            </select>
            <select name="rentals" value={form.rentals} onChange={handleChange} className="block w-full border rounded px-3 py-1">
              <option value="">Rentals</option>
              {options.rentals.map(opt => <option key={opt} value={opt}>{opt}</option>)}
            </select>
          </div>
          <div>
            <div className="font-semibold mb-2">Clothing/Gear</div>
            <select name="backpack" value={form.backpack} onChange={handleChange} className="block w-full mb-2 border rounded px-3 py-1">
              <option value="">Backpack</option>
              {options.backpack.map(opt => <option key={opt} value={opt}>{opt}</option>)}
            </select>
            <select name="boots" value={form.boots} onChange={handleChange} className="block w-full border rounded px-3 py-1">
              <option value="">Hiking Boots</option>
              {options.boots.map(opt => <option key={opt} value={opt}>{opt}</option>)}
            </select>
          </div>
          <div>
            <div className="font-semibold mb-2">Other Expenses</div>
            <select name="food" value={form.food} onChange={handleChange} className="block w-full mb-2 border rounded px-3 py-1">
              <option value="">Food</option>
              {options.food.map(opt => <option key={opt} value={opt}>{opt}</option>)}
            </select>
            <select name="activities" value={form.activities} onChange={handleChange} className="block w-full mb-2 border rounded px-3 py-1">
              <option value="">Activities</option>
              {options.activities.map(opt => <option key={opt} value={opt}>{opt}</option>)}
            </select>
            <select name="visas" value={form.visas} onChange={handleChange} className="block w-full border rounded px-3 py-1">
              <option value="">Visas</option>
              {options.visas.map(opt => <option key={opt} value={opt}>{opt}</option>)}
            </select>
          </div>
          <button type="submit" className="bg-blue-100 text-gray-800 rounded-full px-6 py-2 text-base font-medium w-max hover:bg-blue-200 mt-2">Estimate Cost</button>
        </form>
        )}
        {loading && <div className="mt-8">Calculating...</div>}
        {result && (
          <div className="mt-8">
            <div className="font-semibold text-lg mb-2">Total Estimated Cost</div>
            <div className="text-2xl font-bold mb-4">${result.totalCost}</div>
            <div className="font-semibold mb-1">Cost Optimization Suggestions</div>
            <ul className="list-disc ml-6 text-gray-700">
              {result.suggestions.map((s, i) => (
                <li key={i}>{s}</li>
              ))}
            </ul>
          </div>
        )}
      </main>
    </div>
  );
};

export default TravelCostOptimizer;
