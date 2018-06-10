import java.io.*;
import java.net.*;

public class FileService implements Serializable{ 
	private String name;
	private String user;
	private String ext;
	private int size;
	private byte[] file = new byte[size];

	public String getname(){ return name; }
	public String getuser(){ return user; }
	public String getext() { return ext;  }
	public int    getsize(){ return size; }
	public byte[] getfile(){ return file; }

	public FileService(String n, String e, byte[] b, String u, int s){
		name = n;
		ext = e;
		user = u;
		size = s;
		file = b;
	}
	/******************** SERVER SIDE METHODS ********************/

	// Armazena o vetor de bytes vindo do Client em 2 arquivos no filesystem do Server
	public void partFile(FileService f, String serverpath){
		try{
			int rest = f.size%2==0?0:1;
			String path = serverpath+f.user+"/";
			File f1 = new File(path, f.name+"part1"+ext);
			File f2 = new File(path, f.name+"part2"+ext);
			FileOutputStream part1 = new FileOutputStream(f1);
			FileOutputStream part2 = new FileOutputStream(f2);

	        part1.write(f.file,0,(f.size/2));
	        part1.close();
	        part2.write(f.file,(f.size/2),(f.size/2)+rest);
	        part2.close();
		}catch(Exception error){
			error.printStackTrace();
		}
	}

	// Instancia um objeto FileService unindo os arquivos particionados no Server
	public FileService setFileService(String serverpath, FileService fs){
		try{
			InputStream fr1 = new BufferedInputStream(new FileInputStream(serverpath+fs.user+"/"+fs.name+"part1"+fs.ext));
			InputStream fr2 = new BufferedInputStream(new FileInputStream(serverpath+fs.user+"/"+fs.name+"part2"+fs.ext));
			int size1 = (int) (new File(serverpath+user, name+"part1"+ext)).length(); 
			int size2 = (int) (new File(serverpath+user, name+"part2"+ext)).length(); 
			byte[] b = new byte[size1+size2];
			fr1.read(b,0,size1);
			fr2.read(b,size1,size2);
			fr1.close();
			fr2.close();
			return new FileService(fs.name, fs.ext, b, fs.user, size1+size2);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
		/******************** CLIENT SIDE METHODS ********************/
	
	// Armazena o vetor de bytes vindo do Server em 1 arquivo no filesystem do Client
	public void receiveFile(FileService fs, String path){
		try{
			File f = new File(path, fs.name+fs.ext);
			FileOutputStream save = new FileOutputStream(f);
			save.write(fs.file);
			save.close();
		}catch(Exception error){
			error.printStackTrace();
		}
	}

	// Instancia um objeto FileService através de um arquivo no Client
	public static FileService sendFile(String user, String path, String name, String ext){
		try{
			int size = (int) (new File(path, name+ext)).length(); 
			InputStream fr = new BufferedInputStream(new FileInputStream(path+name+ext));
			byte[] b = new byte[size];
			fr.read(b);
			fr.close();
			FileService fs = new FileService(name, ext, b, user,size);
			return fs;
		}catch(Exception error){
			error.printStackTrace();
			return null;
		}
	}
}