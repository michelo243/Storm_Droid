package synapsehub.m.storm.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import synapsehub.m.storm.R

/**
 * Created by Michelo on 3/17/19 at 8:49 PM.
 * for project -> Storm_droid Copyright : SynapseHub
 */
class DefaultFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(container?.context).inflate(R.layout.fragmentdefault,container,false)

        return view
    }

}