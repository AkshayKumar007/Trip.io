-- Create trip table
CREATE TABLE IF NOT EXISTS trip (
    id BIGSERIAL PRIMARY KEY,
    destination VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    budget DECIMAL(10, 2),
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CHECK (end_date >= start_date)
);

-- Create weather_forecast table
CREATE TABLE IF NOT EXISTS weather_forecast (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL,
    forecast_date DATE NOT NULL,
    temperature_min DECIMAL(5, 2),
    temperature_max DECIMAL(5, 2),
    condition VARCHAR(100),
    precipitation_chance INT,
    humidity INT,
    wind_speed DECIMAL(5, 2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (trip_id) REFERENCES trip(id) ON DELETE CASCADE,
    UNIQUE (trip_id, forecast_date)
);

-- Create packing_item table
CREATE TABLE IF NOT EXISTS packing_item (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    is_packed BOOLEAN DEFAULT FALSE,
    is_required BOOLEAN DEFAULT TRUE,
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (trip_id) REFERENCES trip(id) ON DELETE CASCADE
);

-- Create shop_item table
CREATE TABLE IF NOT EXISTS shop_item (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    estimated_price DECIMAL(10, 2),
    is_purchased BOOLEAN DEFAULT FALSE,
    priority VARCHAR(20) CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (trip_id) REFERENCES trip(id) ON DELETE CASCADE
);

-- Create cost_item table
CREATE TABLE IF NOT EXISTS cost_item (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    is_paid BOOLEAN DEFAULT FALSE,
    payment_date DATE,
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (trip_id) REFERENCES trip(id) ON DELETE CASCADE
);

-- Create index for better query performance
CREATE INDEX idx_trip_dates ON trip(start_date, end_date);
CREATE INDEX idx_weather_forecast ON weather_forecast(trip_id, forecast_date);
CREATE INDEX idx_packing_item_trip ON packing_item(trip_id, category);
CREATE INDEX idx_shop_item_trip ON shop_item(trip_id, is_purchased);
CREATE INDEX idx_cost_item_trip ON cost_item(trip_id, category, is_paid);
