package projet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Poeme {
	public static void poeme_xml(File path, String fileName) throws Exception {
		System.out.println("Traitement de: "+path);
		// Instanciation de DOMImplementation
		DOMImplementation imp = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
		// Noeud d'élément racine du fichier résultat
		Document doc_res = imp.createDocument(null, "poema", imp.createDocumentType("poema", null, "neruda.dtd"));
		// Récupération du noeud d'élément racine de la source
		Element rac_res = doc_res.getDocumentElement();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(path.toString())), "UTF-8"));
		// Création de l'élément titulo avec le titre lis depuis le fichier txt
		Element createElement = doc_res.createElement("titulo");
		createElement.appendChild(doc_res.createTextNode(reader.readLine()));
		rac_res.appendChild(createElement);
		// Lecture du fichier txt et remplissage du resultat xml
		String line;
		int i = 5;
		while (i > 0) {
			// Sauter les lignes vides
			while ((line = reader.readLine()).isEmpty());
			// Créer un élément estrofa
			Element estrofa = doc_res.createElement("estrofa");
			// Lecture et création des verses
			do {
				Element verse = doc_res.createElement("verso");
				verse.appendChild(doc_res.createTextNode(line));
				estrofa.appendChild(verse);
			}while(!(line = reader.readLine()).isEmpty());
			rac_res.appendChild(estrofa);
			i--;
		}
		// Fermeture du fichier txt
		reader.close();
		// Transformation du fichier (output)
		DOMSource ds = new DOMSource(doc_res);
		StreamResult res = new StreamResult(new File("neruda.xml"));
		Transformer tr = TransformerFactory.newInstance().newTransformer();
		tr.setOutputProperty(OutputKeys.METHOD, "xml");
		tr.setOutputProperty(OutputKeys.INDENT, "yes");
		tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "neruda.dtd");
		tr.transform(ds, res);
	}
}
