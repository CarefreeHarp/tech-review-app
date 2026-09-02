package com.example.devicersapp.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.devicersapp.R
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

    val configuration = NavigationLogic.configurationFor(
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
                val accessViewModel: AccessViewModel = viewModel()

                AccessView(
                    onSignInClick = {
                        navController.navigate(AppDestination.Home.route) {
                            popUpTo(AppDestination.Login.route) { inclusive = true }
                        }
                    },
                    onCreateAccountClick = {
                        navController.navigate(AppDestination.Register.route)
                    },
                    viewModel = accessViewModel
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
                val searchProductViewModel: SearchProductViewModel = viewModel()

                SearchProductView(
                    viewModel = searchProductViewModel,
                    onApplyFilters = {
                        navController.navigate(AppDestination.FoundProducts.route)
                    },
                    onUsersClick = {
                        navController.navigate(AppDestination.SearchProfile.route)
                    }
                )
            }
            composable(route = AppDestination.CreateReview.route) {
                val createReviewViewModel: CreateReviewViewModel = viewModel()

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
                    viewModel = createReviewViewModel
                )
            }
            composable(route = AppDestination.Activity.route) {
                val activityViewModel: ActivityViewModel = viewModel()

                ActivityView(
                    onReviewClick = { reviewId ->
                        navController.navigate(AppDestination.Review.createRoute(reviewId))
                    },
                    onProfileClick = { profileId ->
                        navController.navigate(AppDestination.Profile.createRoute(profileId))
                    },
                    viewModel = activityViewModel
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
                val searchProfileViewModel: SearchProfileViewModel = viewModel()

                SearchProfileView(
                    viewModel = searchProfileViewModel,
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
                val requestProductViewModel: RequestProductViewModel = viewModel()

                RequestProductView(
                    viewModel = requestProductViewModel
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
                    val rateProductViewModel: RateProductViewModel = viewModel()

                    RateProductView(
                        viewModel = rateProductViewModel,
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
                    val reviewViewModel: ReviewViewModel = viewModel()

                    ReviewView(
                        reviewId = reviewId,
                        onProductClick = { productNameResId ->
                            navController.navigate(
                                AppDestination.Product.createRoute(
                                    productNameResId
                                )
                            )
                        },
                        viewModel = reviewViewModel
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
