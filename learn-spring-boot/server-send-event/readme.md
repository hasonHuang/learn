# Server-Send Event

服务器发送事件，简称 SSE。

流程

1. 客户端发起请求
2. 服务器有消息时再返回
3. 服务器再有消息时，执行步骤2，如此循环
4. 异步请求超时，客户端重新发起连接。