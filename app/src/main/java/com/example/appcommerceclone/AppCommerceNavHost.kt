package com.example.appcommerceclone

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.data.user.model.User
import com.example.appcommerceclone.ui.cart.CartScreen
import com.example.appcommerceclone.ui.cart.CartViewModel
import com.example.appcommerceclone.ui.favorites.FavoritesScreen
import com.example.appcommerceclone.ui.favorites.FavoritesViewModel
import com.example.appcommerceclone.ui.order.OrdersScreen
import com.example.appcommerceclone.ui.order.UserOrdersViewModel
import com.example.appcommerceclone.ui.product.CategoriesScreen
import com.example.appcommerceclone.ui.product.ProductCategories
import com.example.appcommerceclone.ui.product.ProductDetailScreen
import com.example.appcommerceclone.ui.product.ProductViewModel
import com.example.appcommerceclone.ui.product.ProductsScreen
import com.example.appcommerceclone.ui.user.LoginScreen
import com.example.appcommerceclone.ui.user.ProfileScreen
import com.example.appcommerceclone.ui.user.RegisterScreen
import com.example.appcommerceclone.ui.user.UserViewModel
import com.example.appcommerceclone.util.formatPrice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class LoadingState {
    NOT_STARTED,
    LOADING,
    SUCCESS,
    FAILURE
}

enum class AppCommerceRoutes(val route: String) {
    PRODUCTS_SCREEN("ProductsScreen"),
    PRODUCT_DETAIL_SCREEN("ProductDetailScreen"),
    FAVORITES_SCREEN("FavoritesScreen"),
    CATEGORIES_SCREEN("CategoriesScreen"),
    CART_SCREEN("CartScreen"),
    ORDERS_SCREEN("OrdersScreen"),
    LOGIN_SCREEN("LoginScreen"),
    PROFILE_SCREEN("ProfileScreen"),
    REGISTER_SCREEN("RegisterScreen")
}

class AppCommerceNavigationActions(private val navHostController: NavHostController) {

    fun navigateTo(route: String) {
        navHostController.navigate(
            route = route,
            navOptions = navOptions {
                launchSingleTop = true
            }
        )
    }

    fun shouldInterceptBackPressed(currentRoute: String?): Boolean {
        return when (currentRoute) {
            AppCommerceRoutes.LOGIN_SCREEN.route -> true
            AppCommerceRoutes.CART_SCREEN.route -> true
            AppCommerceRoutes.FAVORITES_SCREEN.route -> true
            AppCommerceRoutes.ORDERS_SCREEN.route -> true
            else -> false
        }
    }

    fun navigateToProductsScreen() {
        val startRoute = AppCommerceRoutes.PRODUCTS_SCREEN.route
        navHostController.navigate(
            route = startRoute,
            navOptions = navOptions {
                launchSingleTop = true
                popUpTo(startRoute)
            }
        )
    }

    fun navigateToProductDetailScreen() {
        navigateTo(AppCommerceRoutes.PRODUCT_DETAIL_SCREEN.route)
    }

    fun navigateToFavoritesScreen() {
        navigateTo(AppCommerceRoutes.FAVORITES_SCREEN.route)
    }

    fun navigateToCartScreen() {
        navigateTo(AppCommerceRoutes.CART_SCREEN.route)
    }

    fun navigateToOrdersScreen() {
        navigateTo(AppCommerceRoutes.ORDERS_SCREEN.route)
    }

    fun navigateToUserLoginScreen() {
        navigateTo(AppCommerceRoutes.LOGIN_SCREEN.route)
    }

