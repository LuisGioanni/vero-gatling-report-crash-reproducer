import java.util.Random;
import java.util.UUID;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class DummyWebSocketServer extends AbstractVerticle {

  public enum WAMPMessageType {

    WELCOME, // 0 Server-to-client Auxiliary
    PREFIX, // 1 Client-to-server Auxiliary
    CALL, // 2 Client-to-server RPC
    CALLRESULT, // 3 Server-to-client RPC
    CALLERROR, // 4 Server-to-client RPC
    SUBSCRIBE, // 5 Client-to-server PubSub
    UNSUBSCRIBE, // 6 Client-to-server PubSub
    PUBLISH, // 7 Client-to-server PubSub
    EVENT, // 8 Server-to-client PubSub

    UNKNOWN;

    public static WAMPMessageType lookup(final int i) {
      if (i >= 0 && i <= 8) {
        return WAMPMessageType.values()[i];
      }
      return UNKNOWN;
    }
  }

  private static final Logger log = LoggerFactory.getLogger(DummyWebSocketServer.class);
  private static final Random rand = new Random();

  @Override
  public void start(Future<Void> fut) {
    vertx.createHttpServer().websocketHandler(ws -> {
      if (!"/wamp".equals(ws.path())) {
        log.info("Requested unexpected path: {}", ws.path());
        ws.reject();
        return;
      }

      log.debug("New WAMP connection from {}", ws.remoteAddress());

      ws.closeHandler(new Handler<Void>() {

        @Override
        public void handle(final Void event) {
          log.debug("WebSocket connection closed");
        }
      });
      ws.exceptionHandler(new Handler<Throwable>() {

        @Override
        public void handle(final Throwable e) {
          log.warn("Exception occured", e);
        }
      });
      send(ws, WAMPMessageType.WELCOME, ws.textHandlerID(), "1", "VeroServer/1");
      ws.handler(buffer -> {
        try {
          final JsonArray data = new JsonArray(buffer.toString());

          final int typeId = data.getInteger(0);
          final WAMPMessageType messageType = WAMPMessageType.lookup(typeId);

          log.trace("WAMP request {}:{}", messageType, data);

          switch (messageType) {
            case CALL:
              handleCall(ws, data);
              break;
            case PUBLISH:
            case SUBSCRIBE:
            case UNSUBSCRIBE:
              log.info("Ignoring unsupported message {}", messageType);
              break;
            default:
              log.warn("Unexpected message type " + typeId);
          }
        } catch (final Exception e) {
          log.warn("Failed to handle WAMP request ", e);
        }
      });
    }).listen(8080, result -> {
      if (result.succeeded()) {
        log.info("HTTP web service started");
        fut.complete();
      } else {
        fut.fail(result.cause());
      }
    });
  }

  void send(final ServerWebSocket ws, final WAMPMessageType type, final Object... args) {
    final JsonArray data = new JsonArray();
    data.add(type.ordinal());
    for (final Object arg : args) {
      data.add(arg);
    }
    log.trace("Sending WAMP message {}:{}", type, data);
    try {
      ws.writeFinalTextFrame(data.toString());
    } catch (final IllegalStateException e) {
      log.debug("Trying to write on a closed WebSocket, WAMP message {}:{}", type, data, e);
    }
  }

  protected void handleCall(final ServerWebSocket ws, final JsonArray data) {
    final String callId = data.getString(1);
    final String callUri = data.getString(2);

    log.trace("About to process new call {}", data);

    if ("auth:req".equals(callUri)) {
      send(ws, WAMPMessageType.CALLRESULT, callId,
        new JsonObject().put("salt", "thisisadumsalt").put("token", "atoken"));
    } else if ("auth".equals(callUri)) {
      send(ws, WAMPMessageType.CALLRESULT, callId, new JsonObject().put("id", UUID.randomUUID().toString())
        .put("token", UUID.randomUUID().toString())
        .put("check", "authcode"));
    } else if ("ping".equals(callUri)) {
      send(ws, WAMPMessageType.CALLRESULT, callId, "pong");
    } else if (data.size() >= 3) {
      double offset;
      //vertx.setTimer(200 + (long) ((offset = (rand.nextGaussian()) * Math.sqrt(10000)) <= -199 ? -199 : offset),
      vertx.setTimer(rand.nextInt(10000)+1,
        event -> {
          int seed = rand.nextInt(100);
          if (seed < 80) {
            send(ws, WAMPMessageType.CALLRESULT, "result");
          } else {
            send(ws, WAMPMessageType.CALLERROR, "error");
          }
        });
    } else {
      log.warn("Invalid call argument format {}", data);
      send(ws, WAMPMessageType.CALLERROR, callId, "Error");
    }
  }

}
