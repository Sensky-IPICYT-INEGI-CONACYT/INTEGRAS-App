package youilab.aire.processingdata;

import android.app.Service;
import android.location.Location;
import android.os.Environment;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;


public class parserJSONAirAnalysisInformation {

    private boolean validated = false;


    private static String streamFile_Buffer( File file ) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader( file ) );
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }


    public JSONArray getJSONNets(){
        File localFile = new File(Environment.getExternalStorageDirectory(), "/Air/ejemplo2.json");
        boolean bndValidateFile= validateFile("/Air/ejemplo2.json");

        if(bndValidateFile){
            JSONParser parser = new JSONParser();
            try {
                JSONArray jsonArray = (JSONArray) parser.parse(streamFile_Buffer(localFile));
                return jsonArray;
            }catch (IOException e)
            {
                e.printStackTrace();
                return null;
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }else
            return null;
    }

    public boolean validateFile(String paramString)
    {
        if (!new File(Environment.getExternalStorageDirectory(), paramString).exists()) {
            this.validated = false;
        } else {
            this.validated = true;
        }
        return this.validated;
    }

}
