package model.DAO;

import java.io.File;

public class PhotosDAO {
	
		public static String folder(){
			
			return "POSSIBLES";
			
		}
		
		public static String[] photos ( ){
	
			File directory = new File("POSSIBLES");
			
			String[] photos = directory.list();
			
			return photos;
	
	}
	
}
