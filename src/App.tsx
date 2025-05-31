import "./App.css";
import { BrowserRouter as Router, Route, Switch, Redirect } from "react-router-dom";
import TripDetailsPage from "./pages/trip-details";
import ComponentSample from "./pages/component-sample";
import Outlook from "./pages/outlook";
import PackingListPage from "./pages/packing-list";
import NextSteps from "./pages/next-steps";
import ShopEssentials from "./pages/shop-essentials";
import TravelCostOptimizer from "./pages/travel-cost-optimizer";

function App() {
  return (
    <Router>
      <Switch>
        <Route path="/trip-details" component={TripDetailsPage} />
        <Route path="/component-sample" component={ComponentSample} />
        <Route path="/outlook" render={() => (
          <Outlook
            temperatureRange="15°C – 25°C"
            chanceOfRain="20%"
            generalOutlook="Sunny with occasional clouds"
            suggestions={[
              "Warm layers needed for cooler evenings",
              "Pack swimwear and sun protection for sunny days"
            ]}
          />
        )} />
        <Route path="/packing-list" component={PackingListPage} />
        <Route path="/next-steps" component={NextSteps} />
        <Route path="/shop-essentials" component={ShopEssentials} />
        <Route path="/travel-cost-optimizer" component={TravelCostOptimizer} />
        <Redirect to="/next-steps" />
      </Switch>
    </Router>
  );
}

export default App;
