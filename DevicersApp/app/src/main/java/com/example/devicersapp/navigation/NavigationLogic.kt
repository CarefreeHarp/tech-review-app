package com.example.devicersapp.navigation

import com.example.devicersapp.ui.utils.navigation.TopBarStyle
import androidx.annotation.StringRes
import androidx.navigation.NavHostController

/**
 * Describe la estructura visual compartida que necesita un destino de navegación.
 */
data class NavigationUiConfiguration(
    val selectedItem: String = "",
    val showBottomBar: Boolean = false,
    val showDrawer: Boolean = false,
    val topBarStyle: TopBarStyle? = null,
    @param:StringRes val topBarUserHandleResId: Int? = null
)

/**
 * Centraliza las reglas que determinan las barras y elementos
 * de navegación visibles para cada destino.
 */
object NavigationLogic {

    /**
     * Obtiene la configuración visual correspondiente al destino actual.
     */
    fun configurationFor(
        route: String?
    ): NavigationUiConfiguration = when (route) {

        AppDestination.Splash.route,
        AppDestination.UserPreferences.route ->
            NavigationUiConfiguration()

        AppDestination.Login.route,
        AppDestination.Register.route ->
            NavigationUiConfiguration(topBarStyle = TopBarStyle.Authentication)

        AppDestination.Home.route ->
            NavigationUiConfiguration(
                selectedItem = AppDestination.Home.route,
                showBottomBar = true,
                showDrawer = true,
                topBarStyle = TopBarStyle.Authentication
            )

        AppDestination.SearchProduct.route ->
            NavigationUiConfiguration(
                selectedItem = AppDestination.SearchProduct.route,
                showBottomBar = true,
                showDrawer = true
            )

        AppDestination.CreateReview.route ->
            NavigationUiConfiguration(
                selectedItem = AppDestination.CreateReview.route,
                showBottomBar = true,
                showDrawer = true,
                topBarStyle = TopBarStyle.CreateReview
            )

        AppDestination.Activity.route ->
            NavigationUiConfiguration(
                selectedItem = AppDestination.Activity.route,
                showBottomBar = true,
                showDrawer = true,
                topBarStyle = TopBarStyle.Activity
            )

        AppDestination.OwnProfile.route,
        AppDestination.OwnProfile.routeWithDeletionArgument ->
            NavigationUiConfiguration(
                selectedItem = AppDestination.OwnProfile.route,
                showBottomBar = true,
                showDrawer = true,
                topBarStyle = TopBarStyle.Profile
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
                topBarStyle = TopBarStyle.FoundProducts
            )

        AppDestination.ProfileSearchResults.route ->
            NavigationUiConfiguration(
                selectedItem = AppDestination.SearchProduct.route,
                showBottomBar = true,
                topBarStyle = TopBarStyle.ProfileSearchResults
            )

        "${AppDestination.Product.route}/{productId}" ->
            NavigationUiConfiguration(
                selectedItem = AppDestination.SearchProduct.route,
                showBottomBar = true,
                topBarStyle = TopBarStyle.Product
            )

        "${AppDestination.RateProduct.route}/{productId}" ->
            NavigationUiConfiguration(
                topBarStyle = TopBarStyle.RateProduct
            )

        AppDestination.FollowList.routeWithArguments ->
            NavigationUiConfiguration(topBarStyle = TopBarStyle.FollowList)

        AppDestination.EditProfile.route ->
            NavigationUiConfiguration(topBarStyle = TopBarStyle.EditProfile)

        "${AppDestination.EditReview.route}/{reviewId}" ->
            NavigationUiConfiguration(topBarStyle = TopBarStyle.EditReview)

        AppDestination.Review.route,
        "${AppDestination.Review.route}/{reviewId}",
        "${AppDestination.Review.route}/local/{reviewId}" ->
            NavigationUiConfiguration(topBarStyle = TopBarStyle.Review)

        AppDestination.RequestProduct.route ->
            NavigationUiConfiguration(
                selectedItem = AppDestination.CreateReview.route,
                topBarStyle = TopBarStyle.RequestProduct
            )

        AppDestination.Profile.route,
        AppDestination.ProfileSavedReviews.route,
        "${AppDestination.Profile.route}/{profileId}" ->
            NavigationUiConfiguration(
                selectedItem = AppDestination.OwnProfile.route,
                showBottomBar = true,
                topBarStyle = TopBarStyle.Profile
            )

        else ->
            NavigationUiConfiguration(
                topBarStyle = TopBarStyle.Authentication
            )
    }
}

/**
 * Navega entre destinos principales y conserva el historial,
 * excepto al volver al inicio.
 *
 * Home se convierte en la única pantalla del historial para que
 * el botón Atrás del sistema cierre la aplicación desde allí.
 * Los demás destinos se agregan a la pila normalmente.
 */
fun NavHostController.navigateToDestination(route: String) {

    // No se modifica la pila si el ítem pulsado ya representa
    // la pantalla visible.
    if (currentDestination?.route == route ||
        (route == AppDestination.OwnProfile.route && currentDestination?.route == AppDestination.OwnProfile.routeWithDeletionArgument)) {
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
