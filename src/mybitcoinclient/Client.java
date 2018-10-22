/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mybitcoinclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Stefan Toltesi
 */
public class Client {

    private final CredentialsProvider credentialsProvider;
    private final CloseableHttpClient httpclient;
    private final String ipAddress;
    private final String port;

    public Client() {
        this.credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("test", "test"));
        this.httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
        this.ipAddress = "http://127.0.0.1";
        this.port = "18443";
    }

    public String newAddress;

    private JSONObject createJSONObject() {
        JSONObject json = new JSONObject();
        json.put("jsonrpc", "2.0");
        json.put("id", "0");
        return json;
    }

    private JSONObject getResponse(JSONObject json) throws UnsupportedEncodingException, IOException, ParseException {
        StringEntity myEntity = new StringEntity(json.toJSONString());
        HttpPost httppost = new HttpPost(ipAddress + ":" + port);
        httppost.setEntity(myEntity);

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();

        return (JSONObject) new JSONParser().parse(EntityUtils.toString(entity));
    }

    public void checkBalance() throws org.json.simple.parser.ParseException, IOException {
        JSONObject json = createJSONObject();
        json.put("method", "getbalance");

        JSONObject responseJsonObj = getResponse(json);

        System.out.println("Your balance is: " + responseJsonObj.get("result"));
    }

    public void createNewAddress() throws org.json.simple.parser.ParseException, IOException {
        JSONObject json = createJSONObject();
        json.put("method", "getnewaddress");

        JSONObject responseJsonObj = getResponse(json);
        newAddress = (String) responseJsonObj.get("result");
        System.out.println("Your new address is : " + responseJsonObj.get("result"));
    }

    public void sendToAdress(List<String> params) throws org.json.simple.parser.ParseException, IOException {
        JSONObject json = createJSONObject();
        json.put("method", "sendtoaddress");

        JSONArray paramsArray = new JSONArray();
        paramsArray.addAll(params);
        json.put("params", paramsArray);

        JSONObject responseJsonObj = getResponse(json);

        System.out.println("Your transaction ID is : " + responseJsonObj.get("result"));
    }

    public void listUnspentTransactions() throws org.json.simple.parser.ParseException, IOException {
        JSONObject json = createJSONObject();
        json.put("method", "listunspent");

        List<Integer> param = new ArrayList<Integer>();
        param.add(0);
        JSONArray paramArray = new JSONArray();
        paramArray.addAll(param);
        json.put("params", paramArray);

        JSONObject responseJsonObj = getResponse(json);
        String[] splitStr = responseJsonObj.get("result").toString().split("}");
        String strPrint = "";
        for (int i = 0; i < splitStr.length; i++) {
            strPrint = strPrint + splitStr[i] + "}" + "\n";
        }
        System.out.println("Your unspent transactions are : \n" + strPrint);
    }

    public void closeConnection() throws IOException {
        httpclient.close();
    }
}
