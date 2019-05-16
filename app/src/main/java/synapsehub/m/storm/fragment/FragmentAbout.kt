package synapsehub.m.storm.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import synapsehub.m.storm.R

/**
 * Created by Michelo on 3/28/19 at 10:24 AM.
 * for project -> Storm_droid Copyright : SynapseHub
 */

class FragmentAbout : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v=LayoutInflater.from(container?.context).inflate(R.layout.fragment_about,container,false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}