    fun navigateToUserRegisterScreen() {
        navigateTo(AppCommerceRoutes.REGISTER_SCREEN.route)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppCommerceNavHost(
    modifier: Modifier = Modifier,
    isConnected: Boolean,
    productViewModel: ProductViewModel,
    favoritesViewModel: FavoritesViewModel,
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel,
    userOrdersViewModel: UserOrdersViewModel,
    onUpdateProfilePicture: () -> Unit,
    context: Context = LocalContext.current,
    drawerScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    snackbarScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scaffoldState: ScaffoldState = rememberScaffoldState(drawerState, snackbarHostState),
    navHostController: NavHostController = rememberNavController(),
    startDestination: String = AppCommerceRoutes.PRODUCTS_SCREEN.route,
    navActions: AppCommerceNavigationActions = remember(navHostController) {
        AppCommerceNavigationActions(navHostController)
    }
) {
    val user: User? by userViewModel.loggedUser.observeAsState(initial = null)
    val currentBackStackEntryState: NavBackStackEntry? by navHostController.currentBackStackEntryAsState()
    val currentRoute: String? = currentBackStackEntryState?.destination?.route

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBarContent(
                context = context,
                isDrawerOpen = drawerState.isOpen,
                startDestination = startDestination,
                currentRoute = currentRoute,
                onNavigationIconClicked = {
                    if (currentRoute != startDestination) {
                        if (navActions.shouldInterceptBackPressed(currentRoute)) navActions.navigateTo(AppCommerceRoutes.PRODUCTS_SCREEN.route)
                        else navHostController.popBackStack()
                    } else {
                        drawerScope.launch {
                            if (drawerState.isOpen) drawerState.close()
                            else drawerState.open()
                        }
                    }
                },
                showErrorMessage = !isConnected,
                errorMessage = stringResource(id = R.string.error_no_connection)
            )
        },
        drawerContent = {
            ModalDrawerContent(
                user = user,
                onLogoutRequest = {
                    userViewModel.logout()
                    drawerScope.launch {
                        drawerState.close()
                    }
                },
                onNavigationRequest = { route: String ->
                    navActions.navigateTo(route)
                    drawerScope.launch {
                        drawerState.close()
                    }
                }
            )
        },
        drawerGesturesEnabled = currentRoute == startDestination
    ) { contentPadding ->
        NavHost(
            modifier = Modifier.padding(contentPadding),
            navController = navHostController,
            startDestination = startDestination
        ) {
            composable(route = AppCommerceRoutes.PRODUCTS_SCREEN.route) {
                val loadingState by productViewModel.loadingState.observeAsState(initial = LoadingState.NOT_STARTED)
                val products by productViewModel.products.observeAsState(initial = emptyList())
                if (isConnected && loadingState == LoadingState.NOT_STARTED) productViewModel.updateProductList()
                ProductsScreen(
                    loadingState = loadingState,
                    products = products,
                    onProductClicked = { product: Product ->
                        productViewModel.selectProduct(product)
                        navActions.navigateToProductDetailScreen()
                    },
                    onRefresh = {
                        if (loadingState == LoadingState.FAILURE) productViewModel.updateProductList()
                        productViewModel.filterProductList(ProductCategories.NONE)
                    }
                )
            }
            composable(route = AppCommerceRoutes.PRODUCT_DETAIL_SCREEN.route) {
                val selectedProduct by productViewModel.selectedProduct.observeAsState()
                if (selectedProduct == null) navHostController.popBackStack()
                selectedProduct?.let { product ->
                    ProductDetailScreen(
                        product = product,
                        isFavorite = favoritesViewModel.isFavorite(product),
                        onAddToFavorites = {
                            if (user != null) {
                                favoritesViewModel.addToFavorites(product)
                                navActions.navigateToFavoritesScreen()
                            } else {
                                navActions.navigateToUserLoginScreen()
                            }
                        },
                        onAddToFavoritesFailed = { errorMessage ->
                            snackbarScope.launch {
                                snackbarHostState.showSnackbar(message = errorMessage)
                            }
                        },
                        onBuyNow = { appreciationMessage ->
                            if (user != null) {
                                if (!isConnected) return@ProductDetailScreen
                                userOrdersViewModel.createOrder(userId = user!!.id, product = product)
                                snackbarScope.launch {
                                    snackbarHostState.showSnackbar(message = appreciationMessage)
                                }
                                navHostController.popBackStack()
                            } else {
                                navActions.navigateToUserLoginScreen()
                            }
                        },
                        onAddToCart = {
                            cartViewModel.addToCart(product)
                            navActions.navigateToCartScreen()
                        },
                        onPurchaseFailed = { errorMessage ->
                            snackbarScope.launch {
                                snackbarHostState.showSnackbar(message = errorMessage)
                            }
                        }
                    )
                }
            }
            composable(route = AppCommerceRoutes.FAVORITES_SCREEN.route) {
                BackHandler {
                    navActions.navigateToProductsScreen()
                }

                val favoriteProducts = favoritesViewModel.favorites
                FavoritesScreen(
                    favoriteProducts = favoriteProducts,
                    onRemoveFavoriteProduct = { product ->
                        favoritesViewModel.removeFromFavorites(product)
                    }
                )
            }
            composable(route = AppCommerceRoutes.CATEGORIES_SCREEN.route) {
                CategoriesScreen(onProductCategorySelected = { category: ProductCategories ->
                    productViewModel.filterProductList(category)
                    navHostController.popBackStack()
                })
            }
            composable(route = AppCommerceRoutes.CART_SCREEN.route) {
                BackHandler {
                    navActions.navigateToProductsScreen()
                }

                val cartProducts = cartViewModel.cartProducts
                val cartTotalPrice by cartViewModel.cartTotalPrice.observeAsState(initial = 0.0)
                CartScreen(
                    cartProducts = cartProducts,
                    onQuantityIncrease = { orderedProduct ->
                        cartViewModel.increaseQuantity(orderedProduct)
                    },
                    onQuantityDecrease = { orderedProduct ->
                        cartViewModel.decreaseQuantity(orderedProduct)
                    },
                    cartTotalPrice = cartTotalPrice.formatPrice(),
                    onAbandonCart = {
                        cartViewModel.abandonCart()
                    },
                    onConfirmPurchase = {
                        if (user != null) {
                            if (!isConnected) return@CartScreen
                            userOrdersViewModel.createOrder(userId = user!!.id, orderedProductList = cartProducts)
                            cartViewModel.abandonCart()
                            navActions.navigateToOrdersScreen()
                        } else {
                            navActions.navigateToUserLoginScreen()
                        }
                    }
                )
            }
            composable(route = AppCommerceRoutes.ORDERS_SCREEN.route) {
                BackHandler {
                    navActions.navigateToProductsScreen()
                }

                val orders = userOrdersViewModel.orders
                OrdersScreen(orders = orders)
            }
            composable(route = AppCommerceRoutes.LOGIN_SCREEN.route) {
                BackHandler {
                    navActions.navigateToProductsScreen()
                    userViewModel.resetLoadingState()
                }

                val loadingState by userViewModel.loadingState.observeAsState(initial = LoadingState.NOT_STARTED)
                LoginScreen(
                    loadingState = loadingState,
                    onLoginRequest = { username: String, password: String ->
                        if (isConnected) userViewModel.login(username = username, password = password)
                    },
                    onRegisterRequest = {
                        navActions.navigateToUserRegisterScreen()
                    }
                )

                if (loadingState == LoadingState.SUCCESS || loadingState == LoadingState.FAILURE) {
                    LaunchedEffect(user) {
                        if (user != null) {
                            snackbarHostState.showSnackbar(message = context.getString(R.string.login_success_message, user!!.username))
                            navHostController.popBackStack()
                            userViewModel.resetLoadingState()
                        } else {
                            snackbarHostState.showSnackbar(message = context.getString(R.string.login_failure_message_user_not_found))
                        }
                    }
                }
            }
            composable(route = AppCommerceRoutes.PROFILE_SCREEN.route) {
                val profilePicture by userViewModel.profilePicture.observeAsState(initial = null)
                ProfileScreen(
                    user = user!!,
                    userPicture = profilePicture,
                    onUpdateProfilePicture = onUpdateProfilePicture,
                    onUpdateUserProfile = {},
                    onLogout = {
                        navActions.navigateToProductsScreen()
                        userViewModel.logout()
                    }
                )
            }
            composable(route = AppCommerceRoutes.REGISTER_SCREEN.route) {
                RegisterScreen(onRegisterRequest = { registrationMessage ->
                    snackbarScope.launch {
                        snackbarHostState.showSnackbar(message = registrationMessage)
                    }
                    navHostController.popBackStack()
                })
            }
        }
    }
}

