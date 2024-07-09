package top.lzxu.javadb.backend.dm;

import top.lzxu.javadb.backend.dm.dataItem.DataItem;
import top.lzxu.javadb.backend.dm.logger.Logger;
import top.lzxu.javadb.backend.dm.page.PageOne;
import top.lzxu.javadb.backend.dm.pageCache.PageCache;
import top.lzxu.javadb.backend.tm.TransactionManager;

public interface DataManager {
    DataItem read(long uid) throws Exception;
    long insert(long xid, byte[] data) throws Exception;
    void close();

    public static DataManager create(String path, long mem, TransactionManager tm) {
        PageCache pc = PageCache.create(path, mem);
        Logger lg = Logger.create(path);

        top.guoziyang.mydb.backend.dm.DataManagerImpl dm = new top.guoziyang.mydb.backend.dm.DataManagerImpl(pc, lg, tm);
        dm.initPageOne();
        return dm;
    }

    public static DataManager open(String path, long mem, TransactionManager tm) {
        PageCache pc = PageCache.open(path, mem);
        Logger lg = Logger.open(path);
        top.guoziyang.mydb.backend.dm.DataManagerImpl dm = new top.guoziyang.mydb.backend.dm.DataManagerImpl(pc, lg, tm);
        if(!dm.loadCheckPageOne()) {
            top.guoziyang.mydb.backend.dm.Recover.recover(tm, lg, pc);
        }
        dm.fillPageIndex();
        PageOne.setVcOpen(dm.pageOne);
        dm.pc.flushPage(dm.pageOne);

        return dm;
    }
}
