package top.lzxu.javadb.backend.dm.pageCache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import top.lzxu.javadb.backend.dm.page.Page;
import top.lzxu.javadb.backend.utils.Panic;
import top.lzxu.javadb.common.Error;

public interface PageCache {
    
    public static final int PAGE_SIZE = 1 << 13;

    int newPage(byte[] initData);
    Page getPage(int pgno) throws Exception;
    void close();
    void release(Page page);

    void truncateByBgno(int maxPgno);
    int getPageNumber();
    void flushPage(Page pg);

    public static top.lzxu.javadb.backend.dm.pageCache.PageCacheImpl create(String path, long memory) {
        File f = new File(path+ top.lzxu.javadb.backend.dm.pageCache.PageCacheImpl.DB_SUFFIX);
        try {
            if(!f.createNewFile()) {
                Panic.panic(Error.FileExistsException);
            }
        } catch (Exception e) {
            Panic.panic(e);
        }
        if(!f.canRead() || !f.canWrite()) {
            Panic.panic(Error.FileCannotRWException);
        }

        FileChannel fc = null;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(f, "rw");
            fc = raf.getChannel();
        } catch (FileNotFoundException e) {
           Panic.panic(e);
        }
        return new top.lzxu.javadb.backend.dm.pageCache.PageCacheImpl(raf, fc, (int)memory/PAGE_SIZE);
    }

    public static top.lzxu.javadb.backend.dm.pageCache.PageCacheImpl open(String path, long memory) {
        File f = new File(path+ top.lzxu.javadb.backend.dm.pageCache.PageCacheImpl.DB_SUFFIX);
        if(!f.exists()) {
            Panic.panic(Error.FileNotExistsException);
        }
        if(!f.canRead() || !f.canWrite()) {
            Panic.panic(Error.FileCannotRWException);
        }

        FileChannel fc = null;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(f, "rw");
            fc = raf.getChannel();
        } catch (FileNotFoundException e) {
           Panic.panic(e);
        }
        return new top.lzxu.javadb.backend.dm.pageCache.PageCacheImpl(raf, fc, (int)memory/PAGE_SIZE);
    }
}
