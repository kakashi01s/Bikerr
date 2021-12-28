package com.firefly.bikerr.navigation.community.map.bikerr.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firefly.bikerr.navigation.community.map.bikerr.R
import com.firefly.bikerr.navigation.community.map.bikerr.adapters.SparePartsAdapter
import com.firefly.bikerr.navigation.community.map.bikerr.base.BaseFragment
import com.firefly.bikerr.navigation.community.map.bikerr.databinding.FragmentShopBinding
import com.firefly.bikerr.navigation.community.map.bikerr.ui.CartActivity
import com.firefly.bikerr.navigation.community.map.bikerr.ui.ShopActivity
import com.firefly.bikerr.navigation.community.map.bikerr.ui.WebActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.firestore.FirebaseFirestore


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShopFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShopFragment : BaseFragment(), SparePartsAdapter.SparePartsitemClickListener{
    // TODO: Rename and change types of parameters
    private var param1: Int? = null
    private var param2: String? = null

    var db = FirebaseFirestore.getInstance()
    var binding : FragmentShopBinding? = null
    var rvspareparts : RecyclerView? = null
    var sparePartsAdapter : SparePartsAdapter? = null
    var rvcategoryStores : RecyclerView? = null
    private var shopBannerAD1 : AdView? = null



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
        binding = FragmentShopBinding.inflate(inflater, container,false)
        // Inflate the layout for this fragment
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        getSpareParts()
        LoadAds()

        binding!!.openCart.setOnClickListener {
            val intent = Intent(activity , CartActivity::class.java)
            startActivity(intent)
        }
        binding!!.cardAccessories.setOnClickListener {
            val intent = Intent(activity, ShopActivity::class.java)
            intent.putExtra("shopItem", "Accessories")
            startActivity(intent)
        }
        binding!!.cardGPS.setOnClickListener {
            Toast.makeText(requireContext(), "Coming Soon!", Toast.LENGTH_SHORT).show()
        }
        binding!!.cardGears.setOnClickListener {
            val intent = Intent(activity, ShopActivity::class.java)
            intent.putExtra("shopItem", "Gear")
            startActivity(intent)
        }
        binding!!.cardHelmet.setOnClickListener {
            val intent = Intent(activity, ShopActivity::class.java)
            intent.putExtra("shopItem", "Helmets")
            startActivity(intent)
        }
        binding!!.cardMerchandise.setOnClickListener {
            val intent = Intent(activity, ShopActivity::class.java)
            intent.putExtra("shopItem", "Merchandise")
            startActivity(intent)
        }
        binding!!.cardLeather.setOnClickListener {
            val intent = Intent(activity, ShopActivity::class.java)
            intent.putExtra("shopItem", "Leather")
            startActivity(intent)
        }

    }
    private fun initViews(view: View) {
        rvspareparts = view.findViewById(R.id.rvSpareParts)
        shopBannerAD1 = view.findViewById(R.id.shopBanneradView1)


    }

    private fun LoadAds() {
        val adrequest = AdRequest.Builder().build()
        shopBannerAD1!!.loadAd(adrequest)


    }

    fun getSpareParts(){
        db.collection("SpareParts")
            .get()
            .addOnSuccessListener { result ->
                var shopitems = result.documents
                Log.d("TAG", shopitems.toString())
                sparePartsAdapter = SparePartsAdapter(requireContext(),shopitems , this)
                rvspareparts?.adapter = sparePartsAdapter
                rvspareparts?.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)
            }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentHome.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            ShopFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun OnSparePartsCardClick(position: Int, itemname: String, itemurl: String) {
        val intent: Intent? = Intent(activity, WebActivity::class.java)
        intent?.putExtra("title", position)
        intent?.putExtra("url", itemurl)
        startActivity(intent)
    }



}
