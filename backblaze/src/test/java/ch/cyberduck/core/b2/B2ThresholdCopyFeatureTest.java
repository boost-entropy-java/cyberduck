package ch.cyberduck.core.b2;

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
import ch.cyberduck.core.DisabledConnectionCallback;
import ch.cyberduck.core.DisabledLoginCallback;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.features.Delete;
import ch.cyberduck.core.io.DisabledStreamListener;
import ch.cyberduck.core.io.StreamCopier;
import ch.cyberduck.core.transfer.TransferStatus;
import ch.cyberduck.test.IntegrationTest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.EnumSet;

import static org.junit.Assert.*;

@Category(IntegrationTest.class)
public class B2ThresholdCopyFeatureTest extends AbstractB2Test{

    @Test
    public void testCopyFileSizeGreaterPartSize() throws Exception {
        final B2VersionIdProvider fileid = new B2VersionIdProvider(session);
        final Path container = new Path("test-cyberduck", EnumSet.of(Path.Type.directory, Path.Type.volume));
        final String name = new AlphanumericRandomStringService().random();
        final byte[] content = RandomUtils.nextBytes(6 * 1000 * 1000);
        final Path test = new Path(container, name, EnumSet.of(Path.Type.file));
        final OutputStream out = new B2WriteFeature(session, fileid).write(test, new TransferStatus().setLength(content.length), new DisabledConnectionCallback());
        new StreamCopier(new TransferStatus(), new TransferStatus().setLength(content.length)).transfer(new ByteArrayInputStream(content), out);
        out.close();
        assertTrue(new B2FindFeature(session, fileid).find(test));
        final B2ThresholdCopyFeature feature = new B2ThresholdCopyFeature(session, fileid, 5 * 1000L * 1000L);
        final Path copy = feature.copy(test, new Path(container, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file)),
                new TransferStatus().setLength(content.length), new DisabledConnectionCallback(), new DisabledStreamListener());
        assertNotEquals(test.attributes().getVersionId(), copy.attributes().getVersionId());
        assertTrue(new B2FindFeature(session, fileid).find(new Path(container, name, EnumSet.of(Path.Type.file))));
        assertTrue(new B2FindFeature(session, fileid).find(copy));
        final byte[] compare = new byte[content.length];
        final InputStream stream = new B2ReadFeature(session, fileid).read(copy, new TransferStatus().setLength(content.length), new DisabledConnectionCallback());
        IOUtils.readFully(stream, compare);
        stream.close();
        assertArrayEquals(content, compare);
        new B2DeleteFeature(session, fileid).delete(Arrays.asList(test, copy), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test
    public void testCopyFileSizeBelowPartSize() throws Exception {
        final B2VersionIdProvider fileid = new B2VersionIdProvider(session);
        final Path container = new Path("test-cyberduck", EnumSet.of(Path.Type.directory, Path.Type.volume));
        final String name = new AlphanumericRandomStringService().random();
        final byte[] content = RandomUtils.nextBytes(4 * 1000 * 1000);
        final Path test = new Path(container, name, EnumSet.of(Path.Type.file));
        final OutputStream out = new B2WriteFeature(session, fileid).write(test, new TransferStatus().setLength(content.length), new DisabledConnectionCallback());
        new StreamCopier(new TransferStatus(), new TransferStatus().setLength(content.length)).transfer(new ByteArrayInputStream(content), out);
        out.close();
        assertTrue(new B2FindFeature(session, fileid).find(test));
        final B2ThresholdCopyFeature feature = new B2ThresholdCopyFeature(session, fileid, 5 * 1000L * 1000L);
        final Path copy = feature.copy(test, new Path(container, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file)),
                new TransferStatus().setLength(content.length), new DisabledConnectionCallback(), new DisabledStreamListener());
        assertNotEquals(test.attributes().getVersionId(), copy.attributes().getVersionId());
        assertTrue(new B2FindFeature(session, fileid).find(new Path(container, name, EnumSet.of(Path.Type.file))));
        assertTrue(new B2FindFeature(session, fileid).find(copy));
        final byte[] compare = new byte[content.length];
        final InputStream stream = new B2ReadFeature(session, fileid).read(copy, new TransferStatus().setLength(content.length), new DisabledConnectionCallback());
        IOUtils.readFully(stream, compare);
        stream.close();
        assertArrayEquals(content, compare);
        new B2DeleteFeature(session, fileid).delete(Arrays.asList(test, copy), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }
}