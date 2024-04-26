### Example: balance

Inflow and outflow must be sorted in ascending order.

~~~java
val src1 = SourceResource.fromIterator(inflow);
val src2 = SourceResource.fromIterator(outflow);
val snk1 = SinkResource.of(unbaIn::add);
val snk2 = SinkResource.of(unbaOut::add);

Batch.from(src1, src2).into(snk1, snk2)
        .proceed((rd1, rd2, wr1, wr2) -> {
            String da1 = rd1.get();         // read inflow
            String da2 = rd2.get();         // read outflow
            while (da1 != null || da2 != null) {
                if (da1 == null) {          // no more in
                    wr2.accept(da2);        // write unbalance outflow
                    da2 = rd2.get();        // read next outflow
                } else if (da2 == null) {   // no more out
                    wr1.accept(da1);        // write unbalance inflow
                    da1 = rd1.get();        // read next inflow
                } else {
                    int cmp = da1.compareTo(da2);
                    if (cmp > 0) {          // in > out
                        wr2.accept(da2);    // write unbalance outflow
                        da2 = rd2.get();    // read next outflow
                    } else if (cmp < 0) {   // in < out
                        wr1.accept(da1);    // write unbalance inflow
                        da1 = rd1.get();    // read next inflow
                    } else {                // in == out
                        da1 = rd1.get();    // read next inflow
                        da2 = rd2.get();    // read next outflow
                    }
                }
            }

        });
~~~