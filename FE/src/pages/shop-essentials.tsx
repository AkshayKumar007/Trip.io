import React, { useEffect, useState } from "react";

interface ShopItem {
  id: string;
  name: string;
  description: string;
  image: string;
}

const ShopEssentials: React.FC = () => {
  const [items, setItems] = useState<ShopItem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Mock API call to fetch shop essentials from local JSON
    import("./shop-essentials.mock.json").then((module) => {
      const data = (module as { default: { items: ShopItem[] } }).default;
      setItems(data.items || []);
      setLoading(false);
    });
  }, []);

  return (
    <div className="min-h-screen bg-gray-50 px-8 py-8">
      <header className="flex items-center justify-between border-b pb-4 mb-8">
        <span className="font-bold text-lg">Smart Back Packing</span>
        <nav className="flex gap-8 text-sm">
          <a href="/trips" className="hover:underline">My Trips</a>
          <a href="/wardrobe" className="hover:underline">Wardrobe</a>
          <a href="/packing-list" className="hover:underline">Packing Lists</a>
          <a href="/shop-essentials" className="hover:underline font-semibold">Shopping</a>
        </nav>
        <div className="flex items-center gap-4">
          <span className="rounded-full bg-gray-200 w-8 h-8 flex items-center justify-center">
            <svg width="20" height="20" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10" /></svg>
          </span>
          <img src="https://randomuser.me/api/portraits/women/44.jpg" alt="avatar" className="w-8 h-8 rounded-full" />
        </div>
      </header>
      <main className="max-w-3xl mx-auto">
        <h1 className="text-2xl font-bold mb-2">Shopping List</h1>
        <p className="text-sm text-gray-500 mb-8">Items from your finalized wardrobe that you still need to purchase.</p>
        {loading ? (
          <div>Loading...</div>
        ) : (
          <ul className="flex flex-col gap-4">
            {items.map((item) => (
              <li key={item.id} className="flex items-center gap-4 bg-white rounded-lg px-4 py-2 shadow-sm">
                <img src={item.image} alt={item.name} className="w-12 h-12 rounded object-cover border" />
                <div className="flex-1">
                  <div className="font-medium">{item.name}</div>
                  <div className="text-xs text-gray-500">{item.description}</div>
                </div>
                <button className="bg-blue-100 text-gray-800 rounded-full px-4 py-1 text-sm font-medium hover:bg-blue-200">Shop Now</button>
              </li>
            ))}
          </ul>
        )}
      </main>
    </div>
  );
};

export default ShopEssentials;
