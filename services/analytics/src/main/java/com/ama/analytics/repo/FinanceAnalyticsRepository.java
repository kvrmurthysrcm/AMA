package com.ama.analytics.repo;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.response.ProfitabilityResponse;
import com.ama.analytics.query.FilterSqlBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FinanceAnalyticsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public FinanceAnalyticsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ProfitabilityResponse fetchProfitability(AnalyticsFilterRequest request) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("""
                SELECT COALESCE(SUM(fs.gross_sales_amount),0) AS gross_sales,
                       COALESCE(SUM(fs.net_sales_amount),0) AS net_sales,
                       COALESCE(SUM(ff.fee_amount + ff.commission_amount + ff.fulfillment_fee_amount + ff.storage_fee_amount + ff.refund_amount),0) AS fees,
                       COALESCE(SUM(fa.spend_amount),0) AS ad_spend,
                       0::numeric AS cogs,
                       COALESCE(SUM(ff.estimated_profit_amount),0) AS gross_profit,
                       COALESCE(SUM(ff.estimated_profit_amount),0) AS contribution_margin,
                       CASE WHEN COALESCE(SUM(fs.net_sales_amount),0) = 0 THEN 0 ELSE (COALESCE(SUM(ff.estimated_profit_amount),0) / NULLIF(SUM(fs.net_sales_amount),0)) * 100 END AS margin_pct
                FROM ama.fact_finance_daily ff
                LEFT JOIN ama.fact_sales_daily fs ON fs.tenant_id = ff.tenant_id AND fs.account_id = ff.account_id AND fs.product_id = ff.product_id AND fs.date_key = ff.date_key
                LEFT JOIN ama.fact_advertising_daily fa ON fa.tenant_id = ff.tenant_id AND fa.account_id = ff.account_id AND fa.product_id = ff.product_id AND fa.date_key = ff.date_key
                LEFT JOIN ama.product p ON p.product_id = ff.product_id
                JOIN ama.dim_date dd ON dd.date_key = ff.date_key
                """);
        FilterSqlBuilder.appendCommonFilters(sql, params, request, "ff", "p");
        return jdbcTemplate.queryForObject(sql.toString(), params, (rs, rowNum) -> new ProfitabilityResponse(
                rs.getDouble("gross_sales"), rs.getDouble("net_sales"), rs.getDouble("fees"), rs.getDouble("ad_spend"), rs.getDouble("cogs"), rs.getDouble("gross_profit"), rs.getDouble("contribution_margin"), rs.getDouble("margin_pct")
        ));
    }
}
