package com.firefly.bikerr.navigation.community.map.bikerr.ui.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.firefly.bikerr.navigation.community.map.bikerr.ApiInterface.Placeservice
import com.firefly.bikerr.navigation.community.map.bikerr.base.BaseFragment
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.adapters.NearbyAdapter
import com.firefly.bikerr.navigation.community.map.bikerr.model.Nearby.Geometry
import com.firefly.bikerr.navigation.community.map.bikerr.model.Nearby.Places
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LastFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NearbyFragment : BaseFragment(), NearbyAdapter.NearbyitemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null
    private var param2: String? = null
    private var isLocationPermissionOk = false
    private var radius = 1500
    private var nearbyBannerAD : AdView? = null
    private var nearbyBannerAD2 : AdView? = null
    private var currentLocation: Location? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var rvfuel : RecyclerView? = null
    var nearbyadapter : NearbyAdapter? = null
    var fuel : CardView? = null
    var garage : CardView? = null
    var atm : CardView? = null
    var food : CardView? = null
    var medicine : CardView? = null
    var rental : CardView? = null
    var nearbyimg : ImageView? = null
    var nearbytext : TextView? = null
    override val bindingVariable: Int
        get() = 0
    override val layoutId: Int
        get() = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nearby, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        getcurrentloc()
        getnearbyPetrolpumps()

        fuel!!.setOnClickListener {
            getnearbyPetrolpumps()
            nearbyimg!!.load(R.drawable.fuel)
            nearbytext!!.text = "Fuel "
        }
        garage!!.setOnClickListener {
            getnearbygarages()
            nearbyimg!!.load(R.drawable.garage)
            nearbytext!!.text = "Garage "
        }
        atm!!.setOnClickListener {
            getnearbyatm()
            nearbyimg!!.load(R.drawable.atm)
            nearbytext!!.text = "ATM "
        }
        medicine!!.setOnClickListener {
            getnearbymedicine()
            nearbyimg!!.load(R.drawable.medicine)
            nearbytext!!.text = "Medicine "
        }
        rental!!.setOnClickListener {
            getnearbyrentals()
            nearbyimg!!.load(R.drawable.car)
            nearbytext!!.text = "Rentals "
        }
        food!!.setOnClickListener {
            getnearbyfood()
            nearbyimg!!.load(R.drawable.food)
            nearbytext!!.text = "Restaurants "
        }

        LoadAds()


    }
    private fun initViews(view: View) {
        rvfuel = view.findViewById(R.id.rv_fuel)
        nearbyBannerAD = view.findViewById(R.id.nearbyBanneradView1)
        fuel = view.findViewById(R.id.card_fuel)
        garage = view.findViewById(R.id.card_garage)
        food = view.findViewById(R.id.card_food)
        atm = view.findViewById(R.id.card_atm)
        medicine = view.findViewById(R.id.card_medicine)
        rental = view.findViewById(R.id.card_rentals)
        nearbyimg = view.findViewById(R.id.Nearby_Img)
        nearbytext = view.findViewById(R.id.Nearby_Text)
    }


    private fun LoadAds() {
        val adrequest = AdRequest.Builder().build()
        nearbyBannerAD!!.loadAd(adrequest)
    }




    private fun getcurrentloc() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            isLocationPermissionOk = false
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            currentLocation = location
            Log.d("currentloc" ,currentLocation.toString())
            Log.d("currentloc" ,currentLocation?.latitude.toString())
            Log.d("currentloc" ,currentLocation?.longitude.toString())
            val lat = currentLocation?.latitude.toString()
            val lon = currentLocation?.longitude.toString()
            val latlngcurrent ="$lat,$lon"


        }
    }
    private fun getnearbyfood() {
        LoadAds()
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            isLocationPermissionOk = false
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            currentLocation = location
            Log.d("currentloc" ,currentLocation.toString())
            Log.d("currentloc" ,currentLocation?.latitude.toString())
            Log.d("currentloc" ,currentLocation?.longitude.toString())
            val lat = currentLocation?.latitude.toString()
            val lon = currentLocation?.longitude.toString()
            val latlngcurrent ="$lat,$lon"
            val url = ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$latlngcurrent&rankby=distance&type=restaurant&key=AIzaSyBLFffJm0Zy2ZPE64RpUic2TG-_-hwY5kw")
            val nearby = Placeservice.Placesinstance.getNearbyPlaces(url)
            nearby.enqueue(object : retrofit2.Callback<Places>{
                override fun onResponse(call: Call<Places>, response: Response<Places>) {
                    val places = response.body()
                    Log.d("fuel",places.toString())
                    nearbyadapter = NearbyAdapter(context!!, places!!.results, this@NearbyFragment)
                    rvfuel?.adapter = nearbyadapter
                    rvfuel?.layoutManager = LinearLayoutManager(context!!)

                }

                override fun onFailure(call: Call<Places>, t: Throwable) {
                    Log.d("fuel",t.localizedMessage)
                }
            })


        }
    }

    private fun getnearbyrentals() {
        LoadAds()
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            isLocationPermissionOk = false
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            currentLocation = location
            Log.d("currentloc" ,currentLocation.toString())
            Log.d("currentloc" ,currentLocation?.latitude.toString())
            Log.d("currentloc" ,currentLocation?.longitude.toString())
            val lat = currentLocation?.latitude.toString()
            val lon = currentLocation?.longitude.toString()
            val latlngcurrent ="$lat,$lon"
            val url = ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$latlngcurrent&rankby=distance&type=car_rental&key=AIzaSyBLFffJm0Zy2ZPE64RpUic2TG-_-hwY5kw")
            val nearby = Placeservice.Placesinstance.getNearbyPlaces(url)
            nearby.enqueue(object : retrofit2.Callback<Places>{
                override fun onResponse(call: Call<Places>, response: Response<Places>) {
                    val places = response.body()
                    Log.d("fuel",places.toString())
                    nearbyadapter = NearbyAdapter(context!!, places!!.results, this@NearbyFragment)
                    rvfuel?.adapter = nearbyadapter
                    rvfuel?.layoutManager = LinearLayoutManager(context!!)

                }

                override fun onFailure(call: Call<Places>, t: Throwable) {
                    Log.d("fuel",t.localizedMessage)
                }
            })


        }
    }

    private fun getnearbymedicine() {
        LoadAds()
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            isLocationPermissionOk = false
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            currentLocation = location
            Log.d("currentloc" ,currentLocation.toString())
            Log.d("currentloc" ,currentLocation?.latitude.toString())
            Log.d("currentloc" ,currentLocation?.longitude.toString())
            val lat = currentLocation?.latitude.toString()
            val lon = currentLocation?.longitude.toString()
            val latlngcurrent ="$lat,$lon"
            val url = ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$latlngcurrent&rankby=distance&type=pharmacy&key=AIzaSyBLFffJm0Zy2ZPE64RpUic2TG-_-hwY5kw")
            val nearby = Placeservice.Placesinstance.getNearbyPlaces(url)
            nearby.enqueue(object : retrofit2.Callback<Places>{
                override fun onResponse(call: Call<Places>, response: Response<Places>) {
                    val places = response.body()
                    Log.d("fuel",places.toString())
                    nearbyadapter = NearbyAdapter(context!!, places!!.results, this@NearbyFragment)
                    rvfuel?.adapter = nearbyadapter
                    rvfuel?.layoutManager = LinearLayoutManager(context!!)

                }

                override fun onFailure(call: Call<Places>, t: Throwable) {
                    Log.d("fuel",t.localizedMessage)
                }
            })


        }
    }

    private fun getnearbyatm() {
        LoadAds()
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            isLocationPermissionOk = false
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            currentLocation = location
            Log.d("currentloc" ,currentLocation.toString())
            Log.d("currentloc" ,currentLocation?.latitude.toString())
            Log.d("currentloc" ,currentLocation?.longitude.toString())
            val lat = currentLocation?.latitude.toString()
            val lon = currentLocation?.longitude.toString()
            val latlngcurrent ="$lat,$lon"
            val url = ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$latlngcurrent&rankby=distance&type=atm&key=AIzaSyBLFffJm0Zy2ZPE64RpUic2TG-_-hwY5kw")
            val nearby = Placeservice.Placesinstance.getNearbyPlaces(url)
            nearby.enqueue(object : retrofit2.Callback<Places>{
                override fun onResponse(call: Call<Places>, response: Response<Places>) {
                    val places = response.body()
                    Log.d("fuel",places.toString())
                    nearbyadapter = NearbyAdapter(context!!, places!!.results, this@NearbyFragment)
                    rvfuel?.adapter = nearbyadapter
                    rvfuel?.layoutManager = LinearLayoutManager(context!!)

                }

                override fun onFailure(call: Call<Places>, t: Throwable) {
                    Log.d("fuel",t.localizedMessage)
                }
            })


        }
    }
    private fun getnearbygarages() {
        LoadAds()
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            isLocationPermissionOk = false
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            currentLocation = location
            Log.d("currentloc" ,currentLocation.toString())
            Log.d("currentloc" ,currentLocation?.latitude.toString())
            Log.d("currentloc" ,currentLocation?.longitude.toString())
            val lat = currentLocation?.latitude.toString()
            val lon = currentLocation?.longitude.toString()
            val latlngcurrent ="$lat,$lon"
            val url = ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$latlngcurrent&rankby=distance&type=car_repair&key=AIzaSyBLFffJm0Zy2ZPE64RpUic2TG-_-hwY5kw")
            val nearby = Placeservice.Placesinstance.getNearbyPlaces(url)
            nearby.enqueue(object : retrofit2.Callback<Places>{
                override fun onResponse(call: Call<Places>, response: Response<Places>) {
                    val places = response.body()
                    Log.d("garage",places.toString())
                    nearbyadapter = NearbyAdapter(context!!, places!!.results, this@NearbyFragment)
                    rvfuel?.adapter = nearbyadapter
                    rvfuel?.layoutManager = LinearLayoutManager(context!!)
                }

                override fun onFailure(call: Call<Places>, t: Throwable) {
                    Log.d("garage",t.localizedMessage)
                }
            })


        }


    }

    private fun getnearbyPetrolpumps() {

        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            isLocationPermissionOk = false
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            currentLocation = location
            Log.d("currentloc" ,currentLocation.toString())
            Log.d("currentloc" ,currentLocation?.latitude.toString())
            Log.d("currentloc" ,currentLocation?.longitude.toString())
            val lat = currentLocation?.latitude.toString()
            val lon = currentLocation?.longitude.toString()
            val latlngcurrent ="$lat,$lon"
            val url = ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$latlngcurrent&rankby=distance&type=gas_station&key=AIzaSyBLFffJm0Zy2ZPE64RpUic2TG-_-hwY5kw")
            val nearby = Placeservice.Placesinstance.getNearbyPlaces(url)
            nearby.enqueue(object : retrofit2.Callback<Places>{
                override fun onResponse(call: Call<Places>, response: Response<Places>) {
                    val places = response.body()
                    Log.d("fuel",places.toString())
                    nearbyadapter = NearbyAdapter(context!!, places!!.results, this@NearbyFragment)
                    rvfuel?.adapter = nearbyadapter
                    rvfuel?.layoutManager = LinearLayoutManager(context!!)

                }

                override fun onFailure(call: Call<Places>, t: Throwable) {
                    Log.d("fuel",t.localizedMessage)
                }
            })


        }


    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LastFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            NearbyFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun OnNearbyCardClick(position: Int, geometry: Geometry) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${geometry.location.lat},${geometry.location.lng}&mode=l"))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }


}