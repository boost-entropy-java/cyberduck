package ch.cyberduck.core.local;

/*
 * Copyright (c) 2002-2018 iterate GmbH. All rights reserved.
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

import ch.cyberduck.core.Local;
import ch.cyberduck.core.LocaleFactory;
import ch.cyberduck.core.exception.AccessDeniedException;
import ch.cyberduck.core.local.features.Symlink;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;

public class DefaultSymlinkFeature implements Symlink {

    @Override
    public void symlink(final Local link, final String target) throws AccessDeniedException {
        try {
            Files.createSymbolicLink(Paths.get(link.getAbsolute()), Paths.get(target));
        }
        catch(IOException | UnsupportedOperationException e) {
            throw new AccessDeniedException(MessageFormat.format(LocaleFactory.localizedString(
                    "Cannot create {0}", "Error"), link.getName()), e.getMessage(), e);
        }
    }
}
