package model.filemanager;

import connection.MailChimpConnection;
import model.MailchimpObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Class for representing a file manager folder.
 * Created by alexanderweiss on 22.01.16.
 */
public class FileManagerFolder extends MailchimpObject{


    private int id;
    private String name;
    private int file_count;
    private String createdAt;
    private String createdBy;
    private ArrayList<FileManagerFile> files;
    private JSONObject jsonData;
    private MailChimpConnection connection;



    public FileManagerFolder(int id, String name, int file_count, String createdAt, String createdBy, JSONObject jsonData, MailChimpConnection connection){
        super(String.valueOf(id),jsonData);
        setName(name);
        setFile_count(file_count);
        setCreatedAt(createdAt);
        setCreatedBy(createdBy);
        setConnection(connection);
        try{
            setFiles();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFile_count() {
        return file_count;
    }

    public void setFile_count(int file_count) {
        this.file_count = file_count;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ArrayList<FileManagerFile> getFiles() {
        return files;
    }

    public void setFiles() throws Exception{
        ArrayList<FileManagerFile> files = new ArrayList<FileManagerFile>();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        // parse response
        JSONObject jsonFileManagerFiles = new JSONObject(getConnection().do_Get(new URL(connection.getFILESENDPOINT()),connection.getApikey()));
        JSONArray filesArray = jsonFileManagerFiles.getJSONArray("files");
        for( int i = 0; i< filesArray.length();i++)
        {
            FileManagerFile file = null;
            JSONObject fileDetail = filesArray.getJSONObject(i);
            if(fileDetail.getString("type").equals("image")){
                file = new FileManagerFile(fileDetail.getInt("id"),fileDetail.getInt("folder_id"),fileDetail.getString("type"),fileDetail.getString("name"),fileDetail.getString("full_size_url"),fileDetail.getInt("size"),formatter.parse(fileDetail.getString("created_at")),fileDetail.getString("created_by"), fileDetail.getInt("width"), fileDetail.getInt("height"), fileDetail);
            }else{
                file = new FileManagerFile(fileDetail.getInt("id"),fileDetail.getInt("folder_id"),fileDetail.getString("type"),fileDetail.getString("name"),fileDetail.getString("full_size_url"),fileDetail.getInt("size"),formatter.parse(fileDetail.getString("created_at")),fileDetail.getString("created_by"), fileDetail);

            }

            if(file.getFolder_id() == Integer.parseInt(this.getId())) {
                files.add(file);
            }
        }

        this.files = files;
    }

    public FileManagerFile getFile(int id){
        for (FileManagerFile file:files){
            if(Integer.parseInt(file.getId()) == id){
                return file;
            }
        }
        return null;
    }

    public JSONObject getJsonData() {
        return jsonData;
    }

    public void setJsonData(JSONObject jsonData) {
        this.jsonData = jsonData;
    }

    public MailChimpConnection getConnection() {
        return connection;
    }

    public void setConnection(MailChimpConnection connection) {
        this.connection = connection;
    }


    @Override
    public String toString(){
        return "Folder-name: " + this.getName() + "Folder-Id: " + this.getId() + " File-count: " + this.getFile_count() + " Created at: " + this.getCreatedAt() +System.lineSeparator()+ " Files: "+ this.getFiles();
    }
}
