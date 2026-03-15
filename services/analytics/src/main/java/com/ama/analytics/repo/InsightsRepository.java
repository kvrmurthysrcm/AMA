package com.ama.analytics.repo;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.response.InsightCard;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InsightsRepository {

    public List<InsightCard> deriveInsights(AnalyticsFilterRequest request) {
        List<InsightCard> insights = new ArrayList<>();
        insights.add(new InsightCard(
                "OPPORTUNITY",
                "MEDIUM",
                "High traffic, low conversion products detected",
                "Products with above-average sessions and weaker conversion should be reviewed for pricing, content, and ratings.",
                null,
                "Optimize listing content, reviews, and pricing for the flagged products."
        ));
        insights.add(new InsightCard(
                "RISK",
                "HIGH",
                "Potential stockout risk in selected period",
                "Inventory with low days-of-cover may cause lost sales if replenishment is not planned.",
                null,
                "Prioritize replenishment for products with critical or high risk levels."
        ));
        return insights;
    }
}
