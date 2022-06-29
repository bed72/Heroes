package github.bed72.bedapp.presentation.sort

import android.os.Bundle

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.core.view.forEach

import androidx.fragment.app.viewModels

import dagger.hilt.android.AndroidEntryPoint

import com.google.android.material.chip.Chip
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import github.bed72.bedapp.R

import github.bed72.core.domain.model.SortingType

import github.bed72.bedapp.databinding.FragmentSortBinding

import github.bed72.bedapp.presentation.sort.SortViewModel.States.Apply.Error
import github.bed72.bedapp.presentation.sort.SortViewModel.States.Apply.Loading
import github.bed72.bedapp.presentation.sort.SortViewModel.States.Apply.Success
import github.bed72.bedapp.presentation.sort.SortViewModel.States.SortingResult

@AndroidEntryPoint
class SortFragment : BottomSheetDialogFragment() {

    private val viewModel: SortViewModel by viewModels()

    private var _binding: FragmentSortBinding? = null
    private val binding: FragmentSortBinding get() = _binding!!

    private var orderBy = SortingType.ORDER_BY_NAME.value
    private var order = SortingType.ORDER_ASCENDING.value

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSortBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setChipGroupListeners()
        observerStates()
    }

    private fun setChipGroupListeners() {
        with (binding) {
            chipGroupOrderBy.setOnCheckedStateChangeListener { group, checkedIds ->
                checkedIds.forEach {
                    orderBy = getOrderByValue(group.findViewById<Chip>(it).id)
                }
            }

            chipGroupOrder.setOnCheckedStateChangeListener { group, checkedIds ->
                checkedIds.forEach {
                    order = getOrderValue(group.findViewById<Chip>(it).id)
                }
            }

            buttonApplySort.setOnClickListener {
                viewModel.applySorting(orderBy, order)
            }
        }
    }

    private fun observerStates() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SortingResult -> {
                    val orderBy = state.storedSorting.first
                    val order = state.storedSorting.second

                    with (binding) {
                        chipGroupOrderBy.forEach {
                            val chip = it as Chip
                            if (getOrderByValue(chip.id) == orderBy) chip.isChecked = true
                        }

                        chipGroupOrder.forEach {
                            val chip = it as Chip
                            if (getOrderValue(chip.id) == order) chip.isChecked = true
                        }
                    }
                }
                Error -> binding.flipperApply.displayedChild = FLIPPER_CHILD_BUTTON
                Loading -> binding.flipperApply.displayedChild = FLIPPER_CHILD_LOADING
                Success -> binding.flipperApply.displayedChild = FLIPPER_CHILD_BUTTON
            }
        }
    }

    private fun getOrderByValue(chipId: Int): String = when (chipId) {
        R.id.chip_name -> SortingType.ORDER_BY_NAME.value
        R.id.chip_modified -> SortingType.ORDER_BY_MODIFIED.value
        else -> SortingType.ORDER_BY_NAME.value
    }

    private fun getOrderValue(chipId: Int): String = when (chipId) {
        R.id.chip_ascending -> SortingType.ORDER_ASCENDING.value
        R.id.chip_descending -> SortingType.ORDER_DESCENDING.value
        else -> SortingType.ORDER_ASCENDING.value
    }

    companion object {
        private const val FLIPPER_CHILD_BUTTON = 0
        private const val FLIPPER_CHILD_LOADING = 1
    }
}