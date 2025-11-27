package com.outfitlab.project.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionModelTest {

    // ========== CONSTRUCTOR TEST ==========

    @Test
    void givenEmptyConstructorWhenCreateThenFieldsAreNull() {
        // WHEN
        SubscriptionModel subscription = new SubscriptionModel();

        // THEN
        assertNull(subscription.getName());
        assertNull(subscription.getPlanCode());
        assertNull(subscription.getPrice());
        assertNull(subscription.getCurrency());
        assertNull(subscription.getFrequency());
        assertNull(subscription.getDescription());
        assertFalse(subscription.isPopular());
        assertFalse(subscription.isHasAnalytics());
        assertFalse(subscription.isHasAdvancedReports());
    }

    // ========== SETTER/GETTER TESTS - BASIC FIELDS ==========

    @Test
    void givenSubscriptionWhenSetNameThenGetReturnsCorrectValue() {
        // GIVEN
        SubscriptionModel subscription = new SubscriptionModel();
        String name = "Premium Plan";

        // WHEN
        subscription.setName(name);

        // THEN
        assertEquals(name, subscription.getName());
    }

    @Test
    void givenSubscriptionWhenSetPlanCodeThenGetReturnsCorrectValue() {
        // GIVEN
        SubscriptionModel subscription = new SubscriptionModel();
        String planCode = "PLAN-PREMIUM-001";

        // WHEN
        subscription.setPlanCode(planCode);

        // THEN
        assertEquals(planCode, subscription.getPlanCode());
    }

    @Test
    void givenSubscriptionWhenSetPriceThenGetReturnsCorrectValue() {
        // GIVEN
        SubscriptionModel subscription = new SubscriptionModel();
        Double price = 99.99;

        // WHEN
        subscription.setPrice(price);

        // THEN
        assertEquals(price, subscription.getPrice());
    }

    @Test
    void givenSubscriptionWhenSetCurrencyThenGetReturnsCorrectValue() {
        // GIVEN
        SubscriptionModel subscription = new SubscriptionModel();
        String currency = "USD";

        // WHEN
        subscription.setCurrency(currency);

        // THEN
        assertEquals(currency, subscription.getCurrency());
    }

    @Test
    void givenSubscriptionWhenSetFrequencyThenGetReturnsCorrectValue() {
        // GIVEN
        SubscriptionModel subscription = new SubscriptionModel();
        String frequency = "monthly";

        // WHEN
        subscription.setFrequency(frequency);

        // THEN
        assertEquals(frequency, subscription.getFrequency());
    }

    @Test
    void givenSubscriptionWhenSetDescriptionThenGetReturnsCorrectValue() {
        // GIVEN
        SubscriptionModel subscription = new SubscriptionModel();
        String description = "Premium features for advanced users";

        // WHEN
        subscription.setDescription(description);

        // THEN
        assertEquals(description, subscription.getDescription());
    }

    // ========== SETTER/GETTER TESTS - FEATURES ==========

    @Test
    void givenSubscriptionWhenSetFeaturesThenGetReturnsCorrectValues() {
        // GIVEN
        SubscriptionModel subscription = new SubscriptionModel();
        String feature1 = "Unlimited garments";
        String feature2 = "Advanced analytics";
        String feature3 = "Priority support";

        // WHEN
        subscription.setFeature1(feature1);
        subscription.setFeature2(feature2);
        subscription.setFeature3(feature3);

        // THEN
        assertEquals(feature1, subscription.getFeature1());
        assertEquals(feature2, subscription.getFeature2());
        assertEquals(feature3, subscription.getFeature3());
    }

    @Test
    void givenSubscriptionWhenSetCardColorThenGetReturnsCorrectValue() {
        // GIVEN
        SubscriptionModel subscription = new SubscriptionModel();
        String cardColor = "#FF5733";

        // WHEN
        subscription.setCardColor(cardColor);

        // THEN
        assertEquals(cardColor, subscription.getCardColor());
    }

    // ========== SETTER/GETTER TESTS - BOOLEAN FLAGS ==========

    @Test
    void givenSubscriptionWhenSetPopularThenGetReturnsCorrectValue() {
        // GIVEN
        SubscriptionModel subscription = new SubscriptionModel();

        // WHEN
        subscription.setPopular(true);

        // THEN
        assertTrue(subscription.isPopular());
    }

    @Test
    void givenSubscriptionWhenSetHasAnalyticsThenGetReturnsCorrectValue() {
        // GIVEN
        SubscriptionModel subscription = new SubscriptionModel();

        // WHEN
        subscription.setHasAnalytics(true);

        // THEN
        assertTrue(subscription.isHasAnalytics());
    }

    @Test
    void givenSubscriptionWhenSetHasAdvancedReportsThenGetReturnsCorrectValue() {
        // GIVEN
        SubscriptionModel subscription = new SubscriptionModel();

        // WHEN
        subscription.setHasAdvancedReports(true);

        // THEN
        assertTrue(subscription.isHasAdvancedReports());
    }

    // ========== SETTER/GETTER TESTS - PLAN DETAILS ==========

    @Test
    void givenSubscriptionWhenSetPlanTypeThenGetReturnsCorrectValue() {
        // GIVEN
        SubscriptionModel subscription = new SubscriptionModel();
        String planType = "PREMIUM";

        // WHEN
        subscription.setPlanType(planType);

        // THEN
        assertEquals(planType, subscription.getPlanType());
    }

    @Test
    void givenSubscriptionWhenSetMaxGarmentsThenGetReturnsCorrectValue() {
        // GIVEN
        SubscriptionModel subscription = new SubscriptionModel();
        Integer maxGarments = 100;

        // WHEN
        subscription.setMaxGarments(maxGarments);

        // THEN
        assertEquals(maxGarments, subscription.getMaxGarments());
    }

    // ========== BUSINESS LOGIC TESTS ==========

    @Test
    void givenFreePlanWhenConfigureThenHasBasicFeatures() {
        // GIVEN
        SubscriptionModel subscription = new SubscriptionModel();

        // WHEN
        subscription.setName("Free Plan");
        subscription.setPlanCode("PLAN-FREE");
        subscription.setPrice(0.0);
        subscription.setMaxGarments(10);
        subscription.setHasAnalytics(false);
        subscription.setHasAdvancedReports(false);
        subscription.setPopular(false);

        // THEN
        assertEquals("Free Plan", subscription.getName());
        assertEquals(0.0, subscription.getPrice());
        assertEquals(10, subscription.getMaxGarments());
        assertFalse(subscription.isHasAnalytics());
        assertFalse(subscription.isHasAdvancedReports());
        assertFalse(subscription.isPopular());
    }

    @Test
    void givenPremiumPlanWhenConfigureThenHasAllFeatures() {
        // GIVEN
        SubscriptionModel subscription = new SubscriptionModel();

        // WHEN
        subscription.setName("Premium Plan");
        subscription.setPlanCode("PLAN-PREMIUM");
        subscription.setPrice(99.99);
        subscription.setMaxGarments(null); // unlimited
        subscription.setHasAnalytics(true);
        subscription.setHasAdvancedReports(true);
        subscription.setPopular(true);

        // THEN
        assertEquals("Premium Plan", subscription.getName());
        assertEquals(99.99, subscription.getPrice());
        assertNull(subscription.getMaxGarments()); // unlimited
        assertTrue(subscription.isHasAnalytics());
        assertTrue(subscription.isHasAdvancedReports());
        assertTrue(subscription.isPopular());
    }

    @Test
    void givenBasicPlanWhenConfigureThenHasMidTierFeatures() {
        // GIVEN
        SubscriptionModel subscription = new SubscriptionModel();

        // WHEN
        subscription.setName("Basic Plan");
        subscription.setPlanCode("PLAN-BASIC");
        subscription.setPrice(29.99);
        subscription.setMaxGarments(50);
        subscription.setHasAnalytics(true);
        subscription.setHasAdvancedReports(false);
        subscription.setPopular(true);

        // THEN
        assertEquals("Basic Plan", subscription.getName());
        assertEquals(29.99, subscription.getPrice());
        assertEquals(50, subscription.getMaxGarments());
        assertTrue(subscription.isHasAnalytics());
        assertFalse(subscription.isHasAdvancedReports());
        assertTrue(subscription.isPopular());
    }
}
