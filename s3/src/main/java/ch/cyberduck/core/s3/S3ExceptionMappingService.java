package ch.cyberduck.core.s3;

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
 * dkocher@cyberduck.ch
 */

import ch.cyberduck.core.AbstractExceptionMappingService;
import ch.cyberduck.core.DefaultIOExceptionMappingService;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.exception.ConnectionTimeoutException;
import ch.cyberduck.core.exception.ExpiredTokenException;
import ch.cyberduck.core.exception.InteroperabilityException;
import ch.cyberduck.core.exception.LoginFailureException;
import ch.cyberduck.core.http.DefaultHttpResponseExceptionMappingService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.xml.sax.SAXException;

import java.io.IOException;

public class S3ExceptionMappingService extends AbstractExceptionMappingService<ServiceException> {
    private static final Logger log = LogManager.getLogger(S3ExceptionMappingService.class);

    private static final String MINIO_ERROR_CODE = "x-minio-error-code";
    private static final String MINIO_ERROR_DESCRIPTION = "x-minio-error-desc";

    public BackgroundException map(final HttpResponse response) throws IOException {
        final S3ServiceException failure;
        if(null == response.getEntity()) {
            failure = new S3ServiceException(response.getStatusLine().getReasonPhrase());
        }
        else {
            EntityUtils.updateEntity(response, new BufferedHttpEntity(response.getEntity()));
            failure = new S3ServiceException(response.getStatusLine().getReasonPhrase(),
                    EntityUtils.toString(response.getEntity()));
        }
        failure.setResponseCode(response.getStatusLine().getStatusCode());
        if(response.containsHeader(MINIO_ERROR_CODE)) {
            failure.setErrorCode(response.getFirstHeader(MINIO_ERROR_CODE).getValue());
        }
        if(response.containsHeader(MINIO_ERROR_DESCRIPTION)) {
            failure.setErrorMessage(response.getFirstHeader(MINIO_ERROR_DESCRIPTION).getValue());
        }
        return this.map(failure);
    }

    @Override
    public BackgroundException map(final ServiceException e) {
        log.warn("Map failure {}", e.toString());
        if(e.getCause() instanceof ServiceException) {
            return this.map((ServiceException) e.getCause());
        }
        final StringBuilder buffer = new StringBuilder();
        if(StringUtils.isNotBlank(e.getErrorMessage())) {
            // S3 protocol message parsed from XML
            this.append(buffer, StringEscapeUtils.unescapeXml(e.getErrorMessage()));
        }
        else {
            this.append(buffer, e.getResponseStatus());
            this.append(buffer, e.getMessage());
            this.append(buffer, e.getErrorCode());
        }
        switch(e.getResponseCode()) {
            case HttpStatus.SC_FORBIDDEN:
                if(StringUtils.isNotBlank(e.getErrorCode())) {
                    switch(e.getErrorCode()) {
                        case "SignatureDoesNotMatch":
                        case "InvalidAccessKeyId":
                        case "InvalidClientTokenId":
                        case "InvalidSecurity":
                        case "MissingClientTokenId":
                        case "MissingAuthenticationToken":
                            return new LoginFailureException(buffer.toString(), e);
                    }
                }
            case HttpStatus.SC_BAD_REQUEST:
                if(StringUtils.isNotBlank(e.getErrorCode())) {
                    switch(e.getErrorCode()) {
                        case "RequestTimeout":
                            return new ConnectionTimeoutException(buffer.toString(), e);
                        case "ExpiredToken":
                        case "InvalidToken":
                        case "TokenRefreshRequired":
                            return new ExpiredTokenException(buffer.toString(), e);
                    }
                }
        }
        if(e.getCause() instanceof IOException) {
            return new DefaultIOExceptionMappingService().map((IOException) e.getCause());
        }
        if(e.getCause() instanceof SAXException) {
            final InteroperabilityException f = new InteroperabilityException(buffer.toString(), e);
            final SAXException cause = (SAXException) e.getCause();
            f.setDetail(cause.getMessage());
            return f;
        }
        if(-1 == e.getResponseCode()) {
            return new InteroperabilityException(buffer.toString(), e);
        }
        return new DefaultHttpResponseExceptionMappingService().map(new HttpResponseException(e.getResponseCode(), buffer.toString()));
    }
}
