package ch.cyberduck.core.shared;

/*
 * Copyright (c) 2002-2015 David Kocher. All rights reserved.
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
 * Bug fixes, suggestions and comments should be sent to feedback@cyberduck.ch
 */

import ch.cyberduck.core.DisabledConnectionCallback;
import ch.cyberduck.core.DisabledLoginCallback;
import ch.cyberduck.core.Local;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.features.Delete;
import ch.cyberduck.core.ftp.AbstractFTPTest;
import ch.cyberduck.core.ftp.FTPReadFeature;
import ch.cyberduck.core.ftp.FTPTouchFeature;
import ch.cyberduck.core.ftp.FTPWriteFeature;
import ch.cyberduck.core.io.BandwidthThrottle;
import ch.cyberduck.core.io.DisabledStreamListener;
import ch.cyberduck.core.io.StreamCopier;
import ch.cyberduck.core.transfer.TransferStatus;
import ch.cyberduck.test.IntegrationTest;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

@Category(IntegrationTest.class)
public class DefaultDownloadFeatureTest extends AbstractFTPTest {

    @Test
    public void testTransferAppend() throws Exception {
        final Path test = new Path(new DefaultHomeFinderService(session).find(), UUID.randomUUID().toString(), EnumSet.of(Path.Type.file));
        new FTPTouchFeature(session).touch(test, new TransferStatus());
        final byte[] content = new byte[39864];
        new Random().nextBytes(content);
        {
            final TransferStatus status = new TransferStatus().setLength(content.length);
            final OutputStream out = new FTPWriteFeature(session).write(test, status, new DisabledConnectionCallback());
            assertNotNull(out);
            new StreamCopier(status, status).withLimit(new Long(content.length)).transfer(new ByteArrayInputStream(content), out);
            out.close();
        }
        final Local local = new Local(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
        {
            final TransferStatus status = new TransferStatus().setLength(content.length / 2);
            new DefaultDownloadFeature(new FTPReadFeature(session)).download(
                test, local, new BandwidthThrottle(BandwidthThrottle.UNLIMITED), new DisabledStreamListener(),
                status,
                new DisabledConnectionCallback());
        }
        {
            final TransferStatus status = new TransferStatus().setLength(content.length / 2).setOffset(content.length / 2).setAppend(true);
            new DefaultDownloadFeature(new FTPReadFeature(session)).download(
                test, local, new BandwidthThrottle(BandwidthThrottle.UNLIMITED), new DisabledStreamListener(),
                status,
                new DisabledConnectionCallback());
        }
        final byte[] buffer = new byte[39864];
        final InputStream in = local.getInputStream();
        IOUtils.readFully(in, buffer);
        in.close();
        assertArrayEquals(content, buffer);
        final Delete delete = session.getFeature(Delete.class);
        delete.delete(Collections.singletonList(test), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }
}
