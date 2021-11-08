package LadoServidor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {

  static ArrayList<String> nomesUsuariosCadastrados = new ArrayList<String>();
  static ArrayList<Socket> socketsUsuariosConectados = new ArrayList<Socket>();
  static ArrayList<String> emailsCadastrados = new ArrayList<String>();

  public static void gerenciarUsuarios(String nome, Socket novoCliente) {
    boolean jaConectou = false;

    for (int i = 0; i < nomesUsuariosCadastrados.size(); i++) {
      if (nomesUsuariosCadastrados.get(i) == nome) {
        jaConectou = true;
      }
    }

    if (!jaConectou) {
      nomesUsuariosCadastrados.add(nome);
      socketsUsuariosConectados.add(novoCliente);
    }
  }

  public static void cadastraEmail(String email) throws IOException {

    ArrayList<String> emailsCadastrados = new ArrayList<String>(retornaEmailsCadastrados());

    FileWriter arquivo = new FileWriter("C:\\Users\\lsq\\Desktop\\TrabalhoFinal\\LadoServidor\\emailsCadastrados.txt");
    PrintWriter gravarArquivo = new PrintWriter(arquivo);

    for (int i = 0; i < emailsCadastrados.size(); i++) {
      gravarArquivo.printf(emailsCadastrados.get(i) + "%n");
    }

    gravarArquivo.printf(email + "%n");

    arquivo.close();
  }

  public static ArrayList<String> retornaEmailsCadastrados() throws IOException {

    ArrayList<String> emailsCadastrados = new ArrayList<String>();

    FileReader arquivo = new FileReader("C:\\Users\\lsq\\Desktop\\TrabalhoFinal\\LadoServidor\\emailsCadastrados.txt");
    BufferedReader lerArquivo = new BufferedReader(arquivo);

    String linha = lerArquivo.readLine();
    while (linha != null) {
      emailsCadastrados.add(linha);

      linha = lerArquivo.readLine();
    }

    arquivo.close();
    return emailsCadastrados;

  }

  public static int retornaIdUsuario(String nome) {
    int indexDestinatario = -1;

    for (int i = 0; i < nomesUsuariosCadastrados.size(); i++) {
      if (nomesUsuariosCadastrados.get(i).equals(nome)) {
        indexDestinatario = i;
      }
    }

    return indexDestinatario;
  }

  public static void main(String[] args) throws IOException {

    // Criar Serviço-servidor
    ServerSocket servidor = new ServerSocket(11111);
    System.out.println("Servidor On!");

    while (true) {

      // Aceitar Conexão com o usuário-cliente
      Socket cliente = servidor.accept();

      new Thread(new AtendeUsuario(cliente)).start();

    }

  }
}