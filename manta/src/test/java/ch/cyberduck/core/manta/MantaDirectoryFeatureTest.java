package ch.cyberduck.core.manta;

/*
 * Copyright (c) 2002-2017 iterate GmbH. All rights reserved.
 * https://cyberduck.io/
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
 */

import ch.cyberduck.core.AlphanumericRandomStringService;
import ch.cyberduck.core.Attributes;
import ch.cyberduck.core.DisabledLoginCallback;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.PathAttributes;
import ch.cyberduck.core.Permission;
import ch.cyberduck.core.RandomStringService;
import ch.cyberduck.core.features.Delete;
import ch.cyberduck.test.IntegrationTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Collections;
import java.util.EnumSet;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

@Category(IntegrationTest.class)
public class MantaDirectoryFeatureTest extends AbstractMantaTest {

    @Test
    public void testMkdir() throws Exception {
        final Path target = new MantaDirectoryFeature(session).mkdir(new MantaWriteFeature(session), randomDirectory(), null);
        final PathAttributes found = new MantaAttributesFinderFeature(session).find(target);
        assertNotEquals(Permission.EMPTY, found.getPermission());
        new MantaDeleteFeature(session).delete(Collections.singletonList(target), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }

    @Test
    public void testWhitespaceMkdir() throws Exception {
        final RandomStringService randomStringService = new AlphanumericRandomStringService();
        final Path target = new MantaDirectoryFeature(session)
            .mkdir(
                    new MantaWriteFeature(session), new Path(
                    testPathPrefix,
                    String.format("%s %s", randomStringService.random(), randomStringService.random()),
                    EnumSet.of(Path.Type.directory)
                ), null);
        final Attributes found = new MantaAttributesFinderFeature(session).find(target);
        assertNull(found.getOwner());
        assertNotEquals(Permission.EMPTY, found.getPermission());
        new MantaDeleteFeature(session).delete(Collections.singletonList(target), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }
}
