//package com.example.devicersapp.navigation
//
//import androidx.annotation.StringRes
//import androidx.navigation.NavHostController
//import com.example.devicersapp.data.local.LocalProfileProvider
//
///** Describe la estructura visual compartida que necesita un destino de navegación. */
//data class NavigationUiConfiguration(
//    val selectedItem: String = "",
//    val showBottomBar: Boolean = false,
//    val topBarNumber: Int? = null,
//    @param:StringRes val topBarUserHandleResId: Int? = null
//)
//
///** Centraliza las reglas que determinan las barras visibles para cada destino principal. */
//object NavigationLogic {
//    /** Obtiene la configuración visual correspondiente al destino actual. */
//    fun configurationFor(
//        route: String?,
//        profileId: String? = null
//    ): NavigationUiConfiguration = when (route) {
//        AppDestination.Login.route -> NavigationUiConfiguration(topBarNumber = 5)
//        AppDestination.Home.route -> NavigationUiConfiguration(
//            selectedItem = AppDestination.Home.route,
//            showBottomBar = true,
//            topBarNumber = 5
//        )
//        AppDestination.SearchProduct.route -> NavigationUiConfiguration(
//            selectedItem = AppDestination.SearchProduct.route,
//            showBottomBar = true
//        )
//        AppDestination.CreateReview.route -> NavigationUiConfiguration(
//            selectedItem = AppDestination.CreateReview.route,
//            showBottomBar = true,
//            topBarNumber = 3
//        )
//        AppDestination.Activity.route -> NavigationUiConfiguration(
//            selectedItem = AppDestination.Activity.route,
//            showBottomBar = true,
//            topBarNumber = 10
//        )
//        AppDestination.OwnProfile.route -> NavigationUiConfiguration(
//            selectedItem = AppDestination.OwnProfile.route,
//            showBottomBar = true,
//            topBarNumber = 1,
//            topBarUserHandleResId = LocalProfileProvider.profile.handleResId
//        )
//        AppDestination.Register.route -> NavigationUiConfiguration(topBarNumber = 5)
//        AppDestination.SearchProfile.route -> NavigationUiConfiguration(
//            selectedItem = AppDestination.SearchProduct.route,
//            showBottomBar = true
//        )
//        AppDestination.FoundProducts.route -> NavigationUiConfiguration(
//            selectedItem = AppDestination.SearchProduct.route,
//            showBottomBar = true,
//            topBarNumber = 8
//        )
//        AppDestination.ProfileSearchResults.route -> NavigationUiConfiguration(
//            selectedItem = AppDestination.SearchProduct.route,
//            showBottomBar = true,
//            topBarNumber = 7
//        )
//        "${AppDestination.Product.route}/{productNameResId}" -> NavigationUiConfiguration(
//            selectedItem = AppDestination.SearchProduct.route,
//            showBottomBar = true,
//            topBarNumber = 6
//        )
//        "${AppDestination.RateProduct.route}/{productNameResId}" ->
//            NavigationUiConfiguration(topBarNumber = 2)
//        AppDestination.Review.route -> NavigationUiConfiguration(topBarNumber = 4)
//        AppDestination.RequestProduct.route -> NavigationUiConfiguration(
//            selectedItem = AppDestination.CreateReview.route,
//            topBarNumber = 9
//        )
//        AppDestination.Profile.route -> NavigationUiConfiguration(
//            selectedItem = AppDestination.OwnProfile.route,
//            showBottomBar = true,
//            topBarNumber = 1,
//            topBarUserHandleResId = LocalProfileProvider.profile.handleResId
//        )
//        AppDestination.ProfileSavedReviews.route -> NavigationUiConfiguration(
//            selectedItem = AppDestination.OwnProfile.route,
//            showBottomBar = true,
//            topBarNumber = 1,
//            topBarUserHandleResId = LocalProfileProvider.profile.handleResId
//        )
//        "${AppDestination.Profile.route}/{profileId}" -> NavigationUiConfiguration(
//            selectedItem = AppDestination.OwnProfile.route,
//            showBottomBar = true,
//            topBarNumber = 1,
//            topBarUserHandleResId = profileId?.let { id ->
//                LocalProfileProvider.getPublicProfileById(id)?.handleResId
//            }
//        )
//        "${AppDestination.Review.route}/{reviewId}" ->
//            NavigationUiConfiguration(topBarNumber = 4)
//        else -> NavigationUiConfiguration(topBarNumber = 5)
//    }
//}
//
///**
// * Navega entre destinos principales y conserva el historial, excepto al volver al inicio.
// *
// * Home se convierte en la única pantalla del historial para que el botón Atrás del sistema
// * cierre la aplicación desde allí. Los demás destinos se agregan a la pila normalmente.
// */
//fun NavHostController.navigateToDestination(route: String) {
//    // No se modifica la pila si el ítem pulsado ya representa la pantalla visible.
//    if (currentDestination?.route == route) {
//        return
//    }
//
//    navigate(route) {
//        if (route == AppDestination.Home.route) {
//            popUpTo(0) {
//                inclusive = true
//            }
//        }
//        launchSingleTop = true
//    }
//}
