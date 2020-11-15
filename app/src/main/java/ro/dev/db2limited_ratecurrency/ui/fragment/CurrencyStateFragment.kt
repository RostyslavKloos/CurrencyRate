package ro.dev.db2limited_ratecurrency.ui.fragment

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import ro.dev.db2limited_ratecurrency.R
import ro.dev.db2limited_ratecurrency.data.model.responseNBU.CurrencyResponseNBU
import ro.dev.db2limited_ratecurrency.data.model.responsePBbyDate.ExchangeRate
import ro.dev.db2limited_ratecurrency.databinding.CurrencyStateFragmentBinding
import ro.dev.db2limited_ratecurrency.ui.viewmodel.CurrencyStateViewModel
import ro.dev.db2limited_ratecurrency.utills.*
import java.util.*

class CurrencyStateFragment : Fragment() {
    private var _binding: CurrencyStateFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CurrencyStateViewModel by viewModels()
    private lateinit var viewColorStatePB: View
    private lateinit var viewColorStateNBU: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = CurrencyStateFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservers()
    }

    private fun setupObservers() {

        //Check for data refreshing
        viewModel.getIsRefreshingDataPB().observe(viewLifecycleOwner, {isRefreshPB ->
            viewModel.getIsRefreshingDataNBU().observe(viewLifecycleOwner, {
                binding.swipeContainer.isRefreshing = isRefreshPB
            })
        })

        //Observe privat bank state
        viewModel.getResponsePB().observe(viewLifecycleOwner, {
            val response = CurrencyMapper().mapFrom(it.exchangeRate)
            addTablePB(response)
        })

        //Observe NBU state
        viewModel.getResponseNBU().observe(viewLifecycleOwner, {
            addTableNBU(it)
        })

        // response from Privat Bank
        viewModel.currencyResponsePBbyDate.observe(viewLifecycleOwner, { responsePB ->
            when (responsePB.status) {
                Resource.Status.SUCCESS -> {
                    responsePB.data?.let {
                        viewModel.saveResponsePB(it)
                        binding.tvDatePB.text = it.date
                        binding.pbProgressBarPB.visibility = View.GONE
                        binding.tlPrivatBank.visibility = View.VISIBLE
                    }
                }
                Resource.Status.LOADING -> {
                    viewModel.isRefreshingLiveDataPB.value = false
                    binding.tvHintPB.visibility = View.GONE
                    binding.pbProgressBarPB.visibility = View.VISIBLE
                }
                Resource.Status.ERROR -> {
                    binding.pbProgressBarPB.visibility = View.GONE
                    binding.tlPrivatBank.visibility = View.VISIBLE
                    binding.tvHintPB.visibility = View.VISIBLE
                    viewModel.isRefreshingLiveDataPB.value = false
                    Toast.makeText(requireContext(), "${responsePB.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        //response from NBU
        viewModel.currencyResponseNBU.observe(viewLifecycleOwner, { responseNBU ->
            when (responseNBU.status) {
                Resource.Status.SUCCESS -> {
                    responseNBU.data?.let {
                        viewModel.saveResponseNBU(it)
                        binding.tvDateNBU.text = it[0].exchangedate
                        binding.pbProgressBarNBU.visibility = View.GONE
                        binding.svScrollViewNBU.visibility = View.VISIBLE
                        viewModel.isRefreshingLiveDataNBU.value = false
                    }
                }
                Resource.Status.LOADING -> {
                    viewModel.isRefreshingLiveDataNBU.value = false
                    binding.tvHintNBU.visibility = View.GONE
                    binding.pbProgressBarNBU.visibility = View.VISIBLE
                }
                Resource.Status.ERROR -> {
                    binding.tlNationalBank.visibility = View.VISIBLE
                    binding.pbProgressBarNBU.visibility = View.GONE
                    binding.tvHintNBU.visibility = View.VISIBLE
                    viewModel.isRefreshingLiveDataNBU.value = false
                    Toast.makeText(requireContext(), "${responseNBU.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun setupViews() {
        binding.swipeContainer.setOnRefreshListener {
            val currentDateTimeNBU = getDateFormatNBU(Date())
            val currentDateTimePB = getDateFormanPB(Date())
            viewModel.setDateNBU(currentDateTimeNBU)
            viewModel.setDatePB(currentDateTimePB)
        }

        // init temporary views for color replace
        viewColorStatePB = binding.tvViewColorStatePB
        viewColorStateNBU = binding.tvViewColorStateNBU

        // Create datePicker NBU
        binding.ibDatePickerNBU.setOnClickListener {
            createDatePickerDialog(it)
        }

        // Create datePicker PB
        binding.ibDatePickerPB.setOnClickListener {
            createDatePickerDialog(it)
        }
    }

    private fun createDatePickerDialog(view: View) {
        val materialDateBuilder = MaterialDatePicker.Builder.datePicker()
        materialDateBuilder.setTitleText(getString(R.string.select_date))
        val materialDatePicker = materialDateBuilder.build()

        materialDatePicker.show(childFragmentManager, getString(R.string.material_date_picker))
        materialDatePicker.addOnPositiveButtonClickListener {

            //delete current items if exist
            if (view == binding.ibDatePickerPB) {
                checkPBTableChildCount()
                viewModel.setDatePB(getDateFormanPB(it))
            } else if (view == binding.ibDatePickerNBU) {
                viewModel.setDateNBU(getDateFormatNBU(it))
            }
            viewColorStatePB.setBackgroundColor(Color.WHITE)
        }
    }

    // When PB item selected change current & temporary view
    private fun privatBankItemSelected(ccy: String, view: View) {
        viewColorStatePB.setBackgroundColor(Color.WHITE)
        view.setBackgroundColor(Color.LTGRAY)
        viewColorStatePB = view

        if (Build.VERSION.SDK_INT >= 29) {
            view.focusable = View.FOCUSABLE
            view.requestFocus()
            if (view.isFocusable) {
                view.setBackgroundColor(Color.GRAY)
            }
        }
        findItemNBU(ccy)
    }

    // Find selected view from PB table and transform view from NBU
    private fun findItemNBU(ccy: String) {
        viewColorStateNBU.setBackgroundColor(Color.WHITE)
        when (ccy) {
            "USD" -> {
                val view:View? = activity?.findViewById(R.id.tv_usd)
                view?.let {
                    transformView(it)
                }
            }
            "EUR" -> {
                val view: View? = activity?.findViewById(R.id.tv_eur)
                view?.let {
                    transformView(it)
                }
            }
            "RUB" -> {
                val view: View? = activity?.findViewById(R.id.tv_rur)
                view?.let {
                    transformView(it)
                }
            }
        }
    }

    // Dynamically create NBU table
    private fun addTableNBU(responseNBU: CurrencyResponseNBU) {
        binding.tlNationalBank.removeAllViews()
        for (i in responseNBU.indices) {
            val tableRow = TableRow(requireContext())

            // Create currency name TextView
            val tv1 = TextView(requireContext())
            tv1.text = responseNBU[i].txt
            tv1.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            setTextViewParams(tv1)

            //check for necessary currency
            when (responseNBU[i].txt) {
                getString(R.string.USD) -> tableRow.id = R.id.tv_usd
                getString(R.string.EUR) -> tableRow.id = R.id.tv_eur
                getString(R.string.RUB) -> tableRow.id = R.id.tv_rur
            }

            tableRow.addView(tv1)

            // Create currency rate TextView
            val tv2 = TextView(requireContext())
            tv2.text = roundDoubleTo(responseNBU[i].rate, 2)
            tv2.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
            setTextViewParams(tv2)

            tableRow.addView(tv2)

            binding.tlNationalBank.addView(tableRow)
        }
    }

    // Dynamically create NBU table
    private fun addTablePB(responsePB: List<ExchangeRate>) {
        checkPBTableChildCount()

        for (i in responsePB.indices) {
            val tableRow = TableRow(requireContext())

            // Create currency name TextView
            val tv1 = TextView(requireContext())
            tv1.text = responsePB[i].currency
            tv1.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.table_currency_pb_size)
            )
            setTextViewParams(tv1)

            tableRow.addView(tv1)

            // Create currency purchase rate TextView
            val tv2 = TextView(requireContext())
            tv2.text = roundDoubleTo(responsePB[i].purchaseRate, 3)
            tv2.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.table_currency_pb_size)
            )
            setTextViewParams(tv2)

            tableRow.addView(tv2)

            // Create currency sale rate TextView
            val tv3 = TextView(requireContext())
            tv3.text = roundDoubleTo(responsePB[i].saleRate, 3)
            tv3.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.table_currency_pb_size)
            )
            setTextViewParams(tv3)

            tableRow.addView(tv3)

            tableRow.setOnClickListener {
                privatBankItemSelected(responsePB[i].currency, it)
            }

            binding.tlPrivatBank.addView(tableRow)
        }
    }

    private fun setTextViewParams(textView: TextView) {
        textView.apply {
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
            layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1f
            )
        }
    }

    // Shows equal NBU currency to selected PB row
    private fun transformView(view: View) {
        view.setBackgroundColor(Color.LTGRAY)
        viewColorStateNBU = view
        binding.svScrollViewNBU.scrollTo(0, view.top)
        if (Build.VERSION.SDK_INT >= 29) {
            binding.svScrollViewNBU.scrollToDescendant(view)
            view.setBackgroundColor(Color.LTGRAY)
        }
    }

    private fun checkPBTableChildCount() {
        if (binding.tlPrivatBank.childCount > 4) {
            binding.tlPrivatBank.removeViews(4, 3)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            try {
                val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false)
                }
                ft.detach(this).attach(this).commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}