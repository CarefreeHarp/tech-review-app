package com.example.devicersapp.navigation

import com.example.devicersapp.data.utils.Utils
import com.example.devicersapp.ui.utils.profile.asUserHandle


import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.consumeWindowInsets
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel as viewModel
import com.example.devicersapp.ui.models.FollowListSection
import com.example.devicersapp.ui.screens.follow_list.FollowListView
import com.example.devicersapp.ui.screens.follow_list.FollowListViewModel
import com.example.devicersapp.ui.screens.user_preferences.UserPreferencesView
import com.example.devicersapp.ui.screens.user_preferences.UserPreferencesViewModel
import com.example.devicersapp.R
import com.example.devicersapp.ui.screens.edit_profile.EditProfileView
import com.example.devicersapp.ui.screens.edit_profile.EditProfileViewModel
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
    data object RateProduct : AppDestination("rateProduct") {

        fun createRoute(productId: Int): String {
            return "rateProduct/$productId"
        }
    }
    data object FollowList : AppDestination("follow-list") {
        val routeWithArguments = "$route/{profileId}?section={section}"
        fun createRoute(profileId: Int, section: FollowListSection) = "$route/$profileId?section=${section.name}"
    }
    data object UserPreferences : AppDestination("user_preferences")
    data object EditProfile : AppDestination("edit-profile")
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
 * @param modifier Modificador aplicado al contenedor del grafo.
 */
