package graphics;

import actors.GameObject;
import actors.MovingObject;
import utils.Log;
import utils.XmlHandler;

import java.util.Map;
import java.util.HashMap;

public class GraphicsController {

    private static final String TAG = "GraphicsController";
    private static Map<String, MovingObject> graphicalItems;
    private static GcElements settings = null;
    private static Board graphicsBoard = null;
    private static int stepSize = 1;

    /** This can be used to halt & resume registered graphics processing **/
    public static boolean gameRunning = true;

    /**
     * This MUST be called before GraphicsController is used or you will
     * get a lot of null pointer exceptions
     *
     * This automatically attempts to import settings from "Files/GraphicsController.xml"
     * If there is no file found, it uses the defaults.
     */
    public static void init(){
        graphicalItems = new HashMap<>();
        settings = XmlHandler.ImportGcElements();
        if(settings == null) { // no import found
            settings = new GcElements(); //default value
        }
        stepSize = 1000 / settings.fps; //fps into milsecond delay
        Log.send(Log.type.INFO, TAG, "Initialized successfully.");
    }

    /**
     * Registers a GameObject for rendering
     * @param obj the GameObject to display / use
     * @return success value; log output shows errors.
     */
    public static boolean register(GameObject obj){
        boolean result = false;

        //todo: save MovingObjects as MovingObjects, and GameObjects as GameObjects
        MovingObject mObj = new MovingObject(); //temporary
        mObj.setHeight(obj.getHeight());
        mObj.setWidth(obj.getWidth());
        mObj.setId(obj.getId());
        mObj.setImgFilename(obj.getImgFilename());
        mObj.setImage(obj.getImage());

        if(obj.getId() == null){
            Log.send(Log.type.ERROR, TAG, "Failed to register object; no ID assigned.");
        } else if(graphicalItems.containsKey(obj.getId())){
            if(graphicalItems.containsValue(obj)){
                Log.send(Log.type.INFO, TAG, "Failed to register " + mObj.getId() +
                        ", object already registered.");
            } else {
                Log.send(Log.type.ERROR, TAG, "Failed to register " + mObj.getId() +
                        ", ID already in use.");
            }
        } else {
            graphicalItems.put(mObj.getId(), mObj);
            Log.send(Log.type.INFO, TAG, "Successfully registered " + mObj.getId());
            result = true;
        }

        return result;
    }

    // getters and setters for graphicalItems list
    public static Map<String, MovingObject> getGraphicalItems() {
        return graphicalItems;
    }

    public static MovingObject getGraphicalItemsById(String id) {
        return graphicalItems.get(id);
    }

    /**
     * Moves all sprites 1 step forward
     */
    public static void step() {
        if(gameRunning) {
            Map<String, MovingObject> m = GraphicsController.getGraphicalItems();
            for (Map.Entry<String, MovingObject> entry : m.entrySet()) {
                MovingObject obj = entry.getValue();
                obj.step();
            }
        }
    }

    /**
     * Moves all sprites 1 step forward based on time in game loop
     */
    public static void step(double delta) {
        Map<String, MovingObject> m = GraphicsController.getGraphicalItems();
        for(Map.Entry<String, MovingObject> entry : m.entrySet()){
            MovingObject obj = entry.getValue();
            //adjusts distance of step
            double xAxis = obj.getdX() * delta;
            double yAxis = obj.getdY() * delta;
            obj.setdX((int)xAxis);
            obj.setdY((int)yAxis);
            //
            obj.step();
        }
    }

    /**
     * Exports all graphical items currently rendering to XML documents.
     * In "Generated/..." directory.
     */
    public static void exportAll(){
        for(Map.Entry<String, MovingObject> entry : graphicalItems.entrySet()){
            XmlHandler.ObjectToXml(entry.getValue());
        }
    }

    /**
     * Clears the contents of graphicalItems
     */
    public static void clearGraphicalItems(){
        graphicalItems.clear();
        Log.send(Log.type.INFO, TAG, "Graphics items cleared.");
    }

    /** Getters and setters **/
    public static String getWindowTitle() {
        return settings.windowTitle;
    }

    public static void setWindowTitle(String windowTitle) {
        settings.windowTitle = windowTitle;
    }

    public static int getWindowHeight() {
        return settings.windowHeight;
    }

    public static void setWindowHeight(int windowHeight) {
        settings.windowHeight = windowHeight;
    }

    public static int getWindowWidth() {
        return settings.windowWidth;
    }

    public static void setWindowWidth(int windowWidth) {
        settings.windowWidth = windowWidth;
    }

    public static GcElements getSettings() {
        return settings;
    }

    public static void setSettings(GcElements settings) {
        GraphicsController.settings = settings;
    }

    public static Board getGraphicsBoard() {
        return graphicsBoard;
    }

    public static void setGraphicsBoard(Board graphicsBoard) {
        GraphicsController.graphicsBoard = graphicsBoard;
    }

    /**
     * Gives the number of miliseconds between graphics
     * processing loops.
     * @return 1000 / fps = miliseconds wait time
     */
    public static int getStepSize() {
        return stepSize;
    }

    public static void setStepSize(int stepSize) {
        GraphicsController.stepSize = stepSize;
    }
}

