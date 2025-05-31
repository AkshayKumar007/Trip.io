import React from "react";
import { useLocation } from "react-router-dom";

interface OutlookProps {
  temperatureRange: string;
  chanceOfRain: string;
  generalOutlook: string;
  suggestions: string[];
}

interface OutlookLocationState {
  temperatureRange?: string;
  chanceOfRain?: string;
  generalOutlook?: string;
  suggestions?: string[];
}

const Outlook: React.FC<Partial<OutlookProps>> = (props) => {
  const location = useLocation<OutlookLocationState>();
  const temperatureRange = props.temperatureRange ?? location.state?.temperatureRange ?? "";
  const chanceOfRain = props.chanceOfRain ?? location.state?.chanceOfRain ?? "";
  const generalOutlook = props.generalOutlook ?? location.state?.generalOutlook ?? "";
  const suggestions = props.suggestions ?? location.state?.suggestions ?? [];

  return (
    <div style={{ padding: "2rem" }}>
      <h1 style={{ fontSize: "2rem", fontWeight: 700, marginBottom: "1.5rem" }}>
        Weather and Packing Suggestions for Your Trip
      </h1>
      <h2 style={{ fontWeight: 600, marginBottom: "1rem" }}>Weather Summary</h2>
      <div style={{ display: "flex", gap: "1.5rem", marginBottom: "2rem" }}>
        <div style={{ background: "#f3f6fa", borderRadius: 12, padding: "1.5rem", minWidth: 220 }}>
          <div style={{ fontWeight: 500, marginBottom: 8 }}>Temperature Range</div>
          <div style={{ fontSize: 22, fontWeight: 700 }}>{temperatureRange}</div>
        </div>
        <div style={{ background: "#f3f6fa", borderRadius: 12, padding: "1.5rem", minWidth: 220 }}>
          <div style={{ fontWeight: 500, marginBottom: 8 }}>Chance of Rain</div>
          <div style={{ fontSize: 22, fontWeight: 700 }}>{chanceOfRain}</div>
        </div>
        <div style={{ background: "#f3f6fa", borderRadius: 12, padding: "1.5rem", minWidth: 220 }}>
          <div style={{ fontWeight: 500, marginBottom: 8 }}>General Outlook</div>
          <div style={{ fontSize: 20, fontWeight: 700 }}>{generalOutlook}</div>
        </div>
      </div>
      <h3 style={{ fontWeight: 600, marginBottom: 8 }}>Packing Suggestions</h3>
      <ul style={{ marginLeft: 24 }}>
        {suggestions.map((s: string, i: number) => (
          <li key={i} style={{ marginBottom: 6 }}>{s}</li>
        ))}
      </ul>
      <button
        style={{
          marginTop: 32,
          padding: '10px 24px',
          background: '#3d99f5',
          color: 'white',
          border: 'none',
          borderRadius: 8,
          fontWeight: 600,
          fontSize: 16,
          cursor: 'pointer'
        }}
        onClick={() => window.location.href = '/packing-list'}
      >
        Go to Packing List
      </button>
    </div>
  );
};

export default Outlook;
