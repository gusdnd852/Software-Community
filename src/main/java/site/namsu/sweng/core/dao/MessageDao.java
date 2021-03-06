package site.namsu.sweng.core.dao;


import site.namsu.sweng.base.Accessor;
import site.namsu.sweng.base.Dao;
import site.namsu.sweng.core.entity.Message;

import java.util.List;

@SuppressWarnings("unchecked")
public class MessageDao extends Accessor implements Dao<Message> {

    @Override
    public synchronized Message select(Message input) {
        return SQL("select * from mingus.message where messageNumber = ? ")
                .param(db -> db.setInt(1, input.getMessageNumber()))
                .map(db -> Message.builder()
                        .messageNumber(db.getInt("messageNumber"))
                        .groupName(db.getString("groupName"))
                        .name(db.getString("name"))
                        .contents(db.getString("contents"))
                        .time(db.getString("time"))
                        .build())
                .getOnce(Message.class);
    }

    @Override
    public synchronized List<Message> selectAll() {
        return SQL("select * from mingus.message")
                .map(db -> Message.builder()
                        .messageNumber(db.getInt("messageNumber"))
                        .groupName(db.getString("groupName"))
                        .name(db.getString("name"))
                        .contents(db.getString("contents"))
                        .time(db.getString("time"))
                        .build())
                .getList(Message.class);
    }

    @Override
    public synchronized boolean insert(Message input) {
        return SQL("insert into mingus.message values (?,?,?,?,?)")
                .param(db -> db.setInt(1, input.getMessageNumber()))
                .param(db -> db.setString(2, input.getGroupName()))
                .param(db -> db.setString(3, input.getName()))
                .param(db -> db.setString(4, input.getContents()))
                .param(db -> db.setString(5, input.getTime()))
                .set();
    }

    @Override
    public synchronized boolean update(Message input, String property, String value) {
        return SQL("update mingus.message set " + property + " = ?" + "where  messageNumber = ?")
                .param(db -> db.setString(1, value))
                .param(db -> db.setInt(2, input.getMessageNumber()))
                .set();
    }

    @Override
    public boolean delete(Message input) {
        return false;
    }

}
