package detrack.zaryansgroup.com.detrack.activity.ksoap;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import detrack.zaryansgroup.com.detrack.activity.Model.Params;
import detrack.zaryansgroup.com.detrack.activity.utilites.Utility;

public class SendDataToService extends AsyncTask<Void, Void, String> {
    private static final String NAMESPACE = "http://tempuri.org/";
    static String SOAP_URL = null;
    static String SOAP_ACTION = null;
    static String METHOD_NAME = null;
    ArrayList<Params> parameters;
    SoapObject request;
    HttpTransportSE transportSE;
    SoapPrimitive soapPrimitive;
    private static String CLASS_NAME;
    String resultant = null;
    Context context = null;

    public SendDataToService(Context context, String methodName, String className, ArrayList<Params> list) {
        METHOD_NAME = methodName;
        CLASS_NAME = className;
        SOAP_ACTION = (NAMESPACE + METHOD_NAME);
        http://aquagreenwebapi.zederp.net/Webservice1/ZEDtrack.asmx
        SOAP_URL = "http://" + Utility.WEBSERVICES_SERVER_IP + "Webservice1/" + CLASS_NAME; // for server url
        // SOAP_URL = "http://"+ Utility.WEBSERVICES_SERVER_IP +"/ZED/Webservice/"+CLASS_NAME; // for iis url

        Utility.logCatMsg("Path: " + SOAP_URL);
        this.parameters = list;
        request = new SoapObject(NAMESPACE, METHOD_NAME);
        transportSE = new HttpTransportSE(SOAP_URL, (60 * 1000));   // 1 minute timeout
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                Utility.logCatMsg("Iteration: " + (i + 1));
                request.addProperty(parameters.get(i).getKey(), parameters.get(i).getValue());
                Log.d("soapparam key=",parameters.get(i).getKey()+"value="+parameters.get(i).getValue());
            }
        } else Utility.logCatMsg("Parameter is null");
        Utility.logCatMsg("Parameters: " + request.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        try {
            transportSE.call(SOAP_ACTION, envelope);
            soapPrimitive = (SoapPrimitive) envelope.getResponse();
            resultant = soapPrimitive.toString();
            Log.d("in65",resultant);
        } catch (IOException e) {
            Utility.logCatMsg("IOException in SOAP: " + e.getMessage());
            if (e.getMessage() == null) {
                Utility.logCatMsg("Failed Once");
                // Call Again
                try {
                    transportSE.call(SOAP_ACTION, envelope);
                    soapPrimitive = (SoapPrimitive) envelope.getResponse();
                    resultant = soapPrimitive.toString();
                } catch (IOException e1) {
                    if (e.getMessage() == null) {
                        Utility.logCatMsg("Failed Again");
                        // Call Again (Exception Handling)
                        try {
                            transportSE.call(SOAP_ACTION, envelope);
                            soapPrimitive = (SoapPrimitive) envelope.getResponse();
                            resultant = soapPrimitive.toString();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        } catch (XmlPullParserException e2) {
                            e2.printStackTrace();
                        }
                    }
                } catch (XmlPullParserException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (XmlPullParserException e) {
            Utility.logCatMsg("XMLException in SOAP: " + e.getMessage());
        } catch (Exception e) {
            Utility.logCatMsg("Exception in SOAP: " + e.getMessage());
        }
        Utility.logCatMsg("Result: " + resultant);
        if (resultant != null) {
            if (resultant.equals("Old") || resultant.equals("-2")) {
                Activity act = (Activity) context;
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Utility.Toast(context, "We detect that you have older version of the app, please upgrade to new one");
                    }
                });
                return null;
            }
        }
        return resultant;
    }
}