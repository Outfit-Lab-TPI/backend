-- ============================================
-- SEED DATA: Subscription Plans
-- ============================================
-- Inserta los 3 planes de suscripción: FREE, PREMIUM_MONTHLY, PREMIUM_YEARLY
-- FREE: 0 ARS, 2 favoritos, no expira (0 días)
-- PREMIUM_MONTHLY: 299 ARS, 20 favoritos, 30 días
-- PREMIUM_YEARLY: 2999 ARS, 20 favoritos, 365 días
-- ============================================

INSERT INTO subscription_plans (
    plan_code, 
    name, 
    description, 
    price, 
    max_favorites, 
    duration_days, 
    is_active
) VALUES
    (
        'FREE',
        'Plan Gratuito',
        'Acceso básico con hasta 2 combinaciones favoritas',
        0.00,
        2,
        0,
        true
    ),
    (
        'PREMIUM_MONTHLY',
        'Premium Mensual',
        'Acceso premium con hasta 20 combinaciones favoritas durante 30 días',
        299.00,
        20,
        30,
        true
    ),
    (
        'PREMIUM_YEARLY',
        'Premium Anual',
        'Acceso premium con hasta 20 combinaciones favoritas durante 1 año',
        2999.00,
        20,
        365,
        true
    )
ON CONFLICT (plan_code) DO NOTHING;
