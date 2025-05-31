import "../App.css";
import { ActionButton } from "../components/action-button";
import { Avatar, AvatarFallback } from "../components/avatar";
import { Header } from "../components/header";
import { PackingItem } from "../components/packing-item";
import { ProgressIndicator } from "../components/progress-indicator";
import { RecommendationCard } from "../components/recommendation-card";
import { SearchInput } from "../components/search-input";
import { TripCard } from "../components/trip-card";
import { WeatherCard } from "../components/weather-card";
import { CalendarPicker } from "../components/calendar-picker";

function ComponentSample() {
  return (
    <>
      {/* Header Sample */}
      <Header
        title="Trip.io Demo"
        showSearch
        showMenu
        userAvatar="https://randomuser.me/api/portraits/men/32.jpg"
        userName="Akshay"
      />
      <div style={{ display: "flex", flexWrap: "wrap", gap: 24, margin: 24 }}>
        {/* ActionButton Sample */}
        <div>
          <h3>ActionButton</h3>
          <ActionButton variant="primary">Primary</ActionButton>
          <ActionButton variant="secondary">Secondary</ActionButton>
          <ActionButton variant="outline">Outline</ActionButton>
          <ActionButton variant="ghost">Ghost</ActionButton>
        </div>
        <div>
          <h3>PackingItem</h3>
          <PackingItem id="1" name="Socks" isChecked={false} />
          <PackingItem id="2" name="Toothbrush" isChecked={true} />
          {/* ProgressIndicator Sample */}
          <div>
            <h3>ProgressIndicator</h3>
            <ProgressIndicator
              steps={["Plan", "Book", "Pack", "Go!"]}
              currentStep={2}
              completedSteps={[0, 1]}
            />
          </div>
          {/* RecommendationCard Sample */}
          <div style={{ maxWidth: 240 }}>
            <h3>RecommendationCard</h3>
            <RecommendationCard
              title="Hotel Blue"
              location="Goa"
              rating={4.5}
              price="$120"
              image="https://source.unsplash.com/240x120/?hotel"
              type="accommodation"
              description="A beautiful stay near the beach."
            />
          </div>
          {/* WeatherCard Sample */}
          <div style={{ maxWidth: 240 }}>
            <h3>WeatherCard</h3>
            <WeatherCard
              temperature="32°C"
              condition="sunny"
              humidity="60%"
              location="Goa"
              date="2025-06-10"
            />
          </div>
          {/* TripCard Sample */}
          <div style={{ maxWidth: 240 }}>
            <h3>TripCard</h3>
            <TripCard
              title="Goa Trip"
              location="Goa"
              date="2025-06-10"
              participants={5}
              image="https://source.unsplash.com/240x120/?beach"
              status="upcoming"
            />
          </div>
          {/* SearchInput Sample */}
          <div style={{ minWidth: 200 }}>
            <h3>SearchInput</h3>
            <SearchInput placeholder="Search places..." />
          </div>
          <div>
            <h3>CalendarPicker</h3>
            <CalendarPicker />
          </div>
          <div>
            {/* <h3>CategorySection</h3>
          <CategorySection title="Essentials">
            <div>Passport</div>
            <div>Tickets</div>
          </CategorySection> */}
          </div>
          {/* Avatar Sample */}
          <div>
            <h3>Avatar</h3>
            <Avatar
              src="https://randomuser.me/api/portraits/women/44.jpg"
              alt="User"
              style={{ width: 48, height: 48 }}
            />
            <AvatarFallback>AF</AvatarFallback>
          </div>
        </div>
      </div>
    </>
  );
}

export default ComponentSample;
