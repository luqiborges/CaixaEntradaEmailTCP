package LadoCliente;

import java.net.Socket;
import java.util.Scanner;

public class CaixaDeEntrada implements Runnable {

  Socket conexaoServidor;

  public CaixaDeEntrada(Socket conexaoServidor) {
    this.conexaoServidor = conexaoServidor;
  }

  @Override
  public void run() {
    try {
      System.out.println("Escutando servidor...");

      while (true) {
        // Receber emails do servidor
        Scanner ouvirServidor = new Scanner(conexaoServidor.getInputStream());
        String respostaRecebida = ouvirServidor.nextLine();

        if (respostaRecebida.contains(";")) {
          String novaRespostaRecebida = "CAIXA DE ENTRADA: " + "\n";

          String palavra = "";
          for (int i = 0; i < respostaRecebida.length(); i++) {
            if (respostaRecebida.charAt(i) != ';') {
              palavra += respostaRecebida.charAt(i);
            } else {
              novaRespostaRecebida = novaRespostaRecebida + palavra + "\n";
              palavra = "";
            }
          }

          respostaRecebida = novaRespostaRecebida;

        }

        // Imprimir a resposta do servidor na tela do usuario
        System.out.println(respostaRecebida);

      }

    } catch (Exception e) {
      e.printStackTrace();

    }
  }

}
