package vectorization.index;

import java.io.File;

public class Directory{

	private String parentPath;
	private String name;
	private String absolutePath;
	private File file;
	private int depth;
	
	public Directory(String parentPath, String name){
		this.parentPath = parentPath;
		this.name = name;
		this.absolutePath = parentPath+"/"+name;
		this.file = new File(this.absolutePath);
		this.depth = 0;
	}
	
	public Directory(String absolutePath){
		this.absolutePath = absolutePath;
		this.depth = 0;
		this.file = new File(this.absolutePath);
		this.name = this.file.getName();
		this.parentPath = this.file.getParent();
	}

	private boolean isDirectory() {
		return this.file.isDirectory();
	}
	
	public String getRootPath() {
		return parentPath;
	}

	public void setRootPath(String rootPath) {
		this.parentPath = rootPath;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}
	
	public String getName() {
		return name;
	}
	
	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public File getFile() {
		return file;
	}

	public void cleanDirectory(){
		String[] childs = file.list();
		for(int i=0;i<childs.length;i++){
			Directory child = new Directory(this.absolutePath,childs[i]);
			if(child.isDirectory()){
				child.cleanDirectory();
			}else{
				child.getFile().delete();
			}
		}
			
	}
	
}
