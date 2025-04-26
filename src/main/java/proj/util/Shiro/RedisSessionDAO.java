package proj.util.Shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import proj.util.Redis.Redis;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class RedisSessionDAO extends EnterpriseCacheSessionDAO {
  @Autowired
  private Redis redis;
  private static String prefix = "retire/sessionId";

  @Override
  protected Serializable doCreate(Session session) {
    Serializable sessionId = super.doCreate(session);
    try {
      redis.putInMap(prefix, sessionId.toString(), new String(serialize(session), StandardCharsets.ISO_8859_1));
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("create session: " + session.getId());
    return sessionId;
  }

  @Override
  protected Session doReadSession(Serializable sessionId) {
    Session session = super.doReadSession(sessionId);
    if (session == null) {
      Object obj = redis.getInMap(prefix, sessionId.toString());
      if (obj != null) {
        SimpleSession s = null;
        try {
          s = (SimpleSession) deserialize(obj.toString().getBytes(StandardCharsets.ISO_8859_1));
          if (s.getId() == null) { //id为null将影响session本身逻辑, 返回null会自动重建
            System.out.println("read session: " + s.getId());
            s.setId(sessionId);
          }
          update(s);
        } catch (IOException | ClassNotFoundException e) {
          e.printStackTrace();
        }
        return s;
      }
    }
    return session;
  }

  @Override
  protected void doUpdate(Session session) {
    super.doUpdate(session);
    String sessionId = session.getId().toString();
    if (redis.hasKeyInMap(prefix, sessionId)) {
      try {
        redis.putInMap(prefix, sessionId, new String(serialize(session), StandardCharsets.ISO_8859_1));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  protected void doDelete(Session session) {
    System.out.println("delete session: " + session.getId());
    super.doDelete(session);
    redis.deleteInMap(prefix, session.getId().toString());
  }

  @Override
  public Collection<Session> getActiveSessions() {
    //从redis中读取用户信息
    Map<Object, Object> map = redis.multiGetInMap(prefix);
    List<Session> sessions = new LinkedList<>();
    map.forEach((k,v) -> {
      if (v != null) {
        try {
          SimpleSession s = (SimpleSession) deserialize(v.toString().getBytes(StandardCharsets.ISO_8859_1));
          s.setId(k.toString());
          sessions.add(s);
        } catch (IOException | ClassNotFoundException e) {
          e.printStackTrace();
        }
      } else {
        sessions.add(readSession(k.toString()));
      }
    });
    sessions.sort(Comparator.comparing(Session::getLastAccessTime).reversed());
    return sessions;
  }

  private byte[] serialize(Object obj) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ObjectOutputStream os = new ObjectOutputStream(out);
    os.writeObject(obj);
    os.close();
    out.close();
    return out.toByteArray();
  }

  private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
    ByteArrayInputStream in = new ByteArrayInputStream(data);
    ObjectInputStream is = new ObjectInputStream(in);
    Object res = is.readObject();
    is.close();
    in.close();
    return res;
  }
}
