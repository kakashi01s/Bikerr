package com.firefly.bikerr.navigation.community.map.bikerr.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.firefly.bikerr.navigation.community.map.bikerr.base.BaseFragment
import com.firefly.bikerr.navigation.community.map.bikerr.ApiInterface.RetrofitClient
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.directions.DirectionResponses
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.maps.android.PolyUtil
import retrofit2.Call
import retrofit2.Response
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MapsFragment : BaseFragment() , OnMapReadyCallback , GoogleMap.OnMarkerClickListener {

    private var param1: Int? = null
    private var param2: String? = null
    var mapmarker: Marker? = null
    var maproute : com.google.android.gms.maps.model.Polyline? = null
    private val REQUEST_LOCATION_PERMISSION = 101
    private var mMap: GoogleMap? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var LastLocation: Location
    var navigationButton : Button? = null
    var placeinfo : CardView? = null
    var currentplace : TextView? = null
    var backbutton : ImageView? = null



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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.google_maps_key), Locale.US);
        }
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        placeinfo!!.visibility = View.INVISIBLE
        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
        )
        autocompleteFragment.setCountry("IN")
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: ${place.name}, ${place.id}")
                if (mapmarker != null) {
                    mapmarker!!.remove()
                }
                if (maproute != null)
                {
                    maproute!!.remove()
                }
                val name: String? = place.name
                val latLng = LatLng(place.latLng!!.latitude, place.latLng!!.longitude)
                val markerOptions = MarkerOptions().position(latLng).title("$name")
                mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                mMap!!.addMarker(markerOptions)

                getdir(place)

            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: $status")
            }
        })

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    private fun initViews(view: View) {
        navigationButton = view.findViewById(R.id.navigationButton)
        currentplace = view.findViewById(R.id.place_name)
        placeinfo = view.findViewById(R.id.placeinfo)
        backbutton = view.findViewById(R.id.back_button)

    }

    //gets directions
    private fun getdir(place: Place) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_LOCATION_PERMISSION)

            return
        }
        fusedLocationProviderClient!!.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null)
            {   val key = "AIzaSyBLFffJm0Zy2ZPE64RpUic2TG-_-hwY5kw"
                val origin = location.latitude.toString() + "," + location.longitude.toString()
                val destination = place.latLng!!.latitude.toString() + "," + place.latLng!!.longitude.toString()
                val apiServices = RetrofitClient.apiServices(requireContext()).getDirection(origin,destination,key)
                apiServices?.enqueue(object : retrofit2.Callback<DirectionResponses?>{
                    override fun onResponse(
                        call: Call<DirectionResponses?>,
                        response: Response<DirectionResponses?>
                    ) {
                        Log.d("dir", response.body().toString())
                        drawPolyline(response)
                        placeinfo!!.visibility = View.VISIBLE
                        currentplace?.setText(place.name)
                        navigationButton?.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${place.latLng!!.latitude},${place.latLng!!.longitude}&mode=l"))
                            intent.setPackage("com.google.android.apps.maps")
                            startActivity(intent)


                            backbutton!!.setOnClickListener {
                                placeinfo!!.visibility = View.INVISIBLE
                                currentplace?.setText("")
                                maproute?.remove()
                                setupMap()
                            }
                        }

                    }

                    override fun onFailure(call: Call<DirectionResponses?>, t: Throwable) {
                        Log.d("dir", t.localizedMessage)
                    }

                })
            }
        }

    }


    private fun drawPolyline(response: retrofit2.Response<DirectionResponses?>) {
        val shape = response.body()?.routes?.get(0)?.overviewPolyline?.points
        val polyline = PolylineOptions()
            .addAll(PolyUtil.decode(shape))
            .width(8f)
            .color(Color.RED)
       maproute = mMap!!.addPolyline(polyline)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.uiSettings.isZoomControlsEnabled = true
        mMap!!.setOnMarkerClickListener(this)
        setupMap()

    }


    private fun setupMap() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
         {
             ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_LOCATION_PERMISSION)

            return
        }
        mMap!!.isMyLocationEnabled = true
        fusedLocationProviderClient!!.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null)
            {
                LastLocation = location
                val currentLatLng = LatLng(location.latitude,location.longitude)
                placeMarkeronMap(currentLatLng)
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,15f))
                }

        }

    }

    private fun placeMarkeronMap(currentLatLng: LatLng) {
    val markerOptions = MarkerOptions().position(currentLatLng)
        markerOptions.title("You're Here! $currentLatLng")
       mapmarker = mMap?.addMarker(markerOptions)

    }
    override fun onMarkerClick(p0: Marker) = false




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MapsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            MapsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



}


