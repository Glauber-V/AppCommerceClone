package com.example.appcommerceclone.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.databinding.FragmentCategoriesBinding
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_ELECTRONICS
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_JEWELRY
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_MENS_CLOTHING
import com.example.appcommerceclone.util.Constants.CATEGORY_NAME_WOMENS_CLOTHING
import com.example.appcommerceclone.viewmodels.ProductViewModel
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

        binding.productCategoryAll.setOnClickListener { selectCategoryAndNavigateBack("") }
        binding.productCategoryElectronics.setOnClickListener { selectCategoryAndNavigateBack(CATEGORY_NAME_ELECTRONICS) }
        binding.productCategoryJewelery.setOnClickListener { selectCategoryAndNavigateBack(CATEGORY_NAME_JEWELRY) }
        binding.productCategoryMensClothing.setOnClickListener { selectCategoryAndNavigateBack(CATEGORY_NAME_MENS_CLOTHING) }
        binding.productCategoryWomensClothing.setOnClickListener { selectCategoryAndNavigateBack(CATEGORY_NAME_WOMENS_CLOTHING) }
    }


    private fun selectCategoryAndNavigateBack(categoryName: String) {
        productViewModel.selectCategoryAndUpdateProductsList(categoryName)
        findNavController().navigateUp()
    }
}