package ch.cyberduck.core;

/*
 * Copyright (c) 2002-2022 iterate GmbH. All rights reserved.
 * https://cyberduck.io/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import org.junit.Test;

import java.util.EnumSet;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CachingListProgressListenerTest {

    @Test
    public void testEmptyList() throws Exception {
        final PathCache cache = new PathCache(1);
        final CachingListProgressListener listener = new CachingListProgressListener(cache);
        final Path directory = new Path("/", EnumSet.of(Path.Type.directory));
        listener.chunk(directory, new AttributedList<>());
        assertFalse(cache.isCached(directory));
        listener.cleanup(directory, new AttributedList<>(), Optional.empty());
        assertTrue(cache.isCached(directory));
    }

    @Test
    public void testNoResult() throws Exception {
        final PathCache cache = new PathCache(1);
        final CachingListProgressListener listener = new CachingListProgressListener(cache);
        final Path directory = new Path("/", EnumSet.of(Path.Type.directory));
        listener.chunk(directory, AttributedList.emptyList());
        listener.cleanup(directory, AttributedList.emptyList(), Optional.empty());
        assertFalse(cache.isCached(directory));
    }

    @Test
    public void testNoChunk() throws Exception {
        final PathCache cache = new PathCache(1);
        final Path directory = new Path("/", EnumSet.of(Path.Type.directory));
        final CachingListProgressListener listener = new CachingListProgressListener(cache);
        listener.cleanup(directory, AttributedList.emptyList(), Optional.empty());
        assertFalse(cache.isCached(directory));
    }
}