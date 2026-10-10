package com.example.devicersapp.navigation

import androidx.compose.runtime.CompositionLocalProvider
import com.example.devicersapp.ui.session.LocalSessionState

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
import com.example.devicersapp.ui.screens.edit_review.EditReviewView
import com.example.devicersapp.ui.screens.edit_review.EditReviewViewModel
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
import com.example.devicersapp.ui.session.SessionViewModel
import com.example.devicersapp.ui.screens.splash.SplashView
import com.example.devicersapp.ui.screens.splash.SplashViewModel
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

import android.util.Log
/** Representa de forma segura las rutas que componen la navegación principal de la aplicación. */
sealed class AppDestination(val route: String) {
    data object Splash : AppDestination("splash")
    data object Login : AppDestination("login")
    data object Home : AppDestination("home")
    data object SearchProduct : AppDestination("search")
    data object CreateReview : AppDestination("create")
    data object Activity : AppDestination("activity")
    data object OwnProfile : AppDestination("profile") {
        val routeWithDeletionArgument = "$route?deleteReviewId={deleteReviewId}"
        fun createDeletionRoute(reviewId: Int) = "$route?deleteReviewId=$reviewId"
    }
    data object Register : AppDestination("register")
    data object SearchProfile : AppDestination("search-profile")
    data object FoundProducts : AppDestination("found-products") {

        fun createRoute(
            productName: String,
            category: String,
            minimumRating: Float,
            sortBy: String
        ): String {
            return "found-products" +
                    "?productName=$productName" +
                    "&category=$category" +
                    "&minimumRating=$minimumRating" +
                    "&sortBy=$sortBy"
        }
    }
    data object ProfileSearchResults : AppDestination("profile-search-results")
    data object Product : AppDestination("product") {
        fun createRoute(productId: Int): String {
            return "product/$productId"
        }
    }
//    data object RateProduct : AppDestination("rate-product") {
//        fun createRoute(productId: String) = "rate-product/$productId"
//    }
    data object RateProduct : AppDestination("rateProduct") {

