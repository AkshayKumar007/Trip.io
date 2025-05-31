import React from "react";
import { useHistory } from "react-router-dom";

const NextSteps: React.FC = () => {
  const history = useHistory();

  return (
    <div className="min-h-screen bg-white px-8 py-8">
      <header className="flex items-center justify-between border-b pb-4 mb-8">
        <div className="flex items-center gap-2">
          <span className="font-bold text-lg">Smart Back Packing</span>
        </div>
        <nav className="flex gap-8 text-sm">
          <button className="hover:underline" onClick={() => history.push("/trips")}>Home</button>
          <button className="hover:underline" onClick={() => history.push("/trips")}>Trips</button>
          <button className="hover:underline" onClick={() => history.push("/explore")}>Explore</button>
          <button className="hover:underline" onClick={() => history.push("/profile")}>Profile</button>
        </nav>
        <div className="flex items-center gap-4">
          <span className="rounded-full bg-gray-200 w-8 h-8 flex items-center justify-center">
            <svg width="20" height="20" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10" /></svg>
          </span>
          <img src="https://randomuser.me/api/portraits/women/44.jpg" alt="avatar" className="w-8 h-8 rounded-full" />
        </div>
      </header>
      <main className="max-w-5xl mx-auto">
        <h1 className="text-2xl font-bold mb-8">Welcome to Smart Back Packing</h1>
        <div className="flex flex-col gap-8">
          <div className="flex bg-gray-100 rounded-xl overflow-hidden shadow-sm">
            <img src="https://images.unsplash.com/photo-1512436991641-6745cdb1723f?auto=format&fit=crop&w=400&q=80" alt="Shop Essentials" className="w-64 h-48 object-cover" />
            <div className="flex flex-col justify-center px-8 py-4 flex-1">
              <div className="font-semibold">Shop Essentials</div>
              <div className="text-sm text-gray-600 mb-4">Find all your travel essentials in one place. Browse our curated list of high-quality products.</div>
              <button className="bg-blue-100 text-gray-800 rounded-full px-4 py-1 text-sm font-medium w-max hover:bg-blue-200" onClick={() => history.push("/shop-essentials")}>Shop Now</button>
            </div>
          </div>
          <div className="flex bg-gray-100 rounded-xl overflow-hidden shadow-sm">
            <img src="https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=400&q=80" alt="Travel Cost Optimizer" className="w-64 h-48 object-cover" />
            <div className="flex flex-col justify-center px-8 py-4 flex-1">
              <div className="font-semibold">Travel Cost Optimizer</div>
              <div className="text-sm text-gray-600 mb-4">Optimize your travel budget by analyzing expenses and finding cost-saving opportunities.</div>
              <button className="bg-blue-100 text-gray-800 rounded-full px-4 py-1 text-sm font-medium w-max hover:bg-blue-200" onClick={() => history.push("/travel-cost-optimizer")}>Optimize Costs</button>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default NextSteps;
