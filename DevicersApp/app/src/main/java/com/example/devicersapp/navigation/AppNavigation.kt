package com.example.devicersapp.navigation

import androidx.annotation.StringRes
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.ui.screens.access.AccessView
import com.example.devicersapp.ui.screens.access.AccessViewModel
import com.example.devicersapp.ui.screens.activity.ActivityView
import com.example.devicersapp.ui.screens.activity.ActivityViewModel
import com.example.devicersapp.ui.screens.create_review.CreateReviewView
import com.example.devicersapp.ui.screens.create_review.CreateReviewViewModel
import com.example.devicersapp.ui.screens.found_products.FoundProductsView
import com.example.devicersapp.ui.screens.found_products.FoundProductsViewModel
import com.example.devicersapp.ui.screens.home.HomeView
import com.example.devicersapp.ui.screens.home.HomeViewModel
import com.example.devicersapp.ui.screens.own_profile.OwnProfileView
import com.example.devicersapp.ui.screens.own_profile.OwnProfileViewModel
import com.example.devicersapp.ui.screens.product.ProductView
import com.example.devicersapp.ui.screens.product.ProductViewModel
import com.example.devicersapp.ui.screens.profile.ProfileView
import com.example.devicersapp.ui.screens.profile.ProfileViewModel
import com.example.devicersapp.ui.screens.profile_saved_reviews.ProfileSavedReviewsView
import com.example.devicersapp.ui.screens.profile_saved_reviews.ProfileSavedReviewsViewModel
import com.example.devicersapp.ui.screens.profile_search_results.ProfileSearchResultsView
import com.example.devicersapp.ui.screens.profile_search_results.ProfileSearchResultsViewModel
import com.example.devicersapp.ui.screens.rate_product.RateProductView
import com.example.devicersapp.ui.screens.rate_product.RateProductViewModel
import com.example.devicersapp.ui.screens.register.RegisterView
import com.example.devicersapp.ui.screens.register.RegisterViewModel
import com.example.devicersapp.ui.screens.request_product.RequestProductView
import com.example.devicersapp.ui.screens.request_product.RequestProductViewModel
import com.example.devicersapp.ui.screens.review.ReviewView
import com.example.devicersapp.ui.screens.review.ReviewViewModel
import com.example.devicersapp.ui.screens.search_product.SearchProductView
import com.example.devicersapp.ui.screens.search_product.SearchProductViewModel
import com.example.devicersapp.ui.screens.search_profile.SearchProfileView
import com.example.devicersapp.ui.screens.search_profile.SearchProfileViewModel
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
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

/** Describe la estructura visual compartida que necesita un destino de navegación. */
private data class NavigationUiConfiguration(
    val selectedItem: String = "",
    val showBottomBar: Boolean = false,
    val topBarNumber: Int? = null,
    @param:StringRes val topBarUserHandleResId: Int? = null
)

