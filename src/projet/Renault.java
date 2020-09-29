package projet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Renault {
	public static void renault_xml(File path, String fileName) throws ParserConfigurationException, IOException, SAXException, TransformerException, FileNotFoundException, XPathExpressionException {
		System.out.println("Traitement de: "+path);
		// Désactiver des validation que DocumentBuilder effecture par défaut
		// Pour accélérer le parsing du fichier renault.html
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);
		factory.setFeature("http://xml.org/sax/features/namespaces", false);
		factory.setFeature("http://xml.org/sax/features/validation", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		// Instanciation du parseur et DOMImplementation
		DocumentBuilder parseur = factory.newDocumentBuilder();
		DOMImplementation domimp = parseur.getDOMImplementation();
		// Création du document xml
		Document doc_res = domimp.createDocument(null, "Concessionnaires", null);
		doc_res.setXmlStandalone(true);
		// Document element du fichier source
		Document doc_src = parseur.parse(path);
		// Parser le document html original
		// Récupération du noeud d'élément racine des documents but
		Element rac_res = doc_res.getDocumentElement();
		// Récupération de l'élément div contenant les données nécessaire
		// <div class="post-single>
		// Utilisation de l'api XPath de javax.xml.xpath
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile("//div[@class=\"post-single\"]");  //Récupérer le noeud div ayant comme attribut "post-single"
		NodeList liste = (NodeList) expr.evaluate(doc_src, XPathConstants.NODESET);
		Element div_elt = (Element)liste.item(0);
		// Récupération de tout les éléments <p> fils du div concerné
		NodeList p_tags = div_elt.getElementsByTagName("p");
		//FiltrerParasites fp = new FiltrerParasites();	
		for (int i = 1; i < p_tags.getLength(); i++) {
			// Récupération de l'élément <p> actuel
			Element p = (Element)p_tags.item(i);
			// Récupération des éléments fils du <p> actuel
			NodeList p_child = p.getChildNodes();
			if (i == 1) {
				// Traitement du 1er <p> (différence de structure avec le reste
				// Création de <nom>
				Element nom = doc_res.createElement("Nom");
				nom.appendChild(doc_res.createTextNode(p_child.item(1).getFirstChild().getNodeValue().replaceAll("[\n\r]", " ").trim()));
				rac_res.appendChild(nom);
				// Création de <adresse>
				Element adresse = doc_res.createElement("Adresse");
				// Récupération de l'adresse
				String adr = p_child.item(6).getNodeValue();
				adr= adr.replace(":", "");
				adresse.appendChild(doc_res.createTextNode(adr.replaceAll("[\n\r]", " ").trim()));
				rac_res.appendChild(adresse);
				// Création de <Num_téléphone>
				Element tel = doc_res.createElement("Num_téléphone");
				// Récupération du num de téléphone
				String numTel = p_child.item(10).getNodeValue();
				numTel= numTel.replace(":", "");
				tel.appendChild(doc_res.createTextNode(numTel.replaceAll("[\n\r]", " ").trim()));
				rac_res.appendChild(tel);
			}
			if(i > 1 && p_child.getLength()>=4 && p_child.item(1).getNodeName().compareTo("strong")==0)
			{
				// Création des noeuds nom adresse tel pour chaque <p><p/>
				Element nom = doc_res.createElement("Nom");
				Element adresse = doc_res.createElement("Adresse");
				Element tel = doc_res.createElement("Num_téléphone");
				nom.appendChild(doc_res.createTextNode(p_child.item(1).getFirstChild().getNodeValue().replaceAll("[\n\r]", " ").trim()));
				String adr = p_child.item(6).getNodeValue();
				adresse.appendChild(doc_res.createTextNode(adr.replaceAll("[\n\r]", " ").trim()));
				String numTel = p_child.item(8).getNodeValue();
				tel.appendChild(doc_res.createTextNode(numTel.replaceAll("[\n\r]", " ").trim()));
				rac_res.appendChild(nom);
				rac_res.appendChild(adresse);
				rac_res.appendChild(tel);
			}
		}
		// Transformation du fichier (output)
		DOMSource ds = new DOMSource(doc_res);
		StreamResult res = new StreamResult(new File("renault.xml"));
		Transformer tr = TransformerFactory.newInstance().newTransformer();
		tr.setOutputProperty(OutputKeys.METHOD, "xml");
		tr.setOutputProperty(OutputKeys.INDENT, "yes"); 
		tr.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
		tr.transform(ds, res);
	}
}
