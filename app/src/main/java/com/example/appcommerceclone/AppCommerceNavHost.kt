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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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

    fun navigateToProductsScreen() {
        navHostController.popBackStack(route = AppCommerceRoutes.PRODUCTS_SCREEN.route, inclusive = false)
    }

    fun navigateToProductDetailScreen() {
        navHostController.navigate(route = AppCommerceRoutes.PRODUCT_DETAIL_SCREEN.route)
    }

    fun navigateToFavoritesScreen() {
        navHostController.navigate(route = AppCommerceRoutes.FAVORITES_SCREEN.route)
    }

    fun navigateToCartScreen() {
        navHostController.navigate(route = AppCommerceRoutes.CART_SCREEN.route)
    }

    fun navigateToOrdersScreen() {
        navHostController.navigate(route = AppCommerceRoutes.ORDERS_SCREEN.route)
    }

    fun navigateToUserLoginScreen() {
        navHostController.navigate(route = AppCommerceRoutes.LOGIN_SCREEN.route)
    }

    fun navigateToUserRegisterScreen() {
        navHostController.navigate(route = AppCommerceRoutes.REGISTER_SCREEN.route)
    }

    fun navigateTo(route: String) {
        navHostController.navigate(route)
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
    context: Context = LocalContext.current,
    drawerScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    snackbarScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scaffoldState: ScaffoldState = rememberScaffoldState(drawerState, snackbarHostState),
    navHostController: NavHostController,
    currentBackStackEntry: State<NavBackStackEntry?> = navHostController.currentBackStackEntryAsState(),
    startDestination: String = AppCommerceRoutes.PRODUCTS_SCREEN.route,
    navActions: AppCommerceNavigationActions = remember(navHostController) {
        AppCommerceNavigationActions(navHostController)
    }
) {
    val user by userViewModel.loggedUser.observeAsState(initial = null)

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
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
        drawerGesturesEnabled = currentBackStackEntry.value?.destination?.route == startDestination
    ) { contentPadding ->
        NavHost(
            modifier = Modifier.padding(contentPadding),
            navController = navHostController,
            startDestination = startDestination
        ) {
            composable(route = AppCommerceRoutes.PRODUCTS_SCREEN.route) {
                val loadingState by productViewModel.loadingState.observeAsState(initial = LoadingState.NOT_STARTED)
                val products by productViewModel.products.observeAsState(initial = emptyList())
                if (loadingState == LoadingState.NOT_STARTED) productViewModel.updateProductList()
                ProductsScreen(
                    isConnected = isConnected,
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
                ProductDetailScreen(
                    product = selectedProduct!!,
                    isFavorite = favoritesViewModel.isFavorite(selectedProduct!!),
                    onAddToFavorites = { product ->
                        favoritesViewModel.addToFavorites(product)
                        navActions.navigateToFavoritesScreen()
                    },
                    onAddToFavoritesFailed = { errorMessage ->
                        snackbarScope.launch {
                            snackbarHostState.showSnackbar(message = errorMessage)
                        }
                    },
                    onBuyNow = { appreciationMessage ->
                        snackbarScope.launch {
                            snackbarHostState.showSnackbar(message = appreciationMessage)
                        }
                        navHostController.popBackStack()
                    },
                    onAddToCart = { product ->
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
            composable(route = AppCommerceRoutes.FAVORITES_SCREEN.route) {
                if (user == null) {
                    navActions.navigateToUserLoginScreen()
                    return@composable
                }

                val favoriteProducts by favoritesViewModel.favorites.observeAsState(initial = emptyList())
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
                val cartProducts by cartViewModel.cartProducts.observeAsState(initial = emptyList())
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
                            userOrdersViewModel.createOrder(userId = user!!.id, orderedProductList = cartProducts)
                            navActions.navigateToOrdersScreen()
                        } else {
                            navActions.navigateToUserLoginScreen()
                        }
                    }
                )
            }
            composable(route = AppCommerceRoutes.ORDERS_SCREEN.route) {
                if (user == null) {
                    navActions.navigateToUserLoginScreen()
                    return@composable
                }

                BackHandler(enabled = currentBackStackEntry.value?.destination?.route == it.destination.route) {
                    navActions.navigateToProductsScreen()
                }

                val orders by userOrdersViewModel.orders.observeAsState(initial = emptyList())
                OrdersScreen(orders = orders)
            }
            composable(route = AppCommerceRoutes.LOGIN_SCREEN.route) {
                BackHandler(enabled = currentBackStackEntry.value?.destination?.route == it.destination.route) {
                    navActions.navigateToProductsScreen()
                }

                val loadingState by userViewModel.loadingState.observeAsState(initial = LoadingState.NOT_STARTED)
                LoginScreen(
                    isConnected = isConnected,
                    loadingState = loadingState,
                    onLoginRequest = { username: String, password: String ->
                        userViewModel.login(username = username, password = password)
                    },
                    onRegisterRequest = {
                        navActions.navigateToUserRegisterScreen()
                    }
                )

                if (loadingState == LoadingState.SUCCESS || loadingState == LoadingState.FAILURE) {
                    LaunchedEffect(user) {
                        snackbarScope.launch {
                            snackbarHostState.showSnackbar(
                                message = when (loadingState) {
                                    LoadingState.SUCCESS -> context.getString(R.string.user_profile_welcome_message, user!!.username)
                                    LoadingState.FAILURE -> context.getString(R.string.user_error_not_found)
                                    else -> ""
                                }
                            )
                        }
                        if (loadingState == LoadingState.SUCCESS) navHostController.popBackStack()
                    }
                }
            }
            composable(route = AppCommerceRoutes.PROFILE_SCREEN.route) {
                if (user == null) {
                    navActions.navigateToUserLoginScreen()
                    return@composable
                }

                ProfileScreen(
                    user = user!!,
                    onPictureRequest = {},
                    onUpdateUserProfile = {},
                    onLogout = {
                        userViewModel.logout()
                        navActions.navigateToProductsScreen()
                    }
                )
            }
            composable(route = AppCommerceRoutes.REGISTER_SCREEN.route) {
                RegisterScreen(onRegisterRequest = {
                    navHostController.popBackStack()
                })
            }
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
            modifier = Modifier.fillMaxWidth(),
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
            onClick = { onNavigationRequest(AppCommerceRoutes.FAVORITES_SCREEN.route) },
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
            onClick = { onNavigationRequest(AppCommerceRoutes.ORDERS_SCREEN.route) },
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
            style = MaterialTheme.typography.h6
        )
    }
}

@Preview(showSystemUi = false, showBackground = true)
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

@Preview(showSystemUi = false, showBackground = true)
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