package LadoCliente;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class AcessaEmail implements Runnable {

  @Override
  public void run() {

    try {
      Scanner leitor = new Scanner(System.in);

      // Usuario digita seu nome
      System.out.println("Informe qual o seu nome: ");
      String nomeRemetente = leitor.next();

      // Conectar com o servidor
      Socket conectaServidor = new Socket("127.0.0.1", 11111);
      new Thread(new CaixaDeEntrada(conectaServidor)).start();

      // Enviar nome do remetente ao servidor
      PrintStream enviaMsgServidor = new PrintStream(conectaServidor.getOutputStream());
      enviaMsgServidor.println(nomeRemetente);

      String entradaUsuario = "";
      while (entradaUsuario != "-1") {
        // Usuario escolhe se deseja enviar um email ou acessar a caixa de entrada
        System.out.println("Informe a opção desejada:" + "\n" + "1 -> ENVIAR EMAIL" + "\n" + "2 -> CAIXA DE ENTRADA");
        entradaUsuario = leitor.next();

        System.out.println("Entrada do usuario: " + entradaUsuario);

        if (entradaUsuario.equals("1")) {
          // Enviar nome e email ao destinatario
          System.out.println("Informe qual o nome do destinatario e o email desejado:");
          entradaUsuario = leitor.next();
          PrintStream enviaNomeMsgDestinatario = new PrintStream(conectaServidor.getOutputStream());
          enviaNomeMsgDestinatario.println(entradaUsuario);

        }

        if (entradaUsuario.equals("2")) {
          System.out.println("Acessando caixa de entrada...");

          // Enviar solicitacao da caixa de entrada ao servidor
          entradaUsuario = nomeRemetente + ":caixaEntrada";
          PrintStream enviaNomeMsgDestinatario = new PrintStream(conectaServidor.getOutputStream());
          enviaNomeMsgDestinatario.println(entradaUsuario);

        }

      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}