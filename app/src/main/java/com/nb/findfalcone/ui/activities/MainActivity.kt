package com.nb.findfalcone.ui.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nb.findfalcone.R
import com.nb.findfalcone.api.Status
import com.nb.findfalcone.databinding.ActivityMainBinding
import com.nb.findfalcone.model.FalconeRequest
import com.nb.findfalcone.model.Planet
import com.nb.findfalcone.model.Vehicle
import com.nb.findfalcone.model.VehicleToPlanet
import com.nb.findfalcone.ui.custom.CustomSpinnerAdapter
import com.nb.findfalcone.ui.custom.FalconeListAdapter
import com.nb.findfalcone.ui.utils.getPlanetImage
import com.nb.findfalcone.ui.utils.isNetworkAvailable
import com.nb.findfalcone.viewmodel.FalconeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val falconeViewModel: FalconeViewModel by viewModels()
    private lateinit var adapterFalcone: FalconeListAdapter

    private var adapterSpinnerVehicle: CustomSpinnerAdapter? = null
    private var adapterSpinnerPlanet: CustomSpinnerAdapter? = null

    private var tempPlanet: Planet? = null
    private var tempVehicle: Vehicle? = null

    private var timeTaken: Int = 0

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setOnExitAnimationListener {
                setContentView(binding.root)
                dialog = Dialog(this@MainActivity)

                Handler(mainLooper).postDelayed(
                    {
                        if (isNetworkAvailable()) {
                            it.remove()
                            initialize()
                            initListeners()
                        } else {
                            showResponseDialog(getString(R.string.no_internet_please_check_your_network_and_try_again),
                                getPlanetImage(""),
                                getString(R.string.cancel), isCancellable = false
                            ) {
                                finish()
                            }
                        }
                    }, 2000
                )
            }
        }
    }

    private fun initialize() {
        setTime(0)
        falconeViewModel.vehicleToPlanetMutable.observe(this) {
            val ls = falconeViewModel.vehicleToPlanet
            val vPlanetCount = ls.count { vp -> (it.planet?.name == vp.planet?.name) }
            val vehicleCount = ls.count { vp -> (it.vehicle?.name == vp.vehicle?.name) }

            if (ls.size >= 4) {
                showResponseDialog(
                    getString(R.string.king_we_cannot_send_more_than_4_vehicles),
                    getPlanetImage(it.planet?.name), getString(R.string.terminate), isCancellable = false
                )
            } else if (vPlanetCount != 0 || vehicleCount >= (it.vehicle?.totalNo ?: 0)) {
                showResponseDialog(
                    getString(
                        R.string.king_we_cannot_send_this_ship_to_planet,
                        it.vehicle?.name,
                        it.planet?.name
                    ),
                    getPlanetImage(it.planet?.name),getString(R.string.terminate), isCancellable = false
                )
            } else if ((it.vehicle?.maxDistance ?: 0) < (it.planet?.distance ?: 0)) {
                Log.d(TAG, "sendVehicleToPlanet: distance not covered $it")
                showResponseDialog(
                    getString(
                        R.string.hey_king_cannot_cover_the_distance_of,
                        it.vehicle?.name,
                        it.planet?.name
                    ),
                    getPlanetImage(it.planet?.name),getString(R.string.terminate), isCancellable = false
                )
            } else {
                Log.d(TAG, "sendVehicleToPlanet: distance covered $it")
                setTime((it.planet?.distance ?: 0).div(it.vehicle?.speed ?: 0))
                falconeViewModel.vehicleToPlanet.add(it)
                adapterFalcone.apply {
                    submitList(falconeViewModel.vehicleToPlanet)
                    notifyDataSetChanged()
                }
                binding.planetSpinner.setSelection(0)
                binding.vehicleSpinner.setSelection(0)
                tempPlanet = null
                tempVehicle = null
            }

        }

        falconeViewModel.planetsLiveData.observe(this) {
            if (it.isNullOrEmpty().not()) {
                val ls = it.map { it.name }.toMutableList()
                ls.add(0, "Select")
                adapterSpinnerPlanet = CustomSpinnerAdapter(this,ls)
                binding.planetSpinner.adapter = adapterSpinnerPlanet
            }
        }

        falconeViewModel.vehiclesLiveData.observe(this) {
            if (it.isNullOrEmpty().not()) {
                val ls = it.map { it.name }.toMutableList()
                ls.add(0,"Select")
                adapterSpinnerVehicle = CustomSpinnerAdapter(this, ls)
                binding.vehicleSpinner.adapter = adapterSpinnerVehicle
            }
        }

    }

    private fun initListeners() {
        adapterFalcone = FalconeListAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterFalcone
        }

        lifecycleScope.launch {
            falconeViewModel.initialSetUp()
        }

        binding.planetSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                val pos = position - 1
                if (pos != -1 && falconeViewModel.planetsLiveData.value?.get(pos)?.name != "Select") {
                    Log.d(TAG, "onItemSelected1: $tempVehicle")
                    tempPlanet = falconeViewModel.planetsLiveData.value?.get(pos)
                    if (tempVehicle != null) {
                        falconeViewModel.vehicleToPlanetMutable.postValue(
                            VehicleToPlanet(tempPlanet, tempVehicle)
                        )
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.vehicleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                val pos = position - 1
                if (pos != -1 && falconeViewModel.vehiclesLiveData.value?.get(pos)?.name != "Select") {
                    Log.d(TAG, "onItemSelected2: $tempPlanet")
                    tempVehicle = falconeViewModel.vehiclesLiveData.value?.get(pos)
                    if (tempPlanet != null && tempVehicle?.name != "Select") {
                        falconeViewModel.vehicleToPlanetMutable.postValue(
                            VehicleToPlanet(tempPlanet, tempVehicle)
                        )
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.appCompatButton.setOnClickListener {
            if (isNetworkAvailable()) {
                findFalcone()
            } else {
                showResponseDialog(
                    getString(R.string.no_internet_please_check_your_network_and_try_again),
                    getPlanetImage(""),
                    getString(R.string.cancel),true,
                )
            }
        }

    }

    private fun setTime(time: Int) {
        timeTaken = timeTaken.plus(time)
        binding.tvTime.text = getString(R.string.str_time, timeTaken)
    }

    private fun findFalcone() {
        if (falconeViewModel.vehicleToPlanet.size == 4) {
            falconeViewModel.findFalcone(
                FalconeRequest("",
                    falconeViewModel.vehicleToPlanet.map { it.planet?.name },
                    falconeViewModel.vehicleToPlanet.map { it.vehicle?.name }
                )
            ).observe(this) {
                when (it.status) {
                    Status.LOADING -> {
                        showResponseDialog(
                            getString(R.string.please_wait_while_we_are_finding_the_queen),
                            getPlanetImage(it.data?.planetName),null, isCancellable = false
                        )
                    }

                    Status.SUCCESS -> {
                        if (it.data?.planetName.isNullOrEmpty().not()) {
                            showResponseDialog(
                                getString(
                                    R.string.king_we_found_the_queen_of_falicornia_she_is_hiding_in_planet,
                                    timeTaken, it.data?.planetName
                                ),
                                getPlanetImage(it.data?.planetName),
                                getString(R.string.start_again), isCancellable = false
                            ) {
                                resetAll()
                            }
                        } else {
                            showResponseDialog(
                                getString(R.string.king_we_regret_to_inform_you_that_we_failed_to_find_the_queen),
                                getPlanetImage(it.data?.planetName), getString(R.string.terminate), isCancellable = false
                            ) {
                                resetAll()
                            }
                        }
                    }

                    Status.ERROR -> {
                        showResponseDialog(
                            it.data?.error ?: it.message ?: "Something went wrong...",
                            getPlanetImage(""),getString(R.string.terminate), isCancellable = false
                        ) {
                            resetAll()
                        }
                    }
                }
            }
        } else {
            showResponseDialog(
                getString(R.string.please_select_4_vehicles),
                getPlanetImage(""),getString(R.string.terminate), isCancellable = false
            )
        }
    }

    private fun resetAll() {
        binding.planetSpinner.setSelection(0)
        binding.vehicleSpinner.setSelection(0)
        falconeViewModel.vehicleToPlanet.clear()
        timeTaken = 0
        setTime(0)
        adapterFalcone.apply {
            submitList(null)
            notifyDataSetChanged()
        }

    }

    private fun showResponseDialog(
        responseMsg: String,
        resPlanet: Int,
        btnText: String?,
        isCancellable: Boolean = true,
        action: (() -> Unit)? = null
    ) {

        if (dialog.isShowing) {
            dialog.dismiss()
        }

        dialog.setCancelable(isCancellable)
        dialog.setContentView(R.layout.popup_error_view)

        val ivPlanet: ImageView = dialog.findViewById(R.id.ivPlanet)
        ivPlanet.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                resPlanet,
                null
            )
        )

        val tvTitle: TextView = dialog.findViewById(R.id.tvDesc)
        tvTitle.text = responseMsg

        val btnOk: TextView = dialog.findViewById(R.id.btnCancel)
        btnText?.let { btnOk.text = it }
        btnOk.setOnClickListener {
            dialog.dismiss()
            action?.invoke()
        }

        dialog.show()
    }

}