package ch.cyberduck.core.nextcloud;

/*
 * Copyright (c) 2002-2022 iterate GmbH. All rights reserved.
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

import ch.cyberduck.core.Path;
import ch.cyberduck.core.dav.DAVPathEncoder;
import ch.cyberduck.core.dav.DAVReadFeature;
import ch.cyberduck.core.dav.DAVSession;
import ch.cyberduck.core.transfer.TransferStatus;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpRequestBase;

import java.net.URI;

public class NextcloudReadFeature extends DAVReadFeature {

    private final DAVSession session;

    public NextcloudReadFeature(final DAVSession session) {
        super(session);
        this.session = session;
    }

    @Override
    protected HttpRequestBase toRequest(final Path file, final TransferStatus status) {
        final HttpRequestBase request = super.toRequest(file, status);
        if(StringUtils.isNotBlank(file.attributes().getVersionId())) {
            request.setURI(URI.create(String.format("%sversions/%s/%s",
                    new DAVPathEncoder().encode(new NextcloudHomeFeature(session.getHost()).find(NextcloudHomeFeature.Context.versions)),
                    file.attributes().getFileId(), file.attributes().getVersionId())));
        }
        return request;
    }
}