@Composable
fun TopAppBarContent(
    modifier: Modifier = Modifier,
    context: Context,
    isDrawerOpen: Boolean,
    startDestination: String,
    currentRoute: String?,
    onNavigationIconClicked: () -> Unit,
    showErrorMessage: Boolean,
    errorMessage: String
) {
    Column {
        TopAppBar(
            modifier = modifier,
            contentColor = colorResource(id = R.color.onPrimaryColor),
            backgroundColor = colorResource(id = R.color.primaryColor),
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.h5
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigationIconClicked) {
                    Icon(
                        imageVector =
                        if (currentRoute == startDestination) Icons.Filled.Menu
                        else Icons.Filled.ArrowBack,
                        contentDescription =
                        if (currentRoute != startDestination) {
                            context.getString(R.string.content_desc_menu_navigate_back)
                        } else {
                            if (isDrawerOpen) context.getString(R.string.content_desc_menu_close)
                            else context.getString(R.string.content_desc_menu_open)
                        }
                    )
                }
            }
        )
        if (showErrorMessage) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.padding_small)),
                text = errorMessage,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Composable
fun ModalDrawerContent(
    modifier: Modifier = Modifier,
    user: User?,
    onLogoutRequest: () -> Unit,
    onNavigationRequest: (route: String) -> Unit
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_large)),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(
            space = dimensionResource(id = R.dimen.padding_small),
            alignment = Alignment.Top
        )
    ) {
        ModalDrawerItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.padding_large)),
            onClick = {
                onNavigationRequest(
                    if (user == null) AppCommerceRoutes.LOGIN_SCREEN.route
                    else AppCommerceRoutes.PROFILE_SCREEN.route
                )
            },
            itemIcon = Icons.Filled.Person,
            itemText = if (user == null) stringResource(id = R.string.menu_title_login)
            else stringResource(id = R.string.menu_title_profile)
        )
        ModalDrawerItem(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onNavigationRequest(
                    if (user == null) AppCommerceRoutes.LOGIN_SCREEN.route
                    else AppCommerceRoutes.FAVORITES_SCREEN.route
                )
            },
            itemIcon = Icons.Filled.Star,
            itemText = stringResource(id = R.string.menu_title_favorites)
        )
        ModalDrawerItem(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onNavigationRequest(AppCommerceRoutes.CATEGORIES_SCREEN.route) },
            itemIcon = Icons.Filled.Category,
            itemText = stringResource(id = R.string.menu_title_categories)
        )
        ModalDrawerItem(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onNavigationRequest(
                    if (user == null) AppCommerceRoutes.LOGIN_SCREEN.route
                    else AppCommerceRoutes.ORDERS_SCREEN.route
                )
            },
            itemIcon = Icons.Filled.History,
            itemText = stringResource(id = R.string.menu_title_my_orders)
        )
        ModalDrawerItem(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onNavigationRequest(AppCommerceRoutes.CART_SCREEN.route) },
            itemIcon = Icons.Filled.ShoppingCart,
            itemText = stringResource(id = R.string.menu_title_cart)
        )
        if (user != null) {
            ModalDrawerItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = onLogoutRequest,
                itemIcon = Icons.Filled.Close,
                itemText = stringResource(id = R.string.menu_title_logout)
            )
        }
    }
}

