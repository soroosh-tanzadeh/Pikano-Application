package com.ternobo.pikano.database;

import android.content.Context;
import android.content.ContextWrapper;

import com.ternobo.pikano.RESTobjects.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataFileAccess {

    private Context context;


    /**
     * Return Directory PDF Files
     *
     * @param context
     * @return File
     */
    public static File getPDFDirectory(Context context) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File directory = contextWrapper.getDir(context.getFilesDir().getName(), Context.MODE_PRIVATE);
        File file = new File(directory, "PDFs");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }


    /**
     * Return Current User Saved As serialized Object
     *
     * @param context
     * @return
     */
    public static User getCurrentUser(Context context) {
        DataFileAccess dataFileAccess = new DataFileAccess(context);
        MainDB db = null;
        try {
            db = dataFileAccess.readLocalDB();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        User currentUser = db.getCurrentUser();
        return currentUser;
    }

    public DataFileAccess(Context context){
        this.context = context;
    }


    /**
     * Read Serialized Object From Private Files
     * @param fileName
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object readObject(String fileName) throws IOException, ClassNotFoundException {
        ContextWrapper contextWrapper = new ContextWrapper(this.context);
        File directory = contextWrapper.getDir(this.context.getFilesDir().getName(), Context.MODE_PRIVATE);
        File file =  new File(directory,fileName);
        if (file.exists()){
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(file));
            Object streamed = oos.readObject();
            return streamed;
        }else{
            return null;
        }
    }

    /**
     * Write Serializable Object as File
     * @param out
     * @param fileName
     * @return
     * @throws IOException
     */
    public boolean writeObject(Object out, String fileName) throws IOException {
        ContextWrapper contextWrapper = new ContextWrapper(this.context);
        File directory = contextWrapper.getDir(this.context.getFilesDir().getName(), Context.MODE_PRIVATE);
        File file =  new File(directory,fileName);
        if (file.exists()){
            file.delete();
        }
        FileOutputStream fos = new FileOutputStream(file, true); // save
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(out);
        fos.close();
        return true;
    }


    /**
     * Read Local Database and return as {MainDB}
     * @return MainDB
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public MainDB readLocalDB() throws IOException, ClassNotFoundException {
        ContextWrapper contextWrapper = new ContextWrapper(this.context);
        File directory = contextWrapper.getDir(this.context.getFilesDir().getName(), Context.MODE_PRIVATE);
        File file =  new File(directory,"localdatabase_pikano.data");
        if (file.exists()){
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(file));
            MainDB streamed = (MainDB) oos.readObject();
            return streamed;
        }else{
            return null;
        }
    }

    public void writeLocalDB(MainDB out) throws IOException {
        ContextWrapper contextWrapper = new ContextWrapper(this.context);
        File directory = contextWrapper.getDir(this.context.getFilesDir().getName(), Context.MODE_PRIVATE);
        File file =  new File(directory,"localdatabase_pikano.data");
        if (file.exists()){
            file.delete();
        }
        FileOutputStream fos = new FileOutputStream(file, true); // save
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(out);
        fos.close();
    }

}
