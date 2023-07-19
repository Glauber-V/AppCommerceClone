package com.example.appcommerceclone.ui

import androidx.activity.ComponentActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.appcommerceclone.AppCommerceNavHost
import com.example.appcommerceclone.AppCommerceRoutes
import com.example.appcommerceclone.LoadingState
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.ProductsRepository
import com.example.appcommerceclone.data.product.model.Order
import com.example.appcommerceclone.data.product.model.OrderedProduct
import com.example.appcommerceclone.data.product.model.Product
import com.example.appcommerceclone.data.user.FakeUserProvider.Companion.firstUser
import com.example.appcommerceclone.data.user.UserAuthenticator
import com.example.appcommerceclone.data.user.model.User
import com.example.appcommerceclone.di.ConnectivityModule
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.ui.cart.CartViewModel
import com.example.appcommerceclone.ui.favorites.FavoritesViewModel
import com.example.appcommerceclone.ui.order.UserOrdersViewModel
import com.example.appcommerceclone.ui.product.ProductViewModel
import com.example.appcommerceclone.ui.user.UserViewModel
import com.example.appcommerceclone.util.TestMainDispatcherRule
import com.example.appcommerceclone.util.assertCurrentRouteIsEqualTo
import com.example.appcommerceclone.util.getStringResource
import com.example.appcommerceclone.util.productElectronic
import com.example.appcommerceclone.util.productJewelry
import com.example.appcommerceclone.util.productMensClothing
import com.example.appcommerceclone.util.productWomensClothing
import com.example.appcommerceclone.util.showSemanticTreeInConsole
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@UninstallModules(ConnectivityModule::class, ProductsModule::class, UsersModule::class, DispatcherModule::class)
@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class EndToEndLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testMainDispatcherRule = TestMainDispatcherRule()

    @get:Rule(order = 2)
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    @Inject
    lateinit var productsRepository: ProductsRepository

    @Inject
    lateinit var userAuthenticator: UserAuthenticator

    private lateinit var productViewModel: ProductViewModel
    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var userOrdersViewModel: UserOrdersViewModel

    private lateinit var snackbarHostState: SnackbarHostState
    private lateinit var navHostController: TestNavHostController

    private val isConnected = mutableStateOf(true)
    private lateinit var user: State<User?>
    private lateinit var userLoadingState: State<LoadingState>
    private lateinit var products: State<List<Product>>
    private lateinit var productsLoadingState: State<LoadingState>
    private lateinit var selectedProduct: State<Product?>
    private lateinit var cartProducts: State<List<OrderedProduct>>
    private lateinit var orders: State<List<Order>>

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        showSemanticTreeInConsole()
        composeRule.setContent {
            MaterialTheme {
                productViewModel = ProductViewModel(productsRepository, dispatcherProvider)
                favoritesViewModel = FavoritesViewModel()
                cartViewModel = CartViewModel()
                userViewModel = UserViewModel(userAuthenticator, dispatcherProvider)
                userOrdersViewModel = UserOrdersViewModel()

                snackbarHostState = SnackbarHostState()
                navHostController = TestNavHostController(LocalContext.current)
                navHostController.navigatorProvider.addNavigator(ComposeNavigator())

                user = userViewModel.loggedUser.observeAsState(initial = null)
                userLoadingState = userViewModel.loadingState.observeAsState(initial = LoadingState.NOT_STARTED)
                products = productViewModel.products.observeAsState(initial = emptyList())
                productsLoadingState = productViewModel.loadingState.observeAsState(initial = LoadingState.NOT_STARTED)
                selectedProduct = productViewModel.selectedProduct.observeAsState(initial = null)
                cartProducts = cartViewModel.cartProducts.observeAsState(initial = emptyList())
                orders = userOrdersViewModel.orders.observeAsState(initial = emptyList())

                AppCommerceNavHost(
                    isConnected = isConnected.value,
                    productViewModel = productViewModel,
                    favoritesViewModel = favoritesViewModel,
                    cartViewModel = cartViewModel,
                    userViewModel = userViewModel,
                    userOrdersViewModel = userOrdersViewModel,
                    snackbarHostState = snackbarHostState,
                    navHostController = navHostController
                )
            }
        }
    }

    @Test
    fun startOnProductsScreen_addProductElectronicToCart_buyProduct_verifyOrderList_returnToStartDestination() = runTest {
        with(composeRule) {
            navHostController.assertCurrentRouteIsEqualTo(AppCommerceRoutes.PRODUCTS_SCREEN.route)
            onRoot().printToLog(navHostController.createTag())
            advanceUntilIdle()

            assertThat(productsLoadingState.value).isEqualTo(LoadingState.SUCCESS)

            assertThat(products.value).isNotEmpty()
            assertThat(products.value).hasSize(4)
            assertThat(products.value).contains(productJewelry)
            assertThat(products.value).contains(productElectronic)
            assertThat(products.value).contains(productMensClothing)
            assertThat(products.value).contains(productWomensClothing)

            assertThat(selectedProduct.value).isNull()

            onNodeWithText(productElectronic.name)
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()

            navHostController.assertCurrentRouteIsEqualTo(AppCommerceRoutes.PRODUCT_DETAIL_SCREEN.route)
            onRoot().printToLog(navHostController.createTag())

            assertThat(selectedProduct.value).isNotNull()
            assertThat(selectedProduct.value).isEqualTo(productElectronic)

            assertThat(cartProducts.value).isEmpty()

            onNodeWithContentDescription(getStringResource(R.string.product_detail_add_to_cart))
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .performClick()

            navHostController.assertCurrentRouteIsEqualTo(AppCommerceRoutes.CART_SCREEN.route)
            onRoot().printToLog(navHostController.createTag())

            assertThat(cartProducts.value).isNotEmpty()
            assertThat(cartProducts.value).hasSize(1)
            assertThat(cartProducts.value).contains(OrderedProduct(productElectronic))

            assertThat(userLoadingState.value).isEqualTo(LoadingState.NOT_STARTED)
            assertThat(user.value).isNull()

            onNodeWithText(getStringResource(R.string.cart_confirm_purchase_btn))
                .assertExists()
                .assertIsDisplayed()
                .assertIsEnabled()
                .performClick()

            navHostController.assertCurrentRouteIsEqualTo(AppCommerceRoutes.LOGIN_SCREEN.route)
            onRoot().printToLog(navHostController.createTag())

            onNodeWithText(getStringResource(R.string.user_hint_username))
                .assertExists()
                .assertIsDisplayed()
                .performTextReplacement(firstUser.username)

            onNodeWithText(getStringResource(R.string.user_hint_password))
                .assertExists()
                .assertIsDisplayed()
                .performTextReplacement(firstUser.password)

            onAllNodesWithText(getStringResource(R.string.user_login_btn))
                .assertCountEquals(2)
                .onFirst()
                .performClick()

            advanceUntilIdle()

            assertThat(userLoadingState.value).isEqualTo(LoadingState.SUCCESS)
            assertThat(user.value).isNotNull()
            assertThat(user.value).isEqualTo(firstUser)

            onNodeWithText(getStringResource(R.string.user_profile_welcome_message, firstUser.username))
                .assertExists()
                .assertIsDisplayed()

            snackbarHostState.currentSnackbarData?.dismiss()
            waitForIdle()

            onNodeWithText(getStringResource(R.string.user_profile_welcome_message, firstUser.username))
                .assertDoesNotExist()

            navHostController.assertCurrentRouteIsEqualTo(AppCommerceRoutes.CART_SCREEN.route)
            onRoot().printToLog(navHostController.createTag())

            assertThat(cartProducts.value).isNotEmpty()
            assertThat(cartProducts.value).hasSize(1)
            assertThat(cartProducts.value).contains(OrderedProduct(productElectronic))

            onNodeWithText(getStringResource(R.string.cart_confirm_purchase_btn))
                .assertExists()
                .assertIsDisplayed()
                .assertIsEnabled()
                .performClick()

            navHostController.assertCurrentRouteIsEqualTo(AppCommerceRoutes.ORDERS_SCREEN.route)
            onRoot().printToLog(navHostController.createTag())

            assertThat(orders.value).isNotEmpty()
            assertThat(orders.value).hasSize(1)

            onNodeWithText(getStringResource(R.string.order_item_products, orders.value.first().getOrderedProductListAsString()))
                .assertExists()
                .assertIsDisplayed()

            activity.onBackPressed()

            navHostController.assertCurrentRouteIsEqualTo(AppCommerceRoutes.PRODUCTS_SCREEN.route)
        }
    }

    private fun TestNavHostController.createTag(): String {
        return "Current Route: ${this.currentBackStackEntry?.destination?.route}"
    }
}