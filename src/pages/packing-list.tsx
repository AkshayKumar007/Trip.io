import { useEffect, useState } from "react";
import axios from "axios";
import { CategorySection } from "../components/category-picker";
import { PackingItem } from "../components/packing-item";

type PackingItemType = { id: string; name: string };
type Category = "clothing" | "accessory" | "daily_essentials";

type ItemsByCategory = {
  clothing: PackingItemType[];
  accessory: PackingItemType[];
  daily_essentials: PackingItemType[];
};

type SelectedItem = { item: PackingItemType; category: Category };

const TABS = [
  { label: "Clothing", key: "clothing" as Category },
  { label: "Accessories", key: "accessory" as Category },
  { label: "Daily Essentials", key: "daily_essentials" as Category },
];

function PackingListPage() {
  const [activeTab, setActiveTab] = useState<Category>("clothing");
  const [items, setItems] = useState<ItemsByCategory>({
    clothing: [],
    accessory: [],
    daily_essentials: [],
  });
  const [selected, setSelected] = useState<SelectedItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    async function fetchItems() {
      setLoading(true);
      setError("");
      try {
        // Use mock data from local JSON file for now
        const res = await fetch("/src/pages/packing-list.mock.json");
        const data = await res.json();
        setItems(data || { clothing: [], accessory: [], daily_essentials: [] });
      } catch {
        setError("Failed to load packing list");
      } finally {
        setLoading(false);
      }
    }
    fetchItems();
  }, []);

  const handleSelect = (item: PackingItemType, category: Category) => {
    setSelected((prev) => {
      const exists = prev.find((s) => s.item.id === item.id && s.category === category);
      if (exists) {
        return prev.filter((s) => !(s.item.id === item.id && s.category === category));
      } else {
        return [...prev, { item, category }];
      }
    });
  };

  const isSelected = (item: PackingItemType, category: Category) =>
    selected.some((s) => s.item.id === item.id && s.category === category);

  // Returns true if the item is NOT selected by the user
  const isNotSelected = (item: PackingItemType, category: Category) =>
    !selected.some((s) => s.item.id === item.id && s.category === category);

  const handleSubmit = async () => {
    setSubmitting(true);
    try {
      // Only send items NOT selected by the user
      const notSelectedItems: { item: PackingItemType; category: Category }[] = [];
      (Object.keys(items) as Category[]).forEach((category) => {
        items[category].forEach((item) => {
          if (isNotSelected(item, category)) {
            notSelectedItems.push({ item, category });
          }
        });
      });
      await axios.post("/api/submit-packing-list", notSelectedItems);
      alert("Packing list submitted!");
    } catch {
      alert("Failed to submit packing list");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="p-8 max-w-2xl mx-auto">
      <h1 className="text-2xl font-bold mb-6">Packing List for Trip</h1>
      <div className="flex space-x-4 mb-4">
        {TABS.map((tab) => (
          <button
            key={tab.key}
            className={`px-4 py-2 rounded-t-lg font-medium focus:outline-none ${
              activeTab === tab.key ? "bg-white border-b-2 border-blue-500" : "bg-gray-100"
            }`}
            onClick={() => setActiveTab(tab.key)}
          >
            {tab.label}
          </button>
        ))}
      </div>
      <div className="bg-white rounded-lg shadow p-6 min-h-[300px]">
        {loading ? (
          <div>Loading...</div>
        ) : error ? (
          <div className="text-red-500">{error}</div>
        ) : (
          <CategorySection title={TABS.find(t => t.key === activeTab)?.label || ""}>
            {(items[activeTab] || []).map((item) => (
              <PackingItem
                key={item.id}
                id={item.id}
                name={item.name}
                isChecked={isSelected(item, activeTab)}
                onToggle={() => handleSelect(item, activeTab)}
              />
            ))}
          </CategorySection>
        )}
      </div>
      <button
        className="mt-6 px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
        onClick={handleSubmit}
        disabled={submitting || selected.length === 0}
      >
        {submitting ? "Submitting..." : "Submit Packing List"}
      </button>
    </div>
  );
}

export default PackingListPage;
