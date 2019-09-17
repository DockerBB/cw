package csu.cw.util;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Subdivision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class CountryAndCity {

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * 路径改动2
     * **/
    public static List<String> getCountryAndCity(String url){
        List<String> result = new ArrayList<String>();
        String countryStr = "";
        String shengStr = "";
        String tmp1 = System.getProperty("user.dir") + System.getProperty("file.separator") + "GeoLite2-City.mmdb";
        //String tmp1 = tmp + "\\src\\main\\resources\\GeoLite2-City.mmdb";
        //String tmp1 = "E:\\file\\GeoLite2-City.mmdb";
        File database = new File(tmp1);
        Country country = null;
        try{
            int flag = url.indexOf("//");
            String sub = url.substring(flag + 2);
            String host = sub.substring(0, sub.indexOf("/"));
            try {
                DatabaseReader reader = new DatabaseReader.Builder(database).build();
                InetAddress ipAddress = InetAddress.getByName(host);
                String ipAddressResult = ipAddress.getHostAddress();
                InetAddress ipAddressResource = InetAddress.getByName(ipAddressResult);
                CityResponse response = reader.city(ipAddressResource);
                country = response.getCountry();
                countryStr = country.getName();
                List<Subdivision> subdivisions = response.getSubdivisions();
                if(subdivisions.size() > 0){
                    shengStr = response.getSubdivisions().get(0).getNames().get("zh-CN");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GeoIp2Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            System.out.println("url不符合规范");
        }
        result.add(countryStr);
        result.add(shengStr);
        return result;
    }

}
