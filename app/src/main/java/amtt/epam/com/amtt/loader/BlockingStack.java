package amtt.epam.com.amtt.loader;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Artsiom_Kaliaha on 03.04.2015.
 */
public class BlockingStack<E> extends LinkedBlockingDeque<E> {

    public BlockingStack() {
        super();
    }

    public BlockingStack(int capacity) {
        super(capacity);
    }

    @Override
    public void put(E e) throws InterruptedException {
        super.putLast(e);
    }

    @Override
    public E take() throws InterruptedException {
        return super.takeLast();
    }
}
