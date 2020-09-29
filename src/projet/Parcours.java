/*************
 Projet Documents structuré L3 ACAD Section A 2019/2020 
 RIGHI Racim - 1717 3500 6167 - racim458@gmail.com
 BOURENANE Rania - 1717 3105 8532 - raniabrrn@gmail.com
**************/

package projet;

import java.io.File;

public class Parcours {
	public static void main(String[] args) throws Exception{
		if(args.length < 1) {
			System.out.println("Attention vous avez oublié de spécifier le nom du répertoire à traiter !");
			System.exit(-1);
		}
        String dirName = args[0];
        parcoursRecursif(dirName);
    }
	// Parcours récursif de tout les fichier du dossier en paramètre
	public static void parcoursRecursif(String dir) throws Exception{
		File fileName = new File(dir);
        File[] fileList = fileName.listFiles();
        for (File file: fileList) {
            if(file.isDirectory())
            	parcoursRecursif(file.toString());
            if (file.getName().equals("M457.xml") || file.getName().equals("M674.xml"))
            	Sorties.sorties_xml(file, file.getName());
            if (file.getName().equals("renault.html"))
            	Renault.renault_xml(file,  file.getName());
            if (file.getName().equals("poeme.txt"))
            	Poeme.poeme_xml(file, file.getName());    
            if (file.getName().equals("boitedialog.fxml"))
            	Javafx_xml.javafx(file,  file.getName());
            if (file.getName().equals("fiches.txt"))
            	Fiches.fiche(file, file.getName());
        }
	}
	
}
