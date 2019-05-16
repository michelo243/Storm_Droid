package synapsehub.m.storm.utils;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Gracier on 24/01/2019.
 */

public class StormApiSingleton {

    private static StormApiSingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;
    private StormApiSingleton(Context context)
    {
        mCtx=context;
        requestQueue= getRequestQueue();
    }

    private RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
            requestQueue= Volley.newRequestQueue(mCtx.getApplicationContext());
        return requestQueue;
    }
    public static synchronized StormApiSingleton getmInstance(Context context)
    {
        if(mInstance==null)
        {
            mInstance=new StormApiSingleton(context);
        }
        return  mInstance;
    }
    public <T> void addToRequestQue(Request<T> request)
    {
        getRequestQueue().add(request);
    }

}
