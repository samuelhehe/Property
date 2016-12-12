package xj.property.utils.network;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;

/**
 * Created by maxwell on 4/23/14.
 */
public class NetworkUtils
{
    /**
     * logger
     */

    public static String buildUrlExt(Map<String,String> map){
        Set<String> keySet = map.keySet();
        String urlExt = "";
        for (String key : keySet)
        {
            urlExt += "/"+key+"/"+(map.get(key).equals("") ? "": map.get(key));
        }
        return urlExt;
    }


	public static String buildQueryString(Map<String, String> map)
	{
		List<NameValuePair> pairs = buildNameValuePairs(map);
		String queryStr = URLEncodedUtils.format(pairs, "UTF-8");
		return queryStr;
	}

	public static String buildUrlByQueryStringAndBaseUrl(String baseUrl, String queryString)
	{
		return baseUrl + "?" + queryString;
	}

	public static String buildUrlByQueryStringMapAndBaseUrl(String baseUrl, Map<String, String> map)
	{
		if (map == null)
		{
			return baseUrl;
		} else
		{
			return buildUrlByQueryStringAndBaseUrl(baseUrl, buildQueryString(map));
		}
	}

	public static List<NameValuePair> buildNameValuePairs(Map<String, String> map)
	{
		Set<String> keySet = map.keySet();
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (String key : keySet)
		{
			NameValuePair pair = new BasicNameValuePair(key, map.get(key));
			pairs.add(pair);
		}
		return pairs;
	}

	// 下载网络资源的功能方法
	public static byte[] getBytes(String url)
	{
		// 字节数组缓存
		ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(0);
		HttpClient client = new DefaultHttpClient(); // 生成客户端对象
		HttpGet get = new HttpGet(url); // 生成GET请求对象
		try
		{
			HttpResponse resp = client.execute(get); // 客户端执行Get请求，并得到响应
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				// 获取响应的数据
				HttpEntity entity = resp.getEntity();
				InputStream is = entity.getContent();
				byte[] buffer = new byte[1024 * 10];
				int len = -1;
				while ((len = is.read(buffer)) != -1)
				{
					byteArrayBuffer.append(buffer, 0, len);
					// Thread.sleep(500);
				}
				is.close();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return byteArrayBuffer.toByteArray();
	}
}
