// Copyright 2018 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.vespa.athenz.identityprovider.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yahoo.vespa.athenz.identityprovider.api.bindings.IdentityDocumentEntity;
import com.yahoo.vespa.athenz.identityprovider.api.bindings.VespaUniqueInstanceIdEntity;
import com.yahoo.vespa.athenz.identityprovider.api.bindings.SignedIdentityDocumentEntity;

/**
 * Utility class for mapping objects model types and their Jackson binding versions.
 *
 * @author bjorncs
 */
public class EntityBindingsMapper {

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private EntityBindingsMapper() {}

    public static String toAttestationData(SignedIdentityDocument model) {
        try {
            return mapper.writeValueAsString(toSignedIdentityDocumentEntity(model));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static VespaUniqueInstanceId toVespaUniqueInstanceId(VespaUniqueInstanceIdEntity entity) {
        return new VespaUniqueInstanceId(
                entity.clusterIndex, entity.clusterId, entity.instance, entity.application, entity.tenant, entity.region, entity.environment);
    }

    public static VespaUniqueInstanceIdEntity toVespaUniqueInstanceIdEntity(VespaUniqueInstanceId model) {
        return new VespaUniqueInstanceIdEntity(
                model.tenant(), model.application(), model.environment(), model.region(),
                model.instance(), model.clusterId(), model.clusterIndex());
    }

    public static IdentityDocumentEntity toIdentityDocumentEntity(IdentityDocument model) {
        return new IdentityDocumentEntity(
                toVespaUniqueInstanceIdEntity(model.providerUniqueId()),
                model.configServerHostname(),
                model.instanceHostname(),
                model.createdAt(),
                model.ipAddresses());
    }

    public static SignedIdentityDocumentEntity toSignedIdentityDocumentEntity(SignedIdentityDocument model) {
        try {
            IdentityDocumentEntity identityDocumentEntity = toIdentityDocumentEntity(model.identityDocument());
            String rawDocument = mapper.writeValueAsString(identityDocumentEntity);
            return new SignedIdentityDocumentEntity(
                    rawDocument,
                    model.signature(),
                    model.signingKeyVersion(),
                    model.providerUniqueId().asDottedString(),
                    model.dnsSuffix(),
                    model.providerService().getFullName(),
                    model.ztsEndpoint(),
                    model.documentVersion());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}