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
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import ro.dev.db2limited_ratecurrency.R
import ro.dev.db2limited_ratecurrency.data.model.responseNBU.CurrencyResponseNBU
import ro.dev.db2limited_ratecurrency.data.model.responsePBbyDate.ExchangeRate
import ro.dev.db2limited_ratecurrency.databinding.CurrencyStateFragmentBinding
import ro.dev.db2limited_ratecurrency.ui.viewmodel.CurrencyStateViewModel
import ro.dev.db2limited_ratecurrency.utills.*
import java.text.SimpleDateFormat
import java.util.*

class CurrencyStateFragment : Fragment(){
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
        // set currentDate
        val currentDateTimeNBU = getCurrentDateTime()
        val currentDateTimePB = getCurrentDateTimePB()
        viewModel.setDateNBU(currentDateTimeNBU)
        viewModel.setDatePB(currentDateTimePB)

        // response from Privat Bank
        viewModel.currencyResponsePBbyDate.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    it.data?.let {
                        val response = CurrencyMapper().mapFrom(it.exchangeRate)
                        addTablePB(response)
                        binding.tvDatePB.text = it.date
                        binding.pbProgressBarPB.visibility = View.GONE
                        binding.tlPrivatBank.visibility = View.VISIBLE
                    }
                }
                Resource.Status.LOADING -> {
                    binding.pbProgressBarPB.visibility = View.VISIBLE
                }
                Resource.Status.ERROR -> {
                    binding.tlPrivatBank.visibility = View.VISIBLE
                    binding.pbProgressBarPB.visibility = View.GONE
                    Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        //response from NBU
        viewModel.currencyResponseNBU.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    it.data?.let {
                        addTableNBU(it)
                        binding.tvDateNBU.text = it[0].exchangedate
                        binding.pbProgressBarNBU.visibility = View.GONE
                        binding.svScrollViewNBU.visibility = View.VISIBLE
                    }
                }
                Resource.Status.LOADING -> {
                    binding.pbProgressBarNBU.visibility = View.VISIBLE
                }
                Resource.Status.ERROR -> {
                    binding.tlNationalBank.visibility = View.VISIBLE
                    binding.pbProgressBarNBU.visibility = View.GONE
                    Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupViews() {
        // init temporary views for color replace
        viewColorStatePB = binding.tvViewColorStatePB
        viewColorStateNBU = binding.tvViewColorStateNBU

        val materialDateBuilderNBU = MaterialDatePicker.Builder.datePicker()
        materialDateBuilderNBU.setTitleText("ВЫБЕРИТЕ ДАТУ")
        val materialDatePickerNBU = materialDateBuilderNBU.build()

        // Create datePicker NBU
        binding.ibDatePickerNBU.setOnClickListener {
            materialDatePickerNBU.show(childFragmentManager, "MATERIAL_DATE_PICKER")

            materialDatePickerNBU.addOnPositiveButtonClickListener {
                val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.ROOT)
                val formatDate = dateFormat.format(it)
                viewModel.setDateNBU(formatDate)
            }
        }

        val materialDateBuilderPB = MaterialDatePicker.Builder.datePicker()
        materialDateBuilderPB.setTitleText("ВЫБЕРИТЕ ДАТУ")
        val materialDatePickerPB = materialDateBuilderPB.build()
        // Create datePicker PB
        binding.ibDatePickerPB.setOnClickListener {
            materialDatePickerPB.show(childFragmentManager, "MATERIAL_DATE_PICKER")

            materialDatePickerPB.addOnPositiveButtonClickListener {
                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
                val formatDate = dateFormat.format(it)
                //delete current items if exist
                if (binding.tlPrivatBank.childCount > 4)
                {
                    binding.tlPrivatBank.removeViews(3,3)
                }
                viewModel.setDatePB(formatDate)
            }
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
                view.setBackgroundColor(Color.RED)
            }
        }
        findItemNBU(ccy)
    }

    // Find selected view from PB table and transform view from NBU
    private fun findItemNBU(ccy: String) {
        viewColorStateNBU.setBackgroundColor(Color.WHITE )
        when (ccy) {
            "USD" -> {
                val view = activity?.findViewById(R.id.tv_usd) as TableRow
                transformView(view)
            }
            "EUR" -> {
                val view = activity?.findViewById(R.id.tv_eur) as TableRow
                transformView(view)
            }
            "RUB" -> {
                val view = activity?.findViewById(R.id.tv_rur) as TableRow
                transformView(view)
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
            setTextViewParams(tv1)

            //check for necessary currency
            when (responseNBU[i].txt) {
                "Долар США" -> tableRow.id = R.id.tv_usd
                "Євро" -> tableRow.id = R.id.tv_eur
                "Російський рубль" -> tableRow.id = R.id.tv_rur
            }

            tableRow.addView(tv1)

            // Create currency rate TextView
            val tv2 = TextView(requireContext())
            tv2.text = roundDoubleTo(responseNBU[i].rate, 2)
            setTextViewParams(tv2)

            tableRow.addView(tv2)

            binding.tlNationalBank.addView(tableRow)
        }
    }

    // Dynamically create NBU table
    private fun addTablePB(responsePB: List<ExchangeRate>) {

        for (i in responsePB.indices) {
            val tableRow = TableRow(requireContext())

            // Create currency name TextView
            val tv1 = TextView(requireContext())
            tv1.text = responsePB[i].currency
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.table_currency_pb_size))
            setTextViewParams(tv1)

            tableRow.addView(tv1)

            // Create currency purchase rate TextView
            val tv2 = TextView(requireContext())
            tv2.text = roundDoubleTo(responsePB[i].purchaseRate, 3)
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.table_currency_pb_size))
            setTextViewParams(tv2)

            tableRow.addView(tv2)

            // Create currency sale rate TextView
            val tv3 = TextView(requireContext())
            tv3.text = roundDoubleTo(responsePB[i].saleRate, 3)
            tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.table_currency_pb_size))
            setTextViewParams(tv3)
            tableRow.addView(tv3)

            tableRow.setOnClickListener {
                privatBankItemSelected(responsePB[i].currency, it)
            }

            binding.tlPrivatBank.addView(tableRow)
        }
    }

    private fun setTextViewParams(textView: TextView) {
        val params = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        textView.setTextColor(Color.BLACK)
        textView.gravity = Gravity.CENTER
        textView.layoutParams = params
    }


    // Shows equal NBU currency to selected PB row
    private fun transformView(view: View){
        view.setBackgroundColor(Color.LTGRAY)
        viewColorStateNBU = view
        binding.svScrollViewNBU.scrollTo(0, view.top)
        if (Build.VERSION.SDK_INT >= 29) {
            binding.svScrollViewNBU.scrollToDescendant(view)
            view.setBackgroundColor(Color.LTGRAY)
        }
    }

    // Checks the orientation of the screen
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        with(binding) {

            val llParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            val tlParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {


                llMainLinearLayout.orientation = LinearLayout.HORIZONTAL
                llLinearLayoutNBU.layoutParams = LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
                    1f
                )
                tlPrivatBank.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                svScrollViewNBU.layoutParams = llParams
                tlParams.setMargins(20, 0, 0, 0)
                tlNationalBankHeader?.layoutParams = tlParams
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                llMainLinearLayout.orientation = LinearLayout.VERTICAL
                llLinearLayoutNBU.layoutParams = llParams
                tlPrivatBank.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
                svScrollViewNBU.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
            }
        }
    }
}