/** Obtiene la configuración visual correspondiente al destino actual. */
private fun navigationConfigurationFor(
    route: String?,
    profileId: String? = null
): NavigationUiConfiguration = when (route) {

    AppDestination.Login.route ->
        NavigationUiConfiguration(
            topBarNumber = 5
        )

    AppDestination.Home.route ->
        NavigationUiConfiguration(
            selectedItem = AppDestination.Home.route,
            showBottomBar = true,
            topBarNumber = 5
        )

    AppDestination.SearchProduct.route ->
        NavigationUiConfiguration(
            selectedItem = AppDestination.SearchProduct.route,
            showBottomBar = true
        )

    AppDestination.CreateReview.route ->
        NavigationUiConfiguration(
            selectedItem = AppDestination.CreateReview.route,
            showBottomBar = true,
            topBarNumber = 3
        )

    AppDestination.Activity.route ->
        NavigationUiConfiguration(
            selectedItem = AppDestination.Activity.route,
            showBottomBar = true,
            topBarNumber = 10
        )

    AppDestination.OwnProfile.route ->
        NavigationUiConfiguration(
            selectedItem = AppDestination.OwnProfile.route,
            showBottomBar = true,
            topBarNumber = 1,
            topBarUserHandleResId =
                LocalProfileProvider.profile.handleResId
        )

    AppDestination.Register.route ->
        NavigationUiConfiguration(
            topBarNumber = 5
        )

    AppDestination.SearchProfile.route ->
        NavigationUiConfiguration(
            selectedItem = AppDestination.SearchProduct.route,
            showBottomBar = true
        )

    AppDestination.FoundProducts.route ->
        NavigationUiConfiguration(
            selectedItem = AppDestination.SearchProduct.route,
            showBottomBar = true,
            topBarNumber = 8
        )

    AppDestination.ProfileSearchResults.route ->
        NavigationUiConfiguration(
            selectedItem = AppDestination.SearchProduct.route,
            showBottomBar = true,
            topBarNumber = 7
        )

    "${AppDestination.Product.route}/{productNameResId}" ->
        NavigationUiConfiguration(
            selectedItem = AppDestination.SearchProduct.route,
            showBottomBar = true,
            topBarNumber = 6
        )

    "${AppDestination.RateProduct.route}/{productNameResId}" ->
        NavigationUiConfiguration(
            topBarNumber = 2
        )

    AppDestination.Review.route ->
        NavigationUiConfiguration(
            topBarNumber = 4
        )

    AppDestination.RequestProduct.route ->
        NavigationUiConfiguration(
            selectedItem = AppDestination.CreateReview.route,
            topBarNumber = 9
        )

    AppDestination.Profile.route ->
        NavigationUiConfiguration(
            selectedItem = AppDestination.OwnProfile.route,
            showBottomBar = true,
            topBarNumber = 1,
            topBarUserHandleResId =
                LocalProfileProvider.profile.handleResId
        )

    AppDestination.ProfileSavedReviews.route ->
        NavigationUiConfiguration(
            selectedItem = AppDestination.OwnProfile.route,
            showBottomBar = true,
            topBarNumber = 1,
            topBarUserHandleResId =
                LocalProfileProvider.profile.handleResId
        )

    "${AppDestination.Profile.route}/{profileId}" ->
        NavigationUiConfiguration(
            selectedItem = AppDestination.OwnProfile.route,
            showBottomBar = true,
            topBarNumber = 1,
            topBarUserHandleResId = profileId?.let { id ->
                LocalProfileProvider
                    .getPublicProfileById(id)
                    ?.handleResId
            }
        )

    "${AppDestination.Review.route}/{reviewId}" ->
        NavigationUiConfiguration(
            topBarNumber = 4
        )

    else ->
        NavigationUiConfiguration(
            topBarNumber = 5
        )
}

/**
 * Navega entre destinos principales y conserva el historial,
 * excepto al volver al inicio.
 */
