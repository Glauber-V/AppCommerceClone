package com.example.appcommerceclone.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.databinding.FragmentCategoriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment(private val productViewModel: ProductViewModel) : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productCategoryAll.setOnClickListener { selectCategoryAndNavigateBack(ProductCategories.NONE) }
        binding.productCategoryJewelery.setOnClickListener { selectCategoryAndNavigateBack(ProductCategories.JEWELERY) }
        binding.productCategoryElectronics.setOnClickListener { selectCategoryAndNavigateBack(ProductCategories.ELECTRONICS) }
        binding.productCategoryMensClothing.setOnClickListener { selectCategoryAndNavigateBack(ProductCategories.MENS_CLOTHING) }
        binding.productCategoryWomensClothing.setOnClickListener { selectCategoryAndNavigateBack(ProductCategories.WOMENS_CLOTHING) }
    }

    private fun selectCategoryAndNavigateBack(categoryName: ProductCategories) {
        productViewModel.filterProductList(categoryName)
        findNavController().popBackStack()
    }
}