        fun createRoute(productId: Int): String {
            return "rateProduct/$productId"
        }
    }
    data object EditReview : AppDestination("edit_review") {
        fun createRoute(reviewId: Int) = "$route/$reviewId"
    }
    data object Review : AppDestination("review") {
        fun createRoute(reviewId: Int) = "review/$reviewId"
        fun createLocalRoute(reviewId: Int) = "review/local/$reviewId"

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
 * @param startDestination Ruta inicial del `NavHost`; por defecto comprueba la sesión activa.
 * @param navController Controlador que conserva el historial de destinos de la aplicación.
 * @param modifier Modificador aplicado al contenedor del grafo.
 */
@Composable
fun AppNavigation(
    startDestination: String = AppDestination.Splash.route,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val sessionState by sessionViewModel.uiState.collectAsState()

    val backStackEntry by navController.currentBackStackEntryAsState()

    // La barra pública observa el mismo ViewModel, vinculado a la entrada del perfil.
    val publicProfileHandle = backStackEntry
        ?.takeIf {
            it.destination.route == "${AppDestination.Profile.route}/{profileId}" &&
                !sessionState.isCurrentUser(it.arguments?.getString("profileId")?.toIntOrNull())
        }
        ?.let { entry ->
            val profileViewModel: ProfileViewModel = hiltViewModel(entry)
            val profileState by profileViewModel.uiState.collectAsState()
            profileState.profile?.username
        }

    val configuration = NavigationLogic.configurationFor(
        route = backStackEntry?.destination?.route,
        profileId = backStackEntry
            ?.arguments
            ?.getString("profileId")
    )

    val onProfileClick: (String) -> Unit = { profileId ->
        val ownProfile = sessionState.isCurrentUser(profileId.toIntOrNull())
        val currentEntry = navController.currentBackStackEntry
        val alreadyVisible = if (ownProfile) {
            currentEntry?.destination?.route == AppDestination.OwnProfile.routeWithDeletionArgument
        } else {
            currentEntry?.destination?.route == "${AppDestination.Profile.route}/{profileId}" &&
                currentEntry.arguments?.getString("profileId") == profileId
        }
        if (!alreadyVisible) {
            val route = if (ownProfile) AppDestination.OwnProfile.route
                else AppDestination.Profile.createRoute(profileId)
            navController.navigate(route) { launchSingleTop = true }
        }
    }

    CompositionLocalProvider(LocalSessionState provides sessionState) {
        DevicersScaffold(
            onProfileClick = onProfileClick,
            selectedItem = configuration.selectedItem,
            showBottomBar = configuration.showBottomBar,
            showDrawer = configuration.showDrawer,
            topBarNumber = configuration.topBarNumber,
            topBarUserHandleResId = configuration.topBarUserHandleResId,
            topBarUserHandle = publicProfileHandle ?: sessionState.currentProfileHandle.takeIf {
                backStackEntry?.destination?.route == AppDestination.OwnProfile.routeWithDeletionArgument ||
                    backStackEntry?.destination?.route == AppDestination.ProfileSavedReviews.route
            },
            topBarProfileImageUrl = sessionState.profileImageUrl,
            topBarUserId = when (backStackEntry?.destination?.route) {
                "${AppDestination.Profile.route}/{profileId}" -> backStackEntry?.arguments?.getString("profileId")
                AppDestination.OwnProfile.routeWithDeletionArgument,
                AppDestination.ProfileSavedReviews.route -> sessionState.userId?.toString()
                else -> null
            },
            modifier = modifier,
            onNavigationItemClick = { route ->
                navController.navigateToDestination(route)
            },
            onTopBarBackClick = {
                navController.popBackStack()
            },
            onSignOutClick = {
                sessionViewModel.signOut()
                navController.navigate(AppDestination.Login.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
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
                composable(route = AppDestination.Splash.route) {
                    val splashViewModel: SplashViewModel = hiltViewModel()

                    SplashView(
                        onUserAuthenticated = {
                            navController.navigate(AppDestination.Home.route) {
                                popUpTo(AppDestination.Splash.route) { inclusive = true }
                            }
                        },
                        onUserUnauthenticated = {
                            navController.navigate(AppDestination.Login.route) {
                                popUpTo(AppDestination.Splash.route) { inclusive = true }
                            }
                        },
                        viewModel = splashViewModel
                    )
                }
                composable(route = AppDestination.Login.route) {
                    val accessViewModel: AccessViewModel = hiltViewModel()

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
                    val homeViewModel: HomeViewModel = hiltViewModel()

                    HomeView(
                        onProfileClick = onProfileClick,
                        viewModel = homeViewModel,
                        onProductClick = { productId ->
                            navController.navigate(
                                AppDestination.Product.createRoute(productId)
                            )
                        },
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
                    val searchProductViewModel: SearchProductViewModel = hiltViewModel()

                    SearchProductView(
                        viewModel = searchProductViewModel,

                        onApplyFilters = { filters ->

                            navController.navigate(
                                AppDestination.FoundProducts.createRoute(
                                    productName = filters.productName,
                                    category = filters.selectedCategory,
                                    minimumRating = filters.minimumRating,
                                    sortBy = filters.sortBy
                                )
                            )
                        },

                        onUsersClick = {
                            navController.navigate(
                                AppDestination.SearchProfile.route
                            )
                        }
                    )
                }
                composable(route = AppDestination.CreateReview.route) {
                    val createReviewViewModel: CreateReviewViewModel = hiltViewModel()

                    CreateReviewView(
                        onProductClick = { product ->
                            navController.navigate(
                                AppDestination.RateProduct.createRoute(
                                    product.id
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
                    val activityViewModel: ActivityViewModel = hiltViewModel()

                    ActivityView(
                        onReviewClick = { reviewId ->
                            navController.navigate(AppDestination.Review.createLocalRoute(reviewId))
                        },
                        onProfileClick = onProfileClick,
                        viewModel = activityViewModel
                    )
                }
                composable(
                    route = AppDestination.OwnProfile.routeWithDeletionArgument,
                    arguments = listOf(navArgument("deleteReviewId") { type = NavType.IntType; defaultValue = -1 })
                ) {
                    val ownProfileViewModel: OwnProfileViewModel = hiltViewModel()

                    OwnProfileView(
                        onProfileClick = onProfileClick,
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
                    val registerViewModel: RegisterViewModel = hiltViewModel()

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
                    val searchProfileViewModel: SearchProfileViewModel = hiltViewModel()

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
                composable(
                    route = "${AppDestination.FoundProducts.route}" +
                            "?productName={productName}" +
                            "&category={category}" +
                            "&minimumRating={minimumRating}" +
                            "&sortBy={sortBy}",

                    arguments = listOf(

                        navArgument("productName") {
                            type = NavType.StringType
                            defaultValue = ""
                        },

                        navArgument("category") {
                            type = NavType.StringType
                            defaultValue = "all"
                        },

                        navArgument("minimumRating") {
                            type = NavType.FloatType
                            defaultValue = 0f
                        },

                        navArgument("sortBy") {
                            type = NavType.StringType
                            defaultValue = "recent"
                        }
                    )
                ) { backStackEntry ->

                    val productName =
                        backStackEntry.arguments?.getString("productName") ?: ""

                    val category =
                        backStackEntry.arguments?.getString("category") ?: "all"

                    val minimumRating =
                        backStackEntry.arguments?.getFloat("minimumRating") ?: 0f

                    val sortBy =
                        backStackEntry.arguments?.getString("sortBy") ?: "recent"

                    val foundProductsViewModel: FoundProductsViewModel =
                        hiltViewModel()

                    FoundProductsView(
                        viewModel = foundProductsViewModel,
                        productName = productName,
                        category = category,
                        minimumRating = minimumRating,
                        sortBy = sortBy,

                        onProductClick = { product ->

                            Log.d(
                                "FoundProducts",
                                "Producto seleccionado -> id=${product.id}, name=${product.name}"
                            )

                            navController.navigate(
                                AppDestination.Product.createRoute(
                                    product.id
                                )
                            )
                        }
                    )
                }
                composable(route = AppDestination.ProfileSearchResults.route) {
                    val profileSearchResultsViewModel: ProfileSearchResultsViewModel = hiltViewModel()

                    ProfileSearchResultsView(
                        viewModel = profileSearchResultsViewModel,
                        onProfileClick = onProfileClick
                    )
                }

                composable(route = AppDestination.ProfileSavedReviews.route) {
                    val profileSavedReviewsViewModel: ProfileSavedReviewsViewModel = hiltViewModel()

                    ProfileSavedReviewsView(
                        onProfileClick = onProfileClick,
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
                    val requestProductViewModel: RequestProductViewModel = hiltViewModel()

                    RequestProductView(
                        viewModel = requestProductViewModel
                    )
                }

                // ==================== RUTAS CON PARÁMETROS ====================
                composable(route = "${AppDestination.Product.route}/{productId}", arguments = listOf(
                        navArgument("productId") {
                            type = NavType.IntType
                        }
                    )
                ) { backStackEntry ->

                    val productId = backStackEntry.arguments?.getInt("productId")

                    if (productId != null) {
                        val productViewModel: ProductViewModel = hiltViewModel()
                        ProductView(
                            onProfileClick = onProfileClick,
                            viewModel = productViewModel,
                            productId = productId,
                            onRateClick = { id ->
                                navController.navigate(AppDestination.RateProduct.createRoute(id))
                            },
                            onViewMoreClick = { reviewId -> navController.navigate(
                                    AppDestination.Review.createRoute(reviewId)
                            )
                            }
                        )
                    } else {
                        Text(text = stringResource(R.string.product_not_found))
                    }
                }
                composable(
                    route = "${AppDestination.RateProduct.route}/{productId}",
                    arguments = listOf(
                        navArgument("productId") {
                            type = NavType.IntType
                        }
                    )
                ) { backStackEntry ->

                    val productId =
                        backStackEntry.arguments?.getInt("productId")

                    if (productId != null) {
                        val rateProductViewModel: RateProductViewModel = hiltViewModel()

                        RateProductView(
                            productId = productId,
                            viewModel = rateProductViewModel,
                            onChooseProduct = { navController.navigate(AppDestination.CreateReview.route) },
                            onPublishClick = {
                                navController.navigate(AppDestination.OwnProfile.route) {
                                    popUpTo("${AppDestination.RateProduct.route}/{productId}") { inclusive = true }
                                }
                            }
                        )
                    }
                }
                composable(
                    route = "${AppDestination.EditReview.route}/{reviewId}",
                    arguments = listOf(navArgument("reviewId") { type = NavType.IntType })
                ) { entry ->
                    val reviewId = requireNotNull(entry.arguments).getInt("reviewId")
                    val editReviewViewModel: EditReviewViewModel = hiltViewModel()
                    EditReviewView(
                        reviewId = reviewId,
                        viewModel = editReviewViewModel,
                        onSaved = { navController.popBackStack() }
                    )
                }
                composable(
                    route = "${AppDestination.Review.route}/local/{reviewId}",
                    arguments = listOf(navArgument("reviewId") { type = NavType.IntType })
                ) {
                    val reviewId = it.arguments?.getInt("reviewId")
                    if (reviewId != null) {
                        val reviewViewModel: ReviewViewModel = hiltViewModel()
                        ReviewView(
                            onAuthorClick = onProfileClick,
                            reviewId = reviewId,
                            localReview = true,
                            viewModel = reviewViewModel
                        )
                    } else {
                        Text(text = stringResource(R.string.review_not_found))
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
                        val reviewViewModel: ReviewViewModel = hiltViewModel()
                        ReviewView(
                            reviewId = reviewId,
                            viewModel = reviewViewModel,
                            onEditClick = { id -> navController.navigate(AppDestination.EditReview.createRoute(id)) },
                            onDeleteRequested = { id ->
                                navController.navigate(AppDestination.OwnProfile.createDeletionRoute(id)) {
                                    popUpTo("${AppDestination.Review.route}/{reviewId}") { inclusive = true }
                                }
                            },
                            onAuthorClick = onProfileClick,
                            onProductClick = { id -> navController.navigate(AppDestination.Product.createRoute(id)) }
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

                    if (sessionState.isCurrentUser(profileId?.toIntOrNull())) {
                        // Sustituye el perfil público propio para no volver a él al pulsar Atrás.
                        LaunchedEffect(profileId, sessionState.userId) {
                            navController.navigate(AppDestination.OwnProfile.route) {
                                popUpTo("${AppDestination.Profile.route}/{profileId}") {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    } else if (profileId != null) {
                        val profileViewModel: ProfileViewModel = hiltViewModel()

                        ProfileView(
                            onProfileClick = onProfileClick,
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
}
