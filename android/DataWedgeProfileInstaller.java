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
	private static final String LINE_START = "--";
    private static final String LINE_END = "\r\n";
    private static final String BOUNDARY =  "+++++";
	private static final String TARGET_DIR = "/enterprise/device/settings/datawedge/autoimport/";
    public static int FILE_NOT_FOUND_ERR = 1;
    public static int INVALID_URL_ERR = 2;
    public static int CONNECTION_ERR = 3;
    public static int ABORTED_ERR = 4;
	public static int NOT_MODIFIED_ERR = 5;
	private static final int MAX_BUFFER_SIZE = 16 * 1024;


	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException, IOException {
		if (!"install".equals(action)) {
			return false;
		}

		String source = args.getString(0);
		boolean success = download(source);
		if(success) {
			callbackContext.success();
		} else {
			callbackContext.error();
		}
		
		return true;
	}

	private boolean download(final String source) throws IOException {
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
			
			File file = new File(TARGET_DIR,"datawedge.db.tmp");
			outputStream = new FileOutputStream(file);
			while ((bytesRead = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, bytesRead);
			}
			inputStream.close();
			inputStream = null;

			outputStream.flush();
			outputStream.close();
			outputStream = null;

			File fileToImport = new File(TARGET_DIR, "datawedge.db");
			file.renameTo(fileToImport);
			fileToImport.setExecutable(true, false);
			fileToImport.setReadable(true, false);
			fileToImport.setWritable(true, false);
			return true;
		} catch(IOException e) {
			return false;
		} finally {
			if(outputStream != null) {
				outputStream.close();
			}
			if(inputStream != null) {
				inputStream.close();
			}
		}
	}
}
