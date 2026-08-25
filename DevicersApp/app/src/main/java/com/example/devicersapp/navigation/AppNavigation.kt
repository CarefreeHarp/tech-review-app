package com.example.devicersapp.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.devicersapp.R



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
    data object Product : AppDestination("product") {
        fun createRoute(productNameResId: Int) = "product/$productNameResId"
    }
//    data object RateProduct : AppDestination("rate-product") {
//        fun createRoute(productId: String) = "rate-product/$productId"
//    }
    data object RateProduct : AppDestination("rate-product") {
        fun createRoute(productNameResId: Int) = "rate-product/$productNameResId"
    }
    data object Review : AppDestination("review") {
        fun createRoute(reviewId: Int) = "review/$reviewId"

    }
    data object RequestProduct : AppDestination("request-product")
    data object Profile : AppDestination("user-profile") {
        fun createRoute(profileId: String) = "user-profile/$profileId"
    }
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
        // ==================== RUTAS SIN PARÁMETROS ====================
        composable(route = AppDestination.Login.route) {
            AccessScreen(
                onSignInClick = {
                    navController.navigate(AppDestination.Home.route) {
                        popUpTo(AppDestination.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onCreateAccountClick = {
                    navController.navigate(AppDestination.Register.route)
                }
            )
        }
        composable(route = AppDestination.Home.route) {
            HomeScreen(
                onReviewClick = { reviewId ->
                    navController.navigate(
                        AppDestination.Review.createRoute(reviewId)
                    )
                },
                onCommentClick = { reviewId ->
                    navController.navigate(
                        AppDestination.Review.createRoute(reviewId)
                    )
                },
                onSendClick = {
                    navController.navigate(AppDestination.SearchProfile.route)
                }
            )
        }
        composable(route = AppDestination.SearchProduct.route) {
            SearchProductScreen(
                onApplyFilters = {
                    navController.navigate(AppDestination.FoundProducts.route)
                },
                onUsersClick = {
                    navController.navigate(AppDestination.SearchProfile.route)
                }
            )
        }
        composable(route = AppDestination.CreateReview.route) {
            CreateReviewScreen(
                onProductClick = { product ->
                    navController.navigate(
                        AppDestination.RateProduct.createRoute(product.nameResId)
                    )
                },
                onRequestProductClick = {
                    navController.navigate(
                        AppDestination.RequestProduct.route
                    )
                }
            )
        }
        composable(route = AppDestination.Activity.route) {
            ActivityScreen(
                onReviewClick = { reviewId ->
                    navController.navigate(AppDestination.Review.createRoute(reviewId))
                },
                onProfileClick = { profileId ->
                    navController.navigate(AppDestination.Profile.createRoute(profileId))
                }
            )
        }
        composable(route = AppDestination.OwnProfile.route) {
            OwnProfileScreen(
                onReviewClick = { reviewId ->
                    navController.navigate(
                        AppDestination.Review.createRoute(reviewId)
                    )
                },
                onSavedReviewsClick = {
                    navController.navigate(
                        AppDestination.ProfileSavedReviews.route
                    )
                }
            )
        }
        composable(route = AppDestination.Register.route) {
            RegisterScreen(
                onCreateAccountClick = {
                    navController.navigate(AppDestination.Home.route) {
                        popUpTo(AppDestination.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onSignInClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = AppDestination.SearchProfile.route) {
            SearchProfileScreen(
                onProductsClick = {
                    navController.navigate(AppDestination.SearchProduct.route)
                },
                onApplyFilters = {
                    navController.navigate(AppDestination.ProfileSearchResults.route)
                }
            )
        }
        composable(route = AppDestination.FoundProducts.route) {
            FoundProductsScreen(
                onProductClick = { product ->
                    navController.navigate(
                        AppDestination.Product.createRoute(product.nameResId)
                    )
                }
            )
        }
        composable(route = AppDestination.ProfileSearchResults.route) {
            ProfileSearchResultsScreen(
                onProfileClick = { profileId ->
                    navController.navigate(
                        AppDestination.Profile.createRoute(profileId)
                    )
                }
            )
        }

        composable(route = AppDestination.ProfileSavedReviews.route) {
            ProfileSavedReviewsScreen(
                onReviewClick = { reviewId ->
                    navController.navigate(
                        AppDestination.Review.createRoute(reviewId)
                    )
                },
                onReviewsClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = AppDestination.RequestProduct.route) {
            RequestProductScreen()
        }

        // ==================== RUTAS CON PARÁMETROS ====================
        composable(
            route = "${AppDestination.Product.route}/{productNameResId}",
            arguments = listOf(
                navArgument("productNameResId") {
                    type = NavType.IntType
                }
            )
        ) {

            val productNameResId =
                it.arguments?.getInt("productNameResId")

            if (productNameResId != null) {
                ProductScreen(
                    productNameResId = productNameResId,
                    onRateClick = { nameResId ->
                        navController.navigate(
                            AppDestination.RateProduct.createRoute(nameResId)
                        )
                    },
                    onViewMoreClick = { reviewId ->
                        navController.navigate(
                            AppDestination.Review.createRoute(reviewId)
                        )
                    }
                )
            } else {
                Text(text = stringResource(R.string.product_not_found))
            }
        }

        composable(
            route = "${AppDestination.RateProduct.route}/{productNameResId}",
            arguments = listOf(
                navArgument("productNameResId") {
                    type = NavType.IntType
                }
            )
        ) {

            val productNameResId =
                it.arguments?.getInt("productNameResId")

            if (productNameResId != null) {
                RateProductScreen(
                    productNameResId = productNameResId,
                    onPublishClick = {
                        navController.navigateToDestination(
                            AppDestination.Home.route
                        )
                    }
                )
            } else {
                Text(text = stringResource(R.string.product_not_found))
            }
        }
        composable(
            route = "${AppDestination.Review.route}/{reviewId}",
            arguments = listOf(
                navArgument("reviewId") {
                    type = NavType.IntType
                }
            )
        ) {
            val reviewId = it.arguments?.getInt("reviewId")

            if (reviewId != null) {
                ReviewScreen(
                    reviewId = reviewId,
                    onProductClick = { productNameResId ->
                        navController.navigate(
                            AppDestination.Product.createRoute(productNameResId)
                        )
                    }
                )
            } else {
                Text(text = stringResource(R.string.review_not_found))
            }
        }
        composable(
            route = "${AppDestination.Profile.route}/{profileId}",
            arguments = listOf(
                navArgument("profileId") {
                    type = NavType.StringType
                }
            )
        ) {

            val profileId = it.arguments?.getString("profileId")

            if (profileId != null) {
                ProfileScreen(
                    profileId = profileId,
                    onReviewClick = { reviewId ->
                        navController.navigate(
                            AppDestination.Review.createRoute(reviewId)
                        )
                    }
                )
            } else {
                Text(text = stringResource(R.string.profile_not_found))
            }
        }
    }
}
