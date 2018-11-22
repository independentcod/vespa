// Copyright 2018 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.security.tls.json;

import com.yahoo.security.tls.TransportSecurityOptions;
import com.yahoo.security.tls.policy.AuthorizedPeers;
import com.yahoo.security.tls.policy.HostGlobPattern;
import com.yahoo.security.tls.policy.PeerPolicy;
import com.yahoo.security.tls.policy.RequiredPeerCredential;
import com.yahoo.security.tls.policy.Role;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static com.yahoo.security.tls.policy.RequiredPeerCredential.Field.*;
import static java.util.Collections.singleton;
import static org.junit.Assert.*;

/**
 * @author bjorncs
 */
public class TransportSecurityOptionsJsonSerializerTest {

    @Test
    public void can_serialize_and_deserialize_transport_security_options() {
        TransportSecurityOptions options = new TransportSecurityOptions.Builder()
                .withCaCertificates(Paths.get("/path/to/ca-certs.pem"))
                .withCertificates(Paths.get("/path/to/cert.pem"), Paths.get("/path/to/key.pem"))
                .withAuthorizedPeers(
                        new AuthorizedPeers(
                                new HashSet<>(Arrays.asList(
                                        new PeerPolicy("cfgserver", singleton(new Role("myrole")), Arrays.asList(
                                                new RequiredPeerCredential(CN, new HostGlobPattern("mycfgserver")),
                                                new RequiredPeerCredential(SAN_DNS, new HostGlobPattern("*.suffix.com")))),
                                        new PeerPolicy("node", singleton(new Role("anotherrole")), Collections.singletonList(new RequiredPeerCredential(CN, new HostGlobPattern("hostname"))))))))
                .build();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TransportSecurityOptionsJsonSerializer serializer = new TransportSecurityOptionsJsonSerializer();
        serializer.serialize(out, options);
        TransportSecurityOptions deserializedOptions = serializer.deserialize(new ByteArrayInputStream(out.toByteArray()));
        assertEquals(options, deserializedOptions);
    }

}
