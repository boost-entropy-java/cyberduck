package ch.cyberduck.core.openstack;

/*
 * Copyright (c) 2002-2013 David Kocher. All rights reserved.
 * http://cyberduck.ch/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * Bug fixes, suggestions and comments should be sent to:
 * feedback@cyberduck.ch
 */

import ch.cyberduck.core.AttributedList;
import ch.cyberduck.core.DisabledListProgressListener;
import ch.cyberduck.core.DisabledLoginCallback;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.TranscriptListener;
import ch.cyberduck.core.exception.NotfoundException;
import ch.cyberduck.core.features.Delete;
import ch.cyberduck.core.transfer.TransferStatus;
import ch.cyberduck.test.IntegrationTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Category(IntegrationTest.class)
public class SwiftDeleteFeatureTest extends AbstractSwiftTest {

    @Test(expected = NotfoundException.class)
    public void testDeleteNotFoundBucket() throws Exception {
        final Path container = new Path(UUID.randomUUID().toString(), EnumSet.of(Path.Type.directory, Path.Type.volume));
        container.attributes().setRegion("IAD");
        new SwiftDeleteFeature(session).delete(Collections.singletonList(container), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test(expected = NotfoundException.class)
    public void testDeleteNotFoundKey() throws Exception {
        final Path container = new Path("test.cyberduck.ch", EnumSet.of(Path.Type.directory, Path.Type.volume));
        container.attributes().setRegion("IAD");
        final Path test = new Path(container, UUID.randomUUID().toString(), EnumSet.of(Path.Type.file));
        new SwiftDeleteFeature(session).delete(Collections.singletonList(test), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test
    public void testDeleteNoParentPlaceholder() throws Exception {
        final Path container = new Path("test.cyberduck.ch", EnumSet.of(Path.Type.directory, Path.Type.volume));
        container.attributes().setRegion("IAD");
        final String name = "placeholder-" + UUID.randomUUID().toString();
        final Path placeholder = new Path(
            new Path(container, "t", EnumSet.of(Path.Type.directory)),
            name, EnumSet.of(Path.Type.directory));
        final Path test = new Path(placeholder, UUID.randomUUID().toString(), EnumSet.of(Path.Type.file));
        new SwiftTouchFeature(session, new SwiftRegionService(session)).touch(new SwiftWriteFeature(session, new SwiftRegionService(session)), test, new TransferStatus());
        final SwiftFindFeature find = new SwiftFindFeature(session);
        assertTrue(find.find(placeholder));
        final SwiftObjectListService list = new SwiftObjectListService(session);
        // Must contain placeholder object returned
        final AttributedList<Path> children = list.list(placeholder.getParent(), new DisabledListProgressListener());
        assertTrue(children.contains(new Path(
            new Path(container, "t", EnumSet.of(Path.Type.directory)),
            name, EnumSet.of(Path.Type.directory, Path.Type.placeholder))));
        // No directory file at this level
        assertTrue(children.contains(placeholder));
        assertFalse(children.contains(test));
        assertTrue(list.list(placeholder, new DisabledListProgressListener()).contains(test));
        assertTrue(find.find(test));
        new SwiftDeleteFeature(session).delete(Arrays.asList(placeholder, test), new DisabledLoginCallback(), new Delete.DisabledCallback());
        assertFalse(find.find(test));
        assertFalse(find.find(placeholder));
    }

    @Test
    public void testDeletePlaceholder() throws Exception {
        final AtomicBoolean delete = new AtomicBoolean();
        final String name = "placeholder-" + UUID.randomUUID().toString();
        session.withListener(new TranscriptListener() {
            @Override
            public void log(final Type request, final String message) {
                switch(request) {
                    case request:
                        if(("DELETE /v1/MossoCloudFS_59113590-c679-46c3-bf62-9d7c3d5176ee/test.cyberduck.ch/" + name + " HTTP/1.1").equals(message)) {
                            delete.set(true);
                        }
                }
            }
        });
        final Path container = new Path("test.cyberduck.ch", EnumSet.of(Path.Type.directory, Path.Type.volume));
        container.attributes().setRegion("IAD");
        final Path placeholder = new Path(container, name, EnumSet.of(Path.Type.directory));
        new SwiftDirectoryFeature(session).mkdir(new SwiftWriteFeature(session, new SwiftRegionService(session)), placeholder, new TransferStatus());
        final SwiftFindFeature find = new SwiftFindFeature(session);
        assertTrue(find.find(placeholder));
        new SwiftDeleteFeature(session).delete(Collections.singletonList(placeholder), new DisabledLoginCallback(), new Delete.DisabledCallback());
        assertTrue(delete.get());
        Thread.sleep(1000L);
        assertFalse(find.find(placeholder));
    }
}
