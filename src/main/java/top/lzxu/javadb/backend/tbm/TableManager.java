package top.lzxu.javadb.backend.tbm;

import top.lzxu.javadb.backend.dm.DataManager;
import top.lzxu.javadb.backend.parser.statement.Begin;
import top.lzxu.javadb.backend.parser.statement.Create;
import top.lzxu.javadb.backend.parser.statement.Delete;
import top.lzxu.javadb.backend.parser.statement.Insert;
import top.lzxu.javadb.backend.parser.statement.Select;
import top.lzxu.javadb.backend.parser.statement.Update;
import top.lzxu.javadb.backend.utils.Parser;
import top.lzxu.javadb.backend.vm.VersionManager;

public interface TableManager {
    BeginRes begin(Begin begin);
    byte[] commit(long xid) throws Exception;
    byte[] abort(long xid);

    byte[] show(long xid);
    byte[] create(long xid, Create create) throws Exception;

    byte[] insert(long xid, Insert insert) throws Exception;
    byte[] read(long xid, Select select) throws Exception;
    byte[] update(long xid, Update update) throws Exception;
    byte[] delete(long xid, Delete delete) throws Exception;

    public static TableManager create(String path, VersionManager vm, DataManager dm) {
        Booter booter = Booter.create(path);
        booter.update(Parser.long2Byte(0));
        return new top.lzxu.javadb.backend.tbm.TableManagerImpl(vm, dm, booter);
    }

    public static TableManager open(String path, VersionManager vm, DataManager dm) {
        Booter booter = Booter.open(path);
        return new top.lzxu.javadb.backend.tbm.TableManagerImpl(vm, dm, booter);
    }
}
