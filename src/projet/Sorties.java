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

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Sorties {
	public static void sorties_xml(File path, String fileName) throws ParserConfigurationException, IOException, SAXException, TransformerException, FileNotFoundException {
		System.out.println("Traitement de: "+path);
		String sortie = null;
		String sortie_xml = null;
		if (fileName.equals("M457.xml")) {
			sortie = "sortie2";
			sortie_xml = "sortie2.xml";
		} else if (fileName.equals("M674.xml")) {
			sortie = "sortie1";
			sortie_xml = "sortie1.xml";
		}
		// Supprimer des vérifications qui peuvent induire à des erreurs (absence de dtd)
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		// Instanciation du parseur et DOMImplementation
		DocumentBuilder parseur = factory.newDocumentBuilder();
		// Parser le document original
		Document document = parseur.parse(path);
		DOMImplementation domimp = parseur.getDOMImplementation();
		// Lien vers la DTD
		DocumentType dtd = domimp.createDocumentType(sortie, null, "dom.dtd");
		// Création du document xml
		Document doc = domimp.createDocument(null, "TEI_S", dtd);
		doc.setXmlStandalone(true); //DTD interne
		// Récupération du noeud d'élément racine
		Element rac = doc.getDocumentElement();
		// Création du noeud Mxxx.xml
		Element noeuds = doc.createElement(fileName);
		rac.appendChild(noeuds);
		Element racine_src = document.getDocumentElement();
		trait_recursif(racine_src, noeuds, doc);
		DOMSource ds = new DOMSource(doc);
		StreamResult res = new StreamResult(new File(sortie_xml));
		Transformer tr = TransformerFactory.newInstance().newTransformer();
		tr.setOutputProperty(OutputKeys.INDENT, "yes");
		tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "dom.dtd");
		tr.transform(ds, res);
	}
	private static void trait_recursif(Node node, Element elmt, Document doc) {

		if(node.getNodeName().equals("p")){
			if(node.getFirstChild() != null) {
				String phrase = node.getFirstChild().getNodeValue().replaceAll("[\\n\\t]", "");
				Element text_element = doc.createElement("texte");
				if (!phrase.contentEquals("")) {
					text_element.appendChild(doc.createTextNode(phrase));
    				elmt.appendChild(text_element);
				}
					
			}
		}
		if(node.getNodeName().equals("lb") || node.getNodeName().equals("pb")){
			if(node.getNextSibling() != null ){
				String phrase= node.getNextSibling().getNodeValue().replaceAll("[\\n\\t]", "");
				Element text_element = doc.createElement("texte");
				if (!phrase.contentEquals("")) {
					text_element.appendChild(doc.createTextNode(phrase));
    				elmt.appendChild(text_element);
				}
			}
		}
	if (node.hasChildNodes()) {
		NodeList nodes = node.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++) {
			trait_recursif(nodes.item(i), elmt, doc);
		}
	}
}
}
