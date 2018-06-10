import java.io.*;
import java.net.*;

public class Server{

	private static String path = "./Arquivos/";

	public static String listarArquivos(String user){
		String resp = "";
		File arquivo = new File(path+user);
		if(arquivo.exists()){
			File[] lista = arquivo.listFiles();
			for(File a : lista){
				resp = resp+a+"\n";
			}
		}else{
			arquivo.mkdir();
			resp = "Diretório "+user+" criado com sucesso!";
		}
		return resp;
	}

	public static void main(String s[]){
		String opt = "";
		while(!opt.equals("EXIT")){
		try{
			ServerSocket serversocket = new ServerSocket(5555);
			FileService fs;
			Socket socket = serversocket.accept();
			ObjectOutputStream output;
			ObjectInputStream input;
			DataInputStream option = new DataInputStream(socket.getInputStream());
			opt = option.readUTF();
			switch(opt){
				case "->":
					input = new ObjectInputStream(socket.getInputStream());
					fs = (FileService)input.readObject();
					Server.listarArquivos(fs.getuser());
					fs.partFile(fs, Server.path);
					break;
				case "<-":
					input = new ObjectInputStream(socket.getInputStream());
					fs = (FileService)input.readObject();
					output = new ObjectOutputStream(socket.getOutputStream());
					output.writeObject(fs.setFileService(path, fs));
					break;
				case "EXIT":
					System.out.println(opt);
					break;

				default:
					DataOutputStream listfiles = new DataOutputStream(socket.getOutputStream());
					listfiles.writeUTF(Server.listarArquivos(opt));
					listfiles.flush();
					listfiles.close();
					break;

			}
			serversocket.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
}