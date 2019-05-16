package synapsehub.m.storm.communicator

/**
 * Created by Michelo on 2/10/19 at 11:49 AM.
 * for project -> Storm_droid Copyright : SynapseHub
 */
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class Communicator : ViewModel() {
    val modelMessage = MutableLiveData<Any>()

    /**
     * Allow to set message to be shared in all other Fragments or Activity
     */
    fun setMessage(strMessage: String){
        modelMessage.value = strMessage
    }
}