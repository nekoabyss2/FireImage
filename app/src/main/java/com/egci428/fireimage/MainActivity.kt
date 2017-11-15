package com.egci428.fireimage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener{

    private val PICK_IMAGE_REQUEST = 1234
    private var firePath: Uri? = null
    internal var storage: FirebaseStorage? = null
    internal var storageReference: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        selectImgBtn.setOnClickListener(this)
        uploadImgBtn.setOnClickListener(this)

    }

    override fun onClick(p0: View?){
        if(p0 == selectImgBtn){
            showImageFile()
        }else if(p0 == uploadImgBtn){
            uploadImageFile()
        }
    }

    private fun showImageFile(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Select image"), PICK_IMAGE_REQUEST)

    }

    private fun uploadImageFile(){
        if(firePath != null){
            val imageRef = storageReference!!.child("images/" + UUID.randomUUID().toString())
            imageRef.putFile(firePath!!)
                    .addOnSuccessListener { Log.d ("Result", "Success") }
                    .addOnFailureListener { Log.d ("Result", "Fail") }
                    .addOnProgressListener { Log.d ("Result", "Progress") }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null &&
                data.data != null){
            firePath = data.data

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, firePath)
                imageView.setImageBitmap(bitmap)

            }catch (e:IOException){
                e.printStackTrace()
            }
        }
    }

}
