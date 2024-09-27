package com.data.orderbook.infrastructure.kraken.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;

class SnapshotMessageMapperTest {

    SnapshotMessageMapper mapper = new SnapshotMessageMapper(new ObjectMapper());

    @Test
    public void shouldMap() {
        mapper.map(
                new TextMessage(
                        "[119930880,{\"a\":[[\"64540.40000\",\"0.23673675\",\"1727359737.554515\"],[\"64540.60000\",\"0.00284208\",\"1727359703.585358\"],[\"64548.00000\",\"0.00015496\",\"1727359736.408580\"],[\"64549.10000\",\"0.10000000\",\"1727359684.952850\"],[\"64551.30000\",\"0.00015531\",\"1727359691.071404\"],[\"64554.50000\",\"0.00015531\",\"1727359572.864505\"],[\"64555.00000\",\"0.00010000\",\"1727359724.619353\"],[\"64557.70000\",\"0.00015531\",\"1727359717.949504\"],[\"64558.60000\",\"1.00692764\",\"1727359722.822505\"],[\"64560.00000\",\"1.00000000\",\"1727359727.929538\"]],\"b\":[[\"64540.30000\",\"51.18117742\",\"1727359737.787268\"],[\"64536.80000\",\"5.01142884\",\"1727359737.195086\"],[\"64536.10000\",\"0.00100000\",\"1727359734.949140\"],[\"64535.90000\",\"1.99229032\",\"1727359736.407862\"],[\"64535.50000\",\"0.46451565\",\"1727359737.191901\"],[\"64535.00000\",\"0.00061981\",\"1727359732.134133\"],[\"64534.70000\",\"0.23260000\",\"1727359737.895929\"],[\"64534.40000\",\"0.00791729\",\"1727359704.447571\"],[\"64534.20000\",\"0.46451565\",\"1727359735.480251\"],[\"64532.60000\",\"0.00020000\",\"1727359737.475928\"]]},\"book-10\",\"XBT/USD\"]"));
    }
}
