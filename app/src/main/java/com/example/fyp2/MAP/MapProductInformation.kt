package com.example.fyp2.MAP

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.os.ProxyFileDescriptorCallback
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.fyp2.DashboardActivity
import com.example.fyp2.R
import com.example.fyp2.order.order
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.view.View
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_map_product_information.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_request_upload.*
import org.w3c.dom.Text
import java.util.*


class MapProductInformation : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    private var markers: MutableList<Marker> = mutableListOf()
    val REQUEST_CODE = 1000

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode)
        {

            REQUEST_CODE->{
                if (grantResults.size > 0){

                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(this@MapProductInformation, "Permission granted", Toast.LENGTH_SHORT).show()

                    else
                        Toast.makeText(this@MapProductInformation, "Permission denied", Toast.LENGTH_SHORT).show()

                }
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_product_information)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        productPhoto_btn.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }



        SubmitButton.setOnClickListener{
            uploadProductPhoto()

        }

        //check permission
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
        else{

            buildLocationRequest()
            buildLocationCallBack()


            //Create FusedProviderClient
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

            //set Event



        btn_startUpdates.setOnClickListener{

            if(ActivityCompat.checkSelfPermission(this@MapProductInformation, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this@MapProductInformation, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this@MapProductInformation, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
                return@setOnClickListener
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

            //Change state of button

            btn_startUpdates.isEnabled = !btn_startUpdates.isEnabled
            btn_stopUpdates.isEnabled = !btn_stopUpdates.isEnabled

        }

            btn_stopUpdates.setOnClickListener {

                if(ActivityCompat.checkSelfPermission(this@MapProductInformation, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this@MapProductInformation, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this@MapProductInformation, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
                    return@setOnClickListener
                }
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)

                //Change state of button

                btn_startUpdates.isEnabled = !btn_startUpdates.isEnabled
                btn_stopUpdates.isEnabled = !btn_stopUpdates.isEnabled

            }

        }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val hongKong = LatLng(22.307443, 114.172042)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hongKong, 10f))

        mMap.setOnInfoWindowClickListener {
            markers.remove(it)
            it.remove()
        }

        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f


        locationCallback = object : LocationCallback() {

            override fun onLocationResult(p0: LocationResult?) {

                var mLastLocation: Location = p0!!.lastLocation

         //       val a:String = txt_Latitude.text.toString()
         //       val b:String = txt_Longitude.text.toString()
         //       val textlatitude : Double = a.toDouble()
         //       val textlongitude: Double = b.toDouble()
//
                val hongKong = LatLng(mLastLocation.latitude, mLastLocation.longitude)
                mMap.addMarker(MarkerOptions().position(hongKong).title("My Location"))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hongKong, 20f))

               findViewById<TextView>(R.id.txt_Latitude).text = mLastLocation.latitude.toString()
              findViewById<TextView>(R.id.txt_Longitude).text = mLastLocation.longitude.toString()

                return
            }


            //      var latitude = txt_Latitude.text.toString()
            //       var longitude = txt_Longitude.text.toString()


            // Add a marker in Sydney and move the camera
//        val hongKong = LatLng(latitude, longitude)
//        mMap.addMarker(MarkerOptions().position(hongKong).title("Marker in My Home"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(hongKong))
        }
    }

    private fun buildLocationRequest() {

            locationRequest = LocationRequest()
            locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            locationRequest.interval = 5000
            locationRequest.fastestInterval = 3000
            locationRequest.smallestDisplacement = 10f


    }

    private fun buildLocationCallBack() {

        locationCallback = object : LocationCallback(){

            override fun onLocationResult(p0: LocationResult?) {

            val mLastLocation: Location = p0!!.lastLocation

                findViewById<TextView>(R.id.txt_Latitude).text = mLastLocation.latitude.toString()
                findViewById<TextView>(R.id.txt_Longitude).text = mLastLocation.longitude.toString()

                return
            }

        }

    }


    //Select Product Photo

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            val bitmapDrawable = BitmapDrawable(bitmap)
            productPhoto_btn.setBackgroundDrawable(bitmapDrawable)

        }

    }

    private fun uploadProductPhoto(){

        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()

        val photo = FirebaseStorage.getInstance().getReference("/product image/$filename")


        photo.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {

                photo.downloadUrl.addOnSuccessListener {

                    if (it.toString().isEmpty()){
                        Toast.makeText(this, "Please select photo", Toast.LENGTH_SHORT).show()

                        return@addOnSuccessListener
                    }else{

                        it.toString()

                        performSubmmitProduct(it.toString())

                        Toast.makeText(this, "Successfully Selected", Toast.LENGTH_SHORT).show()

                    }

                }
                    .addOnFailureListener {

                        Toast.makeText(this, "Failed to select photo", Toast.LENGTH_SHORT).show()

                    }

            }

    }

    private fun performSubmmitProduct(productPhoto: String){

        val uid = FirebaseAuth.getInstance().uid?:""
        val reference = FirebaseDatabase.getInstance().getReference("/ProductInformation").push()

          if (productName.text.toString().isEmpty()){

              productName.error = "Please enter product name"
              productName.requestFocus()
              Toast.makeText(this, "Please enter product name", Toast.LENGTH_SHORT).show()
              return

          }

          if (productDesciption.text.toString().isEmpty()){
              productDesciption.error = "Please enter product desciption"
              productDesciption.requestFocus()
              Toast.makeText(this, "Please enter product name", Toast.LENGTH_SHORT).show()
              return
          }

          if (txt_Latitude.text.toString().isEmpty()){

              txt_Latitude.error = "Please search your location"
              txt_Latitude.requestFocus()
              Toast.makeText(this, "Please search your location", Toast.LENGTH_SHORT).show()
              return
          }

          if (txt_Longitude.text.toString().isEmpty()){

              txt_Longitude.error = "Please enter search your location"
              txt_Longitude.requestFocus()
              Toast.makeText(this, "Please search your location", Toast.LENGTH_SHORT).show()
              return
          }

          if (submit_Product_exchance.text.toString().isEmpty()){
              submit_Product_exchance.error = "Please enter exchange condition"
              submit_Product_exchance.requestFocus()
              Toast.makeText(this, "Please enter exchange condition", Toast.LENGTH_SHORT).show()
              return
          }

          if (submit_Product_Delivery_Service.text.toString().isEmpty()){
              submit_Product_Delivery_Service.error = "Do you provide delivery service?"
              submit_Product_Delivery_Service.requestFocus()
              Toast.makeText(this, "Do you provide delivery service?", Toast.LENGTH_SHORT).show()
              return

          }
        val productInformation = ProductInformation(uid, productName.text.toString(), productDesciption.text.toString(), txt_Latitude.text.toString(),  txt_Longitude.text.toString(), productPhoto, submit_Product_exchance.text.toString(), submit_Product_Delivery_Service.text.toString())

        reference.setValue(productInformation)
            .addOnSuccessListener {
                Log.d("ProductInformation", "Save Product Information")
                Toast.makeText(this, "Successfully Uploaded", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener{
                Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()

            }

    }



}
