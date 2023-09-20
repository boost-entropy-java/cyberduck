package ch.cyberduck.core.smb;

/*
 * Copyright (c) 2002-2023 iterate GmbH. All rights reserved.
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

import ch.cyberduck.core.AlphanumericRandomStringService;
import ch.cyberduck.core.DisabledLoginCallback;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.PathAttributes;
import ch.cyberduck.core.exception.NotfoundException;
import ch.cyberduck.core.features.Delete;
import ch.cyberduck.core.shared.DefaultHomeFinderService;
import ch.cyberduck.core.transfer.TransferStatus;
import ch.cyberduck.test.TestcontainerTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Collections;
import java.util.EnumSet;

import static org.junit.Assert.*;

@Category(TestcontainerTest.class)
public class SMBTimestampFeatureTest extends AbstractSMBTest {

    @Test
    public void testTimestampFileNotfound() throws Exception {
        final TransferStatus status = new TransferStatus();
        final Path home = new DefaultHomeFinderService(session).find();
        final Path f = new Path(home, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        status.setTimestamp(System.currentTimeMillis());
        assertThrows(NotfoundException.class, () -> new SMBTimestampFeature(session).setTimestamp(f, status));
    }

    @Test
    public void testTimestampFile() throws Exception {
        final TransferStatus status = new TransferStatus();
        final Path home = new DefaultHomeFinderService(session).find();
        final Path f = new SMBTouchFeature(session).touch(
                new Path(home, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file)), new TransferStatus());
        assertNotEquals(-1L, f.attributes().getModificationDate());
        // make sure timestamps are different
        long oldTime = new SMBAttributesFinderFeature(session).find(f).getModificationDate();
        status.setTimestamp(oldTime + 2000);
        new SMBTimestampFeature(session).setTimestamp(f, status);
        PathAttributes newAttributes = new SMBAttributesFinderFeature(session).find(f);
        assertEquals(status.getTimestamp().longValue(), newAttributes.getModificationDate());
        new SMBDeleteFeature(session).delete(Collections.singletonList(f), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test
    public void testTimestampDirectory() throws Exception {
        final TransferStatus status = new TransferStatus();
        final Path home = new DefaultHomeFinderService(session).find();
        final Path f = new SMBDirectoryFeature(session).mkdir(
                new Path(home, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory)), new TransferStatus());
        assertNotEquals(-1L, f.attributes().getModificationDate());
        // make sure timestamps are different
        long oldTime = new SMBAttributesFinderFeature(session).find(f).getModificationDate();
        status.setTimestamp(oldTime + 2000);
        new SMBTimestampFeature(session).setTimestamp(f, status);
        PathAttributes newAttributes = new SMBAttributesFinderFeature(session).find(f);
        assertEquals(status.getTimestamp().longValue(), newAttributes.getModificationDate());
        new SMBDeleteFeature(session).delete(Collections.singletonList(f), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }
}