@Composable
fun AppNavigation(
    startDestination: String = AppDestination.Splash.route,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()

    // Cada cabecera observa los datos de su propia pantalla, sin una sesión global de Compose.
    val route = backStackEntry?.destination?.route
    val ownProfileViewModel: OwnProfileViewModel? = backStackEntry
        ?.takeIf { route == AppDestination.OwnProfile.routeWithDeletionArgument }
        ?.let { hiltViewModel<OwnProfileViewModel>(it) }
    val ownProfileState = ownProfileViewModel?.let { viewModel ->
        val state by viewModel.uiState.collectAsState()
        state
    }
    val publicProfileState = backStackEntry
        ?.takeIf { route == "${AppDestination.Profile.route}/{profileId}" }
        ?.let { entry ->
            val viewModel: ProfileViewModel = hiltViewModel(entry)
            val state by viewModel.uiState.collectAsState()
            state
        }
    val savedProfileState = backStackEntry
        ?.takeIf { route == AppDestination.ProfileSavedReviews.route }
        ?.let { entry ->
            val viewModel: ProfileSavedReviewsViewModel = hiltViewModel(entry)
            val state by viewModel.uiState.collectAsState()
            state
        }
    val activityState = backStackEntry
        ?.takeIf { route == AppDestination.Activity.route }
        ?.let { entry ->
            val viewModel: ActivityViewModel = hiltViewModel(entry)
            val state by viewModel.uiState.collectAsState()
            state
        }
    val ownProfileId = ownProfileState?.userId ?: savedProfileState?.profile?.id
        ?: activityState?.currentProfile?.id?.toString()

    val configuration = NavigationLogic.configurationFor(
        route = backStackEntry?.destination?.route
    )

    val onProfileClick: (String) -> Unit = { profileId ->
        val ownProfile = profileId == ownProfileId
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

    val onFollowListClick: (String, FollowListSection) -> Unit = { profileId, section ->
        profileId.toIntOrNull()?.takeIf { it > 0 }?.let { id ->
            navController.navigate(AppDestination.FollowList.createRoute(id, section))
        }
    }

    DevicersScaffold(
        onProfileClick = onProfileClick,
        selectedItem = configuration.selectedItem,
        showBottomBar = configuration.showBottomBar,
        showDrawer = configuration.showDrawer,
        topBarStyle = configuration.topBarStyle,
        topBarUserHandleResId = configuration.topBarUserHandleResId,
        topBarUserHandle = (publicProfileState?.profile?.username
            ?: ownProfileState?.displayName ?: savedProfileState?.profile?.username)?.asUserHandle(),
        topBarProfileImageUrl = ownProfileState?.profileImageUrl ?: savedProfileState?.profileImageUrl
            ?: activityState?.currentProfile?.profileImageUrl,
        topBarUserId = when (route) {
            "${AppDestination.Profile.route}/{profileId}" -> backStackEntry?.arguments?.getString("profileId")
            AppDestination.OwnProfile.routeWithDeletionArgument,
            AppDestination.ProfileSavedReviews.route,
            AppDestination.Activity.route -> ownProfileId
            else -> null
        },
        modifier = modifier,
        onNavigationItemClick = { route ->
            navController.navigateToDestination(route)
        },
        onTopBarBackClick = {
            navController.popBackStack()
        },
        onEditProfileClick = if (
            backStackEntry?.destination?.route == AppDestination.OwnProfile.routeWithDeletionArgument &&
            Utils.getCurrentUser() != null
        ) {
            { navController.navigate(AppDestination.EditProfile.route) }
        } else null,
        onSavedReviewsClick = if (
            backStackEntry?.destination?.route == AppDestination.OwnProfile.routeWithDeletionArgument &&
            Utils.getCurrentUser() != null
        ) {
            { navController.navigate(AppDestination.ProfileSavedReviews.route) }
        } else null,
        onUserPreferencesClick = if (
            backStackEntry?.destination?.route == AppDestination.OwnProfile.routeWithDeletionArgument
        ) {
            { navController.navigate(AppDestination.UserPreferences.route) }
        } else null,
        onSignOutClick = {
            ownProfileViewModel?.signOut()
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
                        navController.navigate(AppDestination.Review.createRoute(reviewId))
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
                    onFollowersClick = { id -> onFollowListClick(id, FollowListSection.FOLLOWERS) },
                    onFollowingClick = { id -> onFollowListClick(id, FollowListSection.FOLLOWING) },
                    onEditProfileClick = { navController.navigate(AppDestination.EditProfile.route) },
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
            composable(
                route = AppDestination.FollowList.routeWithArguments,
                arguments = listOf(
                    navArgument("profileId") { type = NavType.IntType },
                    navArgument("section") { type = NavType.StringType; defaultValue = FollowListSection.FOLLOWERS.name }
                )
            ) { entry ->
                val profileId = requireNotNull(entry.arguments).getInt("profileId")
                val initialSection = runCatching {
                    FollowListSection.valueOf(requireNotNull(entry.arguments?.getString("section")))
                }.getOrDefault(FollowListSection.FOLLOWERS)
                val followListViewModel = viewModel<FollowListViewModel>()
                FollowListView(profileId = profileId, initialSection = initialSection,
                    viewModel = followListViewModel, onProfileClick = onProfileClick)
            }
            composable(route = AppDestination.UserPreferences.route) {
                val userPreferencesViewModel = viewModel<UserPreferencesViewModel>()
                UserPreferencesView(
                    viewModel = userPreferencesViewModel,
                    onBackClick = { navController.popBackStack() },
                    // El NavHost ya aplica el espacio superior del Scaffold compartido.
                    modifier = Modifier.consumeWindowInsets(innerPadding)
                )
            }
            composable(route = AppDestination.EditProfile.route) {
                val editProfileViewModel: EditProfileViewModel = hiltViewModel()
                EditProfileView(
                    viewModel = editProfileViewModel,
                    onSaved = { navController.popBackStack() },
                    onCancel = { navController.popBackStack() }
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

                if (profileId != null) {
                    val profileViewModel: ProfileViewModel = hiltViewModel()
                    val profileState by profileViewModel.uiState.collectAsState()
                    // El ViewModel resuelve el UID actual y compara los IDs numéricos antes de mostrar el perfil.
                    LaunchedEffect(profileState.isOwnProfile) {
                        if (profileState.isOwnProfile) navController.navigate(AppDestination.OwnProfile.route) {
                            popUpTo("${AppDestination.Profile.route}/{profileId}") { inclusive = true }
                            launchSingleTop = true
                        }
                    }

                    ProfileView(
                        onFollowersClick = { id -> onFollowListClick(id, FollowListSection.FOLLOWERS) },
                        onFollowingClick = { id -> onFollowListClick(id, FollowListSection.FOLLOWING) },
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
