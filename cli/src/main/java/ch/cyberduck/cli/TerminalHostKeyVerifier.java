package ch.cyberduck.cli;

/*
 * Copyright (c) 2002-2014 David Kocher. All rights reserved.
 * http://cyberduck.io/
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
 * feedback@cyberduck.io
 */

import ch.cyberduck.core.Host;
import ch.cyberduck.core.LocalFactory;
import ch.cyberduck.core.LocaleFactory;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.exception.ConnectionCanceledException;
import ch.cyberduck.core.preferences.PreferencesFactory;
import ch.cyberduck.core.sftp.SSHFingerprintGenerator;
import ch.cyberduck.core.sftp.openssh.OpenSSHHostKeyVerifier;

import java.security.PublicKey;
import java.text.MessageFormat;

import net.schmizz.sshj.common.KeyType;

public class TerminalHostKeyVerifier extends OpenSSHHostKeyVerifier {

    private final TerminalPromptReader prompt;

    public TerminalHostKeyVerifier() {
        this(new InteractiveTerminalPromptReader());
    }

    public TerminalHostKeyVerifier(final TerminalPromptReader prompt) {
        super(LocalFactory.get(PreferencesFactory.get().getProperty("ssh.knownhosts")).setBookmark(
                PreferencesFactory.get().getProperty("ssh.knownhosts.bookmark")
        ));
        this.prompt = prompt;
    }

    @Override
    protected boolean isUnknownKeyAccepted(final Host host, final PublicKey key) throws BackgroundException {
        final String message = String.format("%s. %s %s?", LocaleFactory.localizedString("Unknown fingerprint", "Sftp"),
                MessageFormat.format(LocaleFactory.localizedString("The fingerprint for the {1} key sent by the server is {0}.", "Sftp"),
                        new SSHFingerprintGenerator().fingerprint(key),
                        KeyType.fromKey(key).toString()),
                LocaleFactory.localizedString("Continue", "Credentials"));
        if(!prompt.prompt(message)) {
            throw new ConnectionCanceledException();
        }
        this.allow(host, key, true);
        return true;
    }

    @Override
    protected boolean isChangedKeyAccepted(final Host host, final PublicKey key) throws BackgroundException {
        final String message = String.format("%s. %s %s?", LocaleFactory.localizedString("Changed fingerprint", "Sftp"),
                MessageFormat.format(LocaleFactory.localizedString("The fingerprint for the {1} key sent by the server is {0}.", "Sftp"),
                        new SSHFingerprintGenerator().fingerprint(key),
                        KeyType.fromKey(key).toString()),
                LocaleFactory.localizedString("Continue", "Credentials"));
        if(!prompt.prompt(message)) {
            throw new ConnectionCanceledException();
        }
        this.allow(host, key, true);
        return true;
    }
}
