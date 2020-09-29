package projet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Fiches {
	public static void fiche (File path, String fileName) throws Exception, ParserConfigurationException, IOException, SAXException, TransformerException, FileNotFoundException{
		System.out.println("Traitement de: "+path);
		// Instanciation du parseur et DOMImplementation
		DocumentBuilder parseur = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		DOMImplementation domimp = parseur.getDOMImplementation();
		// Création du contenu des documents
		// fiche1.xml
		Document doc1= domimp.createDocument(null,"FICHES", null);
		doc1.setXmlStandalone(true);
		Element rac1 = doc1.getDocumentElement();
		//fiche2.xml
		Document doc2 = domimp.createDocument(null,"FICHES",null);
		Element rac2 = doc2.getDocumentElement();
		doc2.setXmlStandalone(true);
		remplissage(path, doc1, rac1, doc2, rac2);
		// Transformation du fichier (output)
		DOMSource ds = new DOMSource(doc1);
		StreamResult res = new StreamResult(new File("fiche1.xml"));
		Transformer tr = TransformerFactory.newInstance().newTransformer();
		tr.setOutputProperty(OutputKeys.METHOD, "xml");
		tr.setOutputProperty(OutputKeys.INDENT, "yes");
		tr.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
		tr.transform(ds, res);
		ds = new DOMSource(doc2);
		res = new StreamResult(new File("fiche2.xml"));
		tr.transform(ds, res);
	}
	// Fonction de traitement et remplissage des 2 fichier fiche1 et fiche2
	// elle prend en paramètres le chemin du fichier, ainsi que les 2 documents créés et leur noeud d'élément racine
	public static void remplissage (File path, Document doc1, Element e1, Document doc2, Element e2) throws Exception {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(path.toString())), "UTF-8"));
		int id = 1;
		String line = null;
		// Elements <fiche>
		Element fiche1 = null, fiche2 = null;
		// Elements do et sd pour la fiche1 (sauvegarder contenu)
		Element doAr = null, doFr = null, sdAr = null, sdFr = null;
		// Booléen pour savoir si l'élément VE,DF,PH,NT,RF courant est dans ar=true ou fr=false
		boolean ar = true;
		// Elements ar et fr pour sauvegarder les balises de langue
		Element ar1 = null, ar2 = null, fr1 = null, fr2 = null;
		while((line = reader.readLine()) !=null) {
			//System.out.println(line);
			// Création d'un élément <FICHE> dès l'occurrence de BE
			if(line.endsWith("BE")){
				fiche1 = doc1.createElement("FICHE");
				fiche2 = doc2.createElement("FICHE");
				fiche1.setAttribute("id", Integer.toString(id));
				fiche2.setAttribute("id", Integer.toString(id));
				id++;
				e1.appendChild(fiche1);
				e2.appendChild(fiche2);
				Element be1 = doc1.createElement("BE");
				Element be2 = doc2.createElement("BE");
				fiche1.appendChild(be1);
				fiche2.appendChild(be2);
				be1.appendChild(doc1.createTextNode(line.substring(0, (line.length()-2))));
				be2.appendChild(doc2.createTextNode(line.substring(0, (line.length()-2))));
			}
			// Element TY 
			else if(line.endsWith("TY")){
				Element ty1 = doc1.createElement("TY");
				Element ty2 = doc2.createElement("TY");
				fiche1.appendChild(ty1);
				fiche2.appendChild(ty2);
				ty1.appendChild(doc1.createTextNode("TY : " + line.substring(0, (line.length()-2))));
				ty2.appendChild(doc2.createTextNode("TY : " + line.substring(0, (line.length()-2))));
			}
			// Element DO
			else if(line.endsWith("DO")){
				// Création de 2 éléments DO un pour Arabe un pour français
				doAr = doc1.createElement("DO");
				doAr.appendChild(doc1.createTextNode("DO : " + line.substring(0, (line.length()-2))));
				doFr = doc1.createElement("DO");
				doFr.appendChild(doc1.createTextNode("DO : " + line.substring(0, (line.length()-2))));
				Element do2 = doc2.createElement("DO");
				fiche2.appendChild(do2);
				do2.appendChild(doc2.createTextNode("DO : " + line.substring(0, (line.length()-2))));
				}
			// Element SD 
			else if(line.endsWith("SD")){
				// Création de 2 éléméntes SD un pour Arabe un pour français
				sdAr = doc1.createElement("SD");
				sdAr.appendChild(doc1.createTextNode("SD : " + line.substring(0, (line.length()-2))));
				sdFr = doc1.createElement("SD");
				sdFr.appendChild(doc1.createTextNode("SD : " + line.substring(0, (line.length()-2))));
				Element sd2 = doc2.createElement("SD");
				fiche2.appendChild(sd2);
				sd2.appendChild(doc2.createTextNode("SD : " + line.substring(0, (line.length()-2))));
				}
			// Element AU
			else if(line.endsWith("AU")){
				Element au1 = doc1.createElement("AU");
				fiche1.appendChild(au1);
				au1.appendChild(doc1.createTextNode("AU : " + line.substring(0, (line.length()-2))));
				Element au2= doc2.createElement("AU");
				fiche2.appendChild(au2);
				au2.appendChild(doc2.createTextNode("AU : " + line.substring(0, (line.length()-2))));
				}
			// Element AR
			else if(line.startsWith("AR")){
				ar = true;
				ar1 = doc1.createElement("Langue");
				ar1.setAttribute("id", "AR");
				fiche1.appendChild(ar1);
				// Ajout des éléménts SD et DO à Langue dans la fiche1
				ar1.appendChild(doAr);
				ar1.appendChild(sdAr);
				ar2 = doc2.createElement("Langue");
				ar2.setAttribute("id", "AR");
				fiche2.appendChild(ar2);
				}
			// Element FR
			else if(line.startsWith("FR")){
				ar = false;
				fr1 = doc1.createElement("Langue");
				fr1.setAttribute("id", "FR");
				fiche1.appendChild(fr1);
				// Ajout des éléménts SD et DO à Langue dans la fiche1
				fr1.appendChild(doFr);
				fr1.appendChild(sdFr);
				fr2 = doc2.createElement("Langue");
				fr2.setAttribute("id", "FR");
				fiche2.appendChild(fr2);
			}
			// Element VE
			else if(line.endsWith("VE :")){
				Element ve1 = doc1.createElement("VE");
				ve1.appendChild(doc1.createTextNode("VE : " + line.substring(0, (line.length()-4))));
				Element ve2 = doc2.createElement("VE");
				ve2.appendChild(doc2.createTextNode("VE : " + line.substring(0, (line.length()-4))));
				if(ar){
					ar1.appendChild(ve1);
					ar2.appendChild(ve2);
				}
				else{
					fr1.appendChild(ve1);
					fr2.appendChild(ve2);
				}
			}
			// Element DF
			else if(line.endsWith("DF :")){
				Element df1 = doc1.createElement("DF");
				df1.appendChild(doc1.createTextNode("DF : " + line.substring(0, (line.length()-4))));
				Element df2 = doc2.createElement("DF");
				df2.appendChild(doc2.createTextNode("DF : " + line.substring(0, (line.length()-4))));
				if(ar){
					ar1.appendChild(df1);
					ar2.appendChild(df2);
				}
				else{
					fr1.appendChild(df1);
					fr2.appendChild(df2);
				}
			}
			// Element PH
			else if(line.endsWith("PH :")){
				Element ph1 = doc1.createElement("PH");
				ph1.appendChild(doc1.createTextNode("PH : " + line.substring(0, (line.length()-4))));
				Element ph2 = doc2.createElement("PH");
				ph2.appendChild(doc2.createTextNode("PH : " + line.substring(0, (line.length()-4))));
				if(ar){
					ar1.appendChild(ph1);
					ar2.appendChild(ph2);
				}
				else{
					fr1.appendChild(ph1);
					fr2.appendChild(ph2);
				}
			}
			// Element NT
			else if(line.endsWith("NT :")){
				Element nt1 = doc1.createElement("NT");
				nt1.appendChild(doc1.createTextNode("NT : " + line.substring(0, (line.length()-4))));
				Element nt2 = doc2.createElement("NT");
				nt2.appendChild(doc2.createTextNode("NT : " + line.substring(0, (line.length()-4))));
				if(ar){
					ar1.appendChild(nt1);
					ar2.appendChild(nt2);
				}
				else{
					fr1.appendChild(nt1);
					fr2.appendChild(nt2);
				}
			}
			// Element RF
			// Les éléments RF ne se terminent pas tous par RF mais contiennent ces caractères
			else if(line.contains(" ص ") || line.contains("p.")) {
				String suffixe = check_suffixe(line);
				Element rf1 = doc1.createElement("RF");
				rf1.appendChild(doc1.createTextNode(suffixe+ line.substring(0, (line.length()-4))));
				Element rf2 = doc2.createElement("RF");
				rf2.appendChild(doc2.createTextNode(suffixe+ line.substring(0, (line.length()-4))));
				if(ar){
					ar1.appendChild(rf1);
					ar2.appendChild(rf2);
				}
				else{
					fr1.appendChild(rf1);
					fr2.appendChild(rf2);
				}
			}
		}
	}
	public static String check_suffixe(String line) {
		String suffx = "RF | ";
		if (line.contains("NT"))
			suffx = suffx + "NT : ";
		if (line.contains("PH"))
			suffx = suffx + "PH : ";
		if (line.contains("DF"))
			suffx = suffx + "DF : ";
		if (line.contains("VE"))
			suffx = suffx + "VE : ";
		return suffx;
	}
}
