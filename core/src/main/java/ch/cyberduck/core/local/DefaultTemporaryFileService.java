package ch.cyberduck.core.local;

/*
 * Copyright (c) 2013 David Kocher. All rights reserved.
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

import ch.cyberduck.core.AlphanumericRandomStringService;
import ch.cyberduck.core.DefaultPathContainerService;
import ch.cyberduck.core.Local;
import ch.cyberduck.core.LocalFactory;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.UUIDRandomStringService;
import ch.cyberduck.core.exception.AccessDeniedException;
import ch.cyberduck.core.preferences.Preferences;
import ch.cyberduck.core.preferences.PreferencesFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class DefaultTemporaryFileService extends AbstractTemporaryFileService implements TemporaryFileService {
    private static final Logger log = LogManager.getLogger(DefaultTemporaryFileService.class);

    private final Preferences preferences = PreferencesFactory.get();
    private final String delimiter = preferences.getProperty("local.delimiter");

    @Override
    public Local create(final Path file) {
        return this.create(new UUIDRandomStringService().random(), file);
    }

    @Override
    public Local create(final String name) {
        return this.create(LocalFactory.get(preferences.getProperty("tmp.dir"), new UUIDRandomStringService().random()), name);
    }

    /**
     * @return Path with /temporary directory/<uid>/shortened absolute parent path/<region><versionid>/filename
     */
    @Override
    public Local create(final String uid, final Path file) {
        /*
        $1%s: Delimiter
        $2%s: UID
        $3%s: Path
        $4%s: Region
         */
        final String pathFormat = "%2$s%1$s%3$s%1$s%4$s";
        final String normalizedPathFormat = pathFormat + "%1$s%5$s";
        String attributes = StringUtils.EMPTY;
        if(StringUtils.isNotBlank(file.attributes().getRegion())) {
            if(new DefaultPathContainerService().isContainer(file)) {
                attributes += file.attributes().getRegion();
            }
        }
        if(file.isFile()) {
            if(StringUtils.isNotBlank(file.attributes().getVersionId())) {
                attributes += file.attributes().getVersionId();
            }
        }
        final int limit = preferences.getInteger("local.temporaryfiles.shortening.threshold") -
                new File(preferences.getProperty("tmp.dir"), String.format(normalizedPathFormat, delimiter, uid, "", attributes, file.getName())).getAbsolutePath().length();
        final Local folder = LocalFactory.get(preferences.getProperty("tmp.dir"), String.format(pathFormat, delimiter, uid,
                this.shorten(file.getParent().getAbsolute(), limit), attributes));
        return this.create(folder, file.getName());
    }

    private Local create(final Local folder, final String filename) {
        try {
            if(log.isDebugEnabled()) {
                log.debug(String.format("Creating intermediate folder %s", folder));
            }
            folder.mkdir();
        }
        catch(AccessDeniedException e) {
            log.warn(String.format("Failure %s creating intermediate folder", e));
            return this.delete(LocalFactory.get(preferences.getProperty("tmp.dir"), String.format("%s-%s", new AlphanumericRandomStringService().random(), filename)));
        }
        this.delete(folder);
        return this.delete(LocalFactory.get(folder, filename));
    }
}
