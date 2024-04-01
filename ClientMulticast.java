package pack;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ClientMulticast implements Runnable{
	private String inscricao;
	
	public ClientMulticast(String inscricao) {
		this.inscricao = inscricao;
	}

	public void run()   {
		String msg = " ";
		 try {
			 MulticastSocket socket;
		      
			 socket = new MulticastSocket(4321);
			 InetAddress ia =InetAddress.getByName("230.0.0.0");
			 InetSocketAddress grupo = new InetSocketAddress(ia , 4321);
			 NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
			 socket.joinGroup(grupo,ni);
			 while(!msg.contains("Servidor Encerrado!")){
				   System.out.println("[Cliente] Esperando por mensagem Multicast...");
				         
			   do {
				   byte[] buffer = new byte[1024];
				         
				   DatagramPacket packet=new DatagramPacket(buffer,buffer.length);
				   socket.receive(packet);
				   msg =new String(packet.getData());
				   System.out.println("[Cliente] Mensagem recebida do Servidor: "+msg);    
				         }
				         while(!msg.contains("Servidor Encerrado!")&&!msg.contains(inscricao));
			 }
			   
					 System.out.println("[Cliente] Conexao Encerrada!");
				     socket.leaveGroup(grupo, ni);
				     socket.close();
				     
		 
		 } catch (IOException e) {
				e.printStackTrace();
		 }
	}
	
	
   public static void main(String[] args) throws IOException {	   
	   String msg = " ";	 
	   String[] inscricao = {"Lorem ipsum", "Lorem ipsum"};
	   String nomeUser;
	   Scanner sc = new Scanner(System.in);
	   byte[] envio = new byte[1024];
	   
	   MulticastSocket socket;
	   socket = new MulticastSocket(4322);
	   InetAddress ia =InetAddress.getByName("230.0.0.0");
	   InetSocketAddress grupo = new InetSocketAddress(ia , 4322);

	   
	   System.out.println("Bem vindo, digite seu nome de usuário:\n");
	   nomeUser = sc.nextLine();
			


		   Thread a1 = new Thread(new ClientMulticast("Avisos Gerais"));
		   a1.start();
		   
		   String data = " ";
		   String topico = "Suporte técnico";
		   String mensagem = " ";
		   
		   while(!mensagem.equals("Servidor Encerrado!")){
			   
			   System.out.print("[Servidor] Digite a mensagem:");
			   mensagem = sc.nextLine();
			   
			   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); 
			   LocalDateTime now = LocalDateTime.now();  
			   
			   data = dtf.format(now);
			   
			   envio = ("[" + data + "] " + topico + " de "+ nomeUser + " : " + mensagem).getBytes();	
			   DatagramPacket pacote = new DatagramPacket(envio, envio.length,ia, 4320);
			   
			   socket.send(pacote);
			   
			   do {
				   byte[] buffer = new byte[1024];
			         
				   DatagramPacket packet=new DatagramPacket(buffer,buffer.length);
				   socket.receive(packet);
				   msg =new String(packet.getData());
				   System.out.println("[Cliente] Mensagem recebida do Servidor: "+msg);
			   }while(!msg.contains(nomeUser));
			   
		   }
		   
		   //Thread a2 = new Thread(new ClientMulticast("Atividades Extracurriculares"));
		   //a2.start();
	   	      
	      
	   
   }

  
}