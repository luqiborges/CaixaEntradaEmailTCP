package LadoCliente;

import java.io.IOException;

public class Usuario {

  public static void main(String[] args) throws IOException {

    new Thread(new AcessaEmail()).start();

  }
}