private fun NavHostController.navigateToDestination(
    route: String
) {
    // No modifica la pila si ya estamos en ese destino.
    if (currentDestination?.route == route) {
        return
    }

    navigate(route) {
        if (route == AppDestination.Home.route) {
            popUpTo(0) {
                inclusive = true
            }
        }

        launchSingleTop = true
    }
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
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()

    val configuration = navigationConfigurationFor(
        route = backStackEntry?.destination?.route,
        profileId = backStackEntry
            ?.arguments
            ?.getString("profileId")
    )

    DevicersScaffold(
        selectedItem = configuration.selectedItem,
        showBottomBar = configuration.showBottomBar,
        topBarNumber = configuration.topBarNumber,
        topBarUserHandleResId = configuration.topBarUserHandleResId,
        modifier = modifier,
        onNavigationItemClick = { route ->
            navController.navigateToDestination(route)
        },
        onTopBarBackClick = {
            navController.popBackStack()
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding()
            ),
            enterTransition = {
                slideInHorizontally { it }
            },
            exitTransition = {
                slideOutHorizontally { -it }
            },
            popEnterTransition = {
                slideInHorizontally { -it }
            },
            popExitTransition = {
                slideOutHorizontally { it }
            }
        ) {

            // ==================== RUTAS SIN PARÁMETROS ====================
            composable(route = AppDestination.Login.route) {
                AccessView(
                    onSignInClick = {
                        navController.navigate(AppDestination.Home.route) {
                            popUpTo(AppDestination.Login.route) { inclusive = true }
                        }
                    },
                    onCreateAccountClick = {
                        navController.navigate(AppDestination.Register.route)
                    },
                    viewModel = viewModel<AccessViewModel>()
                )
            }
            composable(route = AppDestination.Home.route) {
                val homeViewModel: HomeViewModel = viewModel()

                HomeView(
                    viewModel = homeViewModel,
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
                        navController.navigate(
                            AppDestination.SearchProfile.route
                        )
                    }
                )
            }
            composable(route = AppDestination.SearchProduct.route) {
                SearchProductView(
                    viewModel = viewModel<SearchProductViewModel>(),
                    onApplyFilters = {
                        navController.navigate(AppDestination.FoundProducts.route)
                    },
                    onUsersClick = {
                        navController.navigate(AppDestination.SearchProfile.route)
                    }
                )
            }
            composable(route = AppDestination.CreateReview.route) {
                CreateReviewView(
                    onProductClick = { product ->
                        navController.navigate(
                            AppDestination.RateProduct.createRoute(
                                product.nameResId
                            )
                        )
                    },
                    onRequestProductClick = {
                        navController.navigate(
                            AppDestination.RequestProduct.route
                        )
                    },
                    viewModel = viewModel<CreateReviewViewModel>()
                )
            }
            composable(route = AppDestination.Activity.route) {
                ActivityView(
                    onReviewClick = { reviewId ->
                        navController.navigate(AppDestination.Review.createRoute(reviewId))
                    },
                    onProfileClick = { profileId ->
                        navController.navigate(AppDestination.Profile.createRoute(profileId))
                    },
                    viewModel = viewModel<ActivityViewModel>()
                )
            }
            composable(route = AppDestination.OwnProfile.route) {
                val ownProfileViewModel: OwnProfileViewModel = viewModel()

                OwnProfileView(
                    viewModel = ownProfileViewModel,
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
                val registerViewModel: RegisterViewModel = viewModel()

                RegisterView(
                    viewModel = registerViewModel,
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
                SearchProfileView(
                    viewModel = viewModel<SearchProfileViewModel>(),
                    onProductsClick = {
                        navController.navigate(AppDestination.SearchProduct.route)
                    },
                    onApplyFilters = {
                        navController.navigate(AppDestination.ProfileSearchResults.route)
                    }
                )
            }
            composable(route = AppDestination.FoundProducts.route) {
                val foundProductsViewModel: FoundProductsViewModel = viewModel()

                FoundProductsView(
                    viewModel = foundProductsViewModel,
                    onProductClick = { product ->
                        navController.navigate(
                            AppDestination.Product.createRoute(product.nameResId)
                        )
                    }
                )
            }
            composable(route = AppDestination.ProfileSearchResults.route) {
                val profileSearchResultsViewModel: ProfileSearchResultsViewModel = viewModel()

                ProfileSearchResultsView(
                    viewModel = profileSearchResultsViewModel,
                    onProfileClick = { profileId ->
                        navController.navigate(
                            AppDestination.Profile.createRoute(profileId)
                        )
                    }
                )
            }

            composable(route = AppDestination.ProfileSavedReviews.route) {
                val profileSavedReviewsViewModel: ProfileSavedReviewsViewModel = viewModel()

                ProfileSavedReviewsView(
                    viewModel = profileSavedReviewsViewModel,
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
                RequestProductView(
                    viewModel = viewModel<RequestProductViewModel>()
                )
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

                    val productViewModel: ProductViewModel = viewModel()

                    ProductView(
                        viewModel = productViewModel,
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
                    RateProductView(
                        viewModel = viewModel<RateProductViewModel>(),
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
                    ReviewView(
                        reviewId = reviewId,
                        onProductClick = { productNameResId ->
                            navController.navigate(
                                AppDestination.Product.createRoute(
                                    productNameResId
                                )
                            )
                        },
                        viewModel = viewModel<ReviewViewModel>()
                    )
                } else {
                    Text(
                        text = stringResource(
                            R.string.review_not_found
                        )
                    )
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
                    val profileViewModel: ProfileViewModel = viewModel()

                    ProfileView(
                        viewModel = profileViewModel,
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
}
