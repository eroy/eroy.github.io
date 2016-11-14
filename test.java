import java.nio.charset.StandardCharsets;
import java.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLConnection;
import javax.xml.bind.DatatypeConverter;


public class bb {
    public static void main(String args[]){

        StringBuffer response = new StringBuffer();
        StringBuffer response1 = new StringBuffer();
        String md5=MD5("PASS");
// System.out.println(md5);
        String md51="admin:"+md5;
        String md5en=encodeM(md51);
        System.out.println(md5en);
        String getUrlKey="";
        String s = "Basic "+md5en;
        s=s.replaceAll(" ", "%20");
        s=s.replaceAll("=","%3d");
        s=s.replaceAll(System.getProperty("line.separator"), "");

// System.out.println(s);


        try {

            String stringUrl = "http://213.110.122.91/userRpm/LoginRpm.htm?Save=Save";
            URL url = new URL(stringUrl);
            URLConnection uc = url.openConnection();

            uc.setRequestProperty("Cookie", "Authorization="+s);


            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String responseKey = String.valueOf(response);

        String[] responseKeyArray = responseKey.split("\\/");  

        String key = responseKeyArray[3];
        System.out.println(key);



        try {

            String stringUrl1 = "http://213.110.122.91/"+key+"/userRpm/WlanSecurityRpm.htm?wepSecOpt=3&wpaSecOpt=2&wpaCipher=2&intervalWpa=0&secType=3&pskSecOpt=3&pskCipher=1&interval=0&Save=Save&pskSecret=9999999999";
            URL url1 = new URL(stringUrl1);
            URLConnection uc1 = url1.openConnection();

            uc1.setRequestProperty("Referer", "http://213.110.122.91/"+key+"/userRpm/WlanSecurityRpm.htm");

            uc1.setRequestProperty("Cookie", "Authorization="+s);


            BufferedReader in1 = new BufferedReader(new InputStreamReader(uc1.getInputStream()));
            String inputLine1;

            while ((inputLine1 = in1.readLine()) != null) {
                response1.append(inputLine1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String responseKey1 = String.valueOf(response1);
        
        if (responseKey1.contains("wlanPara")) {
                    System.out.println("OK");

        }
        else {
                     System.out.println("Error");

        }
       



// CHANGE_WIFI_PASS_URL="http://"$IP"/"$KEY"/userRpm/WlanSecurityRpm.htm?wepSecOpt=3&wpaSecOpt=2&wpaCipher=2&intervalWpa=0&secType=3&pskSecOpt=3&pskCipher=1&interval=0&Save=Save&pskSecret="$NEW_PASS;
// CHANGE_WIFI_RESPONSE=`curl -s --cookie "Authorization=$COOKIEVAL" --referer $CHANGE_WIFI_PASS_REFERER_URL $CHANGE_WIFI_PASS_MANUAL_URL`;



    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }


    public static String encodeM(String value) {
      return  Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
  }

  public static String executeCommand(String command) {

    StringBuffer output = new StringBuffer();

    Process p;
    try {
        p = Runtime.getRuntime().exec(command);
        p.waitFor();
        BufferedReader reader =
        new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line = "";
        while ((line = reader.readLine())!= null) {
            output.append(line);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return output.toString();

}

}
