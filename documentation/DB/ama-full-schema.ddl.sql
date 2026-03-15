
-- =========================================================
-- AMA Analytics Platform - Full PostgreSQL DDL
-- Includes all current and future-phase tables
-- Schema: ama
-- Optional extension: pgvector
-- =========================================================

BEGIN;

CREATE SCHEMA IF NOT EXISTS ama;

-- =========================================================
-- 1. OPTIONAL EXTENSIONS
-- =========================================================
-- Uncomment if pgvector is installed in your PostgreSQL instance
-- CREATE EXTENSION IF NOT EXISTS vector;

-- =========================================================
-- 2. CORE SAAS / TENANT TABLES
-- =========================================================

CREATE TABLE IF NOT EXISTS ama.tenant (
    tenant_id           BIGSERIAL PRIMARY KEY,
    tenant_code         VARCHAR(50) NOT NULL UNIQUE,
    tenant_name         VARCHAR(200) NOT NULL,
    plan_code           VARCHAR(50),
    status              VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    default_currency    VARCHAR(10) DEFAULT 'USD',
    default_timezone    VARCHAR(100) DEFAULT 'UTC',
    country_code        VARCHAR(10),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS ama.app_user (
    user_id               BIGSERIAL PRIMARY KEY,
    tenant_id             BIGINT NOT NULL REFERENCES ama.tenant(tenant_id),
    external_user_id      VARCHAR(200),
    email                 VARCHAR(255) NOT NULL,
    full_name             VARCHAR(200),
    role_code             VARCHAR(50) NOT NULL,
    status                VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    last_login_at         TIMESTAMPTZ,
    created_at            TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_app_user_tenant_email UNIQUE (tenant_id, email)
);

CREATE TABLE IF NOT EXISTS ama.tenant_marketplace_account (
    account_id              BIGSERIAL PRIMARY KEY,
    tenant_id               BIGINT NOT NULL REFERENCES ama.tenant(tenant_id),
    marketplace_code        VARCHAR(30) NOT NULL,
    marketplace_name        VARCHAR(100) NOT NULL,
    seller_account_ref      VARCHAR(200),
    account_type            VARCHAR(30) NOT NULL DEFAULT 'SELLER',
    connection_status       VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    auth_mode               VARCHAR(30),
    last_sync_at            TIMESTAMPTZ,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_tenant_marketplace_account UNIQUE (tenant_id, marketplace_code, seller_account_ref)
);

-- =========================================================
-- 3. PRODUCT MASTER / CATALOG TABLES
-- =========================================================

CREATE TABLE IF NOT EXISTS ama.brand (
    brand_id              BIGSERIAL PRIMARY KEY,
    tenant_id             BIGINT NOT NULL REFERENCES ama.tenant(tenant_id),
    brand_code            VARCHAR(100),
    brand_name            VARCHAR(200) NOT NULL,
    created_at            TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_brand_tenant_name UNIQUE (tenant_id, brand_name)
);

CREATE TABLE IF NOT EXISTS ama.category (
    category_id           BIGSERIAL PRIMARY KEY,
    tenant_id             BIGINT NOT NULL REFERENCES ama.tenant(tenant_id),
    category_code         VARCHAR(100),
    category_name         VARCHAR(200) NOT NULL,
    parent_category_id    BIGINT REFERENCES ama.category(category_id),
    category_level        SMALLINT DEFAULT 1,
    created_at            TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_category_tenant_name UNIQUE (tenant_id, category_name)
);

CREATE TABLE IF NOT EXISTS ama.product (
    product_id                BIGSERIAL PRIMARY KEY,
    tenant_id                 BIGINT NOT NULL REFERENCES ama.tenant(tenant_id),
    account_id                BIGINT REFERENCES ama.tenant_marketplace_account(account_id),
    asin                      VARCHAR(30),
    sku                       VARCHAR(100),
    product_name              VARCHAR(500) NOT NULL,
    short_description         TEXT,
    brand_id                  BIGINT REFERENCES ama.brand(brand_id),
    category_id               BIGINT REFERENCES ama.category(category_id),
    product_type              VARCHAR(100),
    price_amount              NUMERIC(14,2),
    currency_code             VARCHAR(10) DEFAULT 'USD',
    rating_avg                NUMERIC(4,2),
    review_count              INTEGER,
    is_active                 BOOLEAN NOT NULL DEFAULT TRUE,
    launched_at               DATE,
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_product_tenant_asin_sku UNIQUE (tenant_id, asin, sku)
);

CREATE TABLE IF NOT EXISTS ama.product_attribute (
    product_attribute_id      BIGSERIAL PRIMARY KEY,
    product_id                BIGINT NOT NULL REFERENCES ama.product(product_id),
    attribute_name            VARCHAR(100) NOT NULL,
    attribute_value           TEXT,
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_product_attribute UNIQUE (product_id, attribute_name)
);

-- =========================================================
-- 4. DATE DIMENSION
-- =========================================================

CREATE TABLE IF NOT EXISTS ama.dim_date (
    date_key              INTEGER PRIMARY KEY,
    full_date             DATE NOT NULL UNIQUE,
    year_num              SMALLINT NOT NULL,
    quarter_num           SMALLINT NOT NULL,
    month_num             SMALLINT NOT NULL,
    month_name            VARCHAR(20) NOT NULL,
    week_num              SMALLINT NOT NULL,
    day_num               SMALLINT NOT NULL,
    day_name              VARCHAR(20) NOT NULL,
    is_weekend            BOOLEAN NOT NULL,
    is_month_start        BOOLEAN NOT NULL,
    is_month_end          BOOLEAN NOT NULL
);

-- =========================================================
-- 5. CORE ANALYTICS FACT TABLES
-- =========================================================

CREATE TABLE IF NOT EXISTS ama.fact_sales_daily (
    sales_daily_id            BIGSERIAL PRIMARY KEY,
    tenant_id                 BIGINT NOT NULL REFERENCES ama.tenant(tenant_id),
    account_id                BIGINT REFERENCES ama.tenant_marketplace_account(account_id),
    product_id                BIGINT NOT NULL REFERENCES ama.product(product_id),
    date_key                  INTEGER NOT NULL REFERENCES ama.dim_date(date_key),
    marketplace_code          VARCHAR(30),
    units_sold                INTEGER NOT NULL DEFAULT 0,
    orders_count              INTEGER NOT NULL DEFAULT 0,
    gross_sales_amount        NUMERIC(14,2) NOT NULL DEFAULT 0,
    discount_amount           NUMERIC(14,2) NOT NULL DEFAULT 0,
    net_sales_amount          NUMERIC(14,2) NOT NULL DEFAULT 0,
    returned_units            INTEGER NOT NULL DEFAULT 0,
    returned_amount           NUMERIC(14,2) NOT NULL DEFAULT 0,
    avg_selling_price         NUMERIC(14,2),
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_fact_sales_daily UNIQUE (tenant_id, account_id, product_id, date_key, marketplace_code)
);

CREATE TABLE IF NOT EXISTS ama.fact_traffic_daily (
    traffic_daily_id          BIGSERIAL PRIMARY KEY,
    tenant_id                 BIGINT NOT NULL REFERENCES ama.tenant(tenant_id),
    account_id                BIGINT REFERENCES ama.tenant_marketplace_account(account_id),
    product_id                BIGINT NOT NULL REFERENCES ama.product(product_id),
    date_key                  INTEGER NOT NULL REFERENCES ama.dim_date(date_key),
    marketplace_code          VARCHAR(30),
    sessions_count            INTEGER NOT NULL DEFAULT 0,
    page_views_count          INTEGER NOT NULL DEFAULT 0,
    buy_box_percentage        NUMERIC(5,2),
    unit_session_percentage   NUMERIC(7,4),
    browser_sessions          INTEGER,
    mobile_app_sessions       INTEGER,
    browser_page_views        INTEGER,
    mobile_app_page_views     INTEGER,
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_fact_traffic_daily UNIQUE (tenant_id, account_id, product_id, date_key, marketplace_code)
);

CREATE TABLE IF NOT EXISTS ama.fact_inventory_daily (
    inventory_daily_id        BIGSERIAL PRIMARY KEY,
    tenant_id                 BIGINT NOT NULL REFERENCES ama.tenant(tenant_id),
    account_id                BIGINT REFERENCES ama.tenant_marketplace_account(account_id),
    product_id                BIGINT NOT NULL REFERENCES ama.product(product_id),
    date_key                  INTEGER NOT NULL REFERENCES ama.dim_date(date_key),
    marketplace_code          VARCHAR(30),
    opening_stock_qty         INTEGER NOT NULL DEFAULT 0,
    closing_stock_qty         INTEGER NOT NULL DEFAULT 0,
    inbound_qty               INTEGER NOT NULL DEFAULT 0,
    reserved_qty              INTEGER NOT NULL DEFAULT 0,
    damaged_qty               INTEGER NOT NULL DEFAULT 0,
    sellable_qty              INTEGER NOT NULL DEFAULT 0,
    days_of_cover             NUMERIC(10,2),
    low_stock_flag            BOOLEAN NOT NULL DEFAULT FALSE,
    out_of_stock_flag         BOOLEAN NOT NULL DEFAULT FALSE,
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_fact_inventory_daily UNIQUE (tenant_id, account_id, product_id, date_key, marketplace_code)
);

CREATE TABLE IF NOT EXISTS ama.fact_finance_daily (
    finance_daily_id          BIGSERIAL PRIMARY KEY,
    tenant_id                 BIGINT NOT NULL REFERENCES ama.tenant(tenant_id),
    account_id                BIGINT REFERENCES ama.tenant_marketplace_account(account_id),
    product_id                BIGINT REFERENCES ama.product(product_id),
    date_key                  INTEGER NOT NULL REFERENCES ama.dim_date(date_key),
    marketplace_code          VARCHAR(30),
    fee_amount                NUMERIC(14,2) NOT NULL DEFAULT 0,
    commission_amount         NUMERIC(14,2) NOT NULL DEFAULT 0,
    fulfillment_fee_amount    NUMERIC(14,2) NOT NULL DEFAULT 0,
    storage_fee_amount        NUMERIC(14,2) NOT NULL DEFAULT 0,
    refund_amount             NUMERIC(14,2) NOT NULL DEFAULT 0,
    adjustment_amount         NUMERIC(14,2) NOT NULL DEFAULT 0,
    payout_amount             NUMERIC(14,2) NOT NULL DEFAULT 0,
    estimated_profit_amount   NUMERIC(14,2),
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_fact_finance_daily UNIQUE (tenant_id, account_id, product_id, date_key, marketplace_code)
);

CREATE TABLE IF NOT EXISTS ama.fact_advertising_daily (
    advertising_daily_id      BIGSERIAL PRIMARY KEY,
    tenant_id                 BIGINT NOT NULL REFERENCES ama.tenant(tenant_id),
    account_id                BIGINT REFERENCES ama.tenant_marketplace_account(account_id),
    product_id                BIGINT REFERENCES ama.product(product_id),
    date_key                  INTEGER NOT NULL REFERENCES ama.dim_date(date_key),
    marketplace_code          VARCHAR(30),
    impressions_count         BIGINT NOT NULL DEFAULT 0,
    clicks_count              INTEGER NOT NULL DEFAULT 0,
    spend_amount              NUMERIC(14,2) NOT NULL DEFAULT 0,
    ad_sales_amount           NUMERIC(14,2) NOT NULL DEFAULT 0,
    acos_percentage           NUMERIC(8,4),
    roas_value                NUMERIC(10,4),
    conversions_count         INTEGER NOT NULL DEFAULT 0,
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_fact_advertising_daily UNIQUE (tenant_id, account_id, product_id, date_key, marketplace_code)
);

-- =========================================================
-- 6. LOWER-GRAIN ORDER TABLES (FUTURE)
-- =========================================================

CREATE TABLE IF NOT EXISTS ama.fact_order_header (
    order_id                  BIGSERIAL PRIMARY KEY,
    tenant_id                 BIGINT NOT NULL REFERENCES ama.tenant(tenant_id),
    account_id                BIGINT REFERENCES ama.tenant_marketplace_account(account_id),
    external_order_id         VARCHAR(100) NOT NULL,
    marketplace_code          VARCHAR(30),
    order_date                TIMESTAMPTZ NOT NULL,
    order_status              VARCHAR(50),
    currency_code             VARCHAR(10),
    order_total_amount        NUMERIC(14,2),
    shipping_amount           NUMERIC(14,2),
    tax_amount                NUMERIC(14,2),
    item_count                INTEGER,
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_fact_order_header UNIQUE (tenant_id, account_id, external_order_id)
);

CREATE TABLE IF NOT EXISTS ama.fact_order_item (
    order_item_id             BIGSERIAL PRIMARY KEY,
    tenant_id                 BIGINT NOT NULL REFERENCES ama.tenant(tenant_id),
    order_id                  BIGINT NOT NULL REFERENCES ama.fact_order_header(order_id),
    product_id                BIGINT REFERENCES ama.product(product_id),
    external_order_item_id    VARCHAR(100),
    quantity_ordered          INTEGER NOT NULL DEFAULT 0,
    quantity_shipped          INTEGER NOT NULL DEFAULT 0,
    item_price_amount         NUMERIC(14,2),
    item_tax_amount           NUMERIC(14,2),
    promotion_amount          NUMERIC(14,2),
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- =========================================================
-- 7. AGENT / MCP / QUERY SUPPORT TABLES
-- =========================================================

CREATE TABLE IF NOT EXISTS ama.analytics_tool_registry (
    tool_id                   BIGSERIAL PRIMARY KEY,
    tool_name                 VARCHAR(100) NOT NULL UNIQUE,
    tool_description          TEXT NOT NULL,
    input_schema_json         JSONB NOT NULL,
    output_schema_json        JSONB,
    is_active                 BOOLEAN NOT NULL DEFAULT TRUE,
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS ama.nl_query_audit (
    query_audit_id            BIGSERIAL PRIMARY KEY,
    tenant_id                 BIGINT REFERENCES ama.tenant(tenant_id),
    user_id                   BIGINT REFERENCES ama.app_user(user_id),
    raw_question              TEXT NOT NULL,
    detected_intent           VARCHAR(100),
    selected_tool_name        VARCHAR(100),
    tool_input_json           JSONB,
    response_summary          TEXT,
    response_chart_type       VARCHAR(50),
    execution_status          VARCHAR(30),
    execution_ms              INTEGER,
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS ama.saved_query_template (
    query_template_id         BIGSERIAL PRIMARY KEY,
    tenant_id                 BIGINT NOT NULL REFERENCES ama.tenant(tenant_id),
    template_name             VARCHAR(200) NOT NULL,
    question_text             TEXT NOT NULL,
    default_tool_name         VARCHAR(100),
    default_parameters_json   JSONB,
    is_public                 BOOLEAN NOT NULL DEFAULT FALSE,
    created_by_user_id        BIGINT REFERENCES ama.app_user(user_id),
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- =========================================================
-- 8. FUTURE INGESTION TABLES
-- =========================================================

CREATE TABLE IF NOT EXISTS ama.ingestion_job (
    ingestion_job_id          BIGSERIAL PRIMARY KEY,
    tenant_id                 BIGINT NOT NULL REFERENCES ama.tenant(tenant_id),
    account_id                BIGINT REFERENCES ama.tenant_marketplace_account(account_id),
    source_system             VARCHAR(50) NOT NULL,
    source_object_type        VARCHAR(100) NOT NULL,
    requested_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    started_at                TIMESTAMPTZ,
    completed_at              TIMESTAMPTZ,
    job_status                VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    request_payload_json      JSONB,
    response_payload_json     JSONB,
    error_message             TEXT
);

CREATE TABLE IF NOT EXISTS ama.raw_file_registry (
    raw_file_id               BIGSERIAL PRIMARY KEY,
    tenant_id                 BIGINT NOT NULL REFERENCES ama.tenant(tenant_id),
    account_id                BIGINT REFERENCES ama.tenant_marketplace_account(account_id),
    ingestion_job_id          BIGINT REFERENCES ama.ingestion_job(ingestion_job_id),
    source_system             VARCHAR(50) NOT NULL,
    file_name                 VARCHAR(500) NOT NULL,
    file_path                 VARCHAR(1000) NOT NULL,
    file_format               VARCHAR(30) NOT NULL,
    file_size_bytes           BIGINT,
    checksum_value            VARCHAR(200),
    business_date             DATE,
    loaded_flag               BOOLEAN NOT NULL DEFAULT FALSE,
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS ama.ingestion_reject_record (
    reject_record_id          BIGSERIAL PRIMARY KEY,
    raw_file_id               BIGINT REFERENCES ama.raw_file_registry(raw_file_id),
    record_number             BIGINT,
    reject_reason             TEXT NOT NULL,
    raw_record_json           JSONB,
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- =========================================================
-- 9. OPTIONAL PGVECTOR TABLES
-- =========================================================
-- Requires: CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS ama.document_chunk (
    chunk_id                  BIGSERIAL PRIMARY KEY,
    tenant_id                 BIGINT NOT NULL REFERENCES ama.tenant(tenant_id),
    source_type               VARCHAR(50) NOT NULL,
    source_ref                VARCHAR(300),
    chunk_text                TEXT NOT NULL,
    metadata_json             JSONB,
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Uncomment after enabling pgvector:
-- CREATE TABLE IF NOT EXISTS ama.document_chunk_embedding (
--     chunk_id                  BIGINT PRIMARY KEY REFERENCES ama.document_chunk(chunk_id),
--     embedding                 VECTOR(1536) NOT NULL,
--     created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW()
-- );

-- =========================================================
-- 10. INDEXES
-- =========================================================

CREATE INDEX IF NOT EXISTS idx_app_user_tenant_role
    ON ama.app_user (tenant_id, role_code);

CREATE INDEX IF NOT EXISTS idx_tenant_marketplace_account_tenant_status
    ON ama.tenant_marketplace_account (tenant_id, connection_status);

CREATE INDEX IF NOT EXISTS idx_product_tenant_category
    ON ama.product (tenant_id, category_id);

CREATE INDEX IF NOT EXISTS idx_product_tenant_brand
    ON ama.product (tenant_id, brand_id);

CREATE INDEX IF NOT EXISTS idx_product_tenant_active
    ON ama.product (tenant_id, is_active);

CREATE INDEX IF NOT EXISTS idx_fact_sales_daily_tenant_date
    ON ama.fact_sales_daily (tenant_id, date_key);

CREATE INDEX IF NOT EXISTS idx_fact_sales_daily_tenant_product_date
    ON ama.fact_sales_daily (tenant_id, product_id, date_key);

CREATE INDEX IF NOT EXISTS idx_fact_sales_daily_account_date
    ON ama.fact_sales_daily (account_id, date_key);

CREATE INDEX IF NOT EXISTS idx_fact_traffic_daily_tenant_date
    ON ama.fact_traffic_daily (tenant_id, date_key);

CREATE INDEX IF NOT EXISTS idx_fact_inventory_daily_tenant_date
    ON ama.fact_inventory_daily (tenant_id, date_key);

CREATE INDEX IF NOT EXISTS idx_fact_inventory_daily_low_stock
    ON ama.fact_inventory_daily (tenant_id, low_stock_flag, out_of_stock_flag, date_key);

CREATE INDEX IF NOT EXISTS idx_fact_finance_daily_tenant_date
    ON ama.fact_finance_daily (tenant_id, date_key);

CREATE INDEX IF NOT EXISTS idx_fact_advertising_daily_tenant_date
    ON ama.fact_advertising_daily (tenant_id, date_key);

CREATE INDEX IF NOT EXISTS idx_fact_order_header_tenant_order_date
    ON ama.fact_order_header (tenant_id, order_date);

CREATE INDEX IF NOT EXISTS idx_fact_order_item_order
    ON ama.fact_order_item (order_id);

CREATE INDEX IF NOT EXISTS idx_nl_query_audit_tenant_created
    ON ama.nl_query_audit (tenant_id, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_saved_query_template_tenant
    ON ama.saved_query_template (tenant_id);

CREATE INDEX IF NOT EXISTS idx_ingestion_job_tenant_status
    ON ama.ingestion_job (tenant_id, job_status, requested_at DESC);

CREATE INDEX IF NOT EXISTS idx_raw_file_registry_tenant_business_date
    ON ama.raw_file_registry (tenant_id, business_date);

-- Optional pgvector index after embedding table creation:
-- CREATE INDEX IF NOT EXISTS idx_document_chunk_embedding_vector
--     ON ama.document_chunk_embedding
--     USING ivfflat (embedding vector_cosine_ops)
--     WITH (lists = 100);

COMMIT;
