package com.example.utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Environment;

import com.example.gamelogic.Music;
import com.example.gamelogic.MusicArray;

public class GameXMLHandler {
	
	public static void saveMusicToXML(Map<String, ArrayList<Music>> gameMusics){
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		
 
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("GameMusics");
		doc.appendChild(rootElement);
		 for (String key : gameMusics.keySet())
		 {		
			 	Element musicPackage = doc.createElement("package");
			 	musicPackage.setAttribute("packagename", key );
				rootElement.appendChild(musicPackage);
				
				for (int mIndex = 0 ; mIndex < gameMusics.get(key).size() ;mIndex++){
					
					Music mObj = gameMusics.get(key).get(mIndex);
				// staff elements
				Element song = doc.createElement("music");
				musicPackage.appendChild(song);
		 
				// set attribute to staff element
				
				Attr attr = doc.createAttribute("id");
				attr.setValue(""+mIndex);
				song.setAttributeNode(attr);
		 
				// shorten way
		//		song.setAttribute("level", "");
				song.setAttribute("name", mObj.getName());
				song.setAttribute("artist", mObj.getArtist());
				song.setAttribute("path", mObj.getPath());
				}
		 }
		
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(
				Environment.getExternalStorageDirectory(),
				"/Android/data/com.example.trabalhovb/gamemusics.xml"));
 
		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);
		System.out.println("File saved!");
		
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void readMusicFromXML(){
		
		try {
			 
			File fXmlFile = new File(
					Environment.getExternalStorageDirectory(),
					"/Android/data/com.example.trabalhovb/gamemusics.xml");
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
			//get list of game packages
			NodeList pList = doc.getElementsByTagName("package");
			
//			MusicArray.musicsOfGame = new Music[nList.getLength()] ;	
            
			for (int tempP = 0; tempP < pList.getLength(); tempP++) {
				Node nNode = pList.item(tempP);
				Element nElement = (Element) nNode;
				NodeList nList = nNode.getChildNodes();
				for (int tempM = 0; tempM < nList.getLength(); tempM++) {
					Node mNode = nList.item(tempM);
					if (mNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) mNode;
						System.out.println(eElement.getAttribute("name"));
						MusicArray.addMusicToMap( nElement.getAttribute("packagename")  + "_" +
						eElement.getAttribute("artist")+ "_" + eElement.getAttribute("name"));
						
					}
				}
			}
			
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		  }
}
