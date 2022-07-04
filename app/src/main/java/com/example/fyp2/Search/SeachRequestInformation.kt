package com.example.fyp2.Search

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.fyp2.DashboardActivity
import com.example.fyp2.MAP.ProductInformation
import com.example.fyp2.R
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_map_product_information.*
import kotlinx.android.synthetic.main.activity_request_upload.*
import java.util.*

class SeachRequestInformation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    var REQUEST_CODE = 1000

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
                        Toast.makeText(this@SeachRequestInformation, "Permission granted", Toast.LENGTH_SHORT).show()

                    else
                        Toast.makeText(this@SeachRequestInformation, "Permission denied", Toast.LENGTH_SHORT).show()

                }
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_upload)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.request_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
        else{

            buildLocationRequest()
            buildLocationCallBack()

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


            request_btn_startUpdates.setOnClickListener {
                if(ActivityCompat.checkSelfPermission(this@SeachRequestInformation, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this@SeachRequestInformation, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this@SeachRequestInformation, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
                    return@setOnClickListener
                }
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

                request_btn_startUpdates.isEnabled = !request_btn_startUpdates.isEnabled
                request_btn_stopUpdates.isEnabled = !request_btn_stopUpdates.isEnabled

            }


            request_btn_stopUpdates.setOnClickListener {

                if (ActivityCompat.checkSelfPermission(
                        this@SeachRequestInformation,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        this@SeachRequestInformation,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@SeachRequestInformation,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_CODE
                    )
                    return@setOnClickListener
                }
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)

                request_btn_startUpdates.isEnabled = !request_btn_startUpdates.isEnabled
                request_btn_stopUpdates.isEnabled = !request_btn_stopUpdates.isEnabled
            }
        }

        request_image.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        request_SubmitButton.setOnClickListener {


            uploadProductPhoto()

        }
    }

    private fun buildLocationCallBack(){

        locationCallback = object : LocationCallback(){

            override fun onLocationResult(p0: LocationResult?) {

                var location = p0!!.locations.get(p0!!.locations.size -1)

                findViewById<TextView>(R.id.request_txt_Latitude).text = location.latitude.toString()
                findViewById<TextView>(R.id.request_txt_Longitude).text = location.longitude.toString()
                return
            }
        }
    }

    private fun buildLocationRequest(){

        locationRequest = LocationRequest()
        locationRequest.priority  = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 15f


    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val hongKong = LatLng(22.307443, 114.172042)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hongKong, 10f))


        locationRequest = LocationRequest()
        locationRequest.priority  = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 15f

        locationCallback = object : LocationCallback(){

            override fun onLocationResult(p0: LocationResult?) {

                var mLastLocation: Location = p0!!.lastLocation

                val hongKong = LatLng(mLastLocation.latitude, mLastLocation.longitude)
                mMap.addMarker(MarkerOptions().position(hongKong).title("My Location"))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hongKong, 20f))

                findViewById<TextView>(R.id.request_txt_Latitude).text = mLastLocation.latitude.toString()
                findViewById<TextView>(R.id.request_txt_Longitude).text = mLastLocation.longitude.toString()
                return
            }
        }

    }
    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            val bitmapDrawable = BitmapDrawable(bitmap)
            request_image.setBackgroundDrawable(bitmapDrawable)

        }

    }
    private fun uploadProductPhoto(){

        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()

        val photo = FirebaseStorage.getInstance().getReference("/request product image/$filename")


        photo.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {

                photo.downloadUrl.addOnSuccessListener {

                    if (it.toString().isEmpty()){
                        request_image.error = "Please enter product name"
                        request_image.requestFocus()
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
        val reference = FirebaseDatabase.getInstance().getReference("/Request-Product_Information").push()

        if (request_productName.text.toString().isEmpty()){

            request_productName.error = "Please enter product name"
            request_productName.requestFocus()
            Toast.makeText(this, "Please enter product name", Toast.LENGTH_SHORT).show()
            return

        }

        if (request_productDesciption.text.toString().isEmpty()){
            request_productDesciption.error = "Please enter product desciption"
            request_productDesciption.requestFocus()
            Toast.makeText(this, "Please enter product name", Toast.LENGTH_SHORT).show()
            return
        }

        if (request_txt_Latitude.text.toString().isEmpty()){

            request_txt_Latitude.error = "Please search your location"
            request_txt_Latitude.requestFocus()
            Toast.makeText(this, "Please search your location", Toast.LENGTH_SHORT).show()
            return
        }

        if (request_txt_Longitude.text.toString().isEmpty()){

            request_txt_Longitude.error = "Please enter search your location"
            request_txt_Longitude.requestFocus()
            Toast.makeText(this, "Please search your location", Toast.LENGTH_SHORT).show()
            return
        }

        val request_productInformation = requestProductInformation(uid, request_productName.text.toString(), request_productDesciption.text.toString(), request_txt_Latitude.text.toString(),  request_txt_Longitude.text.toString(),productPhoto)

        reference.setValue(request_productInformation)
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
