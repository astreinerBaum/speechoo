package org.speechoo.gui;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.speechoo.SpeechOO;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ReadFromXMLFile{

	private int totalTexts;
	private static NodeList listOfTrains;

	public int getTotalTexts(){
		return totalTexts;
	}

	private NodeList getRootNode(){
		return listOfTrains;
	}

	public ReadFromXMLFile(File file){
		try{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (file);
			doc.getDocumentElement().normalize();
			NodeList list = doc.getElementsByTagName("train");
			listOfTrains = list;
			totalTexts = list.getLength();
		} catch (SAXParseException err) {
			System.out.println ("** Parsing error" + ", line "
					+ err.getLineNumber () + ", uri " + err.getSystemId ());
			System.out.println(" " + err.getMessage ());
                        SpeechOO.logger = Logger.getLogger(ReadFromXMLFile.class.getName());
                        SpeechOO.logger.error(err);

		} catch (SAXException e) {
			Exception x = e.getException ();
			((x == null) ? e : x).printStackTrace ();

		} catch (Throwable t) {
			SpeechOO.logger = Logger.getLogger(ReadFromXMLFile.class.getName());
                        SpeechOO.logger.error(t);
		}
	}
/*
	public static void main (String argv []){
		//try {

//			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
//			Document doc = docBuilder.parse (new File("trainingtexts.xml"));
//
//			// normalize text representation
//			doc.getDocumentElement ().normalize ();
//			System.out.println ("Root element of the doc is " +
//					doc.getDocumentElement().getNodeName());
//
//			//trainingtext
//			NodeList listOfTrains = doc.getElementsByTagName("train");
//			int totalPersons = listOfTrains.getLength();
//			System.out.println("Total no of trains : " + totalPersons);

			ReadAndPrintXMLFile ranxf = new ReadAndPrintXMLFile(new File("trainingtexts.xml"));
			//NodeList listOfTrains = ranxf.RootNodeList;

			for(int s=0; s<listOfTrains.getLength() ; s++){

				//captura cada elemento trainingtext da lista
				Node firstTrainNode = listOfTrains.item(s);
				//pega somente os nodes do mesmo tipo de trainingtext
				if(firstTrainNode.getNodeType() == Node.ELEMENT_NODE){

					//iguala ao elemento corrente do laço
					Element firstTrainElement = (Element)firstTrainNode;

					//-------
					NodeList firstTextList = firstTrainElement.getElementsByTagName("text");
					Element firstNameElement = (Element)firstTextList.item(0);

					//NodeList textFNList = firstNameElement.getChildNodes();
					NodeList textFNList = firstNameElement.getElementsByTagName("pt");
					Element firstLanguageTextElement = (Element)textFNList.item(0);

					NodeList languageTextList = firstLanguageTextElement.getChildNodes();
					//                    System.out.println("Train "+(s+1)+": " +
					//                           ((Node)textFNList.item(0)).getNodeValue().trim());
					System.out.println("Train "+(s+1)+": " +
							((Node)languageTextList.item(0)).getNodeValue().trim());

					//-------
					//                    NodeList lastNameList = firstPersonElement.getElementsByTagName("last");
					//                    Element lastNameElement = (Element)lastNameList.item(0);
					//
					//                    NodeList textLNList = lastNameElement.getChildNodes();
					//                    System.out.println("Last Name : " +
					//                           ((Node)textLNList.item(0)).getNodeValue().trim());
					//
					//                    //----
					//                    NodeList ageList = firstPersonElement.getElementsByTagName("age");
					//                    Element ageElement = (Element)ageList.item(0);
					//
					//                    NodeList textAgeList = ageElement.getChildNodes();
					//                    System.out.println("Age : " +
					//                           ((Node)textAgeList.item(0)).getNodeValue().trim());

					//------


				}//end of if clause


			}//end of for loop with s var


//		}catch (SAXParseException err) {
//			System.out.println ("** Parsing error" + ", line "
//					+ err.getLineNumber () + ", uri " + err.getSystemId ());
//			System.out.println(" " + err.getMessage ());
//
//		}catch (SAXException e) {
//			Exception x = e.getException ();
//			((x == null) ? e : x).printStackTrace ();
//
//		}catch (Throwable t) {
//			t.printStackTrace ();
//		}
		//System.exit (0);

	}//end of main
*/
	public String[] getTextsByLanguage(String language){
		String[] texts = new String[listOfTrains.getLength()];

		for(int s=0; s<listOfTrains.getLength() ; s++){

			Node firstTrainNode = listOfTrains.item(s);

			if(firstTrainNode.getNodeType() == Node.ELEMENT_NODE){

				Element firstTrainElement = (Element)firstTrainNode;

				NodeList firstTextList = firstTrainElement.getElementsByTagName("text");
				Element firstNameElement = (Element)firstTextList.item(0);

				NodeList textFNList = firstNameElement.getElementsByTagName(language);
				Element firstLanguageTextElement = (Element)textFNList.item(0);

				NodeList languageTextList = firstLanguageTextElement.getChildNodes();

//				System.out.println("Train "+(s+1)+": " +
//						((Node)languageTextList.item(0)).getNodeValue().trim());
				texts[s] = ((Node)languageTextList.item(0)).getNodeValue().trim();

			}

		}
		return texts;
	}


}
