-- Insert sample trip data
INSERT INTO trip (destination, start_date, end_date, budget, notes)
VALUES 
    ('Paris, France', CURRENT_DATE + 30, CURRENT_DATE + 37, 2500.00, 'Summer vacation in Paris'),
    ('Tokyo, Japan', CURRENT_DATE + 60, CURRENT_DATE + 75, 5000.00, 'Spring trip to Japan'),
    ('New York, USA', CURRENT_DATE + 90, CURRENT_DATE + 97, 4000.00, 'Business conference and sightseeing');

-- Insert sample packing items for the first trip
INSERT INTO packing_item (trip_id, name, category, quantity, is_packed, is_required, notes)
VALUES 
    (1, 'Passport', 'Travel Documents', 1, false, true, 'Check expiry date'),
    (1, 'T-Shirts', 'Clothing', 5, false, true, 'Summer clothes'),
    (1, 'Jeans', 'Clothing', 2, false, true, NULL),
    (1, 'Toothbrush', 'Toiletries', 1, false, true, NULL),
    (1, 'Camera', 'Electronics', 1, true, false, 'With charger');

-- Insert sample shop items
INSERT INTO shop_item (trip_id, name, category, estimated_price, is_purchased, priority, notes)
VALUES 
    (1, 'Travel Adapter', 'Accessories', 25.99, false, 'HIGH', 'European plug type'),
    (1, 'Sunscreen', 'Toiletries', 12.50, false, 'MEDIUM', 'SPF 50+'),
    (1, 'Guide Book', 'Books', 18.99, true, 'LOW', 'Paris 2023 edition');

-- Insert sample cost items
INSERT INTO cost_item (trip_id, name, category, amount, currency, is_paid, payment_date, notes)
VALUES 
    (1, 'Flight Tickets', 'Travel', 1200.00, 'USD', true, CURRENT_DATE - 10, 'Round trip to Paris'),
    (1, 'Hotel Booking', 'Accommodation', 800.00, 'USD', true, CURRENT_DATE - 5, '7 nights in 4-star hotel'),
    (1, 'Travel Insurance', 'Insurance', 85.50, 'USD', false, NULL, 'To be purchased before departure');

-- Insert sample weather forecast for the first trip
INSERT INTO weather_forecast (trip_id, forecast_date, temperature_min, temperature_max, condition, precipitation_chance, humidity, wind_speed)
SELECT 
    1,
    (CURRENT_DATE + 30 + n) AS forecast_date,
    (18 + RANDOM() * 5)::numeric(5,2) AS temperature_min,
    (24 + RANDOM() * 8)::numeric(5,2) AS temperature_max,
    (ARRAY['Sunny', 'Partly Cloudy', 'Cloudy', 'Rainy'])[1 + floor(RANDOM() * 4)] AS condition,
    floor(RANDOM() * 50)::int AS precipitation_chance,
    (40 + RANDOM() * 40)::int AS humidity,
    (5 + RANDOM() * 15)::numeric(5,2) AS wind_speed
FROM generate_series(0, 7) AS n;
