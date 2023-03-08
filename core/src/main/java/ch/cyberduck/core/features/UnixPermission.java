package ch.cyberduck.core.features;

/*
 * Copyright (c) 2002-2016 iterate GmbH. All rights reserved.
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

import ch.cyberduck.core.Local;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.Permission;
import ch.cyberduck.core.exception.BackgroundException;

import java.util.EnumSet;

@Optional
public interface UnixPermission {

    void setUnixOwner(Path file, String owner) throws BackgroundException;

    void setUnixGroup(Path file, String group) throws BackgroundException;

    Permission getUnixPermission(Path file) throws BackgroundException;

    void setUnixPermission(Path file, Permission permission) throws BackgroundException;

    /**
     * @param file File on local disk
     * @return Default mask to set for file
     */
    Permission getDefault(Local file);

    /**
     * @param type File or folder
     * @return Default mask for new file or folder
     */
    Permission getDefault(EnumSet<Path.Type> type);
}
