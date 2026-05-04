package com.example.landerssuperstore.data.repository

import com.example.landerssuperstore.data.model.Category
import com.example.landerssuperstore.data.model.Product

object ProductRepository {

    private val allProducts = listOf(
        Product(1, "Seoju Melon Bar 8 x 75mL", 249.0, 499.0, "Refreshing melon ice cream bars.", "Ice Cream", R.drawable.seoju_melon_bar_8_x_75ml, "51% OFF"),
        Product(2, "Hershey's Crunch Choco Monaka", 199.0, 399.0, "Crunchy chocolate ice cream treat.", "Ice Cream", R.drawable.hersheys_crunch_choco_monaka, "51% OFF"),
        Product(3, "Meiji Ess Cup Mini", 179.0, 299.0, "Assorted mini ice cream cups.", "Ice Cream", R.drawable.meiji_ess_cup_mini, "41% OFF"),
        Product(4, "Selecta Double Dutch 1.5L", 299.0, 350.0, "Classic Filipino ice cream flavor.", "Ice Cream", R.drawable.selecta_double_dutch_1_5l, null),
        Product(5, "Nestle Vanilla Cone 6pcs", 159.0, 210.0, "Creamy vanilla ice cream cones.", "Ice Cream", R.drawable.nestle_vanilla_cone_6pcs, null),
        Product(6, "Magnum Almond White", 120.0, 150.0, "White chocolate almond ice cream bar.", "Ice Cream", R.drawable.magnum_almond_white, null),
        Product(7, "Kirkland Almond Butter 765g", 549.0, 699.0, "Natural almond butter spread.", "Food Cupboard", R.drawable.kirkland_almond_butter_765g, null),
        Product(8, "Heinz Tomato Ketchup 1.25kg", 249.0, 299.0, "Classic tomato ketchup.", "Food Cupboard", R.drawable.heinz_tomato_ketchup_1_25kg, null),
        Product(9, "Nissin Cup Noodles Seafood 5s", 145.0, 175.0, "Instant seafood noodles pack.", "Food Cupboard", R.drawable.nissin_cup_noodles_seafood_5s, null),
        Product(10, "Quaker Oats Old Fashioned 1.2kg", 299.0, 350.0, "Whole grain rolled oats.", "Food Cupboard", R.drawable.quaker_oats_old_fashioned_1_2kg, null),
        Product(11, "Del Monte Pineapple Juice 1.36L", 129.0, 155.0, "100% pineapple juice.", "Beverages", R.drawable.del_monte_pineapple_juice_1_36l, null),
        Product(12, "Coca-Cola Zero 1.5L", 65.0, 75.0, "Zero sugar cola.", "Beverages", R.drawable.coca_cola_zero_1_5l, null),
        Product(13, "Moet & Chandon Brut Imperial", 2899.0, 3200.0, "French champagne.", "Beer, Wine & Spirits", R.drawable.moet_chandon_brut_imperial, null),
        Product(14, "Johnnie Walker Black Label 1L", 1899.0, 2200.0, "Premium blended Scotch whisky.", "Beer, Wine & Spirits", R.drawable.johnnie_walker_black_label_1l, null),
        Product(15, "Corona Extra 355mL x 6", 459.0, 550.0, "Mexican pale lager beer.", "Beer, Wine & Spirits", R.drawable.corona_extra_355ml_x_6, null),
        Product(16, "Kirkland Bath Tissue 30 rolls", 699.0, 850.0, "Premium 2-ply bath tissue.", "Home & Outdoor", R.drawable.kirkland_bath_tissue_30_rolls, null),
        Product(17, "Dyson V12 Detect Slim", 28999.0, 32000.0, "Lightweight cordless vacuum.", "Home & Outdoor", R.drawable.dyson_v12_detect_slim, null),
        Product(18, "Lysol Disinfectant Spray", 299.0, 350.0, "Kills 99.9% of viruses and bacteria.", "Home & Outdoor", R.drawable.lysol_disinfectant_spray, null),
        Product(19, "L'Oreal Paris Revitalift Serum", 899.0, 1100.0, "Anti-aging face serum.", "Health & Beauty", R.drawable.loreal_paris_revitalift_serum, null),
        Product(20, "Cetaphil Gentle Skin Cleanser", 499.0, 650.0, "Mild cleanser for sensitive skin.", "Health & Beauty", R.drawable.ic_launcher_foreground, null),
        Product(21, "Centrum Advance 100s", 799.0, 950.0, "Complete multivitamin supplement.", "Health & Beauty", R.drawable.centrum_advance_100s, null),
        Product(22, "Apple iPad Air 11-inch M2", 34990.0, 36990.0, "Latest iPad Air with M2 chip.", "Electronics", R.drawable.ic_launcher_foreground, null),
        Product(23, "Sony WH-1000XM5 Headphones", 18999.0, 22000.0, "Industry-leading noise canceling.", "Electronics", R.drawable.ic_launcher_foreground, null),
        Product(24, "Samsung 55\" Crystal UHD 4K TV", 28999.0, 32999.0, "Smart 4K UHD television.", "Electronics", R.drawable.samsung_55_crystal_uhd_4k_tv, null)
    )

    private val categories = listOf(
        Category(1, "NEW! Marketplace", "🏪"),
        Category(2, "NEW! Selection by Landers", "🌿"),
        Category(3, "What's New?", "🆕"),
        Category(4, "International Selection", "🌍"),
        Category(5, "Health & Beauty", "💄"),
        Category(6, "Food Cupboard", "🥫"),
        Category(7, "Home & Outdoor", "🏡"),
        Category(8, "Beer, Wine & Spirits", "🍺"),
        Category(9, "Ice Cream", "🍦"),
        Category(10, "Beverages", "🥤"),
        Category(11, "Electronics", "📱")
    )

    fun getAllProducts(): List<Product> = allProducts

    fun getProductsByCategory(categoryName: String): List<Product> {
        return if (categoryName == "NEW! Marketplace" || categoryName == "NEW! Selection by Landers" || categoryName == "What's New?" || categoryName == "International Selection") {
            allProducts.shuffled().take(8)
        } else {
            allProducts.filter { it.category == categoryName }
        }
    }

    fun getCategories(): List<Category> = categories

    fun searchProducts(query: String): List<Product> {
        val q = query.lowercase()
        return allProducts.filter {
            it.name.lowercase().contains(q) || it.category.lowercase().contains(q)
        }
    }

    fun getFeaturedProducts(): List<Product> = allProducts.take(6)

    fun getDeals(): List<Product> = allProducts.filter { it.discount != null }.take(6)
}
