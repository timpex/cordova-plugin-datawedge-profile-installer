package no.timpex.dataWedgeProfileInstaller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.lang.reflect.InvocationTargetException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaResourceApi;
import org.apache.cordova.CordovaResourceApi.OpenForReadResult;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginManager;
import org.apache.cordova.Whitelist;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

public class DataWedgeProfileInstaller extends CordovaPlugin {
	private static final String LOG_TAG = "DataWedgeProfileInstaller";
	private static final String TARGET_DIR = "/enterprise/device/settings/datawedge/autoimport/";
	private static final int MAX_BUFFER_SIZE = 16 * 1024;


	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (!"install".equals(action)) {
			return false;
		}

		JSONObject params = args.getJSONObject(0);
		String url = params.getString("url");
		String targetFileName = params.getString("targetFileName");
		cordova.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				boolean success = download(url, targetFileName);
				if(success) {
					callbackContext.success();
				} else {
					callbackContext.error("Install failed");
				}
			}
		});
		
		return true;
	}

	private boolean download(final String source, final String targetFileName) {
		File dir = new File(TARGET_DIR);
		if(!dir.isDirectory()) return false;

		final CordovaResourceApi resourceApi = webView.getResourceApi();
		final Uri sourceUri = resourceApi.remapUri(Uri.parse(source));
		FileOutputStream outputStream = null;
		InputStream inputStream = null;
		
		try{
			HttpURLConnection connection = resourceApi.createHttpConnection(sourceUri);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept-Encoding", "gzip");
			connection.connect();
			inputStream = connection.getInputStream();
			
			byte[] buffer = new byte[MAX_BUFFER_SIZE];
			int bytesRead = 0;
			
			File file = new File(TARGET_DIR, targetFileName + ".tmp");
			outputStream = new FileOutputStream(file);
			while ((bytesRead = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, bytesRead);
			}
			inputStream.close();
			inputStream = null;

			outputStream.flush();
			outputStream.close();
			outputStream = null;

			File fileToImport = new File(TARGET_DIR, targetFileName);
			file.renameTo(fileToImport);
			fileToImport.setExecutable(true, false);
			fileToImport.setReadable(true, false);
			fileToImport.setWritable(true, false);
			return true;
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
