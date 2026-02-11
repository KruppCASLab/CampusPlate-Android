package com.example.modernization
import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class ServiceClient private constructor(ctx: Context) {

    private val context: Context = ctx
    private var requestQueue: RequestQueue? = null

    // Keeping these since they existed in Java (even if not used directly)
    private val bwUrl = "https://mopsdev.bw.edu/cp/rest.php/"
    private val cwrUrl = "https://caslab.case.edu/cp/rest.php/"

    private var baseUrl: String? = null

    fun setBaseURL(username: String) {
        var url = "https://"
        val domain = username.substring(username.indexOf("@") + 1)

        url += when (domain) {
            "case.edu" -> "caslab.case.edu"
            "bw.edu" -> "mopsdev.bw.edu"
            else -> "mopsdev.bw.edu"
        }

        baseUrl = "$url/cp/rest.php/"
    }

    private fun getRequestQueue(): RequestQueue {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.applicationContext)
        }
        return requestQueue!!
    }

    private fun addRequest(request: Request<*>) {
        getRequestQueue().add(request)
    }

    fun get(
        broker: String,
        listener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ) {
        val base = baseUrl ?: throw RuntimeException("Base URL not set. Call setBaseURL(username) first.")
        val path = base + broker

        val baseRequest = BaseRequest(
            com.android.volley.Request.Method.GET,
            path,
            null,
            Response.Listener { response -> listener.onResponse(response) },
            Response.ErrorListener { error -> errorListener.onErrorResponse(error) }
        )

        addRequest(baseRequest)
    }

    fun get(
        broker: String,
        param: String,
        listener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ) {
        val base = baseUrl ?: throw RuntimeException("Base URL not set. Call setBaseURL(username) first.")
        val path = "$base$broker/$param"

        val baseRequest = BaseRequest(
            com.android.volley.Request.Method.GET,
            path,
            null,
            Response.Listener { response -> listener.onResponse(response) },
            Response.ErrorListener { error -> errorListener.onErrorResponse(error) }
        )

        addRequest(baseRequest)
    }

    fun get(
        broker: String,
        id: Int,
        listener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ) {
        val base = baseUrl ?: throw RuntimeException("Base URL not set. Call setBaseURL(username) first.")

        val path = if (broker == "Listings") {
            val imageRoute = "/$id/image"
            base + broker + imageRoute
        } else {
            base + broker
        }

        val baseRequest = BaseRequest(
            com.android.volley.Request.Method.GET,
            path,
            null,
            Response.Listener { response -> listener.onResponse(response) },
            Response.ErrorListener { error -> errorListener.onErrorResponse(error) }
        )

        addRequest(baseRequest)
    }

    fun delete(
        broker: String,
        id: Int,
        listener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ) {
        val base = baseUrl ?: throw RuntimeException("Base URL not set. Call setBaseURL(username) first.")
        val path = base + broker.lowercase()

        val obj = JSONObject()
        try {
            obj.put("id", id)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val baseRequest = BaseRequest(
            com.android.volley.Request.Method.DELETE,
            path,
            null, // NOTE: matches your Java code (it builds obj but doesn't send it)
            Response.Listener { response -> listener.onResponse(response) },
            Response.ErrorListener { error -> errorListener.onErrorResponse(error) }
        )

        addRequest(baseRequest)
    }

    fun patch(
        broker: String,
        putObject: Any,
        id: String,
        listener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ) {
        val base = baseUrl ?: throw RuntimeException("Base URL not set. Call setBaseURL(username) first.")
        val path = "$base$broker/$id"
        val method = com.android.volley.Request.Method.PATCH

        val gson = Gson()
        val json = gson.toJson(putObject)

        val obj = try {
            JSONObject(json)
        } catch (e: JSONException) {
            println(e)
            JSONObject()
        }

        val baseRequest = BaseRequest(
            method,
            path,
            obj,
            Response.Listener { response -> listener.onResponse(response) },
            Response.ErrorListener { error -> errorListener.onErrorResponse(error) }
        )

        addRequest(baseRequest)
    }

    fun post(
        broker: String,
        postObject: Any,
        listener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ) {
        val base = baseUrl ?: throw RuntimeException("Base URL not set. Call setBaseURL(username) first.")
        val path = base + broker.lowercase()
        val method = com.android.volley.Request.Method.POST

        val gson = Gson()
        val json = gson.toJson(postObject)

        val obj = try {
            JSONObject(json)
        } catch (e: JSONException) {
            // Java code just swallowed this; keeping behavior consistent
            JSONObject()
        }

        val baseRequest = BaseRequest(
            method,
            path,
            obj,
            Response.Listener { response -> listener.onResponse(response) },
            Response.ErrorListener { error -> errorListener.onErrorResponse(error) }
        )

        addRequest(baseRequest)
    }

    companion object {
        @Volatile private var serviceClient: ServiceClient? = null

        @JvmStatic
        fun getInstance(): ServiceClient {
            return serviceClient ?: throw RuntimeException("Service Client Uninitialized")
        }

        @JvmStatic
        fun getInstance(ctx: Context): ServiceClient {
            return serviceClient ?: synchronized(this) {
                serviceClient ?: ServiceClient(ctx).also { serviceClient = it }
            }
        }
    }
}