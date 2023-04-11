# AppCommerce

AppCommerceClone is an e-commerce app that features a simple filterable product list where you can add products to your favorites or easily add them
to your cart. You can see your previous purchases and also login into a predefined user account. All remote data comes from the FakeStoreApi, a free
online REST API ([GitHub](https://github.com/keikaavousi/fake-store-api) / [Site](https://fakestoreapi.com/)).

## Architecture

The app is structured using the [Model-View-ViewModel (MVVM)](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel) pattern in
conjunction with the Repository Pattern acting as [Single Source Of Truth (SSOT)](https://en.wikipedia.org/wiki/Single_source_of_truth) abstracting
the data source out of the view models. The AppCommerceClone app has a single activity that hosts all fragments while the Navigation component handles
the fragment transactions. In addition to good practices, this app follows the [SOLID](https://en.wikipedia.org/wiki/SOLID) principles to achieve a
more understandable, flexible, and maintainable app.

## Screenshots

<p float="left">
  <img src="screenshots/video_start.gif" width="200" alt="app initializing">
  <img src="screenshots/screenshot_product_detail_full.png" width="200" alt="full product detail">
  <img src="screenshots/screenshot_cart.png" width="200" alt="cart">
  <img src="screenshots/screenshot_orders.png" width="200" alt="cart">
</p>

## Testing

Tests were written with [Robolectric](https://robolectric.org/), [Espresso](https://developer.android.com/training/testing/espresso),
and [Truth](https://truth.dev/). The data layer components were defined following
the [Dependency Inversion Principles](https://pt.wikipedia.org/wiki/Princ%C3%ADpio_da_invers%C3%A3o_de_depend%C3%AAncia) where each repository
extends an interface and different implementations can be
created for the production and testing code.

## Used Libraries

- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [Fragment](https://developer.android.com/guide/fragments)
- [Navigation](https://developer.android.com/guide/navigation)
- [RecyclerView](https://developer.android.com/develop/ui/views/layout/recyclerview)
- [ViewBinding](https://developer.android.com/topic/libraries/view-binding)
- [DataBinding](https://developer.android.com/topic/libraries/data-binding)
- [Binding Adapters](https://developer.android.com/topic/libraries/data-binding/binding-adapters)
- [Hilt Dependency Injection](https://dagger.dev/hilt/)
- [Retrofit](https://square.github.io/retrofit/)
- [Moshi](https://github.com/square/moshi/)
- [OkHttp](https://square.github.io/okhttp/)
- [Coroutines](https://kotlinlang.org/docs/coroutines-guide.html)
- [Flow](https://developer.android.com/kotlin/flow)
- [Glide](https://bumptech.github.io/glide/)
- [Facebook's shimmer effect](https://github.com/facebook/shimmer-android)
- [Espresso](https://developer.android.com/training/testing/espresso)
- [Truth](https://truth.dev/)
- [Robolectric](https://robolectric.org/)

### Quick User References

| Username  | Password  |
|-----------|-----------|
| johnd     | m38rmF$   |
| mor_2314  | 83r5^_    |
| kevinryan | kev02937@ |

### Image References

#### Icons

[Diamond Ring Icon](https://icons8.com/icon/19632/diamond-ring)

[T-Shirt Icon](https://icons8.com/icon/105819/t-shirt)

[Womens T-Shirt Icon](https://icons8.com/icon/25497/womens-t-shirt)

[Tech Icon](https://icons8.com/icon/ifjgL624vDhJ/technology)

#### Banners

[Woman Fashion banner](https://www.freepik.com/photos/happy-moments)

[Male fashion banner](https://www.freepik.com/free-photo/business-man-coat-talking-phone-outside_1619086.htm#query=male%20fashion&position=43&from_view=keyword)

[Electronics banner](https://www.freepik.com/free-photo/beautiful-young-woman-home-office-working-from-home-teleworking-concept_11013620.htm#query=coffee%20work&position=10&from_view=keyword")