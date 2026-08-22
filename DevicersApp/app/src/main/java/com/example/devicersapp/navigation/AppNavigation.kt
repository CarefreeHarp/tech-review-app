package com.example.devicersapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.devicersapp.ui.screens.access.AccessScreen
import com.example.devicersapp.ui.screens.activity.ActivityScreen
import com.example.devicersapp.ui.screens.create_review.CreateReviewScreen
import com.example.devicersapp.ui.screens.found_products.FoundProductsScreen
import com.example.devicersapp.ui.screens.home.HomeScreen
import com.example.devicersapp.ui.screens.own_profile.OwnProfileScreen
import com.example.devicersapp.ui.screens.product.ProductScreen
import com.example.devicersapp.ui.screens.profile.ProfileScreen
import com.example.devicersapp.ui.screens.profile_saved_reviews.ProfileSavedReviewsScreen
import com.example.devicersapp.ui.screens.profile_search_results.ProfileSearchResultsScreen
import com.example.devicersapp.ui.screens.rate_product.RateProductScreen
import com.example.devicersapp.ui.screens.register.RegisterScreen
import com.example.devicersapp.ui.screens.request_product.RequestProductScreen
import com.example.devicersapp.ui.screens.review.ReviewScreen
import com.example.devicersapp.ui.screens.search_product.SearchProductScreen
import com.example.devicersapp.ui.screens.search_profile.SearchProfileScreen

/** Representa de forma segura las rutas que componen la navegación principal de la aplicación. */
sealed class AppDestination(val route: String) {
    data object Login : AppDestination("login")
    data object Home : AppDestination("home")
    data object SearchProduct : AppDestination("search")
    data object CreateReview : AppDestination("create")
    data object Activity : AppDestination("activity")
    data object OwnProfile : AppDestination("profile")
    data object Register : AppDestination("register")
    data object SearchProfile : AppDestination("search-profile")
    data object FoundProducts : AppDestination("found-products")
    data object ProfileSearchResults : AppDestination("profile-search-results")
    data object Product : AppDestination("product")
    data object RateProduct : AppDestination("rate-product")
    data object Review : AppDestination("review")
    data object RequestProduct : AppDestination("request-product")
    data object Profile : AppDestination("user-profile")
    data object ProfileSavedReviews : AppDestination("profile-saved-reviews")
}

/**
 * Define el grafo de navegación principal de Devicers.
 *
 * @param startDestination Ruta inicial del `NavHost`; por defecto abre la pantalla de inicio de sesión.
 * @param navController Controlador que conserva el historial de destinos de la aplicación.
 * @param modifier Modificador aplicado al contenedor del grafo.
 */
@Composable
fun AppNavigation(
    startDestination: String = AppDestination.Login.route,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = AppDestination.Login.route) {
            AccessScreen(
                onSignInClick = {
                    navController.navigate(AppDestination.Home.route)
                }
            )
        }
        composable(route = AppDestination.Home.route) {
            HomeScreen()
        }
        composable(route = AppDestination.SearchProduct.route) {
            SearchProductScreen()
        }
        composable(route = AppDestination.CreateReview.route) {
            CreateReviewScreen()
        }
        composable(route = AppDestination.Activity.route) {
            ActivityScreen()
        }
        composable(route = AppDestination.OwnProfile.route) {
            OwnProfileScreen()
        }
        composable(route = AppDestination.Register.route) {
            RegisterScreen()
        }
        composable(route = AppDestination.SearchProfile.route) {
            SearchProfileScreen()
        }
        composable(route = AppDestination.FoundProducts.route) {
            FoundProductsScreen()
        }
        composable(route = AppDestination.ProfileSearchResults.route) {
            ProfileSearchResultsScreen()
        }
        composable(route = AppDestination.Product.route) {
            ProductScreen()
        }
        composable(route = AppDestination.RateProduct.route) {
            RateProductScreen()
        }
        composable(route = AppDestination.Review.route) {
            ReviewScreen()
        }
        composable(route = AppDestination.RequestProduct.route) {
            RequestProductScreen()
        }
        composable(route = AppDestination.Profile.route) {
            ProfileScreen()
        }
        composable(route = AppDestination.ProfileSavedReviews.route) {
            ProfileSavedReviewsScreen()
        }
    }
}
