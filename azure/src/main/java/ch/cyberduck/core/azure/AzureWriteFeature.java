package ch.cyberduck.core.azure;

/*
 * Copyright (c) 2002-2024 iterate GmbH. All rights reserved.
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

import ch.cyberduck.core.ConnectionCallback;
import ch.cyberduck.core.DirectoryDelimiterPathContainerService;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.PathAttributes;
import ch.cyberduck.core.PathContainerService;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.features.AttributesFinder;
import ch.cyberduck.core.features.Find;
import ch.cyberduck.core.features.Write;
import ch.cyberduck.core.io.Checksum;
import ch.cyberduck.core.io.ChecksumCompute;
import ch.cyberduck.core.io.ChecksumComputeFactory;
import ch.cyberduck.core.io.HashAlgorithm;
import ch.cyberduck.core.io.StatusOutputStream;
import ch.cyberduck.core.io.VoidStatusOutputStream;
import ch.cyberduck.core.preferences.HostPreferences;
import ch.cyberduck.core.preferences.Preferences;
import ch.cyberduck.core.preferences.PreferencesFactory;
import ch.cyberduck.core.transfer.TransferStatus;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.azure.core.exception.HttpResponseException;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobType;
import com.azure.storage.blob.options.AppendBlobCreateOptions;
import com.azure.storage.blob.options.BlockBlobOutputStreamOptions;
import com.azure.storage.blob.specialized.AppendBlobClient;
import com.azure.storage.blob.specialized.BlobOutputStream;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.azure.storage.common.implementation.Constants;

public class AzureWriteFeature implements Write<Void> {
    private static final Logger log = LogManager.getLogger(AzureWriteFeature.class);

    private final AzureSession session;

    private final PathContainerService containerService
            = new DirectoryDelimiterPathContainerService();

    private final Preferences preferences
            = PreferencesFactory.get();
    private final BlobType blobType;

    public AzureWriteFeature(final AzureSession session) {
        this(session, BlobType.valueOf(new HostPreferences(session.getHost()).getProperty("azure.upload.blobtype")));
    }

    public AzureWriteFeature(final AzureSession session, final BlobType blobType) {
        this.session = session;
        this.blobType = blobType;
    }

    public AzureWriteFeature(final AzureSession session, final Find finder, final AttributesFinder attributes) {
        this(session, finder, attributes, BlobType.valueOf(new HostPreferences(session.getHost()).getProperty("azure.upload.blobtype")));
    }

    public AzureWriteFeature(final AzureSession session, final Find finder, final AttributesFinder attributes, final BlobType blobType) {
        this.session = session;
        this.blobType = blobType;
    }

    @Override
    public ChecksumCompute checksum(final Path file, final TransferStatus status) {
        return ChecksumComputeFactory.get(HashAlgorithm.md5);
    }

    @Override
    public StatusOutputStream<Void> write(final Path file, final TransferStatus status, final ConnectionCallback callback) throws BackgroundException {
        try {
            final BlobClient client = session.getClient().getBlobContainerClient(containerService.getContainer(file).getName())
                    .getBlobClient(containerService.getKey(file));
            final BlobHttpHeaders headers = new BlobHttpHeaders();
            if(StringUtils.isNotBlank(status.getMime())) {
                headers.setContentType(status.getMime());
            }
            final Map<String, String> metadata = new HashMap<>(status.getMetadata());
            for(final Map.Entry<String, String> m : metadata.entrySet()) {
                if(HttpHeaders.CACHE_CONTROL.equalsIgnoreCase(m.getKey())) {
                    // Update properties
                    headers.setCacheControl(m.getValue());
                }
                if(HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(m.getKey())) {
                    // Update properties
                    headers.setContentType(m.getValue());
                }
            }
            metadata.remove(HttpHeaders.CACHE_CONTROL);
            metadata.remove(HttpHeaders.CONTENT_TYPE);
            final Checksum checksum = status.getChecksum();
            if(Checksum.NONE != checksum) {
                switch(checksum.algorithm) {
                    case md5:
                        try {
                            headers.setContentMd5(Hex.decodeHex(status.getChecksum().hash.toCharArray()));
                        }
                        catch(DecoderException e) {
                            // Ignore
                        }
                        break;
                }
            }
            final BlobOutputStream out;
            if(status.isExists()) {
                if(preferences.getBoolean("azure.upload.snapshot")) {
                    session.getClient().getBlobContainerClient(containerService.getContainer(file).getName())
                            .getBlobClient(containerService.getKey(file)).createSnapshot();
                }
                if(status.isAppend()) {
                    // Existing append blob type
                    out = client.getAppendBlobClient().getBlobOutputStream();
                }
                else {
                    // Existing block blob type
                    final PathAttributes attr = new AzureAttributesFinderFeature(session).find(file);
                    if(BlobType.APPEND_BLOB == BlobType.valueOf(attr.getCustom().get(AzureAttributesFinderFeature.KEY_BLOB_TYPE))) {
                        out = client.getAppendBlobClient().getBlobOutputStream(true);
                    }
                    else {
                        final BlockBlobOutputStreamOptions options = new BlockBlobOutputStreamOptions()
                                .setMetadata(metadata)
                                .setHeaders(headers)
                                .setMetadata(metadata);
                        out = client.getBlockBlobClient().getBlobOutputStream(options);
                    }
                }
            }
            else {
                // Create new blob with default type set in defaults
                switch(blobType) {
                    case APPEND_BLOB: {
                        final AppendBlobClient append = client.getAppendBlobClient();
                        final AppendBlobCreateOptions options = new AppendBlobCreateOptions()
                                .setMetadata(metadata)
                                .setHeaders(headers)
                                .setMetadata(metadata);
                        append.createWithResponse(options, null, null);
                        out = append.getBlobOutputStream();
                        break;
                    }
                    default: {
                        final BlockBlobClient block = client.getBlockBlobClient();
                        final BlockBlobOutputStreamOptions options = new BlockBlobOutputStreamOptions()
                                .setMetadata(metadata)
                                .setHeaders(headers)
                                .setMetadata(metadata);
                        out = block.getBlobOutputStream(options);
                        break;
                    }
                }
            }
            return new VoidStatusOutputStream(out) {
                @Override
                public void close() throws IOException {
                    try {
                        super.close();
                    }
                    catch(RuntimeException e) {
                        this.handleIOException(new IOException(e.getMessage(), e));
                    }
                }

                @Override
                protected void handleIOException(final IOException e) throws IOException {
                    if(StringUtils.equals(Constants.STREAM_CLOSED, e.getMessage())) {
                        log.warn(String.format("Ignore failure %s", e));
                        return;
                    }
                    throw e;
                }
            };
        }
        catch(HttpResponseException e) {
            throw new AzureExceptionMappingService().map("Upload {0} failed", e, file);
        }
    }
}
