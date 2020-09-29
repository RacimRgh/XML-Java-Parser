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

import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class Javafx_xml {
	public static void javafx(File path, String fileName) throws ParserConfigurationException, IOException, SAXException, TransformerException, FileNotFoundException{
		System.out.println("Traitement de: "+path);
		// Instanciation du parseur et DOMImplementation
		DocumentBuilder parseur = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		DOMImplementation domimp = parseur.getDOMImplementation();
		// Création du document
		Document doc_res = domimp.createDocument(null,"Racine", null);
		doc_res.setXmlStandalone(true);
		Element rac_res = doc_res.getDocumentElement();
		// Parser le document src et récupérer le noeud d'élément racine
		Node racine_src = parseur.parse(path).getDocumentElement();
		// Création de l'attribut du noeud d'élément racine du fichier resultat
		Attr attr = (Attr) racine_src.getAttributes().getNamedItem("xmlns:fx");
		rac_res.setAttribute(attr.getName(), attr.getValue().substring(0, attr.getValue().length()-2));
		// Récupération de tout les attributs 
		NamedNodeMap attrs = racine_src.getAttributes();
		Element nv_noeud = null;
		Attr atr = null;
		// Transformation du noeud d'élément racine source
		for (int i = 0; i < attrs.getLength(); i++) {
			nv_noeud = doc_res.createElement("texte");
			atr = (Attr) attrs.item(i);
			nv_noeud.setAttribute(atr.getName(), "x");
			nv_noeud.setTextContent(atr.getValue());
			rac_res.appendChild(nv_noeud);
		}
		transformer_noeuds(racine_src, rac_res, nv_noeud, doc_res);
		
		// Transformation du fichier (output)
		DOMSource ds = new DOMSource(doc_res);
		StreamResult res = new StreamResult(new File("javafx.xml"));
		Transformer tr = TransformerFactory.newInstance().newTransformer();
		tr.setOutputProperty(OutputKeys.METHOD, "xml");
		tr.setOutputProperty(OutputKeys.INDENT, "yes");
		tr.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
		tr.transform(ds, res);
	}
	// Transformer tout les noeuds avec une fonction récursive
	static void transformer_noeuds (Node rac_src, Element rac_res, Element elt, Document doc) {
		//Parcourir tout les noeuds du fichier source récursivement
		for (int j = 0; j < rac_src.getChildNodes().getLength(); j++)
			// Vérifier si le noeud n'est pas un noeud de texte
			if((!rac_src.getChildNodes().item(j).getNodeName().matches("#text|#comment"))){
				Node e = rac_src.getChildNodes().item(j);
				for(int k = 0; k < e.getAttributes().getLength(); k++){
					// Créer et ajouter l'élément texte dans le fichier résultat
					Attr attr = (Attr) e.getAttributes().item(k);
					elt = doc.createElement("texte");
					elt.setAttribute(attr.getName(), "x");
					elt.setTextContent(attr.getValue());
					rac_res.appendChild(elt);
				}
				transformer_noeuds(e, rac_res, elt, doc);
			}
		}
}
