package top.lzxu.javadb.client;

import top.lzxu.javadb.transport.Package;
import top.lzxu.javadb.transport.Packager;

public class Client {
    private top.guoziyang.mydb.client.RoundTripper rt;

    public Client(Packager packager) {
        this.rt = new top.guoziyang.mydb.client.RoundTripper(packager);
    }

    public byte[] execute(byte[] stat) throws Exception {
        Package pkg = new Package(stat, null);
        Package resPkg = rt.roundTrip(pkg);
        if(resPkg.getErr() != null) {
            throw resPkg.getErr();
        }
        return resPkg.getData();
    }

    public void close() {
        try {
            rt.close();
        } catch (Exception e) {
        }
    }

}
