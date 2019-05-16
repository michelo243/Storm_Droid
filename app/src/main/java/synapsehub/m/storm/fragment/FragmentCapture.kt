@file:Suppress("UNUSED_EXPRESSION")

package synapsehub.m.storm.fragment

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import synapsehub.m.storm.interfaces.ISharedFragment
import kotlinx.android.synthetic.main.capture_layout.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import synapsehub.m.storm.R
import synapsehub.m.storm.communicator.Communicator
import synapsehub.m.storm.helper.FieldsContrats
import synapsehub.m.storm.model.FragmentTagValue

/**
 * Created by Michelo on 2/10/19 at 11:36 AM.
 * for project -> Storm_droid Copyright : SynapseHub
 */


class FragmentCapture:Fragment(), ZXingScannerView.ResultHandler, ISharedFragment {

    private val myCameraRequestCode = 6515
    //Object to ne use for SharedData between Fragment or Activity througt ViewModel Class
    private var model: Communicator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(container?.context).inflate(R.layout.capture_layout,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set Fragment Title
        val supportAct = activity as AppCompatActivity
        supportAct.supportActionBar?.title = getString(R.string.title_app)

        //Set FragmentCapture value
        FieldsContrats.FRAGMENT_VALUE_TAG = FragmentTagValue.Capture

        //Instanciate model using with Communicator (ViewModel Class)
        model = ViewModelProviders.of(activity!!).get(Communicator::class.java)

        //Use this line to set parameters for QRCode Scanner
        setQrCodeScannerProperties()
    }

    private fun setQrCodeScannerProperties(){
        qrCodeScanner.setFormats(listOf(BarcodeFormat.QR_CODE))
        qrCodeScanner.setAutoFocus(true)
        qrCodeScanner.setLaserColor(R.color.colorAccent)
        qrCodeScanner.setMaskColor(R.color.colorAccent)

        if(Build.MANUFACTURER.equals("HUAWAEI",ignoreCase = true))
            qrCodeScanner.setAspectTolerance(0.5f)
    }

    // Start QRCode Scanner when resume fragment

    override fun onResume() {
        super.onResume()

        //Set FragmentCapture value
        FieldsContrats.FRAGMENT_VALUE_TAG = FragmentTagValue.Capture

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                this@FragmentCapture.requestPermissions(arrayOf(Manifest.permission.CAMERA), myCameraRequestCode)
                return
            }
        }

        qrCodeScanner.startCamera()
        qrCodeScanner.setResultHandler(this@FragmentCapture)
    }

    // Stop QRCode scanner on when pause fragment

    override fun onPause() {
        super.onPause()
        qrCodeScanner.stopCamera()
    }

    // Performed action after scanned QRCode and save result in Result object

    override fun handleResult(result: Result?) {
        if(result != null){
            //Affecte value captured by QrCode reader in model
            model!!.setMessage(result.text)

//resumeCamera()

            //Allow to open a new Fragment through another
            val fragment = QrScanFragment()
            openFragment(fragment, R.id.framefragmenthome)
        }
    }


    override fun openFragment(fragment: Fragment, fragment_id: Int) {
       // fragmentManager!!.beginTransaction().replace(fragment_id, fragment).addToBackStack(null).commit()
        fragmentManager!!.beginTransaction().replace(fragment_id, fragment).commit()
    }

    // Call when detach Fragment

    override fun onDetach() {
        super.onDetach()

        val fragmentCaptureMain = QrScanFragment()
        openFragment(fragmentCaptureMain, R.id.framefragmenthome)
    }
}