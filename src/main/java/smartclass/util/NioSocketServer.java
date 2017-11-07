package smartclass.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import smartclass.entities.ContextResponsesSubscriptionContainer;

public class NioSocketServer {

    public NioSocketServer() {
        // Cria um canal que escuta na porta 1026
        final AsynchronousServerSocketChannel listener;
        try {
            listener = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(1026));
            // Espera um request
            System.out.println("Antes do request");
            listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {

                @Override
                public void completed(AsynchronousSocketChannel ch, Void att) {
                    // Aceita a conexão
                    System.out.println("Aceitei uma conexão");
                    listener.accept(null, this);

                    // Escreve a volta para o cliente (não necessário)
                    ch.write(ByteBuffer.wrap("Conversando!!!\n".getBytes()));

                    // Buffer para ler mensagem do cliente (4K)
                    ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
                    try {
                        int bytesRead = ch.read(byteBuffer).get(20, TimeUnit.SECONDS);

                        boolean running = true;
                        while (bytesRead != -1 && running) {
                            // Checagem de mensagem nula
                            if (byteBuffer.position() > 2) {
                                byteBuffer.flip();

                                byte[] lineBytes = new byte[bytesRead];
                                byteBuffer.get(lineBytes, 0, bytesRead);
                                String line = new String(lineBytes);
                                
                                ObjectMapper mapper = new ObjectMapper();
                                ContextResponsesSubscriptionContainer crc = mapper.readValue(ApacheRequestFactory.getHttpMessageBodyFromString(line), ContextResponsesSubscriptionContainer.class);

                                // Chama o serviço para mudar a UI
                                Service s = new Service(crc.getContextResponses()[0].getContextElement().getAttributes(), crc.getContextResponses()[0].getContextElement().getId());

                                ch.write(ByteBuffer.wrap(line.getBytes()));

                                byteBuffer.clear();

                                bytesRead = ch.read(byteBuffer).get(20, TimeUnit.SECONDS);
                            } else {
                                // Linha nula é o fim da mensagem
                                running = false;
                            }
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        // Timeout de 20seg
                        ch.write(ByteBuffer.wrap("Good Bye\n".getBytes()));
                        System.out.println("Connection timed out, closing connection");
                    } catch (IOException ex) {
                        Logger.getLogger(NioSocketServer.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    System.out.println("End of conversation");
                    // Fecha a conexão se necessário
                    if (ch.isOpen()) {
                        System.out.println("Channel is open");
                        try {
                            ch.close();
                            System.out.println("Channel is closed");
                        } catch (IOException ex) {
                            Logger.getLogger(NioSocketServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                @Override
                public void failed(Throwable exc, Void att) {
                    ///...
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(NioSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
