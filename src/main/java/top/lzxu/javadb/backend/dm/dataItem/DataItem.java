package top.lzxu.javadb.backend.dm.dataItem;

import java.util.Arrays;

import com.google.common.primitives.Bytes;

import top.lzxu.javadb.backend.common.SubArray;
import top.lzxu.javadb.backend.dm.DataManagerImpl;
import top.lzxu.javadb.backend.dm.page.Page;
import top.lzxu.javadb.backend.utils.Parser;
import top.lzxu.javadb.backend.utils.Types;

public interface DataItem {
    SubArray data();
    
    void before();
    void unBefore();
    void after(long xid);
    void release();

    void lock();
    void unlock();
    void rLock();
    void rUnLock();

    Page page();
    long getUid();
    byte[] getOldRaw();
    SubArray getRaw();

    public static byte[] wrapDataItemRaw(byte[] raw) {
        byte[] valid = new byte[1];
        byte[] size = Parser.short2Byte((short)raw.length);
        return Bytes.concat(valid, size, raw);
    }

    // 从页面的offset处解析处dataitem
    public static DataItem parseDataItem(Page pg, short offset, DataManagerImpl dm) {
        byte[] raw = pg.getData();
        short size = Parser.parseShort(Arrays.copyOfRange(raw, offset+DataItemImpl.OF_SIZE, offset+DataItemImpl.OF_DATA));
        short length = (short)(size + DataItemImpl.OF_DATA);
        long uid = Types.addressToUid(pg.getPageNumber(), offset);
        return new top.lzxu.javadb.backend.dm.dataItem.DataItemImpl(new SubArray(raw, offset, offset+length), new byte[length], pg, uid, dm);
    }

    public static void setDataItemRawInvalid(byte[] raw) {
        raw[DataItemImpl.OF_VALID] = (byte)1;
    }
}
