package com.android.chandan.myapp;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by chandan on 8/23/2015.
 */
public class ParseTop10Xml {
    private String data;
    private ArrayList<Application> applications;

    public ParseTop10Xml(String xmlData){
        data=xmlData;
        applications= new ArrayList<>();
    }

    public ArrayList<Application> getApplications(){
        return applications;
    }

    public boolean process(){
        boolean operationStatus= true;
        Application currentRecord= null;
        boolean inEntry=false;
        String textValue="";
        try{
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.data));
            int eventType= xpp.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagName= xpp.getName();
                if(eventType == XmlPullParser.START_TAG){
                    if(tagName.equalsIgnoreCase("entry")){
                        inEntry=true;
                        currentRecord= new Application();

                    }
                }else if(eventType == XmlPullParser.TEXT){
                    textValue=xpp.getText();
                }else if(eventType==XmlPullParser.END_TAG){
                    if(inEntry){
                        if(tagName.equalsIgnoreCase("entry")){
                            applications.add(currentRecord);
                        }if(tagName.equalsIgnoreCase("Name")){
                            currentRecord.setName(textValue);
                        }else if(tagName.equalsIgnoreCase("artist")){
                            currentRecord.setArtist(textValue);
                        }else if(tagName.equalsIgnoreCase("price")){
                            currentRecord.setPrice(textValue);
                        }
                    }
                }eventType=xpp.next();
            }
        }catch (Exception e){
            e.printStackTrace();
            operationStatus=false;
        }
        return operationStatus;
    }
}
