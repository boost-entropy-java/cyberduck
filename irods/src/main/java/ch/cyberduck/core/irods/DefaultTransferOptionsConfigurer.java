package ch.cyberduck.core.irods;

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

import ch.cyberduck.core.preferences.Preferences;
import ch.cyberduck.core.preferences.PreferencesFactory;

import org.irods.jargon.core.packinstr.TransferOptions;

public class DefaultTransferOptionsConfigurer {

    private final Preferences preferences = PreferencesFactory.get();

    public TransferOptions configure(final TransferOptions options) {
        options.setPutOption(TransferOptions.PutOptions.NORMAL);
        options.setForceOption(TransferOptions.ForceOption.ASK_CALLBACK_LISTENER);
        options.setMaxThreads(preferences.getInteger("queue.connections.limit.default"));
        // Enable progress callbacks
        options.setIntraFileStatusCallbacks(true);
        options.setIntraFileStatusCallbacksNumberCallsInterval(1);
        return options;
    }
}
