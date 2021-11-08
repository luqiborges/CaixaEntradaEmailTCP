package LadoServidor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class AtendeUsuario implements Runnable {

  Socket usuario;

  public AtendeUsuario(Socket usuario) {
    this.usuario = usuario;
  }

  @Override
  public void run() {
    try {
      // Estabelecer canal com o cliente e pegar seu nome
      Scanner ouvirCliente = new Scanner(usuario.getInputStream());
      String nomeRemetente = ouvirCliente.next();

      // Printar no servidor o nome do usuario cadastrado
      System.out.println("Usuario: " + nomeRemetente);
      Servidor.gerenciarUsuarios(nomeRemetente, usuario);

      System.out.println("Usuarios cadastrados: ");
      for (int i = 0; i < Servidor.nomesUsuariosCadastrados.size(); i++) {
        System.out.println(Servidor.nomesUsuariosCadastrados.get(i));
      }

      // Pegar nome e email ou caixa de entrada
      String mensagemCliente = ouvirCliente.next();

      while (mensagemCliente != "-1") {

        for (int i = 0; i < Servidor.emailsCadastrados.size(); i++) {
          System.out.println(Servidor.emailsCadastrados.get(i));
        }

        String nomeDestinatario = mensagemCliente.split(":")[0];

        if (mensagemCliente.split(":")[1].equals("caixaEntrada")) {

          String caixaDeEntrada = "";
          ArrayList<String> emailsCadastrados = new ArrayList<String>(Servidor.retornaEmailsCadastrados());

          for (int i = 0; i < emailsCadastrados.size(); i++) {

            if (emailsCadastrados.get(i).split(":")[0].equals(nomeDestinatario)) {
              caixaDeEntrada = caixaDeEntrada + emailsCadastrados.get(i).split(":")[1] + ";";
            }
          }
          System.out.println(caixaDeEntrada);

          PrintStream enviarCaixaDeEntrada = new PrintStream(usuario.getOutputStream());
          enviarCaixaDeEntrada.println(caixaDeEntrada);
          mensagemCliente = ouvirCliente.next();

        } else {

          // Pegar retorno com destinatario e enviar mensagem
          int idDestinatario = Servidor.retornaIdUsuario(nomeDestinatario);
          if (idDestinatario == -1) {
            PrintStream enviarMsgDestinatario = new PrintStream(usuario.getOutputStream());
            enviarMsgDestinatario.println("Destinatario nao existe!");
            mensagemCliente = ouvirCliente.next();

          } else {
            Servidor.cadastraEmail(mensagemCliente);
            PrintStream enviarMsgDestinatario = new PrintStream(usuario.getOutputStream());
            enviarMsgDestinatario.println("Email enviado com sucesso!");
            mensagemCliente = ouvirCliente.next();

          }
        }

      }

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
