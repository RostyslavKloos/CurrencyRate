package ro.dev.db2limited_ratecurrency.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import ro.dev.db2limited_ratecurrency.R
import ro.dev.db2limited_ratecurrency.data.model.responseCBR.DateParamsCBR
import ro.dev.db2limited_ratecurrency.databinding.CurrencyGraphFragmentBinding
import ro.dev.db2limited_ratecurrency.ui.viewmodel.CurrencyGraphViewModel
import ro.dev.db2limited_ratecurrency.utills.*

class CurrencyGraphFragment : Fragment() {

    private var _binding: CurrencyGraphFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CurrencyGraphViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CurrencyGraphFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        binding.etDateStart.setOnClickListener {
            createDatePickerDialog(it)
        }

        binding.etDateEnd.setOnClickListener {
            createDatePickerDialog(it)
        }

    }

    private fun createDatePickerDialog(view: View) {
        val materialDateBuilder = MaterialDatePicker.Builder.datePicker()
        materialDateBuilder.setTitleText(getString(R.string.select_date))
        val materialDatePicker = materialDateBuilder.build()

        materialDatePicker.show(childFragmentManager, getString(R.string.material_date_picker))
        materialDatePicker.addOnPositiveButtonClickListener {

            if (view == binding.etDateStart) {
                val date = getDateFormatCBR(it)
                viewModel.setStartDate(date)
                binding.etDateStart.setText(date)
            } else if (view == binding.etDateEnd) {
                val date = getDateFormatCBR(it)
                viewModel.setEndDate(date)
                binding.etDateEnd.setText(date)
            }
        }
    }

    private fun setupObservers() {

        // When both data selected, set params
        viewModel.startDate.observe(viewLifecycleOwner, { startDate ->
            viewModel.endDate.observe(viewLifecycleOwner, {endDAte ->
                viewModel.setDateParamsCBR(DateParamsCBR(startDate, endDAte))
            })
        })

        viewModel.currencyResponseCBR.observe(viewLifecycleOwner, { responseCBR ->
            when (responseCBR.status) {
                Resource.Status.SUCCESS -> {
                    responseCBR.data?.let { valCurs ->
                        valCurs.Record?.let {
                            val mappedArray = CurrencyMapperCBR().mapFrom(it)
                            createGraph(mappedArray)
                            binding.pbProgressBarGraph.visibility = View.GONE
                            binding.gvGraph.visibility = View.VISIBLE
                            binding.tvGraphHint.visibility = View.GONE
                        }
                    }
                }
                Resource.Status.LOADING -> {
                    binding.pbProgressBarGraph.visibility = View.VISIBLE
                    binding.tvGraphHint.visibility = View.GONE
                }
                Resource.Status.ERROR -> {
                    binding.pbProgressBarGraph.visibility = View.GONE
                    binding.gvGraph.visibility = View.GONE
                    binding.tvGraphHint.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "${responseCBR.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun createGraph(array: Array<DataPoint>) {

        binding.gvGraph.removeAllSeries()

        val series: LineGraphSeries<DataPoint> = LineGraphSeries(array)

        series.setOnDataPointTapListener { _, dataPoint ->
            Toast.makeText(
                requireContext(), "currency value: ${dataPoint.y} on Data: ${getDateGraph(dataPoint.x)}", Toast.LENGTH_SHORT
            ).show()
        }
        binding.gvGraph.addSeries(series)

        binding.gvGraph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                return if (isValueX) {
                    return getDateGraph(value)
                } else {
                    super.formatLabel(value, isValueX)
                }
            }
        }

        binding.gvGraph.gridLabelRenderer.numHorizontalLabels = 5
        binding.gvGraph.viewport.isScalable = true  // activate horizontal zooming and scrolling
        binding.gvGraph.viewport.isScrollable = true  // activate horizontal scrolling
        binding.gvGraph.viewport.setScalableY(true)  // activate horizontal and vertical zooming and scrolling
        binding.gvGraph.viewport.setScrollableY(true)
    }
}