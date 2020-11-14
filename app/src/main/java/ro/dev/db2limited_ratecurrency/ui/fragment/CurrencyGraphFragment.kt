package ro.dev.db2limited_ratecurrency.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import ro.dev.db2limited_ratecurrency.databinding.CurrencyGraphFragmentBinding
import ro.dev.db2limited_ratecurrency.ui.viewmodel.CurrencyGraphViewModel
import ro.dev.db2limited_ratecurrency.utills.Resource
import ro.dev.db2limited_ratecurrency.utills.getDateFormatNBU
import ro.dev.db2limited_ratecurrency.utills.roundDoubleTo
import java.util.*

class CurrencyGraphFragment: Fragment() {

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

    private fun setupObservers() {
        viewModel.setDateNBU(getDateFormatNBU(Date()))
        viewModel.currencyResponseNBUbyCode.observe(viewLifecycleOwner, { responseNBUbyCode ->
            when (responseNBUbyCode.status) {
                Resource.Status.SUCCESS -> {
                    responseNBUbyCode.data?.let {
                        binding.test.text = roundDoubleTo(it[0].rate, 2)
                        binding.pbProgressBarGraph.visibility = View.GONE
                        binding.gvGraph.visibility = View.VISIBLE
                    }
                }
                Resource.Status.LOADING -> {
                    binding.pbProgressBarGraph.visibility = View.VISIBLE
                }
                Resource.Status.ERROR -> {
                    binding.gvGraph.visibility = View.VISIBLE
                    binding.pbProgressBarGraph.visibility = View.GONE
                    Toast.makeText(requireContext(), "${responseNBUbyCode.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupViews() {
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            arrayOf(
                DataPoint(1.0, 27.5),
                DataPoint(2.0, 30.0),
                DataPoint(3.0, 29.0),
                DataPoint(4.0, 28.5),
                DataPoint(5.0, 27.0),
                DataPoint(6.0, 29.1),
                DataPoint(7.0, 29.1)
                )
        )
        binding.gvGraph.addSeries(series)
    }
}