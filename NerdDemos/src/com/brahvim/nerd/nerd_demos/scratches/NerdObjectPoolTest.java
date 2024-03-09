package com.brahvim.nerd.nerd_demos.scratches;

import java.util.Iterator;

import com.brahvim.nerd.utils.NerdObjectPool;

public class NerdObjectPoolTest {

    public static void main(final String[] args) {
        // Create an object pool of integers
        final NerdObjectPool<Float> floatsPool = new NerdObjectPool<>(5);

        // Promote some objects to the pool
        for (int i = 0; i < 10; i++) {
            floatsPool.promote((float) i);
        }

        // Display the demoted objects
        System.out.println("Demoted Objects:");
        for (final Float demoted : floatsPool.allDemoted()) {
            System.out.print(demoted + " ");
        }
        System.out.println();

        // Demote some objects back to the pool
        final Iterator<Float> promotedIterator = floatsPool.promotedObjectsIterator();
        while (promotedIterator.hasNext()) {
            final Float demoted = promotedIterator.next();
            if (demoted % 2 == 0) {
                promotedIterator.remove(); // TODO: Implement this to demote objects!
            }
        }

        // Display the demoted objects after demotion
        System.out.println("Demoted Objects after Demotion:");
        for (final Float demoted : floatsPool.allDemoted()) {
            System.out.print(demoted + " ");
        }
        System.out.println();

        // Promote some objects again
        floatsPool.promote(3);
        floatsPool.promote(7);

        // Display the promoted objects
        System.out.println("Promoted Objects:");
        for (final Float promoted : floatsPool.allPromoted()) {
            System.out.print(promoted + " ");
        }
        System.out.println();
    }

}
