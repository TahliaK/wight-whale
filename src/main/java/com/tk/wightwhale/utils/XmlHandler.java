package com.tk.wightwhale.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Utility class which is used to export and import XML annotated classes
 * @param <T> The annotated class that this is to be used for
 */
public class XmlHandler<T> {
    /** Debug tag **/
    public static final String TAG = "XmlHandler";
    /** Output directory **/
    public static String PUT_DIR = "Generated/";
    /** Input directory **/
    public static String FIND_DIR = "Files/";

    /* Marshalling is cheap, jaxbContext is not - reuse when possible */
    /** XML reading context object **/
    private JAXBContext jaxbContext;

    /**
     * Constructor
     * @param type ClassName.class for the specified T class type
     */
    public <T> XmlHandler(Class<T> type){
        try {
            jaxbContext = JAXBContext.newInstance(type);
        } catch (JAXBException e){
            Log.send(Log.type.ERROR, TAG + "[Initializing] ",
                    e.getMessage() + " for: " + type.toString());
            jaxbContext = null;
        }
    }

    /**
     * Indicates if construction worked
     * @return true if worked, false if not
     */
    public boolean status(){
        if(jaxbContext != null){
            return true;
        }
        return false;
    }

    /**
     * Writes any XML-annotated item to a file
     * @param item the item to be written
     * @param dir Sub-directory within "Files/"
     * @param itemId The ID, which is used as a filename
     */
    public <T> void writeToXml(T item, String dir, String itemId) {
        if (jaxbContext != null) {
            try {
                Marshaller marshallerObj = jaxbContext.createMarshaller();
                marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                FileOutputStream fOut;
                if(dir != "") {
                    fOut = new FileOutputStream(PUT_DIR + dir + "/" + itemId + ".xml");
                } else {
                    fOut = new FileOutputStream(PUT_DIR + itemId + ".xml");
                }
                marshallerObj.marshal(item, fOut);
            } catch (JAXBException _jEx) {
                Log.send(Log.type.ERROR, TAG, "XML problem: " + _jEx.getMessage() +
                        " (file: " + dir + "/" + itemId + ".xml"); //Issue with JAXB
            } catch (FileNotFoundException _fNfEx) {
                Log.send(Log.type.ERROR, TAG, "File problem: " + _fNfEx.getMessage() +
                        " (file: " + dir + "/" + itemId + ".xml"); //File IO issue
            } catch (NullPointerException _npEx) {
                Log.send(Log.type.ERROR, TAG, "Tried to output gameObject with" + //gameObject issue
                        " ID or object = null: " + _npEx.getMessage() + " (file: " + dir + "/" + itemId + ".xml");
            }
        }
    }

    /**
     * Reads any XML annotated class from document
     * @param dir directory in which to look
     * @param filename file to read
     * @param <T> expected class type
     * @return T type object or null if failed
     */
    public <T> T readFromXml(String dir, String filename){
        T out = null;
        if (jaxbContext != null){
            try{
                File xmlSource;
                if(dir == ""){
                    xmlSource = new File(FIND_DIR + filename);
                } else {
                    xmlSource = new File(FIND_DIR + dir + "/" + filename);
                }
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                out = (T) jaxbUnmarshaller.unmarshal(xmlSource);
            } catch (JAXBException _ex) {
                Log.send(Log.type.ERROR, TAG, "XML error: " + _ex.getMessage()
                        + " (file: " + dir + "/" + filename + ")");
                Log.send(Log.type.ERROR, TAG, _ex);
            } catch (Exception _ex) {
                Log.send(Log.type.ERROR, TAG, "Couldn't load " + filename +
                        " reason: " + _ex.getMessage());
                Log.send(Log.type.ERROR, TAG, _ex);
            }
        }
        return out;
    }

    /**
     * Alternative readFromXml using a File instead of String Filename
     * @param xmlSource File to extract class from
     * @param <T> expected class type
     * @return Extracted object or null if failed
     */
    public <T> T readFromXml(File xmlSource){
        T out = null;
        if (jaxbContext != null) {
            try {
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                out = (T) jaxbUnmarshaller.unmarshal(xmlSource);
            } catch (JAXBException _ex) {
                Log.send(Log.type.ERROR, TAG, "XML error: " + _ex.getMessage()
                        + " (file: " + xmlSource.getPath() + ")");
                Log.send(Log.type.ERROR, TAG, _ex);
            } catch (Exception _ex) {
                Log.send(Log.type.ERROR, TAG, "Couldn't load " + xmlSource.getPath() +
                        " reason: " + _ex.getMessage());
                Log.send(Log.type.ERROR, TAG, _ex);
            }
        }
        return out;
    }
}
