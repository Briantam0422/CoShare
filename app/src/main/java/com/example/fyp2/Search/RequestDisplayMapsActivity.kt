package com.example.fyp2.Search

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.fyp2.MAP.DisplayMapsActivity
import com.example.fyp2.MAP.ProductInformation
import com.example.fyp2.MAP.ShowProductInformation
import com.example.fyp2.R
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_display_maps.*
import kotlinx.android.synthetic.main.activity_request_display_maps.*

class RequestDisplayMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    private var currentMarker: Marker? = null
    val REQUEST_CODE = 1000

    companion object{
        val PRODUCT_KEY_REQUEST = "PRODUCT_KEY_REQUEST"

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {

            REQUEST_CODE -> {
                if (grantResults.size > 0) {

                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()

                }
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_display_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
        else {

            buildLocationRequest()
            buildLocationCallBack()


            //Create FusedProviderClient
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

            //set Event


            request_your_location.setOnClickListener {

                if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_CODE
                    )
                    return@setOnClickListener
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()
                )

            }

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val hongKong = LatLng(22.307443, 114.172042)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hongKong, 10f))

        val ref = FirebaseDatabase.getInstance().getReference("/Request-Product_Information")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach {

                    //  Log.d("display", it.toString())
                    val product = it.getValue(requestProductInformation::class.java) ?:return


                    if (product != null){
                        //      Log.d("display", "${product.productName}")
                        val latLng = LatLng(product.requestLatitude.toDouble(), product.requestLogitude.toDouble())

                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(product.requestProductName)
                                .snippet(product.requestProductDescription))

                        mMap.setOnMarkerClickListener(object: GoogleMap.OnMarkerClickListener{

                            override fun onMarkerClick(p0: Marker?): Boolean {

                                currentMarker = p0

                                if(p0 != null) {

                                    val placeFormView = LayoutInflater.from(this@RequestDisplayMapsActivity).inflate(R.layout.dialog_request_order, null)
                                    val dialog = AlertDialog.Builder(this@RequestDisplayMapsActivity)
                                        .setTitle("Request").setView(placeFormView)
                                        .setNegativeButton("Cancel", null)
                                        .setPositiveButton("More Information", null)
                                        .show()

                                    val productname = placeFormView.findViewById<TextView>(R.id.dialog_request_product_name)
                                    productname.setText(currentMarker?.title)

                                    val productDescription = placeFormView.findViewById<TextView>(R.id.dialog_request_product_description)
                                    productDescription.setText(currentMarker?.snippet)

                                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                                        val intent = Intent(this@RequestDisplayMapsActivity, ShowRequestProductInformation::class.java)
                                        intent.putExtra(PRODUCT_KEY_REQUEST, p0.getTitle())
                                        startActivity(intent)
                                    }
                                }

                                return true
                            }
                        })
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    private fun buildLocationRequest() {

        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f


    }

    private fun buildLocationCallBack() {


        locationCallback = object : LocationCallback() {

            override fun onLocationResult(p0: LocationResult?) {

                var mLastLocation: Location = p0!!.lastLocation

                val hongKong = LatLng(mLastLocation.latitude, mLastLocation.longitude)
                mMap.addMarker(MarkerOptions().position(hongKong).title("Your Position").icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hongKong, 15f))

                return

            }

        }
    }
}
