package edu.vt.ece.hw6.queues;

import java.util.concurrent.atomic.AtomicInteger;

public class BoundedLockFreeQueue<T> {
    private final static int QUEUE_SIZE = 64;
    private final Item<T> [] queue = new Item[QUEUE_SIZE];
    private final AtomicInteger writer = new AtomicInteger ();
    private final AtomicInteger reader = new AtomicInteger ();

    public BoundedLockFreeQueue() {
        for (int i = 0; i < QUEUE_SIZE ; i ++) {
            queue [i] = new Item ();
            }
        }

    public void enqueue(int value) {
        int ticket = writer.getAndIncrement ();
        int turn = (ticket / QUEUE_SIZE) * 2;
        int position = ( ticket % QUEUE_SIZE );
        Item it = queue [ position ];
        while(it.lastID != turn );
        it.value = value ;
        it.lastID = turn + 1;
    }

    public void dequeue () {
        int deq_value = 0;
        int deq_ticket = reader.getAndIncrement();
        int deq_turn = (deq_ticket / QUEUE_SIZE) * 2 + 1;
        int deq_position = (deq_ticket % QUEUE_SIZE);
        Item it = queue[deq_position];
        while(it.lastID != deq_turn);
        it.lastID = deq_turn + 1;
        deq_value = it.value;
        it.value = 0;
        //return deq_value;
    }
}
