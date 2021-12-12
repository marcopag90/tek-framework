package com.tek.jpa.controller.impl;

import com.tek.jpa.controller.WritableDalApi;

//TODO javadoc
public abstract class WritableDalController<E, I> extends ReadOnlyDalController<E, I>
    implements WritableDalApi<E, I> {


}