@Composable
fun ModalDrawerItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    itemIcon: ImageVector,
    itemIconContentDescription: String? = null,
    itemText: String
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .minimumInteractiveComponentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = dimensionResource(id = R.dimen.padding_medium),
            alignment = Alignment.Start
        )
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = itemIcon,
            contentDescription = itemIconContentDescription
        )
        Text(
            text = itemText,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Preview
@Composable
fun PreviewTopAppBar() {
    MaterialTheme {
        TopAppBarContent(
            context = LocalContext.current,
            isDrawerOpen = false,
            startDestination = AppCommerceRoutes.PRODUCTS_SCREEN.route,
            currentRoute = AppCommerceRoutes.PRODUCTS_SCREEN.route,
            onNavigationIconClicked = {},
            showErrorMessage = false,
            errorMessage = ""
        )
    }
}

@Preview
@Composable
fun PreviewTopAppBarWithMessage() {
    MaterialTheme {
        TopAppBarContent(
            context = LocalContext.current,
            isDrawerOpen = false,
            startDestination = AppCommerceRoutes.PRODUCTS_SCREEN.route,
            currentRoute = AppCommerceRoutes.PRODUCTS_SCREEN.route,
            onNavigationIconClicked = {},
            showErrorMessage = true,
            errorMessage = stringResource(id = R.string.error_no_connection)
        )
    }
}

@Preview
@Composable
fun PreviewModalDrawerContentNoUser() {
    MaterialTheme {
        ModalDrawerContent(
            user = null,
            onLogoutRequest = {},
            onNavigationRequest = {}
        )
    }
}

@Preview
@Composable
fun PreviewModalDrawerContentWithUser() {
    MaterialTheme {
        ModalDrawerContent(
            user = User(),
            onLogoutRequest = {},
            onNavigationRequest = {}
        )
    }
}