package festival.network.utils;

import festival.network.rpcprotocol.FestivalClientRpcReflectionWorker;
import services.IFestivalService;

import java.net.Socket;


public class FestivalRpcConcurrentServer extends AbsConcurrentServer {
    private IFestivalService festivalServer;

    public FestivalRpcConcurrentServer(int port, IFestivalService festivalServer) {
        super(port);
        this.festivalServer = festivalServer;
        System.out.println("Festival- FestivalRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        // ChatClientRpcWorker worker=new ChatClientRpcWorker(chatServer, client);
        FestivalClientRpcReflectionWorker worker = new FestivalClientRpcReflectionWorker(festivalServer, client);

        Thread tw = new Thread(worker);
        return tw;
    }

    @Override
    public void stop() {
        System.out.println("Stopping services ...");
    }
}
