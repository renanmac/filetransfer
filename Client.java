import java.io.*;
import java.net.*;
import javax.swing.*;


public class Client{

	public static void main(String s[]){
	char opt = 'Z';	
	String user = JOptionPane.showInputDialog("Digite seu nome de usuário: ");
	while(opt != 'S'){
		try{
			String ext;
			String name;
			String path;
			Socket socket = new Socket("localhost", 5555);
			ObjectOutputStream output;
			ObjectInputStream input;
			DataOutputStream option = new DataOutputStream(socket.getOutputStream()); 

			opt = JOptionPane.showInputDialog("Olá "+user+" o que você deseja fazer: (E)nviar um Arquivo, (R)eceber um Arquivo, (L)istar seus Arquivos no Servidor ou (S)air?").trim().toUpperCase().charAt(0);
			switch(opt){
				case 'E':
					option.writeUTF("->");
					option.flush();
					ext = JOptionPane.showInputDialog("Digite a extensão do arquivo: ");
					name = JOptionPane.showInputDialog("Digite o nome do arquivo: ");
					path = JOptionPane.showInputDialog("Digite o PATH do arquivo que vai enviar: ");
					output = new ObjectOutputStream(socket.getOutputStream());
					output.writeObject(FileService.sendFile(user, path, name, ext));
					output.close();
					JOptionPane.showMessageDialog(null, "Arquivo enviado com sucesso!");
					break;
				case 'R':
					option.writeUTF("<-");
					option.flush();
					ext = JOptionPane.showInputDialog("Digite a extensão do arquivo: ");
					name = JOptionPane.showInputDialog("Digite o nome do arquivo: ");
					output = new ObjectOutputStream(socket.getOutputStream());
					output.writeObject(new FileService(name, ext, null, user, 0));
					input = new ObjectInputStream(socket.getInputStream());
					FileService fs = (FileService)input.readObject();
					path = JOptionPane.showInputDialog("Digite o PATH onde deseja salvar o arquivo: ");
					fs.receiveFile(fs,path);
					JOptionPane.showMessageDialog(null, "Arquivo recebido com sucesso!");
					break;
				case 'L':
					option.writeUTF(user);
					option.flush();
					DataInputStream l = new DataInputStream(socket.getInputStream());
					JOptionPane.showMessageDialog(null, l.readUTF());
					l.close();
					break;
					default:
					option.writeUTF("EXIT");
					option.flush();
					JOptionPane.showMessageDialog(null, "Até logo :)");
			}		
		}catch(Exception e){
			e.printStackTrace();
		}
}
	}
}