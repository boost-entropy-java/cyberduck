package ch.cyberduck.core.googledrive;

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

import ch.cyberduck.core.DefaultIOExceptionMappingService;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.exception.RetriableAccessDeniedException;
import ch.cyberduck.core.http.DefaultHttpResponseExceptionMappingService;
import ch.cyberduck.core.preferences.PreferencesFactory;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpResponseException;

public class DriveExceptionMappingService extends DefaultIOExceptionMappingService {
    private static final Logger log = LogManager.getLogger(DriveExceptionMappingService.class);

    private final DriveFileIdProvider fileid;

    public DriveExceptionMappingService(final DriveFileIdProvider fileid) {
        this.fileid = fileid;
    }

    @Override
    public BackgroundException map(final String message, final IOException failure, final Path file) {
        if(failure instanceof HttpResponseException) {
            final HttpResponseException response = (HttpResponseException) failure;
            switch(response.getStatusCode()) {
                case HttpStatus.SC_NOT_FOUND:
                    fileid.cache(file, null);
            }
        }
        return super.map(message, failure, file);
    }

    @Override
    public BackgroundException map(final IOException failure) {
        log.warn("Map failure {}", failure.toString());
        final StringBuilder buffer = new StringBuilder();
        if(failure instanceof GoogleJsonResponseException) {
            final GoogleJsonResponseException error = (GoogleJsonResponseException) failure;
            final GoogleJsonError details = error.getDetails();
            if(details != null) {
                this.append(buffer, error.getDetails().getMessage());
                final Optional<GoogleJsonError.ErrorInfo> optionalInfo = details.getErrors().stream().findFirst();
                if(optionalInfo.isPresent()) {
                    final GoogleJsonError.ErrorInfo info = optionalInfo.get();
                    this.append(buffer, "domain: " + info.getDomain());
                    this.append(buffer, "reason: " + info.getReason());
                    if("usageLimits".equals(info.getDomain()) && details.getCode() == HttpStatus.SC_FORBIDDEN) {
                        return new RetriableAccessDeniedException(buffer.toString(),
                            Duration.ofSeconds(PreferencesFactory.get().getInteger("connection.retry.delay")), failure);
                    }
                }
            }
        }
        if(failure instanceof HttpResponseException) {
            final HttpResponseException response = (HttpResponseException) failure;
            this.append(buffer, response.getStatusMessage());
            return new DefaultHttpResponseExceptionMappingService().map(new org.apache.http.client
                .HttpResponseException(response.getStatusCode(), buffer.toString()));
        }
        return super.map(failure);
    }
}
